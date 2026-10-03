package Entradas;

import Audio.GestorAudio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ControlAudio {

    private final GestorAudio audio;

    public ControlAudio(GestorAudio audio) {
        this.audio = audio;
    }

    public void actualizar() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            audio.alternarSilencio();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)
            || Gdx.input.isKeyJustPressed(Input.Keys.PLUS)
            || Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)
            || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD)) {
            audio.subirVolumen();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN)
            || Gdx.input.isKeyJustPressed(Input.Keys.MINUS)
            || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)) {
            audio.bajarVolumen();
        }
    }
}
