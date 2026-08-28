package com.lstcompany.gangfights;

import Pantallas.PantallaInicio;
import Utilidades.Render;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Principal extends Game {



    private SpriteBatch batch;
    private Texture image;

    @Override
    public void create() {
        Render.juego = this;
        Render.batch = new SpriteBatch();

        this.setScreen(new PantallaInicio());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

        Render.batch.dispose();
    }
}
