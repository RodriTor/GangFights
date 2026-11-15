package Utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** Herramientas para trabajar con imagenes. */
public final class Sprites {

    private Sprites() { }

    public static TextureRegion sinBordeTransparente(Texture textura, String ruta) {
        Pixmap imagen = new Pixmap(Gdx.files.internal(ruta));

        int minX = imagen.getWidth();
        int minY = imagen.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                boolean tieneDibujo = (imagen.getPixel(x, y) & 0xff) != 0;
                if (tieneDibujo) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }
        imagen.dispose();

        if (maxX < 0) {
            return new TextureRegion(textura);
        }
        return new TextureRegion(textura, minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
}
