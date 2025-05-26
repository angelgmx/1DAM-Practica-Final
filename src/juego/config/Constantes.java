package juego.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Constantes {
    public static final int MAX_JUGADORES = 4;
    public static final int MIN_JUGADORES = 1;
    public static final Path RUTACIUDADES_PATH = Paths.get("src", "data","ciudades.csv");
    public static final Path HISTORICO_PATH = Paths.get("src","data","historico.txt");
    public static final Path RANKING_PATH = Paths.get("src","data","ranking.txt");
    public static final Path RUTA_DEPURACION = Paths.get("src","juego","config","configuration.properties");
    public static final Path LOG_PATH = Paths.get("src","logs", "salida.log");
    public static final Scanner SCANNER = new Scanner(System.in);
}
