package Pantallas;

import Audio.Efecto;
import Audio.GestorAudio;
import Elementos.Texto;
import Entradas.ControlAudio;
import Interfaces.NavegadorPantallas;
import Logica.Partida;
import Utilidades.Config;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaFinal implements Screen {

    private final NavegadorPantallas navegador;
    private final GestorAudio audio;
    private final ControlAudio controlAudio;
    private final Partida partida;

    private OrthographicCamera camara;
    private Viewport viewport;
    private Texto titulo;
    private Texto resultado;
    private Texto opciones;

    public PantallaFinal(NavegadorPantallas navegador, GestorAudio audio, Partida partida) {
        this.navegador = navegador;
        this.audio = audio;
        this.partida = partida;
        this.controlAudio = new ControlAudio(audio);
    }

    @Override
    public void show() {
        camara = new OrthographicCamera();
        viewport = new FitViewport(Config.ANCHO_MUNDO, Config.ALTO_MUNDO, camara);
        float centroX = Config.ANCHO_MUNDO / 2f;

        titulo = new Texto(Recursos.FUENTE_MENU, 64, Color.YELLOW);
        titulo.setTexto("GANA " + partida.getNombre(partida.getGanadorPartida()));
        titulo.centrarEn(centroX, 460);

        resultado = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE);
        resultado.setTexto(partida.getRondasGanadas(0) + " - " + partida.getRondasGanadas(1));
        resultado.centrarEn(centroX, 360);

        opciones = new Texto(Recursos.FUENTE_MENU, 24, Color.LIGHT_GRAY);
        opciones.setTexto("ESC: menu principal");
        opciones.centrarEn(centroX, 200);

        audio.reproducirEfecto(Efecto.VICTORIA);
    }

    @Override
    public void render(float delta) {
        controlAudio.actualizar();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            navegador.irAJuego();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            navegador.irAMenu();
        }

        Render.limpiarPantalla(0.05f, 0.05f, 0.1f);
        viewport.apply();
        camara.update();

        Render.comenzarBatch(camara);
        titulo.dibujar();
        resultado.dibujar();
        opciones.dibujar();
        Render.terminarBatch();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        if (titulo != null) titulo.dispose();
        if (resultado != null) resultado.dispose();
        if (opciones != null) opciones.dispose();
    }
}
