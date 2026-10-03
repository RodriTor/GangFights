package Elementos;

import Audio.GestorAudio;
import Logica.EstadoPartida;
import Logica.Partida;
import Utilidades.Config;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    private static final float MARGEN = 40f;
    private static final float TOPE = Config.ALTO_MUNDO - 20f;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(Config.ANCHO_MUNDO, Config.ALTO_MUNDO, camara);

    private final Texto nombreIzquierda = new Texto(Recursos.FUENTE_MENU, 28, Color.CYAN);
    private final Texto nombreDerecha = new Texto(Recursos.FUENTE_MENU, 28, Color.ORANGE);
    private final Texto rondasIzquierda = new Texto(Recursos.FUENTE_MENU, 20, Color.WHITE);
    private final Texto rondasDerecha = new Texto(Recursos.FUENTE_MENU, 20, Color.WHITE);
    private final Texto marcador = new Texto(Recursos.FUENTE_MENU, 40, Color.YELLOW);
    private final Texto rondaActual = new Texto(Recursos.FUENTE_MENU, 18, Color.WHITE);
    private final Texto mensajeCentral = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE);
    private final Texto ayuda = new Texto(Recursos.FUENTE_MENU, 18, Color.LIGHT_GRAY);
    private final Texto estadoAudio = new Texto(Recursos.FUENTE_MENU, 18, Color.LIGHT_GRAY);

    private boolean hayMensaje;

    public void actualizar(Partida partida, GestorAudio audio) {
        float centroX = Config.ANCHO_MUNDO / 2f;

        nombreIzquierda.setTexto(partida.getNombre(0));
        nombreIzquierda.setPosicion(MARGEN, TOPE);
        rondasIzquierda.setTexto("Rondas: " + partida.getRondasGanadas(0));
        rondasIzquierda.setPosicion(MARGEN, TOPE - 40);

        nombreDerecha.setTexto(partida.getNombre(1));
        nombreDerecha.setPosicion(Config.ANCHO_MUNDO - MARGEN - nombreDerecha.getAncho(), TOPE);
        rondasDerecha.setTexto("Rondas: " + partida.getRondasGanadas(1));
        rondasDerecha.setPosicion(Config.ANCHO_MUNDO - MARGEN - rondasDerecha.getAncho(), TOPE - 40);

        marcador.setTexto(partida.getRondasGanadas(0) + " - " + partida.getRondasGanadas(1));
        marcador.centrarEn(centroX, TOPE);
        rondaActual.setTexto("RONDA " + partida.getRondaActual() + "  (a " + Partida.RONDAS_PARA_GANAR + ")");
        rondaActual.centrarEn(centroX, TOPE - 50);

        actualizarMensajeCentral(partida);

        ayuda.setTexto(partida.getEstado() == EstadoPartida.PAUSADA
            ? "ESC: reanudar    Q: volver al menu"
            : "ESC: pausa");
        ayuda.setPosicion(MARGEN, 40);

        estadoAudio.setTexto(audio.isSilenciado()
            ? "SONIDO: OFF  (M)"
            : "MUSICA: " + audio.getVolumenMusica() + "%   EFECTOS: " + audio.getVolumenEfectos() + "%   (M)");
        estadoAudio.setPosicion(Config.ANCHO_MUNDO - MARGEN - estadoAudio.getAncho(), 40);
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
        mensajeCentral.centrarEn(Config.ANCHO_MUNDO / 2f, Config.ALTO_MUNDO / 2f + 40);
    }

    public void dibujar() {
        viewport.apply();
        camara.update();

        Render.comenzarBatch(camara);
        nombreIzquierda.dibujar();
        nombreDerecha.dibujar();
        rondasIzquierda.dibujar();
        rondasDerecha.dibujar();
        marcador.dibujar();
        rondaActual.dibujar();
        ayuda.dibujar();
        estadoAudio.dibujar();
        if (hayMensaje) mensajeCentral.dibujar();
        Render.terminarBatch();
    }

    public void redimensionar(int ancho, int alto) {
        viewport.update(ancho, alto, true);
    }

    public void dispose() {
        nombreIzquierda.dispose();
        nombreDerecha.dispose();
        rondasIzquierda.dispose();
        rondasDerecha.dispose();
        marcador.dispose();
        rondaActual.dispose();
        mensajeCentral.dispose();
        ayuda.dispose();
        estadoAudio.dispose();
    }
}
