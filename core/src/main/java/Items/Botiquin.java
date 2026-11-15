package Items;

import Entidades.Jugador;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** Recupera 30 de vida (sin pasar del maximo). Solo se agarra si el jugador perdio vida. */
public class Botiquin extends Item {

    private static final int CURACION = 30;

    public Botiquin(TextureRegion imagen, float x, float base) {
        super(imagen, x, base);
    }

    @Override
    protected boolean puedeRecoger(Jugador jugador) {
        return jugador.getVida() < Jugador.VIDA_MAXIMA;
    }

    @Override
    protected void aplicar(Jugador jugador) {
        jugador.curar(CURACION);
    }
}
