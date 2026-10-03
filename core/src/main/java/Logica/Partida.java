package Logica;


public class Partida {

    public static final int RONDAS_PARA_GANAR = 3;

    private final String[] nombres = new String[2];
    private final int[] rondasGanadas = new int[2];

    private int rondaActual = 1;
    private int ganadorRonda = -1;
    private EstadoPartida estado = EstadoPartida.EN_CURSO;

    public Partida(String nombreJugador1, String nombreJugador2) {
        nombres[0] = nombreJugador1;
        nombres[1] = nombreJugador2;
    }

    public void terminarRonda(int indiceGanador) {
        ganadorRonda = indiceGanador;
        rondasGanadas[indiceGanador]++;

        if (rondasGanadas[indiceGanador] >= RONDAS_PARA_GANAR) {
            estado = EstadoPartida.FINALIZADA;
        } else {
            estado = EstadoPartida.RONDA_TERMINADA;
        }
    }

    public void siguienteRonda() {
        rondaActual++;
        ganadorRonda = -1;
        estado = EstadoPartida.EN_CURSO;
    }

    public void alternarPausa() {
        if (estado == EstadoPartida.EN_CURSO) {
            estado = EstadoPartida.PAUSADA;
        } else if (estado == EstadoPartida.PAUSADA) {
            estado = EstadoPartida.EN_CURSO;
        }
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public String getNombre(int indice) {
        return nombres[indice];
    }

    public int getRondasGanadas(int indice) {
        return rondasGanadas[indice];
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public int getGanadorRonda() {
        return ganadorRonda;
    }

    /** Indice del ganador final, o -1 si todavia nadie gano la partida. */
    public int getGanadorPartida() {
        if (rondasGanadas[0] >= RONDAS_PARA_GANAR) return 0;
        if (rondasGanadas[1] >= RONDAS_PARA_GANAR) return 1;
        return -1;
    }
}
