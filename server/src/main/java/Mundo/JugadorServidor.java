
package Mundo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class JugadorServidor {

    private static final float PIXELES_POR_METRO = 10f;
    private static final float ANCHO = 30f;
    private static final float ALTO = 60f;
    private static final float VELOCIDAD = 13f;
    private static final float VELOCIDAD_SALTO = 21f;

    private final int numeroJugador;
    private final Body cuerpo;

    private int contactosSuelo = 0;

    public JugadorServidor(World mundo, int numeroJugador, float x, float y) {

        this.numeroJugador = numeroJugador;

        BodyDef cuerpoDef = new BodyDef();
        cuerpoDef.type = BodyDef.BodyType.DynamicBody;
        cuerpoDef.position.set(
            x / PIXELES_POR_METRO,
            y / PIXELES_POR_METRO
        );
        cuerpoDef.fixedRotation = true;

        cuerpo = mundo.createBody(cuerpoDef);

        // Cuerpo principal del jugador
        PolygonShape forma = new PolygonShape();
        forma.setAsBox(
            (ANCHO / 2) / PIXELES_POR_METRO,
            (ALTO / 2) / PIXELES_POR_METRO
        );

        FixtureDef fixture = new FixtureDef();
        fixture.shape = forma;
        fixture.density = 1f;
        fixture.friction = 0f;

        cuerpo.createFixture(fixture);
        forma.dispose();

        // Sensor ubicado debajo de los pies
        PolygonShape formaSensor = new PolygonShape();
        formaSensor.setAsBox(
            0.8f,
            0.08f,
            new Vector2(0, -0.08f - (ALTO / 2) / PIXELES_POR_METRO),
            0
        );

        FixtureDef sensor = new FixtureDef();
        sensor.shape = formaSensor;
        sensor.isSensor = true;

        cuerpo.createFixture(sensor).setUserData(this);
        formaSensor.dispose();
    }

    public void moverIzquierda() {
        cuerpo.setLinearVelocity(
            -VELOCIDAD,
            cuerpo.getLinearVelocity().y
        );
    }

    public void moverDerecha() {
        cuerpo.setLinearVelocity(
            VELOCIDAD,
            cuerpo.getLinearVelocity().y
        );
    }

    public void detener() {
        cuerpo.setLinearVelocity(
            0,
            cuerpo.getLinearVelocity().y
        );
    }

    public void saltar() {
        if (estaEnSuelo()) {
            cuerpo.setLinearVelocity(
                cuerpo.getLinearVelocity().x,
                VELOCIDAD_SALTO
            );
        }
    }

    public boolean estaEnSuelo() {
        return contactosSuelo > 0;
    }

    public void registrarContactoSuelo(boolean entrando) {
        if (entrando) {
            contactosSuelo++;
        } else {
            contactosSuelo = Math.max(0, contactosSuelo - 1);
        }
    }

    public Vector2 obtenerPosicion() {
        return cuerpo.getPosition().cpy().scl(PIXELES_POR_METRO);
    }

    public int getNumeroJugador() {
        return numeroJugador;
    }

    public void eliminar() {
        cuerpo.getWorld().destroyBody(cuerpo);
    }
}
