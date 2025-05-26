package juego.service;

import juego.config.Constantes;
import juego.model.Jugador;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Historico {

    // Registrar una partida (se añade al final del archivo histórico.txt)
    public static void registrarPartida(Map<Jugador, Integer> jugadores) {

        // Sacamos los nombres de los jugadores del mapa y los introducimos en una lista
        List<Jugador> nombreJugadores = new ArrayList<>(jugadores.keySet());
        // Ordenamos por puntuación descendente
        nombreJugadores.sort((a, b) -> jugadores.get(b) - jugadores.get(a));

        // si quisiera poner una fecha al principio del histórico
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Creamos una línea con todos los jugadores de la partida
        StringBuilder linea = new StringBuilder();

        linea.append("[").append(ahora.format(formato)).append("]");

        for (Jugador jug : nombreJugadores) {
            linea.append(jug.getNombre())
                    .append(" ")
                    .append(jugadores.get(jug))
                    .append(" ");
        }

        // Añadimos un salto de línea al final para que la próxima partida quede en otra línea
        linea.append("\n");

        try {
            Files.writeString(
                    Constantes.HISTORICO_PATH,
                    linea.toString(),
                    StandardOpenOption.CREATE, // Si el archivo no existe lo crea automáticamente
                    StandardOpenOption.APPEND // lo añade al final del archivo existente
            );
        } catch (IOException e) {
            System.err.println("Error al guardar el histórico: " + e.getMessage());
        }

    }

    // Muestra todas las partidas del histórico
    public static void mostrarHistorico() {
        try {
            if (!Files.exists(Constantes.HISTORICO_PATH)) {
                System.out.println("No hay partidas en el histórico aún.");
                return;
            }
            // Metemos en una lista el Historico.txt
            List<String> lineas = Files.readAllLines(Constantes.HISTORICO_PATH);

            //Iteramos las líenas del histórico
            System.out.println("\n [HISTÓRICO DE PARTIDAS]");
            int cont = 0;

            for (String linea : lineas) {
                System.out.println("partida " + cont++ + " :" + linea.trim());
            }

        } catch (IOException e) {
            System.out.println("⚠️ Error al leer historico.txt");
        }
    }
}

