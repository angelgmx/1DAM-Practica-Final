package juego.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuracion {
    private static Configuracion instancia;
    private boolean modoDepuracion;

    // Constructor privado para Singleton
    private Configuracion() {
        cargarConfiguracion();
    }

    // Método Singleton
    public static Configuracion getInstancia() {
        if (instancia == null) {
            instancia = new Configuracion();
        }
        return instancia;
    }

    // Carga la configuración desde el archivo
    private void cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (FileInputStream entrada = new FileInputStream("config/configuracion.properties")) {
            propiedades.load(entrada);
            modoDepuracion = Boolean.parseBoolean(propiedades.getProperty("depuracion", "false"));
        } catch (IOException e) {
            // Si hay error, se asume modo desactivado
            modoDepuracion = false;
         //   Log.registrarError("Error al cargar configuración: " + e.getMessage());
        }
    }

    // Verifica si el modo depuración está activado
    public boolean estaDepuracionActiva() {
        return modoDepuracion;
    }
}
