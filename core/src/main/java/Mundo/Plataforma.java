package Mundo;

import Utilidades.Config;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Plataforma {

    public Plataforma(World mundo, float x, float y, float ancho, float alto) {
        BodyDef cuerpoDef = new BodyDef();
        cuerpoDef.type = BodyDef.BodyType.StaticBody;
        cuerpoDef.position.set((x + ancho / 2f) / Config.PIXELES_POR_METRO,
            (y + alto / 2f) / Config.PIXELES_POR_METRO);

        Body cuerpo = mundo.createBody(cuerpoDef);

        PolygonShape forma = new PolygonShape();
        forma.setAsBox((ancho / 2f) / Config.PIXELES_POR_METRO, (alto / 2f) / Config.PIXELES_POR_METRO);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = forma;
        fixtureDef.density = 0f;
        fixtureDef.filter.categoryBits = Config.CATEGORIA_PLATAFORMA;
        fixtureDef.filter.maskBits = Config.MASCARA_TODOS;

        cuerpo.createFixture(fixtureDef);
        forma.dispose();
    }
}
