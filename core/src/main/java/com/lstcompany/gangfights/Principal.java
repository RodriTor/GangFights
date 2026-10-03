package com.lstcompany.gangfights;

import Audio.GestorAudio;
import Interfaces.NavegadorPantallas;
import Logica.Partida;
import Pantallas.MenuInicial;
import Pantallas.PantallaConfiguracion;
import Pantallas.PantallaFinal;
import Pantallas.PantallaInicio;
import Pantallas.PantallaJuego;
import Utilidades.Render;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Principal extends Game implements NavegadorPantallas {

    private GestorAudio audio;
    private Screen pantallaPendiente;

    @Override
    public void create() {
        Render.batch = new SpriteBatch();
        audio = new GestorAudio();
        setScreen(new PantallaInicio(this));
    }

    @Override
    public void render() {
        super.render();
        aplicarCambioDePantalla();
    }

    private void aplicarCambioDePantalla() {
        if (pantallaPendiente == null) return;

        Screen anterior = getScreen();
        setScreen(pantallaPendiente);
        pantallaPendiente = null;

        if (anterior != null) {
            anterior.dispose();
        }
    }

    @Override
    public void irAMenu() {
        pantallaPendiente = new MenuInicial(this, audio);
    }

    @Override
    public void irAJuego() {
        pantallaPendiente = new PantallaJuego(this, audio);
    }

    @Override
    public void irAConfiguracion() {
        pantallaPendiente = new PantallaConfiguracion(this, audio);
    }

    @Override
    public void irAFinal(Partida partida) {
        pantallaPendiente = new PantallaFinal(this, audio, partida);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (getScreen() != null) getScreen().dispose();
        audio.dispose();
        Render.batch.dispose();
    }
}
