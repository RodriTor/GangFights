
package Red;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClienteRed {

    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;

    private Thread hiloReceptor;

    private volatile boolean conectado;
    private volatile boolean conectando;

    private volatile int numeroJugador = 0;
    private volatile String ultimoEstado = "";
    private volatile String ultimoError = "";
    private volatile int jugadoresConectados = 0;
    private volatile boolean partidaIniciada = false;

    public void conectar(String ip, int puerto) {

        if (conectado || conectando) {
            return;
        }

        conectando = true;

        Thread hiloConexion = new Thread(() -> {

            try {
                socket = new Socket(ip, puerto);

                entrada = new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                );

                salida = new PrintWriter(
                    socket.getOutputStream(),
                    true,
                    StandardCharsets.UTF_8
                );

                String identificacion = entrada.readLine();

                if (identificacion == null ||
                    !identificacion.startsWith("JUGADOR ")) {
                    throw new IOException("Identificacion invalida del servidor");
                }

                numeroJugador = Integer.parseInt(
                    identificacion.substring(8).trim()
                );

                conectado = true;
                conectando = false;

                hiloReceptor = new Thread(() -> recibirEstados());
                hiloReceptor.setName("Receptor-Estados");
                hiloReceptor.setDaemon(true);
                hiloReceptor.start();

            } catch (Exception e) {

                ultimoError = e.getMessage() != null
                    ? e.getMessage()
                    : "No se pudo conectar con el servidor";

                System.out.println("Error al conectar: " + ultimoError);

                cerrar();
            }

        });

        hiloConexion.setName("Conexion-Servidor");
        hiloConexion.setDaemon(true);
        hiloConexion.start();
    }

    private void recibirEstados() {

        try {
            String mensaje;

            while (conectado && (mensaje = entrada.readLine()) != null) {

                if (mensaje.startsWith("ESTADO")) {
                    
                	ultimoEstado = mensaje;
                	
                	
                } else if (mensaje.startsWith("ESPERANDO")) {
                	String[] datos = mensaje.split(";");
                	
                	
                	if(datos.length >= 3) {
                		jugadoresConectados = Integer.parseInt(datos[1].trim());
                		partidaIniciada = false;
                	}
                	
                	
                } else if (mensaje.equals("INICIAR_PARTIDA")) {
                	System.out.println("RECIBI INICIAR_PARTIDA");
                	
                	partidaIniciada = true;
                	jugadoresConectados = 2;
                }
            }

        } catch (IOException e) {

            if (conectado) {
                System.out.println("Se perdio la conexion con el servidor.");
            }

        } finally {
            cerrar();
        }
    }

    public synchronized void enviarAccion(String accion) {

        if (conectado && salida != null) {
            salida.println(accion);
        }
    }

    public String obtenerEstado() {
        return ultimoEstado;
    }

    public int obtenerNumeroJugador() {
        return numeroJugador;
    }

    public boolean estaConectado() {
        return conectado;
    }

    public boolean estaConectando() {
        return conectando;
    }
    
    public String obtenerError() {
        return ultimoError;
    }
    
    public int obtenerJugadoresConectados() {
    	return jugadoresConectados;
    }
    
    public boolean estaPartidaIniciada() {
    	return partidaIniciada;
    }

    public synchronized void cerrar() {

        conectado = false;
        conectando = false;
        partidaIniciada = false;
        jugadoresConectados = 0;

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexion.");
        }

        socket = null;
        entrada = null;
        salida = null;
    }
}
