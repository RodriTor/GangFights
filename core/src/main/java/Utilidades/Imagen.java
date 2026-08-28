package Utilidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Imagen {

    private Texture t;
    private Sprite s;

    public Imagen(String ruta) {
        t = new Texture(ruta);
        s = new Sprite(t);
    }

    public void dibujar() {
        s.draw(Render.batch);
    }

    public void setTrasparencia(float a) {
        s.setAlpha(a);
    }

    public void setPosicion(float x, float y) {
        s.setPosition(x, y);
    }

    public float getAncho() {
        return s.getWidth();
    }

    public float getAlto() {
        return s.getHeight();
    }
}


