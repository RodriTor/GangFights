package Elementos;

import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Texto {

    private final BitmapFont fuente;
    private final GlyphLayout layout = new GlyphLayout();

    private String texto = "";
    private float x;
    private float y;
    private float ancho;
    private float alto;

    public Texto(String rutaFuente, int tamanio, Color color) {
        FreeTypeFontGenerator generador = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
        FreeTypeFontGenerator.FreeTypeFontParameter parametros = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parametros.size = tamanio;
        parametros.color = color;

        fuente = generador.generateFont(parametros);
        generador.dispose();
    }

    public void setTexto(String nuevoTexto) {
        if (nuevoTexto == null) nuevoTexto = "";
        if (nuevoTexto.equals(texto)) return;
        texto = nuevoTexto;
        recalcularMedidas();
    }

    public void setEscala(float escala) {
        fuente.getData().setScale(escala);
        recalcularMedidas();
    }

    public void setColor(Color color) {
        fuente.setColor(color);
    }

    public void setPosicion(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void centrarEn(float centroX, float y) {
        setPosicion(centroX - ancho / 2f, y);
    }

    private void recalcularMedidas() {
        layout.setText(fuente, texto);
        ancho = layout.width;
        alto = layout.height;
    }

    public void dibujar() {
        fuente.draw(Render.batch, texto, x, y);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getAncho() { return ancho; }
    public float getAlto() { return alto; }

    public void dispose() {
        fuente.dispose();
    }
}
