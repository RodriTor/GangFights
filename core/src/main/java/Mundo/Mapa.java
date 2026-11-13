package Mundo;

import Utilidades.Config;
import Utilidades.Recursos;
import Utilidades.Render;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;


public class Mapa {

    private static final String CAPA_COLISIONES = "Colisiones";
    private static final float GROSOR_PARED = 20f;

    private final TiledMap mapaTiled;
    private final OrthogonalTiledMapRenderer renderizador;
    private final float anchoPixeles;
    private final ArrayList<Rectangle> obstaculos = new ArrayList<>();

    public Mapa(World mundo) {
        TmxMapLoader.Parameters parametros = new TmxMapLoader.Parameters();
        parametros.textureMinFilter = Texture.TextureFilter.Nearest;
        parametros.textureMagFilter = Texture.TextureFilter.Nearest;

        mapaTiled = new TmxMapLoader().load(Recursos.MAPA_AULA, parametros);
        renderizador = new OrthogonalTiledMapRenderer(mapaTiled, 1f, Render.batch); // usa el batch compartido

        int columnas = mapaTiled.getProperties().get("width", Integer.class);
        int anchoTile = mapaTiled.getProperties().get("tilewidth", Integer.class);
        anchoPixeles = columnas * anchoTile;

        crearColisiones(mundo);
        crearParedesLaterales(mundo);
    }

    private void crearColisiones(World mundo) {
        MapLayer capa = mapaTiled.getLayers().get(CAPA_COLISIONES);
        if (capa == null) return;

        for (MapObject objeto : capa.getObjects()) {
            if (objeto instanceof RectangleMapObject) {
                Rectangle r = ((RectangleMapObject) objeto).getRectangle();
                new Plataforma(mundo, r.x, r.y, r.width, r.height);
                obstaculos.add(new Rectangle(r));
            }
        }
    }

    private void crearParedesLaterales(World mundo) {
        new Plataforma(mundo, -GROSOR_PARED, 0, GROSOR_PARED, Config.ALTO_MUNDO);
        new Plataforma(mundo, anchoPixeles, 0, GROSOR_PARED, Config.ALTO_MUNDO);
    }

    /**
     * Puntos (x centro, y de la superficie) sobre el piso y las plataformas donde entra un objeto
     * de ese tamano sin quedar dentro de un mueble ni de otra plataforma. Las superficies mas altas que
     * alturaMaxima se descartan (no se llega a ellas saltando).
     */
    public ArrayList<Vector2> buscarPuntosLibres(float ancho, float alto, float paso, float alturaMaxima) {
        ArrayList<Vector2> puntos = new ArrayList<>();

        for (Rectangle base : obstaculos) {
            float superficie = base.y + base.height;
            if (superficie > alturaMaxima) continue;

            for (float x = base.x + ancho / 2f; x <= base.x + base.width - ancho / 2f; x += paso) {
                if (hayEspacioLibre(x - ancho / 2f, superficie, ancho, alto)) {
                    puntos.add(new Vector2(x, superficie));
                }
            }
        }
        return puntos;
    }

    private boolean hayEspacioLibre(float x, float y, float ancho, float alto) {
        Rectangle caja = new Rectangle(x, y + 0.5f, ancho, alto);
        for (Rectangle obstaculo : obstaculos) {
            if (obstaculo.overlaps(caja)) return false;
        }
        return true;
    }

    public void dibujar(OrthographicCamera camara) {
        renderizador.setView(camara);
        renderizador.render();
    }

    public void dispose() {
        renderizador.dispose();
        mapaTiled.dispose();
    }
}
