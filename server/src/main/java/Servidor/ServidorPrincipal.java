
package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.physics.box2d.Box2D;

import Mundo.MundoServidor;

public class ServidorPrincipal {

    private static final int PUERTO = 5000;
    private static final boolean[] jugadores = new boolean[2];
    private static final Map<Integer, PrintWriter> clientes = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        try {
            // Inicializar bibliotecas nativas
            GdxNativesLoader.load();
            Box2D.init();

            // Crear mundo físico
            MundoServidor mundo = new MundoServidor();

            // Iniciar hilo de simulación

            Thread hiloFisica = new Thread(() -> {

            	while (!Thread.currentThread().isInterrupted()) {

            		mundo.actualizar();
        
            		String estado = mundo.obtenerEstado();

            		for (PrintWriter cliente : clientes.values()) {
            			cliente.println(estado);
            		}

            		try {
            			Thread.sleep(16);
        } 	catch (InterruptedException e) {
            	Thread.currentThread().interrupt();
        }
    }

});

hiloFisica.setName("Hilo-Fisica");
hiloFisica.start();


            
            System.out.println("GANGFIGHTS - SERVIDOR");
            System.out.println("Mundo fisico creado correctamente");
            System.out.println("Esperando clientes en el puerto " + PUERTO);

            try (ServerSocket servidor = new ServerSocket(PUERTO)) {

                while (true) {

                    Socket cliente = servidor.accept();

                    int numeroJugador = asignarJugador();

                    if (numeroJugador == -1) {

                        PrintWriter salida = new PrintWriter(
                            cliente.getOutputStream(),
                            true,
                            StandardCharsets.UTF_8
                        );

                        salida.println("RECHAZADO: LA PARTIDA YA ESTA LLENA");
                        cliente.close();

                        System.out.println("Conexion rechazada.");

                    } else {

                        System.out.println(
                            "Jugador " + numeroJugador + " conectado"
                        );

                        // Agregar jugador al mundo físico
                        mundo.agregarJugador(numeroJugador);

                        Thread hilo = new Thread(
                            () -> atenderCliente(
                                cliente, numeroJugador, mundo
                            )
                        );

                        hilo.start();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static synchronized int asignarJugador() {

        for (int i = 0; i < jugadores.length; i++) {

            if (!jugadores[i]) {
                jugadores[i] = true;
                return i + 1;
            }
        }

        return -1;
    }

    private static void atenderCliente(Socket cliente, int numeroJugador, MundoServidor mundo) {

        try (
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(
                    cliente.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            PrintWriter salida = new PrintWriter(
                cliente.getOutputStream(),
                true,
                StandardCharsets.UTF_8
            )
        ) {

            salida.println("JUGADOR " + numeroJugador);
            clientes.put(numeroJugador, salida);
            
            actualizarSala();

            String mensaje;

            while ((mensaje = entrada.readLine()) != null) {

                System.out.println(
                    "Jugador " + numeroJugador + ": " + mensaje
                );

                mundo.procesarAccion(numeroJugador, mensaje);
            }

        } catch (IOException e) {

            System.out.println(
                "Error con el jugador " + numeroJugador +
                ": " + e.getMessage()
            );

        } finally {

            try {
                cliente.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar la conexion.");
            }
            clientes.remove(numeroJugador);
            mundo.quitarJugador(numeroJugador);
            liberarJugador(numeroJugador);
            actualizarSala();

            System.out.println(
                "Jugador " + numeroJugador + " desconectado."
            );
        }
    }
    
    
    private static void actualizarSala() {
    	int cantidadJugadores = clientes.size();
    	
    	if(cantidadJugadores < 2) {
    		
    		for (PrintWriter cliente : clientes.values()) {
    			cliente.println("ESPERANDO;" + cantidadJugadores + ";2");
    		}
    		
    		System.out.println("Esperando al segundo jugador...");
    	} else {
    		System.out.println("Dos jugadores conectados. Iniciando la partida!");
    	
    	for (PrintWriter cliente : clientes.values()) {
    		cliente.println("INICIAR_PARTIDA");
    	}
    	
    	}
    }

    private static synchronized void liberarJugador(int numeroJugador) {
        jugadores[numeroJugador - 1] = false;
    }
}
