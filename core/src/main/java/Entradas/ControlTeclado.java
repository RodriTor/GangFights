package Entradas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.EnumMap;

public class ControlTeclado extends ControlJugador {

    private final EnumMap<Accion, Integer> teclas = new EnumMap<>(Accion.class);

    public ControlTeclado(int izquierda, int derecha, int saltar, int agachar, int golpear, int escapar) {
        teclas.put(Accion.IZQUIERDA, izquierda);
        teclas.put(Accion.DERECHA, derecha);
        teclas.put(Accion.SALTAR, saltar);
        teclas.put(Accion.AGACHAR, agachar);
        teclas.put(Accion.GOLPEAR, golpear);
        teclas.put(Accion.ESCAPAR, escapar);
    }

    public static ControlTeclado crearJugador1() {
        return new ControlTeclado(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S, Input.Keys.NUM_1, Input.Keys.A);
    }

    public static ControlTeclado crearJugador2() {
        return new ControlTeclado(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.N, Input.Keys.LEFT);
    }

    @Override
    public boolean estaPresionada(Accion accion) {
        return Gdx.input.isKeyPressed(teclas.get(accion));
    }

    @Override
    public boolean fuePresionada(Accion accion) {
        return Gdx.input.isKeyJustPressed(teclas.get(accion));
    }
}
