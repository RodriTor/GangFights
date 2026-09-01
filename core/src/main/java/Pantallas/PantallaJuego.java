package Pantallas;

import Entidades.Entidad;
import Entidades.Jugador;
import Mundo.Mapa;
import Mundo.Plataforma;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static Utilidades.Render.batch;

public class PantallaJuego implements Screen {

	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camara;

	private World mundo;
	private Mapa mapa;
	private Jugador jugador;
    private Jugador jugador2;


	private final float ancho_mundo = 1280;
	private final float alto_mundo = 720;




	@Override
	public void show() {
		shapeRenderer = new ShapeRenderer();


		camara = new OrthographicCamera(ancho_mundo, alto_mundo);

		camara.position.set(ancho_mundo / 2f, alto_mundo / 2f, 0);

		camara.update();

        mundo = new World(new Vector2(0, -40f), true);
		mapa = new Mapa(mundo);

        jugador = new Jugador(
            mundo,
            640,
            300,
            1,
            Recursos.JasinskiQUieto,
            Recursos.JasinskiAgachado,
            Recursos.JasinskiCorrer
        );

        jugador2 = new Jugador(
            mundo,
            700,
            300,
            2,
            Recursos.JasinskiQUieto,
            Recursos.JasinskiAgachado,
            Recursos.JasinskiCorrer
        );
    }


	@Override
	public void render(float delta) {

		jugador.actualizar(delta);
        jugador2.actualizar(delta);

		//actualizamos la fisica
		mundo.step(1 / 60f, 6, 2);


		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camara.update();

		// esto hace que shaperender use nuestra camara
		shapeRenderer.setProjectionMatrix(camara.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		for(Plataforma plataforma : mapa.getPlataformas()) {
			shapeRenderer.rect(plataforma.getX(), plataforma.getY(), plataforma.getAncho(), plataforma.getAlto());
		}

		float jugadorX = jugador.getCuerpo().getPosition().x * 10f;

		float jugadorY = jugador.getCuerpo().getPosition().y * 10f;

		shapeRenderer.end();

        Render.comenzarBatch(camara);

        TextureRegion frameJasinski = jugador.getFrameActual();
        float posX = (jugador.getCuerpo().getPosition().x * 10f) - (jugador.getAncho() / 2f);
        float posY = (jugador.getCuerpo().getPosition().y * 10f) - (jugador.getAlto() / 2f);

        batch.draw(frameJasinski, posX, posY, jugador.getAncho(), jugador.getAlto());

        TextureRegion frameJugador2 = jugador2.getFrameActual();
        float posX2 = (jugador2.getCuerpo().getPosition().x * 10f) - (jugador2.getAncho() / 2f);
        float posY2 = (jugador2.getCuerpo().getPosition().y * 10f) - (jugador2.getAlto() / 2f);
        batch.draw(frameJugador2, posX2, posY2, jugador2.getAncho(), jugador2.getAlto());

        Render.terminarBatch();
	}

	@Override

	public void resize(int width, int height) {
		camara.viewportWidth = ancho_mundo;
		camara.viewportHeight = alto_mundo;
		camara.update();
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
		if(shapeRenderer != null) {
			shapeRenderer.dispose();
		}

		if(mundo != null) {
			mundo.dispose();
		}
	}


}
