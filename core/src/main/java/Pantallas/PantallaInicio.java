package Pantallas;
import Elementos.Imagen;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import Utilidades.Recursos;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class PantallaInicio implements Screen {

    Imagen logo;
    SpriteBatch b;

    boolean fadeInTerminado = false, termina = false;
    float a = 0;
    float contTiempo = 0, delay= 3;
    float contTiempoT = 0, delayTerminado= 3;

    @Override
    public void show() {
        logo = new Imagen(Recursos.LOGO);
        b = Render.batch;

        logo.setTrasparencia(a);


        float centroX = (Gdx.graphics.getWidth() / 2f) - (logo.getAncho() / 2f);
        float centroY = (Gdx.graphics.getHeight() / 2f) - (logo.getAlto() / 2f);

        logo.setPosicion(centroX, centroY);

    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(0, 0, 0);
        b.begin();
        logo.dibujar();
        b.end();

        procesarFade();

    }

    private void procesarFade() {
        logo.setTrasparencia(a);
        if (!fadeInTerminado) {
            a += 0.01f;

            if (a > 1) {
                a = 1;
                fadeInTerminado = true;
            }
        } else {
            contTiempo += 0.50f;
            if (contTiempo > delay) {
                a -= 0.01f;

                if (a < 0) {
                    a = 0;
                    termina = true;
                }
            }
        }
        logo.setTrasparencia(a);

        if(termina) {
            contTiempoT+=0.04f;
            if(contTiempoT > delayTerminado) {
                Render.juego.setScreen(new MenuInicial());
            }
        }

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
