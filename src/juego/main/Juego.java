package juego.main;
import juego.config.Configuracion;
import juego.config.Constantes;
import juego.exceptions.NumeroJugadoresInvalidoException;
import juego.model.*;
import juego.questions.Pregunta;
import juego.service.GestorJugadores;
import juego.service.Historico;
import juego.service.LoggerJuego;
import juego.service.Ranking;
import java.io.IOException;
import java.util.List;

/**
 * Clase principal que gestiona el flujo del juego, el menú principal y la inicialización de servicios.
 */
public class Juego {
    /** Lista de jugadores que participan en el juego */
    private List<Jugador> jugadores;
    private List<Pregunta> preguntas;
    private Ranking ranking;
    private Historico historico;

    // Variable que indica si el juego está en modo depuración
    public static boolean modoDepuracion;
    public static GestorJugadores gestorJugadores;

    /**
     * Constructor de la clase Juego.
     * Inicializa el histórico, ranking, gestor de jugadores y el modo depuración.
     */
    public Juego(){
        historico = new Historico();
        ranking = new Ranking();
        gestorJugadores = new GestorJugadores();
        modoDepuracion = Configuracion.estaDepuracionActiva();
    }
    // main
    public static void main(String[] args) throws IOException, NumeroJugadoresInvalidoException {
        Juego juego = new Juego();
        juego.mostrarMenu();
    }

    /**
     * Muestra el menú principal del juego y gestiona la selección de opciones por el usuario.
     * @throws IOException Si ocurre un error de entrada/salida
     */
    public void mostrarMenu() throws IOException {
        int opcion;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Jugar partida");
            System.out.println("2. Ver ranking");
            System.out.println("3. Ver histórico");
            System.out.println("4. Jugadores");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            System.out.print("->  ");
            if (Constantes.SCANNER.hasNextInt()) {
                opcion = Constantes.SCANNER.nextInt();
                Constantes.SCANNER.nextLine();
            } else {
                opcion = 404;
                Constantes.SCANNER.nextLine();
            }

            switch (opcion){
                case 1 -> {
                    try {
                        Partida partida = new Partida();
                        partida.jugarPartida();
                    } catch (NumeroJugadoresInvalidoException e ) {
                        System.out.println();
                        System.out.println(e.getMessage());
                        LoggerJuego.registrar("ERROR: No se pudo iniciar la partida");
                    }
                }
                case 2 ->{
                    System.out.println("--RANKING--🏆");
                    Ranking.mostrarRanking();
                }
                case 3 -> Historico.mostrarHistorico();
                case 4 -> gestorJugadores.menuJugadores();
                case 5 -> System.out.println("saliendo");
                case 404 -> System.err.println("Opción inválida");
            }
        } while (opcion != 5);
    }
}
