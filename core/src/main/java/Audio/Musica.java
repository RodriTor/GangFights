package Audio;

import Utilidades.Recursos;


public enum Musica {
    MENU(Recursos.MUSICA_MENU),
    JUEGO(Recursos.MUSICA_JUEGO);

    private final String ruta;

    Musica(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }
}
