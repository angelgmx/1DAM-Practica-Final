package juego.main;

import juego.config.Configuracion;
import juego.config.Constantes;
import juego.exceptions.NumeroJugadoresInvalidoException;
import juego.model.*;
import juego.questions.Pregunta;
import juego.service.*;
import java.io.IOException;
import java.util.List;

/**
 * Clase principal que gestiona el flujo del juego, el menú principal y la
 * inicialización de servicios.
 */
public class Juego {
    private List<Jugador> jugadores;
    private List<Pregunta> preguntas;
    private Ranking ranking;
    private Historico historico;

    public static boolean modoDepuracion;
    public static GestorJugadores gestorJugadores;

    public Juego() {
        historico = new Historico();
        ranking = new Ranking();
        gestorJugadores = new GestorJugadores();
        modoDepuracion = Configuracion.estaDepuracionActiva();
    }

    public static void main(String[] args) throws IOException, NumeroJugadoresInvalidoException {
        TerminalUtils.clearScreen();
        System.out.println(TerminalUtils.CYAN + TerminalUtils.BOLD + "╔══════════════════════════════════════════╗");
        System.out.println("║          BIENVENIDO A TRIVIA MASTER      ║");
        System.out.println("╚══════════════════════════════════════════╝" + TerminalUtils.RESET);

        Juego juego = new Juego();
        juego.mostrarMenu();
    }

    public void mostrarMenu() throws IOException {
        int opcion;
        do {
            TerminalUtils.printHeader("Menú Principal");
            System.out.println(TerminalUtils.YELLOW + "1." + TerminalUtils.RESET + " 🎮 Jugar partida");
            System.out.println(TerminalUtils.YELLOW + "2." + TerminalUtils.RESET + " 🏆 Ver ranking");
            System.out.println(TerminalUtils.YELLOW + "3." + TerminalUtils.RESET + " 📜 Ver histórico");
            System.out.println(TerminalUtils.YELLOW + "4." + TerminalUtils.RESET + " 👥 Gestionar Jugadores");
            System.out.println(TerminalUtils.YELLOW + "5." + TerminalUtils.RESET + " 🚪 Salir");
            System.out.print("\nSeleccione una opción " + TerminalUtils.CYAN + "-> " + TerminalUtils.RESET);

            if (Constantes.SCANNER.hasNextInt()) {
                opcion = Constantes.SCANNER.nextInt();
                Constantes.SCANNER.nextLine();
            } else {
                opcion = 404;
                Constantes.SCANNER.nextLine();
            }

            switch (opcion) {
                case 1 -> {
                    try {
                        Partida partida = new Partida();
                        partida.jugarPartida();
                    } catch (NumeroJugadoresInvalidoException e) {
                        TerminalUtils.printError(e.getMessage());
                        LoggerJuego.registrar("ERROR: No se pudo iniciar la partida");
                    }
                }
                case 2 -> {
                    TerminalUtils.printHeader("Ranking Global");
                    Ranking.mostrarRanking();
                }
                case 3 -> {
                    TerminalUtils.printHeader("Histórico de Partidas");
                    Historico.mostrarHistorico();
                }
                case 4 -> gestorJugadores.menuJugadores();
                case 5 -> TerminalUtils.printInfo("¡Gracias por jugar! Saliendo...");
                case 404 -> TerminalUtils.printError("Opción no válida. Intente de nuevo.");
                default -> TerminalUtils.printWarning("Opción fuera de rango.");
            }
        } while (opcion != 5);
    }
}
