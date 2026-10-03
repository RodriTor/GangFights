package Entidades;


public abstract class Entidad {

    private float x;
    private float y;
    private final float ancho;
    private final float alto;

    public Entidad(float x, float y, float ancho, float alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }

    protected void setPosicion(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract void actualizar(float delta);

    public abstract void dibujar();
}
