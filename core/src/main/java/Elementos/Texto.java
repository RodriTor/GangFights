package Elementos;

import Pantallas.PantallaInicio;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Texto {

    private BitmapFont fuente;

    private String texto = "";
    private float x, y;
    private float ancho, alto;

    public Texto(String rutaFuente, int tamanio, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = tamanio;
        parameter.color = color;

        fuente = generator.generateFont(parameter);
        generator.dispose();

        this.texto = "";
    }

    public void setTexto(String texto) {
        this.texto = texto;
        GlyphLayout layout = new GlyphLayout(fuente, texto);
        this.ancho = layout.width;
        this.alto = layout.height;
    }

    public void setPosicion(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void dibujar() {
        fuente.draw(Render.batch, texto, x, y);
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
    public String getTexto() {
        return texto; }

    public BitmapFont getFuente() {
        return fuente;
    }
}
