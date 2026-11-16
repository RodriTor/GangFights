
package Pantallas;

import Audio.Efecto;
import Audio.GestorAudio;
import Elementos.Texto;
import Entradas.ControlAudio;
import Interfaces.NavegadorPantallas;
import Red.ClienteRed;
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

public class PantallaOnline implements Screen {

    private enum Opcion {
        BUSCAR("BUSCAR SERVIDOR"),
        VOLVER("VOLVER");

        private final String texto;

        Opcion(String texto) {
            this.texto = texto;
        }
    }

    private static final float PRIMERA_OPCION_Y = 470f;
    private static final float SEPARACION = 60f;
    private static final float Y_ESTADO = 300f;
    private static final float ESCALA_ELEGIDA = 1.3f;

    private final NavegadorPantallas navegador;
    private final GestorAudio audio;
    private final ControlAudio controlAudio;

    private final Opcion[] opciones = Opcion.values();
    private final Texto[] textos = new Texto[opciones.length];

    private OrthographicCamera camara;
    private Viewport viewport;

    private Texto titulo;
    private Texto estado;
    private Texto ayuda;

    private ClienteRed clienteRed;

    private int seleccion = 0;

    private boolean solicitandoIP = false;
    private boolean conexionTransferida = false;

    public PantallaOnline(
        NavegadorPantallas navegador,
        GestorAudio audio
    ) {
        this.navegador = navegador;
        this.audio = audio;
        this.controlAudio = new ControlAudio(audio);
    }

    @Override
    public void show() {
    	
        camara = new OrthographicCamera();

        viewport = new FitViewport(
            Config.ANCHO_MUNDO,
            Config.ALTO_MUNDO,
            camara
        );

        titulo = new Texto(Recursos.FUENTE_MENU, 44, Color.YELLOW);
        titulo.setTexto("ONLINE");
        titulo.centrarEn(Config.ANCHO_MUNDO / 2f, 640);

        for (int i = 0; i < opciones.length; i++) {

            textos[i] = new Texto(
                Recursos.FUENTE_MENU,
                26,
                Color.WHITE
            );

            textos[i].setTexto(opciones[i].texto);
        }

        estado = new Texto(Recursos.FUENTE_MENU,20,Color.LIGHT_GRAY);

        estado.setTexto("Busca un servidor para comenzar");

        estado.centrarEn(Config.ANCHO_MUNDO / 2f, Y_ESTADO);

        ayuda = new Texto(Recursos.FUENTE_MENU, 16 ,Color.LIGHT_GRAY
        );

        ayuda.setTexto("ARRIBA / ABAJO: elegir    ENTER: seleccionar    ESC: volver");

        ayuda.setPosicion(20, 30);

        clienteRed = new ClienteRed();

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);

        actualizarAspectoOpciones();
    }

    @Override
    public void render(float delta) {
    	
        controlAudio.actualizar();

        actualizarConexion();
        procesarTeclado();

        Render.limpiarPantalla(0, 0, 0);

        viewport.apply();
        camara.update();

        Render.comenzarBatch(camara);

        titulo.dibujar();

        for (Texto texto : textos) {
            texto.dibujar();
        }

        estado.dibujar();
        ayuda.dibujar();

        Render.terminarBatch();
    }


private void actualizarConexion() {
	
	if (clienteRed == null) {
		return;
	}
	
	if (clienteRed.estaConectado()) {
		
		int jugadores = clienteRed.obtenerJugadoresConectados();
		
		if(clienteRed.estaPartidaIniciada()) {
			
			estado.setTexto("PARTIDA INICIADA!");
			
			estado.centrarEn(Config.ANCHO_MUNDO / 2f, Y_ESTADO);
			
			
			if(!conexionTransferida) {
				conexionTransferida = true;
				navegador.irAPartidaOnline(clienteRed);
			}
		} else {
			
			estado.setTexto("CONECTADO - JUGADOR " + clienteRed.obtenerNumeroJugador() + " | JUGADORES: " + jugadores + "/2" + " | ESPERANDO");
			
		}
		
		
	}
    
}


    private void procesarTeclado() {

        if (solicitandoIP) {
            return;
        }

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

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            navegador.irAMenu();
        }
    }

    private void ejecutarOpcion(Opcion opcion) {

        switch (opcion) {

            case BUSCAR:
                buscarServidor();
                break;

            case VOLVER:
                navegador.irAMenu();
                break;
        }
    }

    private void buscarServidor() {

        if (clienteRed.estaConectado()) {
            estado.setTexto("Ya estas conectado como jugador "
                + clienteRed.obtenerNumeroJugador());
            return;
        }

        if (clienteRed.estaConectando()) {
            return;
        }

        estado.setTexto("Conectando con el servidor...");
        estado.centrarEn(Config.ANCHO_MUNDO / 2f, Y_ESTADO);

        System.out.println("Iniciando conexion con el servidor...");

        clienteRed.conectar("127.0.0.1", 5000);
    }

    private void actualizarAspectoOpciones() {

        for (int i = 0; i < textos.length; i++) {

            boolean elegida = (i == seleccion);

            textos[i].setEscala(elegida ? ESCALA_ELEGIDA : 1f);

            textos[i].setColor(elegida ? Color.YELLOW : Color.WHITE);

            textos[i].centrarEn(Config.ANCHO_MUNDO / 2f,PRIMERA_OPCION_Y - i * SEPARACION);
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

        if (clienteRed != null && !conexionTransferida) {
            clienteRed.cerrar();
        }

        if (titulo != null) titulo.dispose();
        if (estado != null) estado.dispose();
        if (ayuda != null) ayuda.dispose();

        for (Texto texto : textos) {

            if (texto != null) {
                texto.dispose();
            }
        }
    }
}
