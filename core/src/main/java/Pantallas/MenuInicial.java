package Pantallas;

import Utilidades.Imagen;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuInicial implements Screen {

    Imagen menuInicial;
    SpriteBatch b;

    @Override
    public void show() {
        menuInicial = new Imagen(Recursos.MENUINICIAL);
        b = Render.batch;

        float centroX = (Gdx.graphics.getWidth() / 2f) - (menuInicial.getAncho() / 2f);
        float centroY = (Gdx.graphics.getHeight() / 2f) - (menuInicial.getAlto() / 2f);

        menuInicial.setPosicion(centroX, centroY);


    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(0,0,0);
        b.begin();
        menuInicial.dibujar();
        b.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
