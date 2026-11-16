
package Interfaces;

import Logica.Partida;
import Red.ClienteRed;

public interface NavegadorPantallas {

    void irAMenu();

    void irAJuego();

    void irAConfiguracion();

    void irAOnline();

    void irAFinal(Partida partida);
    
    void irAPartidaOnline(ClienteRed clienteRed);
}
