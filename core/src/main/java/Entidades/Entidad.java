package Entidades;

public abstract class Entidad {
	
	private float x;
	private float y;
	
	private float ancho;
	private float alto;
	
	
	public Entidad(float x, float y, float ancho, float alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		
		
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
	
	public abstract void actualizar(float delta);
	
	public abstract void dibujar();
	

}
