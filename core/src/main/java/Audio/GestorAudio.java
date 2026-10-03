package Audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

import java.util.EnumMap;


public class GestorAudio {

    private static final int VOLUMEN_MUSICA_INICIAL = 40;
    private static final int VOLUMEN_EFECTOS_INICIAL = 70;
    private static final int PASO_VOLUMEN = 10;

    private final EnumMap<Musica, Music> musicas = new EnumMap<>(Musica.class);
    private final EnumMap<Efecto, Sound> efectos = new EnumMap<>(Efecto.class);

    private Music musicaActual;
    private int volumenMusica = VOLUMEN_MUSICA_INICIAL;
    private int volumenEfectos = VOLUMEN_EFECTOS_INICIAL;
    private boolean silenciado = false;

    public GestorAudio() {
        for (Musica m : Musica.values()) {
            FileHandle archivo = Gdx.files.internal(m.getRuta());
            if (archivo.exists()) {
                Music musica = Gdx.audio.newMusic(archivo);
                musica.setLooping(true);
                musicas.put(m, musica);
            } else {
            }
        }
        for (Efecto e : Efecto.values()) {
            FileHandle archivo = Gdx.files.internal(e.getRuta());
            if (archivo.exists()) {
                efectos.put(e, Gdx.audio.newSound(archivo));
            } else {
            }
        }
    }

    public void reproducirMusica(Musica pista) {
        Music nueva = musicas.get(pista);
        if (nueva == null || nueva == musicaActual) return;

        if (musicaActual != null) musicaActual.stop();
        musicaActual = nueva;
        aplicarVolumen();
        musicaActual.play();
    }

    public void reproducirEfecto(Efecto efecto) {
        Sound sonido = efectos.get(efecto);
        if (sonido != null && !silenciado && volumenEfectos > 0) {
            sonido.play(volumenEfectos / 100f);
        }
    }


    public void cambiarVolumenMusica(int pasos) {
        volumenMusica = MathUtils.clamp(volumenMusica + pasos * PASO_VOLUMEN, 0, 100);
        aplicarVolumen();
    }

    public void cambiarVolumenEfectos(int pasos) {
        volumenEfectos = MathUtils.clamp(volumenEfectos + pasos * PASO_VOLUMEN, 0, 100);
    }

    public void subirVolumen() {
        cambiarVolumenMusica(1);
        cambiarVolumenEfectos(1);
    }

    public void bajarVolumen() {
        cambiarVolumenMusica(-1);
        cambiarVolumenEfectos(-1);
    }

    public void alternarSilencio() {
        silenciado = !silenciado;
        aplicarVolumen();
    }

    public boolean isSilenciado() {
        return silenciado;
    }

    public int getVolumenMusica() {
        return volumenMusica;
    }

    public int getVolumenEfectos() {
        return volumenEfectos;
    }

    private void aplicarVolumen() {
        if (musicaActual != null) {
            musicaActual.setVolume(silenciado ? 0f : volumenMusica / 100f);
        }
    }

    public void dispose() {
        for (Music m : musicas.values()) m.dispose();
        for (Sound s : efectos.values()) s.dispose();
        musicas.clear();
        efectos.clear();
    }
}
