package Audio;

import Utilidades.Recursos;

public enum Efecto {
    GOLPE(Recursos.SFX_GOLPE),
    SALTO(Recursos.SFX_SALTO),
    ELIMINACION(Recursos.SFX_ELIMINACION),
    CLICK(Recursos.SFX_CLICK),
    VICTORIA(Recursos.SFX_VICTORIA);

    private final String ruta;

    Efecto(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }
}
