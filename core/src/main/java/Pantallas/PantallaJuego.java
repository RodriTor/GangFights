package Pantallas;

import Audio.Efecto;
import Audio.GestorAudio;
import Audio.Musica;
import Elementos.Hud;
import Entidades.EventosJugador;
import Entidades.Jugador;
import Entidades.Personaje;
import Entradas.ControlAudio;
import Entradas.ControlTeclado;
import Interfaces.NavegadorPantallas;
import Logica.EstadoPartida;
import Logica.Partida;
import Mundo.DetectorSuelo;
import Mundo.Mapa;
import Utilidades.Config;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Pantalla de la partida. Es dueña (crea y libera) del World, el Mapa,
 * los jugadores y el HUD.
 * Implementa EventosJugador para reproducir sonidos cuando algo pasa.
 */
public class PantallaJuego implements Screen, EventosJugador {

    private static final float TIEMPO_TRANSICION = 2.5f;
    private static final boolean MOSTRAR_COLISIONES = false; // true: dibuja las cajas de Box2D para revisar el mapa

    private final NavegadorPantallas navegador;
    private final GestorAudio audio;
    private final ControlAudio controlAudio;

    private OrthographicCamera camara;
    private Viewport viewport;
    private Box2DDebugRenderer depurador;   // solo para ver las colisiones (MOSTRAR_COLISIONES)
    private World mundo;
    private Mapa mapa;
    private Jugador jugador1;
    private Jugador jugador2;
    private Partida partida;
    private Hud hud;

    private float acumuladorFisica = 0f;
    private float tiempoTransicion = 0f;

    public PantallaJuego(NavegadorPantallas navegador, GestorAudio audio) {
        this.navegador = navegador;
        this.audio = audio;
        this.controlAudio = new ControlAudio(audio);
    }

    @Override
    public void show() {
        camara = new OrthographicCamera();
        viewport = new FitViewport(Config.ANCHO_MUNDO, Config.ALTO_MUNDO, camara);
        depurador = new Box2DDebugRenderer();

        mundo = new World(new Vector2(0, Config.GRAVEDAD), true);
        mundo.setContactListener(new DetectorSuelo());
        mapa = new Mapa(mundo);

        jugador1 = new Jugador(mundo, 230, 300, Personaje.JASINSKI, ControlTeclado.crearJugador1(), true);
        jugador2 = new Jugador(mundo, 1050, 300, Personaje.SCHEPIS, ControlTeclado.crearJugador2(), false);
        jugador1.setOyente(this);
        jugador2.setOyente(this);

        partida = new Partida(Personaje.JASINSKI.getNombre(), Personaje.SCHEPIS.getNombre());
        hud = new Hud(Personaje.JASINSKI, Personaje.SCHEPIS);

        audio.reproducirMusica(Musica.JUEGO);
    }

    // ------------------------------------------------------------- bucle

    @Override
    public void render(float delta) {
        controlAudio.actualizar();
        procesarTeclasDePantalla();

        if (partida.getEstado() != EstadoPartida.PAUSADA) {
            actualizarJuego(delta);
        }

        dibujarMundo();
        hud.actualizar(partida, audio);
        hud.dibujar();
    }

    private void procesarTeclasDePantalla() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            partida.alternarPausa();
        }
        if (partida.getEstado() == EstadoPartida.PAUSADA && Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            navegador.irAMenu();
        }
    }

    private void actualizarJuego(float delta) {
        jugador1.actualizar(delta);
        jugador2.actualizar(delta);

        if (partida.getEstado() == EstadoPartida.EN_CURSO) {
            jugador1.comprobarAtaque(jugador2);
            jugador2.comprobarAtaque(jugador1);
            verificarFinDeRonda();
        } else {
            avanzarTransicion(delta);
        }

        avanzarFisica(delta);
    }

    /** Paso fijo: la simulacion avanza igual sin importar cuantos FPS tenga el juego. */
    private void avanzarFisica(float delta) {
        acumuladorFisica += Math.min(delta, Config.MAXIMO_DELTA);
        while (acumuladorFisica >= Config.PASO_FISICA) {
            mundo.step(Config.PASO_FISICA, Config.ITERACIONES_VELOCIDAD, Config.ITERACIONES_POSICION);
            acumuladorFisica -= Config.PASO_FISICA;
        }
    }

    private void verificarFinDeRonda() {
        if (!jugador1.isActivo()) {
            partida.terminarRonda(1);
        } else if (!jugador2.isActivo()) {
            partida.terminarRonda(0);
        }
    }

    private void avanzarTransicion(float delta) {
        tiempoTransicion += delta;
        if (tiempoTransicion < TIEMPO_TRANSICION) return;

        tiempoTransicion = 0f;
        if (partida.getEstado() == EstadoPartida.FINALIZADA) {
            navegador.irAFinal(partida);
        } else {
            partida.siguienteRonda();
            jugador1.reaparecer();
            jugador2.reaparecer();
        }
    }

    private void dibujarMundo() {
        Render.limpiarPantalla(0, 0, 0);
        viewport.apply();
        camara.update();

        mapa.dibujar(camara);

        Render.comenzarBatch(camara);
        jugador1.dibujar();
        jugador2.dibujar();
        Render.terminarBatch();

        if (MOSTRAR_COLISIONES) {
            depurador.render(mundo, camara.combined.cpy().scl(Config.PIXELES_POR_METRO));
        }
    }

    // ------------------------------------------------- EventosJugador

    @Override
    public void alSaltar() {
        audio.reproducirEfecto(Efecto.SALTO);
    }

    @Override
    public void alGolpear() {
        audio.reproducirEfecto(Efecto.GOLPE);
    }

    @Override
    public void alSerEliminado() {
        audio.reproducirEfecto(Efecto.ELIMINACION);
    }

    // ---------------------------------------------------------- Screen

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.redimensionar(width, height);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        if (hud != null) hud.dispose();
        if (jugador1 != null) jugador1.dispose();
        if (jugador2 != null) jugador2.dispose();
        if (depurador != null) depurador.dispose();
        if (mapa != null) mapa.dispose();
        if (mundo != null) mundo.dispose();
    }
}
