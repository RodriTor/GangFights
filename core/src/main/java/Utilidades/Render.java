package Utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lstcompany.gangfights.Principal;

public class Render {
    public static SpriteBatch batch;
    public static Principal juego;

    public static void limpiarPantalla(float r, float g, float b) {
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    public static void comenzarBatch(OrthographicCamera camara) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
    }

    public static void terminarBatch() {
        batch.end();
    }
}
