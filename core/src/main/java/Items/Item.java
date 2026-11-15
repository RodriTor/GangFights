package Items;

import Entidades.Entidad;
import Entidades.Jugador;
import Utilidades.Render;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Objeto del mapa que un jugador puede agarrar. Cada tipo de item decide cuando se puede
 * recoger (puedeRecoger) y que efecto tiene (aplicar).
 * Se crea con la posicion de su base: la parte de abajo del dibujo queda apoyada en el piso.
 */
public abstract class Item extends Entidad {

    private static final float ESCALA = 2f;

    private final TextureRegion imagen;

    protected Item(TextureRegion imagen, float x, float base) {
        super(x, base + imagen.getRegionHeight() * ESCALA / 2f,
            imagen.getRegionWidth() * ESCALA, imagen.getRegionHeight() * ESCALA);
        this.imagen = imagen;
    }

    protected abstract boolean puedeRecoger(Jugador jugador);

    protected abstract void aplicar(Jugador jugador);

    /** Se recoge si el jugador esta agachado y toca el item (aunque sea un poco). */
    public boolean intentarRecoger(Jugador jugador) {
        if (!jugador.isActivo() || !jugador.estaAgachado()) return false;
        if (!tocaA(jugador) || !puedeRecoger(jugador)) return false;

        aplicar(jugador);
        return true;
    }

    private boolean tocaA(Jugador jugador) {
        float distanciaX = Math.abs(getX() - jugador.getX());
        float distanciaY = Math.abs(getY() - jugador.getY());

        return distanciaX < (getAncho() + jugador.getAncho()) / 2f
            && distanciaY < (getAlto() + jugador.getAlto()) / 2f;
    }

    @Override
    public void actualizar(float delta) { }

    @Override
    public void dibujar() {
        Render.batch.draw(imagen, getX() - getAncho() / 2f, getY() - getAlto() / 2f, getAncho(), getAlto());
    }
}
