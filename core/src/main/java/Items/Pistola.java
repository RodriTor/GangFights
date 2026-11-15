package Items;

import Entidades.Jugador;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** Por ahora solo se equipa (se ve en el HUD). Un jugador no puede llevar dos. */
public class Pistola extends Item {

    public Pistola(TextureRegion imagen, float x, float base) {
        super(imagen, x, base);
    }

    @Override
    protected boolean puedeRecoger(Jugador jugador) {
        return !jugador.tienePistola();
    }

    @Override
    protected void aplicar(Jugador jugador) {
        jugador.equiparPistola();
    }
}
