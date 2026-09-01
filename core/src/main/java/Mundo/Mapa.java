package Mundo;

import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Mapa {

	private World mundo;

	private ArrayList<Plataforma> plataformas;

	public Mapa(World mundo) {
		this.mundo = mundo;

		plataformas = new ArrayList<Plataforma>();
		crearMapa();
	}

	private void crearMapa() {
		plataformas.add(new Plataforma(mundo, 0, 0, 1280, 40));

		plataformas.add(new Plataforma(mundo, 150, 430, 980, 35));

		plataformas.add(new Plataforma(mundo, 80, 550, 350, 30));

		plataformas.add(new Plataforma(mundo, 850, 550, 350, 30));

		plataformas.add(new Plataforma(mundo, 80, 40, 300, 200));

		plataformas.add(new Plataforma(mundo, 900, 40, 300, 200));

		plataformas.add(new Plataforma(mundo, 480, 40, 35, 150));

		plataformas.add(new Plataforma(mundo, 765, 40, 35, 150));

		plataformas.add(new Plataforma(mundo, 515, 40, 250, 25));

		plataformas.add(new Plataforma(mundo,515, 65, 35, 100));

		plataformas.add(new Plataforma(mundo, 730, 65, 35, 100));
	}

	public ArrayList<Plataforma> getPlataformas(){
		return plataformas;
	}
}
