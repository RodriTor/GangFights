package Interfaces;

import Logica.Partida;


public interface NavegadorPantallas {

    void irAMenu();

    void irAJuego();

    void irAConfiguracion();

    void irAOnline();

    void irAFinal(Partida partida);
}
