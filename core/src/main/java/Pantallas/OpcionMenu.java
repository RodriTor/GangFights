package Pantallas;

public enum OpcionMenu {
    PLAY("PLAY"),
    ONLINE("ONLINE"),
    CONFIGURACION("CONFIGURACION");

    private final String texto;

    OpcionMenu(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
