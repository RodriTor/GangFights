
package Servidor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

public class PruebaBox2D {

    public static void main(String[] args) {

        try {

            Box2D.init();

            World mundo = new World(
                new Vector2(0, -9.8f),
                true
            );

           
            System.out.println("GANGFIGHTS - BOX2D");
            System.out.println("Box2D inicializado correctamente.");
            System.out.println("Mundo fisico creado correctamente.");

            mundo.dispose();

            System.out.println("Mundo fisico cerrado correctamente.");

        } catch (Exception e) {

            System.out.println("Error al inicializar Box2D:");
            e.printStackTrace();
        }
    }
}
