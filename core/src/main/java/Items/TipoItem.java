package Items;

import Utilidades.Recursos;

/** Tipos de objeto que pueden aparecer en el mapa. Cada uno sabe cual es su imagen. */
public enum TipoItem {
    BOTIQUIN(Recursos.VIDA),
    PISTOLA(Recursos.PISTOLA);

    private final String ruta;

    TipoItem(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }
}
