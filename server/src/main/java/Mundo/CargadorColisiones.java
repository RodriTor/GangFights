
package Mundo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CargadorColisiones {

    public static List<float[]> cargar(String ruta) {

        List<float[]> colisiones = new ArrayList<>();

        try {
            File archivo = new File(ruta);

            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

            factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl",
                true
            );

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(archivo);

            Element mapa = documento.getDocumentElement();

            float altoMapa =
                Float.parseFloat(mapa.getAttribute("height"))
                * Float.parseFloat(mapa.getAttribute("tileheight"));

            NodeList capas = documento.getElementsByTagName("objectgroup");

            for (int i = 0; i < capas.getLength(); i++) {

                Element capa = (Element) capas.item(i);

                if (!capa.getAttribute("name").equals("Colisiones")) {
                    continue;
                }

                NodeList objetos = capa.getElementsByTagName("object");

                for (int j = 0; j < objetos.getLength(); j++) {

                    Element objeto = (Element) objetos.item(j);

                    float x = Float.parseFloat(objeto.getAttribute("x"));
                    float y = Float.parseFloat(objeto.getAttribute("y"));
                    float ancho = Float.parseFloat(objeto.getAttribute("width"));
                    float alto = Float.parseFloat(objeto.getAttribute("height"));

                    // Tiled usa coordenadas desde arriba;
                    // Box2D trabaja con el origen abajo.
                    float yConvertida = altoMapa - y - alto;

                    colisiones.add(new float[] {
                        x, yConvertida, ancho, alto
                    });
                }
            }

            System.out.println(
                "Colisiones cargadas: " + colisiones.size()
            );

        } catch (Exception e) {
            throw new RuntimeException(
                "No se pudo cargar el mapa: " + ruta, e
            );
        }

        return colisiones;
    }
}
