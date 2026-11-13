package Items;

import Entidades.Jugador;
import Mundo.Mapa;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;

/**
 * Hace aparecer los items sobre el piso o las plataformas del mapa, los dibuja y
 * entrega cada uno al jugador que lo agarra. Es dueno de las texturas (las libera en dispose).
 */
public class GestorItems {

    // Los sprites son de 32x42: se dibujan 1.5 veces mas grandes
    private static final float ANCHO_ITEM = 48f;
    private static final float ALTO_ITEM = 63f;

    // Superficies mas altas (en pixeles) no se alcanzan con el salto actual, ahi no aparecen items
    private static final float ALTURA_MAXIMA_APARICION = 224f;
    private static final float PASO_PUNTOS = 24f;
    private static final float SEPARACION_ENTRE_ITEMS = 100f;
    private static final int INTENTOS_POSICION = 10;

    private static final float ESPERA_INICIAL_MIN = 4f;
    private static final float ESPERA_INICIAL_MAX = 8f;
    private static final float ESPERA_MIN = 12f;
    private static final float ESPERA_MAX = 22f;

    private final EnumMap<TipoItem, Texture> texturas = new EnumMap<>(TipoItem.class);
    private final float[] esperas = new float[TipoItem.values().length];
    private final ArrayList<Item> activos = new ArrayList<>();
    private final ArrayList<Vector2> puntos;

    public GestorItems(Mapa mapa) {
        for (TipoItem tipo : TipoItem.values()) {
            Texture textura = new Texture(Gdx.files.internal(tipo.getRuta()));
            textura.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            texturas.put(tipo, textura);
        }
        puntos = mapa.buscarPuntosLibres(ANCHO_ITEM, ALTO_ITEM, PASO_PUNTOS, ALTURA_MAXIMA_APARICION);
        reiniciar();
    }

    /** Saca todos los items y vuelve a programar la primera aparicion (cada ronda). */
    public void reiniciar() {
        activos.clear();
        for (TipoItem tipo : TipoItem.values()) {
            esperas[tipo.ordinal()] = MathUtils.random(ESPERA_INICIAL_MIN, ESPERA_INICIAL_MAX);
        }
    }

    public void actualizar(float delta, Jugador... jugadores) {
        for (TipoItem tipo : TipoItem.values()) {
            if (hayActivo(tipo)) continue;

            esperas[tipo.ordinal()] -= delta;
            if (esperas[tipo.ordinal()] <= 0f) {
                aparecer(tipo);
            }
        }

        Iterator<Item> it = activos.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            item.actualizar(delta);

            for (Jugador jugador : jugadores) {
                if (item.intentarRecoger(jugador)) {
                    it.remove();
                    esperas[item.getTipo().ordinal()] = MathUtils.random(ESPERA_MIN, ESPERA_MAX);
                    break;
                }
            }
        }
    }

    public void dibujar() {
        for (Item item : activos) {
            item.dibujar();
        }
    }

    private boolean hayActivo(TipoItem tipo) {
        for (Item item : activos) {
            if (item.getTipo() == tipo) return true;
        }
        return false;
    }

    private void aparecer(TipoItem tipo) {
        Vector2 punto = elegirPunto();
        if (punto == null) {
            esperas[tipo.ordinal()] = 1f; // no hay lugar libre: reintenta en 1 segundo
            return;
        }
        activos.add(crear(tipo, punto.x, punto.y));
    }

    /** Punto al azar sobre el piso/plataformas, lejos de otros items. */
    private Vector2 elegirPunto() {
        if (puntos.isEmpty()) return null;

        Vector2 elegido = puntos.get(MathUtils.random(puntos.size() - 1));
        for (int i = 0; i < INTENTOS_POSICION && estaCercaDeOtroItem(elegido); i++) {
            elegido = puntos.get(MathUtils.random(puntos.size() - 1));
        }
        return elegido;
    }

    private boolean estaCercaDeOtroItem(Vector2 punto) {
        for (Item item : activos) {
            if (Math.abs(item.getX() - punto.x) < SEPARACION_ENTRE_ITEMS) return true;
        }
        return false;
    }

    private Item crear(TipoItem tipo, float x, float base) {
        TextureRegion imagen = new TextureRegion(texturas.get(tipo));
        return switch (tipo) {
            case BOTIQUIN -> new Botiquin(imagen, x, base, ANCHO_ITEM, ALTO_ITEM);
            case PISTOLA -> new Pistola(imagen, x, base, ANCHO_ITEM, ALTO_ITEM);
        };
    }

    public void dispose() {
        for (Texture textura : texturas.values()) {
            textura.dispose();
        }
        texturas.clear();
        activos.clear();
    }
}
