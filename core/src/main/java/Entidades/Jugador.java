package Entidades;

import Entradas.Accion;
import Entradas.ControlJugador;
import Logica.ReaccionGolpe;
import Utilidades.Config;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;


public class Jugador extends Entidad {

    private static final int ANCHO_FRAME = 32;
    private static final int ALTO_FRAME = 42;

    private static final float ANCHO_CUERPO = 30f;
    private static final float ALTO_CUERPO = 60f;

    private static final float VELOCIDAD = 13f;
    private static final float VELOCIDAD_SALTO = 21f;

    public static final int VIDA_MAXIMA = 100;
    private static final int DANIO_GOLPE = 10;
    private static final float PROBABILIDAD_ESCAPE = 0.3f;

    private static final Color COLOR_DANIO = new Color(1f, 0.55f, 0.55f, 1f);

    private static final float RANGO_GOLPE_X = 3.0f;
    private static final float RANGO_GOLPE_Y = 2.0f;

    private static final float MITAD_GROSOR_SENSOR = 0.2f;

    private final World mundo;
    private final Personaje personaje;
    private final ControlJugador control;
    private final float spawnX;
    private final float spawnY;
    private final boolean mirandoDerechaInicial;
    private final ArrayList<Texture> texturas = new ArrayList<>();
    private final ReaccionGolpe reaccion = new ReaccionGolpe();

    private Body cuerpo;
    private EventosJugador oyente;

    private Animation<TextureRegion> animacionQuieto;
    private Animation<TextureRegion> animacionAgachado;
    private Animation<TextureRegion> animacionCorrer;
    private Animation<TextureRegion> animacionGolpe;
    private TextureRegion regionSalto;
    private TextureRegion regionMuerto;
    private final TextureRegion[] regionesRecibir = new TextureRegion[ReaccionGolpe.ETAPAS];

    private float tiempoAnimacion;
    private float tiempoGolpe;

    private int contactosSuelo = 0;
    private int vida = VIDA_MAXIMA;

    private boolean estaAgachado;
    private boolean estaCorriendo;
    private boolean estaSaltando;
    private boolean estaGolpeando;
    private boolean mirandoDerecha;
    private boolean activo = true;
    private boolean golpeConectado;
    private boolean tienePistola;

    public Jugador(World mundo, float x, float y, Personaje personaje, ControlJugador control,
                   boolean mirandoDerecha) {
        super(x, y, ANCHO_CUERPO, ALTO_CUERPO);
        this.mundo = mundo;
        this.personaje = personaje;
        this.control = control;
        this.spawnX = x;
        this.spawnY = y;
        this.mirandoDerechaInicial = mirandoDerecha;
        this.mirandoDerecha = mirandoDerecha;

        crearCuerpo();
        cargarAnimaciones();
    }


    private TextureRegion[][] cargarHoja(String ruta) {
        Texture hoja = new Texture(Gdx.files.internal(ruta));
        texturas.add(hoja);
        return TextureRegion.split(hoja, ANCHO_FRAME, ALTO_FRAME);
    }

    /** Si la hoja no tiene ese frame (por ejemplo un personaje con una sola imagen), usa el primero. */
    private TextureRegion obtenerFrame(TextureRegion[][] hoja, int fila, int columna) {
        if (fila < hoja.length && columna < hoja[fila].length) {
            return hoja[fila][columna];
        }
        return hoja[0][0];
    }

    private void cargarAnimaciones() {
        TextureRegion[][] quieto = cargarHoja(personaje.getRutaQuieto());
        animacionQuieto = new Animation<>(0.15f, quieto[0][0], quieto[0][1], quieto[1][0]);
        animacionQuieto.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] agachado = cargarHoja(personaje.getRutaAgachado());
        animacionAgachado = new Animation<>(0.15f, agachado[0][0]);

        TextureRegion[][] correr = cargarHoja(personaje.getRutaCorrer());
        animacionCorrer = new Animation<>(0.10f, correr[0][0], correr[0][1], correr[1][0]);
        animacionCorrer.setPlayMode(Animation.PlayMode.LOOP);

        regionSalto = cargarHoja(personaje.getRutaSalto())[0][0];

        // La hoja de golpe mide 64x42: dos frames de 32x42
        TextureRegion[][] golpe = cargarHoja(personaje.getRutaGolpe());
        animacionGolpe = new Animation<>(0.4f, golpe[0][0], golpe[0][1]);
        animacionGolpe.setPlayMode(Animation.PlayMode.NORMAL);

        // La hoja de recibir tiene 3 imagenes: una por cada golpe seguido del combo
        TextureRegion[][] recibir = cargarHoja(personaje.getRutaRecibir());
        regionesRecibir[0] = obtenerFrame(recibir, 0, 0);
        regionesRecibir[1] = obtenerFrame(recibir, 0, 1);
        regionesRecibir[2] = obtenerFrame(recibir, 1, 0);

