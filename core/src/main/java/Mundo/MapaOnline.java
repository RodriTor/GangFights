package Mundo;


import Utilidades.Recursos;
import Utilidades.Render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapaOnline {
	private final TiledMap mapa;
	private final OrthogonalTiledMapRenderer renderizador;
	
	
	public MapaOnline() {
		
		TmxMapLoader.Parameters parametros = new TmxMapLoader.Parameters();
		
		mapa = new TmxMapLoader().load(Recursos.MAPA_AULA, parametros);
		renderizador = new OrthogonalTiledMapRenderer(mapa, 1f, Render.batch);
		 
	}
	

    public void dibujar(OrthographicCamera camara) {

        renderizador.setView(camara);
        renderizador.render();
    }
    
    public void dispose() {

        renderizador.dispose();
        mapa.dispose();
    }
    
	
	
	
}
