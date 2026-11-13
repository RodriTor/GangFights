package Items;

import Entidades.Entidad;
import Entidades.Jugador;
import Utilidades.Render;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Objeto del mapa que un jugador puede agarrar. Clase abstracta: cada tipo de item decide
 * cuando se puede recoger (puedeRecoger) y que efecto tiene (aplicar).
 *
 * La posicion se da por la base: la parte de abajo de la imagen queda apoyada en el piso.
 */
public abstract class Item extends Entidad {

    private final TipoItem tipo;
    private final TextureRegion imagen;

    protected Item(TipoItem tipo, TextureRegion imagen, float x, float base, float ancho, float alto) {
        super(x, base + alto / 2f, ancho, alto);
        this.tipo = tipo;
        this.imagen = imagen;
    }

    protected abstract boolean puedeRecoger(Jugador jugador);

    protected abstract void aplicar(Jugador jugador);

    /** Se recoge si el jugador esta agachado y toca el item (aunque sea un poco). */
    public boolean intentarRecoger(Jugador jugador) {
        if (!jugador.isActivo() || !jugador.estaAgachado()) return false;
        if (!tocaA(jugador) || !puedeRecoger(jugador)) return false;

        aplicar(jugador);
        jugador.notificarRecogida();
        return true;
    }

    private boolean tocaA(Jugador jugador) {
        float dx = Math.abs(getX() - jugador.getX());
        float dy = Math.abs(getY() - jugador.getY());
        return dx < (getAncho() + jugador.getAncho()) / 2f
            && dy < (getAlto() + jugador.getAlto()) / 2f;
    }

    public TipoItem getTipo() {
        return tipo;
    }

    @Override
    public void actualizar(float delta) { }

    @Override
    public void dibujar() {
        Render.batch.draw(imagen, getX() - getAncho() / 2f, getY() - getAlto() / 2f, getAncho(), getAlto());
    }
}
