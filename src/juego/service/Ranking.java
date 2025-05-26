package juego.service;

import juego.config.Constantes;
import juego.model.Jugador;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ranking {


    private static Map<String, Integer> rankingMap = new HashMap<>();
    String ganador = "";

    /**
     * Actualiza el ranking de jugadores con los puntos proporcionados.
     * Si un jugador ya existe en el ranking, se suman sus puntos.
     * Si no existe, se añade al ranking con los puntos iniciales.
     *
     * @param jugadores Mapa de jugadores y sus puntos a actualizar.
     */

    public static void actualizarRanking(Map<Jugador, Integer> jugadores) {

        // Tenemos que extraer los puntos de los jugadores ya existentes en el mapa del ranking
        // y sumarle los nuevos puntos por eso usaremos .getOrDefault que nos da el valor por defecto de un nombre
        for (Map.Entry<Jugador, Integer> entrada : jugadores.entrySet()) {

            // Como el mapa de ranking usa String para su clave necesitamos pasarla
            String nombre = entrada.getKey().getNombre();
            // Obtenemos los nuevos puntos del jugador
            int nuevosPuntos = entrada.getValue();
            // Si el jugador ya existe en el ranking, sumamos sus puntos
            int puntosPrevios = rankingMap.getOrDefault(nombre, 0);
            // Actualizamos el ranking con los nuevos puntos
            rankingMap.put(nombre, puntosPrevios + nuevosPuntos);
        }
    }

    /**
     * Guarda el ranking de jugadores en un archivo.
     * El archivo se crea o se trunca si ya existe.
     *
     * @param jugadores Mapa de jugadores y sus puntos.
     */
    public static void guardarRanking(Map<Jugador, Integer> jugadores) {

        actualizarRanking(jugadores); // metemos los jugadores y sus puntos al mapa Ranking actualizados

        // guardaremos Jugador y puntuación
        List<String> lineas = new ArrayList<>();

        for (Map.Entry<Jugador, Integer> entrada : jugadores.entrySet()) {
            // guardamos nombre y puntos -> "Juan 32"
            lineas.add(entrada.getKey().getNombre() + " " + entrada.getValue());
        }

        // Escribimos las líneas en el archivo ranking.txt
        try {
            Files.write(
                    Constantes.RANKING_PATH,       // ruta
                    lineas,                         // Que vamos a escribir
                    StandardOpenOption.CREATE,   // Si no existe el archivo lo crea
                   StandardOpenOption.TRUNCATE_EXISTING // lo borra y escribe de nuevo
            );

        } catch (IOException e) {
            System.out.println("Error guardando Ranking.txt");
        }
    }

    public static void mostrarRanking () {

        // Para mostrar el ranking de forma ordenada necesito pasarlo a una lista, entonces creamos una
        //lista con el mapa rankingMap

        // Lista de objetos Map.entry<clave-valor>
        List<Map.Entry<String, Integer>> rankingOrdenado = new ArrayList<>(rankingMap.entrySet());

        // Ordenar de mayor a menor puntuación
        // de b - a se ordena de forma descendente y de a - b de forma ascendente
        // b - a -> si el resultado es negativo significa que b es menor que a, b va a antes
        rankingOrdenado.sort((a, b) -> b.getValue() - a.getValue());

        for (Map.Entry<String, Integer> entrada : rankingOrdenado) {
                System.out.println("👤 " + entrada.getKey() + " -> " + entrada.getValue() + " puntos");
        }
    }

    public static String ganador(){

        int max;
        String ganador = " ";
        for (Map.Entry<String, Integer> entrada : rankingMap.entrySet()) {
            max = 1;
            if (entrada.getValue() > max) {
                max = entrada.getValue();
                ganador = entrada.getKey();
            }
        }
        return ganador;
    }

    public static void eliminarJugador (String nombre){

        if (rankingMap.containsKey(nombre)) {
            rankingMap.remove(nombre);
            System.out.println("-> jugador: " + nombre + " ha sido eliminado...");
        }

    }


}
