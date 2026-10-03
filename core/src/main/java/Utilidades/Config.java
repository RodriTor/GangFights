package Utilidades;

public final class Config {

    private Config() { }

    public static final float ANCHO_MUNDO = 1280f;
    public static final float ALTO_MUNDO = 720f;

    public static final float PIXELES_POR_METRO = 10f;
    public static final float GRAVEDAD = -40f;

    public static final float PASO_FISICA = 1 / 60f;
    public static final float MAXIMO_DELTA = 0.25f;
    public static final int ITERACIONES_VELOCIDAD = 6;
    public static final int ITERACIONES_POSICION = 2;

    public static final short CATEGORIA_PLATAFORMA = 0x0001;
    public static final short CATEGORIA_JUGADOR = 0x0002;
    public static final short MASCARA_TODOS = -1;
}
