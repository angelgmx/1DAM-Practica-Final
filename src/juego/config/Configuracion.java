package juego.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Configuracion {

    private static boolean depuracionActiva;

    static {cargarConfiguracion();}


    private static void cargarConfiguracion() {
        depuracionActiva = false;

        // Valor por defecto
        try {
            if (Files.exists(Constantes.RUTA_DEPURACION)) {
                List<String> lineas = Files.readAllLines(Constantes.RUTA_DEPURACION);
                for (String linea : lineas) {
                    String[] partes = linea.trim().split("=");
                    if (partes.length == 2) {
                        if (partes[0].trim().equalsIgnoreCase("depuracion")) {
                            depuracionActiva = partes[1].trim().equalsIgnoreCase("true");
                        }
                    }
                }
            } else {
                System.out.println("Archivo de configuración no encontrado");
            }
        } catch (IOException e) {
            System.err.println("Error al leer configuración: " + e.getMessage());
        }
    }

    public static boolean estaDepuracionActiva() {
        return depuracionActiva;
    }
}
