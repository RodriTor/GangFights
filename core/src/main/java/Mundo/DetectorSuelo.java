package Mundo;

import Entidades.Jugador;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class DetectorSuelo implements ContactListener {

    @Override
    public void beginContact(Contact contacto) {
        procesar(contacto, true);
    }

    @Override
    public void endContact(Contact contacto) {
        procesar(contacto, false);
    }

    private void procesar(Contact contacto, boolean inicio) {
        Fixture a = contacto.getFixtureA();
        Fixture b = contacto.getFixtureB();
        notificar(a, b, inicio);
        notificar(b, a, inicio);
    }

    private void notificar(Fixture posibleSensor, Fixture otro, boolean inicio) {
        if (posibleSensor.isSensor() && !otro.isSensor()
            && posibleSensor.getUserData() instanceof Jugador) {

            Jugador jugador = (Jugador) posibleSensor.getUserData();
            if (inicio) {
                jugador.sumarContactoSuelo();
            } else {
                jugador.restarContactoSuelo();
            }
        }
    }

    @Override
    public void preSolve(Contact contacto, Manifold viejo) { }

    @Override
    public void postSolve(Contact contacto, com.badlogic.gdx.physics.box2d.ContactImpulse impulso) { }
}
