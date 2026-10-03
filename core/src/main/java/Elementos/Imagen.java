package Elementos;

import Utilidades.Render;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Imagen {

    private final Texture textura;
    private final Sprite sprite;

    public Imagen(String ruta) {
        textura = new Texture(ruta);
        sprite = new Sprite(textura);
    }

    public void dibujar() {
        sprite.draw(Render.batch);
    }

    public void setTrasparencia(float alfa) {
        sprite.setAlpha(alfa);
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setTamanio(float ancho, float alto) {
        sprite.setSize(ancho, alto);
    }

    public float getAncho() {
        return sprite.getWidth();
    }

    public float getAlto() {
        return sprite.getHeight();
    }

    public void dispose() {
        textura.dispose();
    }
}
