
package Entidades;

import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class JugadorVisualOnline {

    private static final int ANCHO_FRAME = 32;
    private static final int ALTO_FRAME = 42;

    private final ArrayList<Texture> texturas = new ArrayList<>();

    private Animation<TextureRegion> animacionQuieto;
    private Animation<TextureRegion> animacionCorrer;
    private TextureRegion regionSalto;

    private float x;
    private float y;
    private float tiempoAnimacion;

    private boolean mirandoDerecha;
    private boolean estaCorriendo;
    private boolean estaSaltando;

    public JugadorVisualOnline(Personaje personaje, float x, float y, boolean mirandoDerecha) {
        this.x = x;
        this.y = y;
        this.mirandoDerecha = mirandoDerecha;

        cargarAnimaciones(personaje);
    }

    private TextureRegion[][] cargarHoja(String ruta) {
        Texture textura = new Texture(Gdx.files.internal(ruta));
        texturas.add(textura);
        return TextureRegion.split(textura, ANCHO_FRAME, ALTO_FRAME);
    }

    private void cargarAnimaciones(Personaje personaje) {
        TextureRegion[][] quieto = cargarHoja(personaje.getRutaQuieto());

        animacionQuieto = new Animation<>(
            0.15f, quieto[0][0], quieto[0][1], quieto[1][0]
        );
        animacionQuieto.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] correr = cargarHoja(personaje.getRutaCorrer());

        animacionCorrer = new Animation<>(
            0.10f, correr[0][0], correr[0][1], correr[1][0]
        );
        animacionCorrer.setPlayMode(Animation.PlayMode.LOOP);

        regionSalto = cargarHoja(personaje.getRutaSalto())[0][0];
    }

    public void actualizar(float nuevaX, float nuevaY, float delta) {
        float diferenciaX = nuevaX - x;
        float diferenciaY = nuevaY - y;

        estaCorriendo = Math.abs(diferenciaX) > 0.5f;
        estaSaltando = diferenciaY > 2f;

        if (diferenciaX > 0.1f) {
            mirandoDerecha = true;
        } else if (diferenciaX < -0.1f) {
            mirandoDerecha = false;
        }

        x = nuevaX;
        y = nuevaY;
        tiempoAnimacion += delta;
    }

    public void dibujar() {
        TextureRegion frame;

        if (estaSaltando) {
            frame = regionSalto;
        } else if (estaCorriendo) {
            frame = animacionCorrer.getKeyFrame(tiempoAnimacion);
        } else {
            frame = animacionQuieto.getKeyFrame(tiempoAnimacion);
        }

        // Orientación del personaje según la dirección del movimiento.
        if (frame.isFlipX() == mirandoDerecha) {
            frame.flip(true, false);
        }

        Render.batch.draw(frame, x - 15f, y - 30f, 30f, 60f);
    }

    public void dispose() {
        for (Texture textura : texturas) {
            textura.dispose();
        }
        texturas.clear();
    }
}
