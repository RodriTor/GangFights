package Entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Jugador extends Entidad {
    private Body cuerpo;
    private World mundo;

    private Texture hojaQuieto;
    private Texture hojaAgachado;
    private Texture hojaCorrer;

    private Animation<TextureRegion> animacionQuieto;
    private Animation<TextureRegion> animacionAgachado;
    private Animation<TextureRegion> animacionCorrer;

    private float tiempoAnimacion;

    private static final int ANCHO_FRAME = 32;
    private static final int ALTO_FRAME = 42;

    private int idJugador; // 1 para Jugador 1, 2 para Jugador 2
    private float escala = 10f;

    private boolean estaAgachado = false;
    private boolean estaCorriendo = false;
    private boolean mirandoDerecha = true;

    public Jugador(World mundo, float x, float y, int idJugador, String rutaQuieto, String rutaAgachado, String rutaCorrer) {
        super(x, y, 40, 80);
        this.mundo = mundo;
        this.idJugador = idJugador;

        crearCuerpo();
        tiempoAnimacion = 0f;

        spriteQuieto(rutaQuieto);
        spriteAgachado(rutaAgachado);
        spriteCorrer(rutaCorrer);
    }

    private void spriteQuieto(String ruta) {
        hojaQuieto = new Texture(Gdx.files.internal(ruta));
        TextureRegion[][] division = TextureRegion.split(hojaQuieto, ANCHO_FRAME, ALTO_FRAME);

        TextureRegion[] framesQuieto = new TextureRegion[] {
            division[0][0],
            division[0][1],
            division[1][0]
        };

        animacionQuieto = new Animation<>(0.15f, framesQuieto);
        animacionQuieto.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void spriteAgachado(String ruta) {
        hojaAgachado = new Texture(Gdx.files.internal(ruta));
        TextureRegion[][] division = TextureRegion.split(hojaAgachado, ANCHO_FRAME, ALTO_FRAME);

        TextureRegion[] framesAgachado = new TextureRegion[] {
            division[0][0]
        };

        animacionAgachado = new Animation<>(0.15f, framesAgachado);
        animacionAgachado.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private void spriteCorrer(String ruta) {
        hojaCorrer = new Texture(Gdx.files.internal(ruta));
        TextureRegion[][] division = TextureRegion.split(hojaCorrer, ANCHO_FRAME, ALTO_FRAME);

        TextureRegion[] framesCorrer = new TextureRegion[] {
            division[0][0],
            division[0][1],
            division[1][0]
        };

        animacionCorrer = new Animation<>(0.10f, framesCorrer);
        animacionCorrer.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void crearCuerpo() {
        BodyDef cuerpoDef = new BodyDef();
        cuerpoDef.type = BodyDef.BodyType.DynamicBody;
        cuerpoDef.position.set(getX() / escala, getY() / escala);
        cuerpoDef.fixedRotation = true;

        cuerpo = mundo.createBody(cuerpoDef);

        PolygonShape forma = new PolygonShape();
        forma.setAsBox((getAncho() / 2f) / escala, (getAlto() / 2f) / escala);

        com.badlogic.gdx.physics.box2d.FixtureDef fixtureDef = new com.badlogic.gdx.physics.box2d.FixtureDef();
        fixtureDef.shape = forma;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.2f;

        fixtureDef.filter.categoryBits = 0x0002;
        fixtureDef.filter.maskBits = 0x0001;

        cuerpo.createFixture(fixtureDef);
        forma.dispose();
    }

    @Override
    public void actualizar(float delta) {
        tiempoAnimacion += delta;
        procesarMovimiento();
    }

    private void procesarMovimiento() {
        float velocidad = 13f;
        Vector2 velocidadActual = cuerpo.getLinearVelocity();

        // Definimos las teclas según el idJugador
        boolean teclaAbajo, teclaIzquierda, teclaDerecha, teclaSalto;

        if (idJugador == 1) {
            // Controles Jugador 1 (WASD)
            teclaAbajo = Gdx.input.isKeyPressed(Input.Keys.S);
            teclaIzquierda = Gdx.input.isKeyPressed(Input.Keys.A);
            teclaDerecha = Gdx.input.isKeyPressed(Input.Keys.D);
            teclaSalto = Gdx.input.isKeyJustPressed(Input.Keys.W);
        } else {
            // Controles Jugador 2 (Flechas del teclado)
            teclaAbajo = Gdx.input.isKeyPressed(Input.Keys.DOWN);
            teclaIzquierda = Gdx.input.isKeyPressed(Input.Keys.LEFT);
            teclaDerecha = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            teclaSalto = Gdx.input.isKeyJustPressed(Input.Keys.UP);
        }

        // 1. Control de agachado
        if (teclaAbajo) {
            estaAgachado = true;
            estaCorriendo = false;
            cuerpo.setLinearVelocity(0, velocidadActual.y);
            return;
        } else {
            estaAgachado = false;
        }

        // 2. Control de movimiento horizontal
        if (teclaIzquierda) {
            cuerpo.setLinearVelocity(-velocidad, velocidadActual.y);
            estaCorriendo = true;
            mirandoDerecha = false;
        } else if (teclaDerecha) {
            cuerpo.setLinearVelocity(velocidad, velocidadActual.y);
            estaCorriendo = true;
            mirandoDerecha = true;
        } else {
            cuerpo.setLinearVelocity(0, velocidadActual.y);
            estaCorriendo = false;
        }

        // 3. Control de salto
        if (teclaSalto) {
            cuerpo.setLinearVelocity(velocidadActual.x, 12f);
        }

        // Cortar animación de correr si está en el aire
        if (Math.abs(cuerpo.getLinearVelocity().y) > 0.1f) {
            estaCorriendo = false;
        }
    }

    @Override
    public void dibujar() {
        // Renderizado gestionado en PantallaJuego
    }

    public TextureRegion getFrameActual() {
        TextureRegion region;

        if (estaAgachado) {
            region = animacionAgachado.getKeyFrame(tiempoAnimacion, false);
        } else if (estaCorriendo) {
            region = animacionCorrer.getKeyFrame(tiempoAnimacion, true);
        } else {
            region = animacionQuieto.getKeyFrame(tiempoAnimacion, true);
        }

        if (region != null) {
            if (!mirandoDerecha && !region.isFlipX()) {
                region.flip(true, false);
            } else if (mirandoDerecha && region.isFlipX()) {
                region.flip(true, false);
            }
        }

        return region;
    }

    public Body getCuerpo() {
        return cuerpo;
    }

    public void dispose() {
        if (hojaQuieto != null) hojaQuieto.dispose();
        if (hojaAgachado != null) hojaAgachado.dispose();
        if (hojaCorrer != null) hojaCorrer.dispose();
    }
}
