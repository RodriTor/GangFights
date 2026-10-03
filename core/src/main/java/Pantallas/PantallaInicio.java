package Pantallas;

import Elementos.Imagen;
import Interfaces.NavegadorPantallas;
import Utilidades.Config;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaInicio implements Screen {

    private static final float DURACION_FADE_IN = 1.0f;
    private static final float DURACION_PAUSA = 1.5f;
    private static final float DURACION_FADE_OUT = 1.0f;
    private static final float DURACION_TOTAL = DURACION_FADE_IN + DURACION_PAUSA + DURACION_FADE_OUT;

    private final NavegadorPantallas navegador;

    private OrthographicCamera camara;
    private Viewport viewport;
    private Imagen logo;

    private float tiempo = 0f;
    private boolean terminada = false;

    public PantallaInicio(NavegadorPantallas navegador) {
        this.navegador = navegador;
    }

    @Override
    public void show() {
        camara = new OrthographicCamera();
        viewport = new FitViewport(Config.ANCHO_MUNDO, Config.ALTO_MUNDO, camara);

        logo = new Imagen(Recursos.LOGO);
        logo.setPosicion((Config.ANCHO_MUNDO - logo.getAncho()) / 2f,
                         (Config.ALTO_MUNDO - logo.getAlto()) / 2f);
        logo.setTrasparencia(0f);
    }

    @Override
    public void render(float delta) {
        tiempo += delta;
        logo.setTrasparencia(calcularAlfa());

        Render.limpiarPantalla(0, 0, 0);
        viewport.apply();
        camara.update();

        Render.comenzarBatch(camara);
        logo.dibujar();
        Render.terminarBatch();

        if (tiempo >= DURACION_TOTAL && !terminada) {
            terminada = true;
            navegador.irAMenu();
        }
    }

    private float calcularAlfa() {
        float alfa;
        if (tiempo < DURACION_FADE_IN) {
            alfa = tiempo / DURACION_FADE_IN;
        } else if (tiempo < DURACION_FADE_IN + DURACION_PAUSA) {
            alfa = 1f;
        } else {
            alfa = 1f - (tiempo - DURACION_FADE_IN - DURACION_PAUSA) / DURACION_FADE_OUT;
        }
        return MathUtils.clamp(alfa, 0f, 1f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        if (logo != null) logo.dispose();
    }
}
