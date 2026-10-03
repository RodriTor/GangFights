package Pantallas;

import Audio.Efecto;
import Audio.GestorAudio;
import Audio.Musica;
import Elementos.Imagen;
import Elementos.Texto;
import Entradas.ControlAudio;
import Interfaces.NavegadorPantallas;
import Utilidades.Config;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MenuInicial implements Screen {

    private static final float PRIMERA_OPCION_Y = 120f;   // borde superior del texto
    private static final float SEPARACION = 45f;
    private static final float ESCALA_SELECCIONADA = 1.4f;
    private static final float ESCALA_NORMAL = 1.0f;

    private final NavegadorPantallas navegador;
    private final GestorAudio audio;
    private final ControlAudio controlAudio;
    private final OpcionMenu[] opciones = OpcionMenu.values();
    private final Texto[] textos = new Texto[opciones.length];

    private OrthographicCamera camara;
    private Viewport viewport;
    private Imagen fondo;
    private Texto ayudaAudio;

    private int seleccion = 0;

    public MenuInicial(NavegadorPantallas navegador, GestorAudio audio) {
        this.navegador = navegador;
        this.audio = audio;
        this.controlAudio = new ControlAudio(audio);
    }

    @Override
    public void show() {
        camara = new OrthographicCamera();
        viewport = new FitViewport(Config.ANCHO_MUNDO, Config.ALTO_MUNDO, camara);

        fondo = new Imagen(Recursos.MENU_INICIAL);
        fondo.setTamanio(Config.ANCHO_MUNDO, Config.ALTO_MUNDO);
        fondo.setPosicion(0, 0);

        for (int i = 0; i < opciones.length; i++) {
            textos[i] = new Texto(Recursos.FUENTE_MENU, 20, Color.WHITE);
            textos[i].setTexto(opciones[i].getTexto());
        }

        ayudaAudio = new Texto(Recursos.FUENTE_MENU, 16, Color.LIGHT_GRAY);
        ayudaAudio.setTexto("ARRIBA / ABAJO: elegir    ENTER: seleccionar    M: silenciar");
        ayudaAudio.setPosicion(20, 30);

        // Se oculta el cursor: el menu solo se maneja con el teclado
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);

        audio.reproducirMusica(Musica.MENU);
        actualizarAspectoOpciones();
    }

    @Override
    public void render(float delta) {
        controlAudio.actualizar();
        procesarTeclado();

        Render.limpiarPantalla(0, 0, 0);
        viewport.apply();
        camara.update();

        Render.comenzarBatch(camara);
        fondo.dibujar();
        for (Texto texto : textos) {
            texto.dibujar();
        }
        ayudaAudio.dibujar();
        Render.terminarBatch();
    }

    private void procesarTeclado() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccion = (seleccion - 1 + opciones.length) % opciones.length;
            audio.reproducirEfecto(Efecto.CLICK);
            actualizarAspectoOpciones();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccion = (seleccion + 1) % opciones.length;
            audio.reproducirEfecto(Efecto.CLICK);
            actualizarAspectoOpciones();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            audio.reproducirEfecto(Efecto.CLICK);
            ejecutarOpcion(opciones[seleccion]);
        }
    }

    private void ejecutarOpcion(OpcionMenu opcion) {
        switch (opcion) {
            case PLAY:
                navegador.irAJuego();
                break;
            case CONFIGURACION:
                navegador.irAConfiguracion();
                break;
        }
    }

    private void actualizarAspectoOpciones() {
        for (int i = 0; i < textos.length; i++) {
            boolean elegida = (i == seleccion);
            textos[i].setEscala(elegida ? ESCALA_SELECCIONADA : ESCALA_NORMAL);
            textos[i].setColor(elegida ? Color.YELLOW : Color.WHITE);
            textos[i].centrarEn(Config.ANCHO_MUNDO / 2f, PRIMERA_OPCION_Y - i * SEPARACION);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }

    @Override
    public void hide() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    @Override
    public void dispose() {
        if (fondo != null) fondo.dispose();
        if (ayudaAudio != null) ayudaAudio.dispose();
        for (Texto texto : textos) {
            if (texto != null) texto.dispose();
        }
    }
}
