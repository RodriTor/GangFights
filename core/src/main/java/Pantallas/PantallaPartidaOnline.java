package Pantallas;


import Entidades.JugadorVisualOnline;
import Entidades.Personaje;
import Interfaces.NavegadorPantallas;

import Mundo.MapaOnline;
import Red.ClienteRed;
import Utilidades.Config;
import Utilidades.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



public class PantallaPartidaOnline implements Screen {
	
	private final NavegadorPantallas navegador;
	private final ClienteRed clienteRed;
	
	private OrthographicCamera camara;
	private Viewport viewport;
	
	private MapaOnline mapa;
	private JugadorVisualOnline jugador1;
	private JugadorVisualOnline jugador2;
	
	private float x1 = 300;
    private float y1 = 400;
    private float x2 = 900;
    private float y2 = 400;
    
    private boolean detenido;
    
    
    public PantallaPartidaOnline(NavegadorPantallas navegador, ClienteRed clienteRed) {
    	this.navegador = navegador;
    	this.clienteRed = clienteRed;
    }
    
    
    @Override
    public void show() {
    	camara = new OrthographicCamera();
    	
    	
    	viewport = new FitViewport(Config.ANCHO_MUNDO, Config.ALTO_MUNDO, camara);
    
    	camara.position.set(Config.ANCHO_MUNDO / 2f, Config.ALTO_MUNDO / 2f, 0);
    
    
    
    camara.update();
    
    mapa = new MapaOnline();
    
    jugador1 = new JugadorVisualOnline(Personaje.JASINSKI, x1, y1, true);
    
    jugador2 = new JugadorVisualOnline(Personaje.SCHEPIS, x2, y2, false);
    
    
    
    
    }
    
    @Override
    public void render(float delta) {
    	if(!clienteRed.estaConectado()) {
    		Render.limpiarPantalla(0, 0, 0);
    		return;
    	}
    	
    	procesarControles();
    	actualizarJugadores(delta);
    	
    	Render.limpiarPantalla(0, 0, 0);
    	
    	viewport.apply();
    	camara.update();
    	
    	
    	mapa.dibujar(camara);
    	
    	Render.comenzarBatch(camara);
    	
    	jugador1.dibujar();
    	jugador2.dibujar();
    	
    	Render.terminarBatch();
    }
    
    private void procesarControles() {
    	if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            clienteRed.enviarAccion("IZQUIERDA");
            detenido = false;

        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            clienteRed.enviarAccion("DERECHA");
            detenido = false;

        } else if (!detenido) {
            clienteRed.enviarAccion("DETENER");
            detenido = true;
        }
    	
    	if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            clienteRed.enviarAccion("SALTAR");
        }
    
    	if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            navegador.irAOnline();
        }
    }
    
    private void actualizarJugadores(float delta) {

        String estado = clienteRed.obtenerEstado();

        if (estado != null && estado.startsWith("ESTADO")) {

            String[] jugadores = estado.split(";");

            for (int i = 1; i < jugadores.length; i++) {

                String[] datos = jugadores[i].split(",");

                if (datos.length < 3) continue;

                try {
                    int numero = Integer.parseInt(datos[0]);
                    float x = Float.parseFloat(datos[1]);
                    float y = Float.parseFloat(datos[2]);

                    if (numero == 1) {
                        x1 = x;
                        y1 = y;
                    } else if (numero == 2) {
                        x2 = x;
                        y2 = y;
                    }

                } catch (NumberFormatException e) {
                    // Se ignora un estado incompleto.
                }
            }
        }

        jugador1.actualizar(x1, y1, delta);
        jugador2.actualizar(x2, y2, delta);
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
    	if (mapa != null) mapa.dispose();
        if (jugador1 != null) jugador1.dispose();
        if (jugador2 != null) jugador2.dispose();
        
        clienteRed.cerrar();
    }
    
}
