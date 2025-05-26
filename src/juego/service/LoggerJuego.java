package juego.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static juego.config.Constantes.LOG_PATH;

public class LoggerJuego {

    private static final DateTimeFormatter FECHA_HORA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FECHA_ARCHIVO_FORMATO = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static void registrar(String mensaje) {
        try {
            comprobarCambioDeDia();

            // Obtiene la fecha y hora actual
            String lineaLog = "[" + LocalDateTime.now().format(FECHA_HORA_FORMATO) + "]: " + mensaje;

            Files.createDirectories(LOG_PATH.getParent()); // Asegura que exista la carpeta

            // Escribimos en el archivo
            Files.writeString(LOG_PATH, lineaLog + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("ERROR. No se pudo escribir en el log: " + e.getMessage());
        }
    }

    // Comprueba si se necesita rotar el archivo
    private static void comprobarCambioDeDia() throws IOException {

        //1. Verificamos si existe el fichero
        if (!Files.exists(LOG_PATH)) {

            // 2. Leemos todas las línas del archivo en memoria como una lista
            List<String> lineas = Files.readAllLines(LOG_PATH);

            // 3. Si el archivo está vacío, salimos (no hay nada que comprobar)
            if (lineas.isEmpty()) {
                System.out.println("está vacío el archivo...");
            }

            // 4. Obtenemos la última liena
            String ultimaLiena = lineas.get(lineas.size() -1);

            // 5. Extraemos la fecha de la última liena
            String ultimaFecha = ultimaLiena.substring(1,11);

            //6. Parseamos esa fecha a un objeto LocalDate para poder comprobarla
            // Convertimos la cadena de texto ultimaFecha en un objeto LocalDate
            LocalDate fechaLog = LocalDate.parse(ultimaFecha, FECHA_ARCHIVO_FORMATO);

            //7. Obtenemos la fecha de hoy
            LocalDate hoy = LocalDate.now();

            // 8. Si la fecha de la última traza no es hoy --> renombramos el log
            if (!fechaLog.equals(hoy)) {

                // Aplicamos el formateador al objeto fechaLog que ahora es un locaDate con el formato yyyMMdd
                String fechaParaNombre = fechaLog.format(FECHA_ARCHIVO_FORMATO);

                // Construimos el nuevo nombre del archivo
                Path nuevoNombre = LOG_PATH.resolveSibling("salida.log" + fechaParaNombre);
                // Renombramos y movemos el archivo
                Files.move(LOG_PATH,nuevoNombre,StandardCopyOption.REPLACE_EXISTING);
            }
        }

    }


}
