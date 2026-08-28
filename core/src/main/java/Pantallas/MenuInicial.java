package Pantallas;

import com.badlogic.gdx.graphics.Color;
import Elementos.Imagen;
import Elementos.Texto;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuInicial implements Screen {

    Texto play;
    Imagen menuInicial;
    SpriteBatch b;

    private float tiempo = 0;

    BitmapFont fuente;
    GlyphLayout glyphLayout;

    @Override
    public void show() {

        menuInicial = new Imagen(Recursos.MENUINICIAL);
        b = Render.batch;

        float centroX = (Gdx.graphics.getWidth() / 2f) - (menuInicial.getAncho() / 2f);
        float centroY = (Gdx.graphics.getHeight() / 2f) - (menuInicial.getAlto() / 2f);

        menuInicial.setPosicion(centroX, centroY);

        play = new Texto(Recursos.FUENTE_MENU, 20, Color.YELLOW);
        play.setTexto("PLAY");

        float playX = (Gdx.graphics.getWidth() / 2f) - (play.getAncho() / 2f);
        float playY = 85;
        play.setPosicion(playX, playY);

    }

    @Override
    public void render(float delta) {
        tiempo += delta;
        Render.limpiarPantalla(0,0,0);

        float escala = 1.0f + 0.15f * (float) Math.sin(tiempo * 6);
        play.getFuente().getData().setScale(escala);

        GlyphLayout layout = new GlyphLayout(play.getFuente(), play.getTexto());

        float playX = (Gdx.graphics.getWidth() / 2f) - (layout.width / 2f);
        float playY = 85;
        play.setPosicion(playX, playY);

        b.begin();
        menuInicial.dibujar();



        play.dibujar();
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
