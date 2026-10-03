package Pantallas;

import Audio.Efecto;
import Audio.GestorAudio;
import Elementos.Imagen;
import Elementos.Texto;
import Entradas.ControlAudio;
import Interfaces.NavegadorPantallas;
import Utilidades.Config;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class PantallaConfiguracion implements Screen {

    private static final String[] CATEGORIAS = {"SONIDO", "PANTALLA", "CONTROLES", "CREDITOS"};
    private static final int SONIDO = 0, PANTALLA = 1, CONTROL = 2, FILAS = 2;

    private static final int[][] RESOLUCIONES = {{1280, 720}, {1366, 768}, {1600, 900}, {1920, 1080}};
    private static final String[] CONTROLES = {
        "JUGADOR 1", "Moverse: A / D", "Saltar: W", "Agacharse: S", "Golpear: 1",
        "JUGADOR 2", "Moverse: FLECHA IZQ / DER", "Saltar: FLECHA ARRIBA", "Agacharse: FLECHA ABAJO", "Golpear: N"};

    private static final float X_CATEGORIAS = 100f, X_PANEL = 420f, X_VALORES = 990f, Y_INICIO = 560f;
    private static final float ESCALA_ELEGIDA = 1.2f;

    private final NavegadorPantallas navegador;
    private final GestorAudio audio;
    private final ControlAudio controlAudio;
    private final Texto[] categoriasTexto = new Texto[CATEGORIAS.length];
    private final Texto[] etiquetas = new Texto[CONTROLES.length];
    private final Texto[] valores = new Texto[FILAS];

    private OrthographicCamera camara;
    private Viewport viewport;
    private Texto titulo, ayuda;
    private Imagen logo;

    private int categoria = 0, fila = 0;
    private boolean enOpciones = false;
    private boolean pantallaCompleta;
    private int resolucion;
    private int usadas;
    private int usadasValores;
    private float y;

    public PantallaConfiguracion(NavegadorPantallas navegador, GestorAudio audio) {
        this.navegador = navegador;
        this.audio = audio;
        this.controlAudio = new ControlAudio(audio);
    }

    @Override
    public void show() {
        camara = new OrthographicCamera();
        viewport = new FitViewport(Config.ANCHO_MUNDO, Config.ALTO_MUNDO, camara);

        titulo = nuevoTexto(44, Color.YELLOW);
        titulo.setTexto("CONFIGURACION");
        titulo.setPosicion(X_CATEGORIAS, 675);
        ayuda = nuevoTexto(18, Color.LIGHT_GRAY);
        for (int i = 0; i < categoriasTexto.length; i++) {
            categoriasTexto[i] = nuevoTexto(26, Color.WHITE);
            categoriasTexto[i].setTexto(CATEGORIAS[i]);
        }
        for (int i = 0; i < etiquetas.length; i++) etiquetas[i] = nuevoTexto(26, Color.WHITE);
        for (int i = 0; i < valores.length; i++) valores[i] = nuevoTexto(26, Color.WHITE);

        logo = new Imagen(Recursos.LogoPrograma);
        float alto = 90f;
        logo.setTamanio(logo.getAncho() * alto / logo.getAlto(), alto);
        logo.setPosicion((Config.ANCHO_MUNDO - logo.getAncho()) / 2f, 15f);

        // Estado actual de la ventana
        pantallaCompleta = Gdx.graphics.isFullscreen();
        for (int i = 0; i < RESOLUCIONES.length; i++) {
            if (RESOLUCIONES[i][0] == Gdx.graphics.getWidth() && RESOLUCIONES[i][1] == Gdx.graphics.getHeight()) {
                resolucion = i + 1;
            }
        }

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        refrescar();
    }

    private Texto nuevoTexto(int tamanio, Color color) {
        return new Texto(Recursos.FUENTE_MENU, tamanio, color);
    }

    @Override
    public void render(float delta) {
        controlAudio.actualizar();
        procesarTeclas();

        Render.limpiarPantalla(0, 0, 0);
        viewport.apply();
        camara.update();

        Render.comenzarBatch(camara);
        titulo.dibujar();
        ayuda.dibujar();
        for (Texto t : categoriasTexto) t.dibujar();
        for (int i = 0; i < usadas; i++) etiquetas[i].dibujar();
        for (int i = 0; i < usadasValores; i++) valores[i].dibujar();
        logo.dibujar();
        Render.terminarBatch();
    }


    private boolean tecla(int tecla) {
        return Gdx.input.isKeyJustPressed(tecla);
    }

    private void procesarTeclas() {
        boolean arriba = tecla(Input.Keys.UP), abajo = tecla(Input.Keys.DOWN);
        boolean izquierda = tecla(Input.Keys.LEFT), derecha = tecla(Input.Keys.RIGHT);

        if (tecla(Input.Keys.ESCAPE)) {
            if (!enOpciones) {
                navegador.irAMenu();
                return;
            }
            enOpciones = false;
        } else if (arriba || abajo) {
            int paso = arriba ? -1 : 1;
            if (enOpciones) {
                fila = (fila + paso + FILAS) % FILAS;
            } else {
                categoria = (categoria + paso + CATEGORIAS.length) % CATEGORIAS.length;
            }
            audio.reproducirEfecto(Efecto.CLICK);
        } else if (enOpciones && (izquierda || derecha)) {
            cambiarValor(derecha ? 1 : -1);
        } else if (!enOpciones && (derecha || tecla(Input.Keys.ENTER)) && categoria <= PANTALLA) {
            enOpciones = true;
            fila = 0;
        } else {
            return; }

        refrescar();
    }

    private void cambiarValor(int paso) {
        if (categoria == SONIDO) {
            if (fila == 0) {
                audio.cambiarVolumenMusica(paso);
            } else {
                audio.cambiarVolumenEfectos(paso);
                audio.reproducirEfecto(Efecto.GOLPE);
            }
        } else if (fila == 0) {
            pantallaCompleta = !pantallaCompleta;
            aplicarPantalla();
        } else {
            resolucion = (resolucion + paso + RESOLUCIONES.length + 1) % (RESOLUCIONES.length + 1);
            aplicarPantalla();
        }
    }


    private int[] tamanioElegido() {
        if (resolucion > 0) return RESOLUCIONES[resolucion - 1];
        DisplayMode monitor = Gdx.graphics.getDisplayMode();
        return pantallaCompleta ? new int[]{monitor.width, monitor.height} : new int[]{1280, 720};
    }

    private void aplicarPantalla() {
        int[] tamanio = tamanioElegido();
        if (!pantallaCompleta) {
            Gdx.graphics.setWindowedMode(tamanio[0], tamanio[1]);
            return;
        }

        DisplayMode elegido = null;
        for (DisplayMode modo : Gdx.graphics.getDisplayModes()) {
            if (modo.width == tamanio[0] && modo.height == tamanio[1]
                && (elegido == null || modo.refreshRate > elegido.refreshRate)) {
                elegido = modo;
            }
        }
        Gdx.graphics.setFullscreenMode(elegido != null ? elegido : Gdx.graphics.getDisplayMode());
    }


    private void refrescar() {
        for (int i = 0; i < categoriasTexto.length; i++) {
            boolean elegida = (i == categoria);
            categoriasTexto[i].setEscala(elegida ? ESCALA_ELEGIDA : 1f);
            categoriasTexto[i].setColor(elegida ? Color.YELLOW : Color.WHITE);
            categoriasTexto[i].setPosicion(X_CATEGORIAS, Y_INICIO - i * 70f);
        }

        usadas = 0;
        usadasValores = 0;
        y = Y_INICIO;

        if (categoria == SONIDO) {
            agregarFila(0, "Ajustar musica", audio.getVolumenMusica() + "%");
            agregarFila(1, "Ajustar efectos", audio.getVolumenEfectos() + "%");
        } else if (categoria == PANTALLA) {
            int[] t = tamanioElegido();
            String medidas = t[0] + "x" + t[1];
            agregarFila(0, "Modo de pantalla", pantallaCompleta ? "Pantalla completa" : "Ventana");
            agregarFila(1, "Resolucion", resolucion == 0 ? "Recomendada (" + medidas + ")" : medidas);
        } else if (categoria == CONTROL) {
            for (String linea : CONTROLES) {
                if (linea.equals("JUGADOR 2")) y -= 20f;
                boolean jugador = linea.startsWith("JUGADOR");
                agregarLinea(linea, !jugador ? Color.WHITE : linea.endsWith("1") ? Color.CYAN : Color.ORANGE);
            }
        }

        if (enOpciones) {
            ayuda.setTexto("ARRIBA / ABAJO: opcion     IZQ / DER: cambiar     ESC: atras");
        } else {
            ayuda.setTexto("ARRIBA / ABAJO: categoria     "
                + (categoria <= PANTALLA ? "ENTER: entrar     " : "") + "ESC: volver al menu");
        }
        ayuda.centrarEn(Config.ANCHO_MUNDO / 2f, 140);
    }

    private void agregarFila(int numeroFila, String etiqueta, String valor) {
        boolean elegida = enOpciones && fila == numeroFila;
        float escala = elegida ? ESCALA_ELEGIDA : 1f;
        Color color = elegida ? Color.YELLOW : Color.WHITE;

        Texto izquierda = etiquetas[usadas++];
        izquierda.setTexto(etiqueta);
        izquierda.setEscala(escala);
        izquierda.setColor(color);
        izquierda.setPosicion(X_PANEL, y);

        Texto derecha = valores[usadasValores++];
        derecha.setTexto("<  " + valor + "  >");
        derecha.setEscala(escala);
        derecha.setColor(color);
        derecha.centrarEn(X_VALORES, y);

        y -= 55f;
    }

    private void agregarLinea(String texto, Color color) {
        Texto linea = etiquetas[usadas++];
        linea.setTexto(texto);
        linea.setEscala(1f);
        linea.setColor(color);
        linea.setPosicion(X_PANEL, y);
        y -= 38f;
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
        titulo.dispose();
        ayuda.dispose();
        logo.dispose();
        for (Texto t : categoriasTexto) t.dispose();
        for (Texto t : etiquetas) t.dispose();
        for (Texto t : valores) t.dispose();
    }
}
