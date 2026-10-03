package Pantallas;

public enum OpcionMenu {
    PLAY("PLAY"),
    CONFIGURACION("CONFIGURACION");

    private final String texto;

    OpcionMenu(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