        regionMuerto = cargarHoja(personaje.getRutaMuerto())[0][0];
    }

    private void crearCuerpo() {
        BodyDef cuerpoDef = new BodyDef();
        cuerpoDef.type = BodyDef.BodyType.DynamicBody;
        cuerpoDef.position.set(getX() / Config.PIXELES_POR_METRO, getY() / Config.PIXELES_POR_METRO);
        cuerpoDef.fixedRotation = true;
        cuerpo = mundo.createBody(cuerpoDef);

        float mitadAncho = (getAncho() / 2f) / Config.PIXELES_POR_METRO;
        float mitadAlto = (getAlto() / 2f) / Config.PIXELES_POR_METRO;

        PolygonShape forma = new PolygonShape();
        forma.setAsBox(mitadAncho, mitadAlto);

        FixtureDef fixtureCuerpo = new FixtureDef();
        fixtureCuerpo.shape = forma;
        fixtureCuerpo.density = 1.0f;
        fixtureCuerpo.friction = 0f;
        fixtureCuerpo.filter.categoryBits = Config.CATEGORIA_JUGADOR;
        fixtureCuerpo.filter.maskBits = Config.CATEGORIA_PLATAFORMA;
        cuerpo.createFixture(fixtureCuerpo);

        forma.setAsBox(mitadAncho * 0.8f, MITAD_GROSOR_SENSOR, new Vector2(0, -mitadAlto), 0f);

        FixtureDef fixtureSensor = new FixtureDef();
        fixtureSensor.shape = forma;
        fixtureSensor.isSensor = true;
        fixtureSensor.filter.categoryBits = Config.CATEGORIA_JUGADOR;
        fixtureSensor.filter.maskBits = Config.CATEGORIA_PLATAFORMA;
        cuerpo.createFixture(fixtureSensor).setUserData(this);

        forma.dispose();
    }


    public void sumarContactoSuelo() {
        contactosSuelo++;
    }

    public void restarContactoSuelo() {
        contactosSuelo = Math.max(0, contactosSuelo - 1);
    }

    public boolean estaEnSuelo() {
        return contactosSuelo > 0;
    }


    public void setOyente(EventosJugador oyente) {
        this.oyente = oyente;
    }

    @Override
    public void actualizar(float delta) {
        if (!activo) {
            sincronizarPosicion(); // el cuerpo sigue cayendo hasta quedar en el piso
            return;
        }

        tiempoAnimacion += delta;
        reaccion.actualizar(delta);

        if (estaGolpeando) {
            tiempoGolpe += delta;
            if (animacionGolpe.isAnimationFinished(tiempoGolpe)) {
                estaGolpeando = false;
            }
        }

        procesarAcciones();
        sincronizarPosicion();
    }

    private void sincronizarPosicion() {
        Vector2 posicion = cuerpo.getPosition();
        setPosicion(posicion.x * Config.PIXELES_POR_METRO, posicion.y * Config.PIXELES_POR_METRO);
    }

    private void procesarAcciones() {
        float velocidadY = cuerpo.getLinearVelocity().y;
        estaSaltando = !estaEnSuelo();

        if (reaccion.estaAturdido()) {
            intentarEscapar();
        }

        if (reaccion.estaAturdido()) {
            estaCorriendo = false;
            estaAgachado = false;
            cuerpo.setLinearVelocity(0, velocidadY);
            return;
        }

        if (estaGolpeando) {
            cuerpo.setLinearVelocity(0, velocidadY);
            return;
        }

        if (control.fuePresionada(Accion.GOLPEAR)) {
            estaGolpeando = true;
            golpeConectado = false;
            estaCorriendo = false;
            tiempoGolpe = 0f;
            cuerpo.setLinearVelocity(0, velocidadY);
            if (oyente != null) oyente.alGolpear();
            return;
        }

        if (control.estaPresionada(Accion.AGACHAR) && !estaSaltando) {
            estaAgachado = true;
            estaCorriendo = false;
            cuerpo.setLinearVelocity(0, velocidadY);
            return;
        }
        estaAgachado = false;

        if (control.estaPresionada(Accion.IZQUIERDA)) {
            cuerpo.setLinearVelocity(-VELOCIDAD, velocidadY);
            estaCorriendo = !estaSaltando;
            mirandoDerecha = false;
        } else if (control.estaPresionada(Accion.DERECHA)) {
            cuerpo.setLinearVelocity(VELOCIDAD, velocidadY);
            estaCorriendo = !estaSaltando;
            mirandoDerecha = true;
        } else {
            cuerpo.setLinearVelocity(0, velocidadY);
            estaCorriendo = false;
        }

        if (control.fuePresionada(Accion.SALTAR) && !estaSaltando) {
            cuerpo.setLinearVelocity(cuerpo.getLinearVelocity().x, VELOCIDAD_SALTO);
            estaSaltando = true;
            estaCorriendo = false;
            if (oyente != null) oyente.alSaltar();
        }
    }


    /** Apretar cualquier tecla (menos agacharse) da una chance de salirse del combo. */
    private void intentarEscapar() {
        boolean apretoTecla = control.fuePresionada(Accion.IZQUIERDA)
            || control.fuePresionada(Accion.DERECHA)
            || control.fuePresionada(Accion.SALTAR)
            || control.fuePresionada(Accion.GOLPEAR);

        if (apretoTecla && MathUtils.randomBoolean(PROBABILIDAD_ESCAPE)) {
            reaccion.escapar();
        }
    }

    public void comprobarAtaque(Jugador rival) {
        if (!estaGolpeando || golpeConectado || !activo || !rival.isActivo()) return;

        Vector2 miPosicion = cuerpo.getPosition();
        Vector2 posicionRival = rival.cuerpo.getPosition();

        float diferenciaX = posicionRival.x - miPosicion.x;
        float distanciaY = Math.abs(posicionRival.y - miPosicion.y);

        boolean rivalDeFrente = mirandoDerecha ? diferenciaX > -0.5f : diferenciaX < 0.5f;

        if (rivalDeFrente && Math.abs(diferenciaX) < RANGO_GOLPE_X && distanciaY < RANGO_GOLPE_Y) {
            golpeConectado = true;
            rival.recibirGolpe(this);
        }
    }

    private void recibirGolpe(Jugador atacante) {
        if (!reaccion.recibirGolpe()) return;

        vida = Math.max(0, vida - DANIO_GOLPE);
        estaGolpeando = false;
        estaCorriendo = false;
        estaAgachado = false;
        mirandoDerecha = atacante.getX() > getX();
        cuerpo.setLinearVelocity(0, cuerpo.getLinearVelocity().y);

        if (vida == 0) {
            eliminar();
        }
    }

    /** El cuerpo no se destruye: asi el jugador cae y queda tirado en el piso o la plataforma. */
    public void eliminar() {
        if (!activo) return;

        activo = false;
        estaGolpeando = false;
        cuerpo.setLinearVelocity(0, cuerpo.getLinearVelocity().y);
        if (oyente != null) oyente.alSerEliminado();
    }

    public void curar(int cantidad) {
        vida = Math.min(VIDA_MAXIMA, vida + cantidad);
    }

    public void equiparPistola() {
        tienePistola = true;
    }

    public void reaparecer() {
        if (cuerpo != null) {
            mundo.destroyBody(cuerpo);
        }
        contactosSuelo = 0;
        setPosicion(spawnX, spawnY);
        crearCuerpo();

        activo = true;
        estaAgachado = false;
        estaCorriendo = false;
        estaSaltando = false;
        estaGolpeando = false;
        golpeConectado = false;
        vida = VIDA_MAXIMA;
        tienePistola = false;
        reaccion.reiniciar();
        mirandoDerecha = mirandoDerechaInicial;
        tiempoAnimacion = 0f;
        tiempoGolpe = 0f;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getVida() {
        return vida;
    }

    public boolean tienePistola() {
        return tienePistola;
    }

    public boolean estaAgachado() {
        return estaAgachado;
    }


    @Override
    public void dibujar() {
        if (!activo) {
            dibujarEchado();
            return;
        }

        TextureRegion frame = obtenerFrameActual();
        float posX = getX() - getAncho() / 2f;
        float posY = getY() - getAlto() / 2f;

        if (reaccion.estaAturdido()) {
            Render.batch.setColor(COLOR_DANIO);
        }
        Render.batch.draw(frame, posX, posY, getAncho(), getAlto());
        Render.batch.setColor(Color.WHITE);
    }

    /** Muerto: la imagen se acuesta (girada 90 grados, cae hacia atras) y queda apoyada en el piso. */
    private void dibujarEchado() {
        TextureRegion frame = obtenerFrameActual();
        float giro = mirandoDerecha ? 90f : -90f;

        // Acostado ocupa menos alto, asi que el centro baja para seguir tocando el piso
        float centroY = getY() - (getAlto() - getAncho()) / 2f;

        Render.batch.draw(frame, getX() - getAncho() / 2f, centroY - getAlto() / 2f,
            getAncho() / 2f, getAlto() / 2f, getAncho(), getAlto(), 1f, 1f, giro);
    }

    private TextureRegion obtenerFrameActual() {
        TextureRegion region;

        if (!activo) {
            region = regionMuerto;
        } else if (reaccion.estaAturdido()) {
            region = regionesRecibir[reaccion.getEtapa() - 1];
        } else if (estaGolpeando) {
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

        // Cada region recuerda si esta espejada, asi que solo la damos vuelta si hace falta
        if (region.isFlipX() == mirandoDerecha) {
            region.flip(true, false);
        }
        return region;
    }


    public void dispose() {
        for (Texture textura : texturas) {
            textura.dispose();
        }
        texturas.clear();
    }
}
