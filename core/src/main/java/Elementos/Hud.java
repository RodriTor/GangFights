package Elementos;

import Audio.GestorAudio;
import Entidades.Personaje;
import Logica.EstadoPartida;
import Logica.Partida;
import Utilidades.Config;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    private static final float ANCHO = Config.ANCHO_MUNDO;
    private static final float ALTO = Config.ALTO_MUNDO;
    private static final float MARGEN = 20f;
    private static final float CAJA = 90f;
    private static final float PANEL_ANCHO = 330f;
    private static final float PANEL_ALTO = 90f;
    private static final float RETRATO = 70f;
    private static final float BARRA_ANCHO = 215f;
    private static final float BARRA_ALTO = 20f;
    private static final int ANCHO_FRAME = 32;
    private static final int ALTO_FRAME = 42;

    private static final Color FONDO = new Color(0.05f, 0.05f, 0.08f, 1f);
    private static final Color FONDO_RETRATO = new Color(0.2f, 0.2f, 0.25f, 1f);
    private static final Color FONDO_RETRATO_MUERTO = new Color(0.45f, 0.05f, 0.05f, 1f);
    private static final Color[] COLORES = {Color.CYAN, Color.ORANGE};

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(ANCHO, ALTO, camara);
    private final ShapeRenderer formas = new ShapeRenderer();

    private final Texture calavera = new Texture(Gdx.files.internal(Recursos.CALAVERA));
    private final Texture[] hojas = new Texture[2];
    private final TextureRegion[] retratos = new TextureRegion[2];

    private final Texto[] nombres = new Texto[2];
    private final Texto[] rondas = new Texto[2];
    private final Texto[] etiquetasRondas = new Texto[2];
    private final Texto marcador = new Texto(Recursos.FUENTE_MENU, 56, Color.YELLOW);
    private final Texto infoRonda = new Texto(Recursos.FUENTE_MENU, 18, Color.WHITE);
    private final Texto mensajeCentral = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE);
    private final Texto ayuda = new Texto(Recursos.FUENTE_MENU, 18, Color.LIGHT_GRAY);
    private final Texto estadoAudio = new Texto(Recursos.FUENTE_MENU, 18, Color.LIGHT_GRAY);

    private Partida partida;
    private boolean hayMensaje;

    public Hud(Personaje izquierdo, Personaje derecho) {
        calavera.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Personaje[] personajes = {izquierdo, derecho};
        for (int i = 0; i < 2; i++) {
            hojas[i] = new Texture(Gdx.files.internal(personajes[i].getRutaQuieto()));
            hojas[i].setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            retratos[i] = TextureRegion.split(hojas[i], ANCHO_FRAME, ALTO_FRAME)[0][0];

            nombres[i] = new Texto(Recursos.FUENTE_MENU, 24, COLORES[i]);
            rondas[i] = new Texto(Recursos.FUENTE_MENU, 40, Color.WHITE);
            etiquetasRondas[i] = new Texto(Recursos.FUENTE_MENU, 14, Color.LIGHT_GRAY);
            etiquetasRondas[i].setTexto("RONDAS");
            etiquetasRondas[i].centrarEn(cajaX(i) + CAJA / 2f, cajaY() + 26f);
        }
    }


    private float cajaX(int i) { return i == 0 ? MARGEN : ANCHO - MARGEN - CAJA; }
    private float cajaY() { return panelY(); }   // alineado con el panel
    private float panelX(int i) { return i == 0 ? MARGEN + CAJA + 16f : ANCHO - MARGEN - CAJA - 16f - PANEL_ANCHO; }
    private float panelY() { return ALTO - MARGEN - PANEL_ALTO; }
    private float retratoX(int i) { return i == 0 ? panelX(i) + 10f : panelX(i) + PANEL_ANCHO - 10f - RETRATO; }
    private float retratoY() { return panelY() + 10f; }
    private float barraX(int i) { return i == 0 ? panelX(i) + RETRATO + 22f : panelX(i) + 10f; }
    private float barraY() { return panelY() + 16f; }


    public void actualizar(Partida partida, GestorAudio audio) {
        this.partida = partida;

        for (int i = 0; i < 2; i++) {
            nombres[i].setTexto(partida.getNombre(i));
            nombres[i].setPosicion(barraX(i), panelY() + PANEL_ALTO - 14f);

            rondas[i].setTexto(String.valueOf(partida.getRondasGanadas(i)));
            rondas[i].centrarEn(cajaX(i) + CAJA / 2f, cajaY() + CAJA - 14f);
        }

        marcador.setTexto(partida.getRondasGanadas(0) + " - " + partida.getRondasGanadas(1));
        marcador.centrarEn(ANCHO / 2f, ALTO - MARGEN);
        infoRonda.setTexto("RONDA " + partida.getRondaActual() + "   |   PARA GANAR: " + Partida.RONDAS_PARA_GANAR);
        infoRonda.centrarEn(ANCHO / 2f, ALTO - MARGEN - 75f);

        actualizarMensajeCentral(partida);

        ayuda.setTexto(partida.getEstado() == EstadoPartida.PAUSADA
            ? "ESC: reanudar    Q: volver al menu"
            : "ESC: pausa");
        ayuda.setPosicion(MARGEN, 40);

        estadoAudio.setTexto(audio.isSilenciado()
            ? "SONIDO: OFF  (M)"
            : "MUSICA: " + audio.getVolumenMusica() + "%   EFECTOS: " + audio.getVolumenEfectos() + "%   (M)");
        estadoAudio.setPosicion(ANCHO - MARGEN - estadoAudio.getAncho(), 40);
    }

    private void actualizarMensajeCentral(Partida partida) {
        String mensaje = "";
        switch (partida.getEstado()) {
            case PAUSADA:
                mensaje = "PAUSA";
                break;
            case RONDA_TERMINADA:
                mensaje = "GANA LA RONDA: " + partida.getNombre(partida.getGanadorRonda());
                break;
            case FINALIZADA:
                mensaje = "GANA LA PARTIDA: " + partida.getNombre(partida.getGanadorRonda());
                break;
            default:
                break;
        }
        hayMensaje = !mensaje.isEmpty();
        mensajeCentral.setTexto(mensaje);
        mensajeCentral.centrarEn(ANCHO / 2f, ALTO / 2f + 40f);
    }


    public void dibujar() {
        viewport.apply();
        camara.update();

        dibujarFormas();

        Render.comenzarBatch(camara);
        for (int i = 0; i < 2; i++) {
            dibujarRetrato(i);
            nombres[i].dibujar();
            rondas[i].dibujar();
            etiquetasRondas[i].dibujar();
        }
        marcador.dibujar();
        infoRonda.dibujar();
        ayuda.dibujar();
        estadoAudio.dibujar();
        if (hayMensaje) mensajeCentral.dibujar();
        Render.terminarBatch();
    }

    private void dibujarFormas() {
        formas.setProjectionMatrix(camara.combined);
        formas.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < 2; i++) {
            boolean eliminado = partida.estaEliminado(i);

            formas.setColor(COLORES[i]);
            formas.rect(cajaX(i) - 3f, cajaY() - 3f, CAJA + 6f, CAJA + 6f);
            formas.setColor(FONDO);
            formas.rect(cajaX(i), cajaY(), CAJA, CAJA);

            // Panel del jugador
            formas.setColor(COLORES[i]);
            formas.rect(panelX(i) - 3f, panelY() - 3f, PANEL_ANCHO + 6f, PANEL_ALTO + 6f);
            formas.setColor(FONDO);
            formas.rect(panelX(i), panelY(), PANEL_ANCHO, PANEL_ALTO);

            formas.setColor(eliminado ? FONDO_RETRATO_MUERTO : FONDO_RETRATO);
            formas.rect(retratoX(i), retratoY(), RETRATO, RETRATO);

            formas.setColor(Color.WHITE);
            formas.rect(barraX(i) - 2f, barraY() - 2f, BARRA_ANCHO + 4f, BARRA_ALTO + 4f);
            formas.setColor(eliminado ? Color.RED : Color.GREEN);
            formas.rect(barraX(i), barraY(), BARRA_ANCHO, BARRA_ALTO);
        }
        formas.end();
    }

    private void dibujarRetrato(int i) {
        SpriteBatch batch = Render.batch;
        float x = retratoX(i);
        float y = retratoY();

        if (partida.estaEliminado(i)) {
            float tamanio = RETRATO - 14f;
            batch.draw(calavera, x + (RETRATO - tamanio) / 2f, y + (RETRATO - tamanio) / 2f, tamanio, tamanio);
            return;
        }

        float alto = RETRATO - 8f;
        float ancho = alto * ANCHO_FRAME / ALTO_FRAME;
        float px = x + (RETRATO - ancho) / 2f;
        if (i == 0) {
            batch.draw(retratos[i], px, y + 4f, ancho, alto);
        } else {
            batch.draw(retratos[i], px + ancho, y + 4f, -ancho, alto); // espejado: mira hacia el centro
        }
    }

    public void redimensionar(int ancho, int alto) {
        viewport.update(ancho, alto, true);
    }

    public void dispose() {
        formas.dispose();
        calavera.dispose();
        for (Texture hoja : hojas) hoja.dispose();
        for (Texto texto : nombres) texto.dispose();
        for (Texto texto : rondas) texto.dispose();
        for (Texto texto : etiquetasRondas) texto.dispose();
        marcador.dispose();
        infoRonda.dispose();
        mensajeCentral.dispose();
        ayuda.dispose();
        estadoAudio.dispose();
    }
}
