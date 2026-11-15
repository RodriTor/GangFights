package Items;

import Entidades.Jugador;
import Mundo.Mapa;
import Utilidades.Recursos;
import Utilidades.Sprites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Hace aparecer el botiquin y la pistola sobre el piso o las plataformas del mapa
 * (uno de cada a la vez), los dibuja y se los entrega al jugador que los agarra.
 */
public class GestorItems {

    // Espacio libre que necesita un item para aparecer (en pixeles)
    private static final float ANCHO_LIBRE = 48f;
    private static final float ALTO_LIBRE = 40f;
    private static final float PASO_ENTRE_PUNTOS = 24f;

    // Las plataformas mas altas no se alcanzan con el salto actual
    private static final float ALTURA_MAXIMA = 224f;
    private static final float SEPARACION_ENTRE_ITEMS = 80f;

    private static final float PRIMERA_ESPERA_MIN = 4f;
    private static final float PRIMERA_ESPERA_MAX = 8f;
    private static final float ESPERA_MIN = 12f;
    private static final float ESPERA_MAX = 22f;

    private final Texture texturaBotiquin = new Texture(Gdx.files.internal(Recursos.VIDA));
    private final Texture texturaPistola = new Texture(Gdx.files.internal(Recursos.PISTOLA));
    private final TextureRegion imagenBotiquin;
    private final TextureRegion imagenPistola;
    private final ArrayList<Vector2> puntos;

    private Botiquin botiquin;
    private Pistola pistola;
    private float esperaBotiquin;
    private float esperaPistola;

    public GestorItems(Mapa mapa) {
        texturaBotiquin.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texturaPistola.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        imagenBotiquin = Sprites.sinBordeTransparente(texturaBotiquin, Recursos.VIDA);
        imagenPistola = Sprites.sinBordeTransparente(texturaPistola, Recursos.PISTOLA);

        puntos = mapa.buscarPuntosLibres(ANCHO_LIBRE, ALTO_LIBRE, PASO_ENTRE_PUNTOS, ALTURA_MAXIMA);
        reiniciar();
    }

    /** Saca los items del mapa y vuelve a programar su aparicion (se usa en cada ronda). */
    public void reiniciar() {
        botiquin = null;
        pistola = null;
        esperaBotiquin = MathUtils.random(PRIMERA_ESPERA_MIN, PRIMERA_ESPERA_MAX);
        esperaPistola = MathUtils.random(PRIMERA_ESPERA_MIN, PRIMERA_ESPERA_MAX);
    }

    public void actualizar(float delta, Jugador jugador1, Jugador jugador2) {
        if (botiquin == null) {
            esperaBotiquin -= delta;
            if (esperaBotiquin <= 0f) {
                aparecerBotiquin();
            }
        } else if (botiquin.intentarRecoger(jugador1) || botiquin.intentarRecoger(jugador2)) {
            botiquin = null;
            esperaBotiquin = MathUtils.random(ESPERA_MIN, ESPERA_MAX);
        }

        if (pistola == null) {
            esperaPistola -= delta;
            if (esperaPistola <= 0f) {
                aparecerPistola();
            }
        } else if (pistola.intentarRecoger(jugador1) || pistola.intentarRecoger(jugador2)) {
            pistola = null;
            esperaPistola = MathUtils.random(ESPERA_MIN, ESPERA_MAX);
        }
    }

    private void aparecerBotiquin() {
        Vector2 punto = elegirPunto(pistola);
        if (punto == null) {
            esperaBotiquin = 1f;
            return;
        }
        botiquin = new Botiquin(imagenBotiquin, punto.x, punto.y);
    }

    private void aparecerPistola() {
        Vector2 punto = elegirPunto(botiquin);
        if (punto == null) {
            esperaPistola = 1f;
            return;
        }
        pistola = new Pistola(imagenPistola, punto.x, punto.y);
    }

    /** Punto al azar sobre el piso o una plataforma, tratando de que quede lejos del otro item. */
    private Vector2 elegirPunto(Item otroItem) {
        if (puntos.isEmpty()) return null;

        Vector2 punto = puntos.get(MathUtils.random(puntos.size() - 1));
        for (int intento = 0; intento < 5 && estaCerca(punto, otroItem); intento++) {
            punto = puntos.get(MathUtils.random(puntos.size() - 1));
        }
        return punto;
    }

    private boolean estaCerca(Vector2 punto, Item item) {
        return item != null && Math.abs(item.getX() - punto.x) < SEPARACION_ENTRE_ITEMS;
    }

    public void dibujar() {
        if (botiquin != null) botiquin.dibujar();
        if (pistola != null) pistola.dibujar();
    }

    public void dispose() {
        texturaBotiquin.dispose();
        texturaPistola.dispose();
    }
}
