package Entidades;

import Utilidades.Recursos;

public enum Personaje {

    JASINSKI("JASINSKI",
        Recursos.JASINSKI_QUIETO, Recursos.JASINSKI_AGACHADO, Recursos.JASINSKI_CORRER,
        Recursos.JASINSKI_SALTAR, Recursos.JASINSKI_GOLPE,
        Recursos.JASINSKI_RECIBIR, Recursos.JASINSKI_MUERTO),

    // TEMPORAL: Schepis todavia no tiene sprites de recibir ni de muerto, por ahora usa estos.
    // Cuando existan, solo hay que cambiar estas dos rutas por SCHEPIS_RECIBIR y SCHEPIS_MUERTO.
    SCHEPIS("SCHEPIS",
        Recursos.SCHEPIS_QUIETO, Recursos.SCHEPIS_AGACHADO, Recursos.SCHEPIS_CORRER,
        Recursos.SCHEPIS_SALTAR, Recursos.SCHEPIS_GOLPE,
        Recursos.SCHEPIS_SALTAR, Recursos.SCHEPIS_AGACHADO);

    private final String nombre;
    private final String rutaQuieto;
    private final String rutaAgachado;
    private final String rutaCorrer;
    private final String rutaSalto;
    private final String rutaGolpe;
    private final String rutaRecibir;
    private final String rutaMuerto;

    Personaje(String nombre, String rutaQuieto, String rutaAgachado, String rutaCorrer,
              String rutaSalto, String rutaGolpe, String rutaRecibir, String rutaMuerto) {
        this.nombre = nombre;
        this.rutaQuieto = rutaQuieto;
        this.rutaAgachado = rutaAgachado;
        this.rutaCorrer = rutaCorrer;
        this.rutaSalto = rutaSalto;
        this.rutaGolpe = rutaGolpe;
        this.rutaRecibir = rutaRecibir;
        this.rutaMuerto = rutaMuerto;
    }

    public String getNombre() { return nombre; }
    public String getRutaQuieto() { return rutaQuieto; }
    public String getRutaAgachado() { return rutaAgachado; }
    public String getRutaCorrer() { return rutaCorrer; }
    public String getRutaSalto() { return rutaSalto; }
    public String getRutaGolpe() { return rutaGolpe; }
    public String getRutaRecibir() { return rutaRecibir; }
    public String getRutaMuerto() { return rutaMuerto; }
}
