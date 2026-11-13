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
    }
}
