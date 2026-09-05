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
    private Texture hojaSalto;
    private Texture hojaGolpe; // NUEVO

    private Animation<TextureRegion> animacionQuieto;
    private Animation<TextureRegion> animacionAgachado;
    private Animation<TextureRegion> animacionCorrer;
    private Animation<TextureRegion> animacionGolpe; // NUEVO (el golpe tiene 2 frames)
    private TextureRegion regionSalto;

    private float tiempoAnimacion;
    private float tiempoGolpe = 0; // NUEVO

    private static final int ANCHO_FRAME = 32;
    private static final int ALTO_FRAME = 42;

    private int idJugador;
    private float escala = 10f;

    private boolean estaAgachado = false;
    private boolean estaCorriendo = false;
    private boolean estaSaltando = false;
    private boolean estaGolpeando = false; // NUEVO
    private boolean mirandoDerecha = true;
    private boolean activo = true; // NUEVO (para saber si sigue en juego)

    public Jugador(World mundo, float x, float y, int idJugador, String rutaQuieto, String rutaAgachado, String rutaCorrer, String rutaSalto, String rutaGolpe) {
        super(x, y, 40, 80);
        this.mundo = mundo;
        this.idJugador = idJugador;

        crearCuerpo();
        tiempoAnimacion = 0f;

        spriteQuieto(rutaQuieto);
        spriteAgachado(rutaAgachado);
        spriteCorrer(rutaCorrer);
        spriteSalto(rutaSalto);
        spriteGolpe(rutaGolpe); // NUEVO
    }

    private void spriteQuieto(String ruta) {
        hojaQuieto = new Texture(Gdx.files.internal(ruta));
        TextureRegion[][] division = TextureRegion.split(hojaQuieto, ANCHO_FRAME, ALTO_FRAME);
        animacionQuieto = new Animation<>(0.15f, division[0][0], division[0][1], division[1][0]);
        animacionQuieto.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void spriteAgachado(String ruta) {
        hojaAgachado = new Texture(Gdx.files.internal(ruta));
        TextureRegion[][] division = TextureRegion.split(hojaAgachado, ANCHO_FRAME, ALTO_FRAME);
        animacionAgachado = new Animation<>(0.15f, division[0][0]);
        animacionAgachado.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private void spriteCorrer(String ruta) {
        hojaCorrer = new Texture(Gdx.files.internal(ruta));
        TextureRegion[][] division = TextureRegion.split(hojaCorrer, ANCHO_FRAME, ALTO_FRAME);
        animacionCorrer = new Animation<>(0.10f, division[0][0], division[0][1], division[1][0]);
        animacionCorrer.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void spriteSalto(String ruta) {
        hojaSalto = new Texture(Gdx.files.internal(ruta));
        TextureRegion[][] division = TextureRegion.split(hojaSalto, ANCHO_FRAME, ALTO_FRAME);
        regionSalto = division[0][0];
    }

    // NUEVO: Cargar sprite de golpe (64x42 significa 2 frames de 32x42)
    private void spriteGolpe(String ruta) {
        hojaGolpe = new Texture(Gdx.files.internal(ruta));
        TextureRegion[][] division = TextureRegion.split(hojaGolpe, ANCHO_FRAME, ALTO_FRAME);
        animacionGolpe = new Animation<>(0.4f, division[0][0], division[0][1]);
        animacionGolpe.setPlayMode(Animation.PlayMode.NORMAL);
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
        if (!activo) return;
        tiempoAnimacion += delta;

        if (estaGolpeando) {
            tiempoGolpe += delta;
            if (animacionGolpe.isAnimationFinished(tiempoGolpe)) {
                estaGolpeando = false; // Termina la animación de golpe
            }
        }

        procesarMovimiento();
    }

    private void procesarMovimiento() {
        float velocidad = 13f;
        Vector2 velocidadActual = cuerpo.getLinearVelocity();

        boolean teclaAbajo, teclaIzquierda, teclaDerecha, teclaSalto, teclaGolpe;

        if (idJugador == 1) {
            teclaAbajo = Gdx.input.isKeyPressed(Input.Keys.S);
            teclaIzquierda = Gdx.input.isKeyPressed(Input.Keys.A);
            teclaDerecha = Gdx.input.isKeyPressed(Input.Keys.D);
            teclaSalto = Gdx.input.isKeyJustPressed(Input.Keys.W);
            teclaGolpe = Gdx.input.isKeyJustPressed(Input.Keys.NUM_1); // Tecla '1'
        } else {
            teclaAbajo = Gdx.input.isKeyPressed(Input.Keys.DOWN);
            teclaIzquierda = Gdx.input.isKeyPressed(Input.Keys.LEFT);
            teclaDerecha = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            teclaSalto = Gdx.input.isKeyJustPressed(Input.Keys.UP);
            teclaGolpe = Gdx.input.isKeyJustPressed(Input.Keys.N); // Tecla 'N'
        }

        if (estaGolpeando) {
            cuerpo.setLinearVelocity(0, velocidadActual.y);
            return;
        }

        if (teclaGolpe) {
            estaGolpeando = true;
            tiempoGolpe = 0f;
            cuerpo.setLinearVelocity(0, velocidadActual.y);
            return;
        }

        estaSaltando = Math.abs(velocidadActual.y) > 0.1f;

        if (teclaAbajo && !estaSaltando) {
            estaAgachado = true;
            estaCorriendo = false;
            cuerpo.setLinearVelocity(0, velocidadActual.y);
            return;
        } else {
            estaAgachado = false;
        }

        if (teclaIzquierda) {
            cuerpo.setLinearVelocity(-velocidad, velocidadActual.y);
            if (!estaSaltando) estaCorriendo = true;
            mirandoDerecha = false;
        } else if (teclaDerecha) {
            cuerpo.setLinearVelocity(velocidad, velocidadActual.y);
            if (!estaSaltando) estaCorriendo = true;
            mirandoDerecha = true;
        } else {
            cuerpo.setLinearVelocity(0, velocidadActual.y);
            estaCorriendo = false;
        }

        if (teclaSalto && !estaSaltando) {
            cuerpo.setLinearVelocity(velocidadActual.x, 12f);
            estaSaltando = true;
            estaCorriendo = false;
        }
    }

    public void comprobarAtaque(Jugador rival) {
        if (!this.estaGolpeando || !rival.isActivo() || rival.getCuerpo() == null || this.cuerpo == null) return;

        Vector2 pos1 = this.cuerpo.getPosition();
        Vector2 pos2 = rival.getCuerpo().getPosition();

        float distanciaX = Math.abs(pos1.x - pos2.x);
        float distanciaY = Math.abs(pos1.y - pos2.y);

        // Ampliamos levemente el rango a 3.0f para asegurar el impacto al estar juntos
        float rangoGolpeX = 3.0f;
        float rangoGolpeY = 2.0f;

        if (distanciaX < rangoGolpeX && distanciaY < rangoGolpeY) {
            System.out.println("¡Impacto registrado del Jugador " + idJugador + "!");
            rival.eliminar();
        }
    }


    public void eliminar() {
        this.activo = false;
        // Opcional: destruir el cuerpo físico de Box2D para que deje de interactuar
        if (cuerpo != null && cuerpo.getWorld() != null) {
            cuerpo.getWorld().destroyBody(cuerpo);
            cuerpo = null;
        }
    }

    public boolean isActivo() {
        return activo;
    }

    @Override
    public void dibujar() {}

    public TextureRegion getFrameActual() {
        TextureRegion region;

        if (estaGolpeando) {
            region = animacionGolpe.getKeyFrame(tiempoGolpe, false);
        } else if (estaSaltando) {
            region = regionSalto;
        } else if (estaAgachado) {
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
        if (hojaSalto != null) hojaSalto.dispose();
        if (hojaGolpe != null) hojaGolpe.dispose();
    }
}
