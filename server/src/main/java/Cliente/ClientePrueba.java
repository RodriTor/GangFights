
package Cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class ClientePrueba {

    public static void main(String[] args) {

        try (
            Socket socket = new Socket("localhost", 5000);

            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            PrintWriter salida = new PrintWriter(
                socket.getOutputStream(),
                true,
                StandardCharsets.UTF_8
            );

            Scanner teclado = new Scanner(System.in)
        ) {

            System.out.println(entrada.readLine());
            AtomicReference<String> estadoActual = new AtomicReference<>("Todavia no se recibio ningun estado");
            // Hilo que recibe los estados del servidor
            Thread receptor = new Thread(() -> {

                try {
                    String mensaje;

                    while ((mensaje = entrada.readLine()) != null) {
                        estadoActual.set(mensaje);
                    }

                } catch (Exception e) {
                    System.out.println("Conexion con el servidor perdida.");
                }
            });

            receptor.setDaemon(true);
            receptor.start();

            System.out.println("Escribi DERECHA, IZQUIERDA, SALTAR o DETENER.");
            System.out.println("Escribi SALIR para desconectarte.");

            while (teclado.hasNextLine()) {

                String mensaje = teclado.nextLine().trim();

                if (mensaje.equalsIgnoreCase("SALIR")) {
                    break;
                }

                if (mensaje.equalsIgnoreCase("VER")) {
                    System.out.println("Estado actual:");
                    System.out.println(estadoActual.get());
                    continue;
                }

                salida.println(mensaje);
                System.out.println("Comando enviado: " + mensaje);
            }

        } catch (Exception e) {
            System.out.println("Error del cliente: " + e.getMessage());
        }
    }
}
