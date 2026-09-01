package Mundo;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.physics.box2d.FixtureDef;
public class Plataforma {
	private Body cuerpo;

	private float x;
	private float y;
	private float ancho;
	private float alto;

	private float escala = 10f;

	public Plataforma(World mundo, float x, float y, float ancho, float alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		crearCuerpo(mundo);
	}

    private void crearCuerpo(World mundo) {
        BodyDef cuerpoDef = new BodyDef();
        cuerpoDef.type = BodyDef.BodyType.StaticBody;

        float centroX = (x + ancho / 2f) / escala;
        float centroY = (y + alto / 2f) / escala;
        cuerpoDef.position.set(centroX, centroY);

        cuerpo = mundo.createBody(cuerpoDef);

        PolygonShape forma = new PolygonShape();
        forma.setAsBox((ancho/2f)/escala, (alto/2f) / escala);

        com.badlogic.gdx.physics.box2d.FixtureDef fixtureDef = new com.badlogic.gdx.physics.box2d.FixtureDef();
        fixtureDef.shape = forma;
        fixtureDef.density = 0f;
        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = -1;

        cuerpo.createFixture(fixtureDef);
        forma.dispose();
    }

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}


	public float getAncho() {
		return ancho;
	}

	public float getAlto() {
		return alto;
	}
}
