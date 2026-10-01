package Controles;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class EntradaJugador {

	private int idJugador;
	
	
	public EntradaJugador(int idJugador) {
		this.idJugador = idJugador;
	}
	
	public boolean abajo() {
		if(idJugador == 1) {
			return Gdx.input.isKeyPressed(Input.Keys.S);
		} else {
			return Gdx.input.isKeyPressed(Input.Keys.DOWN);
		}
	}
	
	
	public boolean izquierda() {
		if(idJugador == 1) {
			return Gdx.input.isKeyPressed(Input.Keys.A);
		} else {
			return Gdx.input.isKeyPressed(Input.Keys.LEFT);
		}
	}
	
	public boolean derecha() {
		if(idJugador == 1) {
			return Gdx.input.isKeyPressed(Input.Keys.D);
		} else {
			return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		}
	}
	
	public boolean saltar() {
		if(idJugador == 1) {
			return Gdx.input.isKeyPressed(Input.Keys.W);
		} else {
			return Gdx.input.isKeyPressed(Input.Keys.UP);
		}
	}
	
	public boolean golpear() {
		if(idJugador == 1) {
			return Gdx.input.isKeyPressed(Input.Keys.NUM_1);
		} else {
			return Gdx.input.isKeyPressed(Input.Keys.N);
		}
	}

}
