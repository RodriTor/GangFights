
package Mundo;

import com.badlogic.gdx.math.Vector2;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.List;

public class MundoServidor {

    private static final float PIXELES_POR_METRO = 10f;
    private static final float ANCHO_MAPA = 1280f;
    private static final float ALTO_MAPA = 544f;
    
    private final ConcurrentLinkedQueue<Runnable> comandos = new ConcurrentLinkedQueue<>();
    private final Map<Integer, JugadorServidor> jugadores = new HashMap<>();

    private final World mundo;

  
    public MundoServidor() {

        mundo = new World(
            new Vector2(0, -40f),
            true
        );

        mundo.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contacto) {
                actualizarContacto(contacto, true);
            }

            @Override
            public void endContact(Contact contacto) {
                actualizarContacto(contacto, false);
            }

            @Override
            public void preSolve(Contact contacto,
                                 com.badlogic.gdx.physics.box2d.Manifold manifold) {
            }

            @Override
            public void postSolve(Contact contacto,
                                  com.badlogic.gdx.physics.box2d.ContactImpulse impulso) {
            }
        });

        crearPlataformas();
    }

    private void actualizarContacto(Contact contacto, boolean entrando) {

        Fixture fixtureA = contacto.getFixtureA();
        Fixture fixtureB = contacto.getFixtureB();

        if (fixtureA.getUserData() instanceof JugadorServidor) {
            JugadorServidor jugador =
                (JugadorServidor) fixtureA.getUserData();

            jugador.registrarContactoSuelo(entrando);
        }

        if (fixtureB.getUserData() instanceof JugadorServidor) {
            JugadorServidor jugador =
                (JugadorServidor) fixtureB.getUserData();

            jugador.registrarContactoSuelo(entrando);
        }
    }

    private void crearPlataformas() {

        List<float[]> plataformas =
        		CargadorColisiones.cargar("../assets/Mapas/Aula.tmx");

        for (float[] datos : plataformas) {

            float x = datos[0];
            float y = datos[1];
            float ancho = datos[2];
            float alto = datos[3];

            BodyDef cuerpoDef = new BodyDef();
            cuerpoDef.type = BodyDef.BodyType.StaticBody;

            cuerpoDef.position.set(
                (x + ancho / 2) / PIXELES_POR_METRO,
                (y + alto / 2) / PIXELES_POR_METRO
            );

            Body cuerpo = mundo.createBody(cuerpoDef);

            PolygonShape forma = new PolygonShape();
            forma.setAsBox(
                (ancho / 2) / PIXELES_POR_METRO,
                (alto / 2) / PIXELES_POR_METRO
            );

            cuerpo.createFixture(forma, 0);
            forma.dispose();
        }

        crearPared(0, ALTO_MAPA / 2, 10, ALTO_MAPA);
        crearPared(ANCHO_MAPA, ALTO_MAPA / 2, 10, ALTO_MAPA);
    }

    private void crearPared(float x, float y, float ancho, float alto) {

        BodyDef cuerpoDef = new BodyDef();
        cuerpoDef.type = BodyDef.BodyType.StaticBody;
        cuerpoDef.position.set(
            x / PIXELES_POR_METRO,
            y / PIXELES_POR_METRO
        );

        Body cuerpo = mundo.createBody(cuerpoDef);

        PolygonShape forma = new PolygonShape();
        forma.setAsBox(
            (ancho / 2) / PIXELES_POR_METRO,
            (alto / 2) / PIXELES_POR_METRO
        );

        cuerpo.createFixture(forma, 0);
        forma.dispose();
    }
    

    public void agregarJugador(int numeroJugador) {

    comandos.add(() -> {

        float x = (numeroJugador == 1) ? 150f : 1130f;
        float y = 400f;

        JugadorServidor jugador = crearJugador(
            numeroJugador, x, y
        );

        jugadores.put(numeroJugador, jugador);

        System.out.println(
            "Jugador " + numeroJugador + " agregado al mundo"
        );
    });
}

    public void procesarAccion(int numeroJugador, String accion) {

    comandos.add(() -> {

        JugadorServidor jugador = jugadores.get(numeroJugador);

        if (jugador == null) {
            return;
        }

        switch (accion.toUpperCase()) {

            case "IZQUIERDA":
                jugador.moverIzquierda();
                break;

            case "DERECHA":
                jugador.moverDerecha();
                break;

            case "DETENER":
                jugador.detener();
                break;

            case "SALTAR":
                jugador.saltar();
                break;
        }
    });
}

    public void quitarJugador(int numeroJugador) {

    comandos.add(() -> {

        JugadorServidor jugador = jugadores.remove(numeroJugador);

        if (jugador != null) {
            eliminarJugador(jugador);

            System.out.println(
                "Jugador " + numeroJugador + " eliminado del mundo"
            );
        }
    });
}


    public void actualizar() {

        Runnable comando;

        while ((comando = comandos.poll()) != null) {
            comando.run();
        }

        mundo.step(1f / 60f, 6, 2);
    }

    public String obtenerEstado() {

    StringBuilder estado = new StringBuilder("ESTADO");

    for (JugadorServidor jugador : jugadores.values()) {

        Vector2 posicion = jugador.obtenerPosicion();

        estado.append(";")
              .append(jugador.getNumeroJugador())
              .append(",")
              .append(posicion.x)
              .append(",")
              .append(posicion.y);
    }

    return estado.toString();
}


    public JugadorServidor crearJugador(int numeroJugador,
                                        float x, float y) {
        return new JugadorServidor(mundo, numeroJugador, x, y);
    }

    public void eliminarJugador(JugadorServidor jugador) {
        jugador.eliminar();
    }

    public void cerrar() {
        mundo.dispose();
    }

    public World getMundo() {
        return mundo;
    }
}
