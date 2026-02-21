package juego.model;

import juego.config.Constantes;
import juego.exceptions.NumeroJugadoresInvalidoException;
import juego.main.Juego;
import juego.questions.GeneradorDePreguntas;
import juego.questions.Pregunta;
import juego.questions.PreguntaCronometro;
import juego.questions.PreguntaAzar;
import juego.service.*;
import java.io.IOException;
import java.util.*;

public class Partida {

    private int numRondas;
    private int numHumanos;
    private int numJugadores;
    private Map<Jugador, Integer> jugadores = new HashMap<>();

    public Partida() throws NumeroJugadoresInvalidoException, IOException {
        this.validarJugadoresRegistrados();
        this.obtenerJugadores();
        this.elegirRonda();
    }

    private void validarJugadoresRegistrados() throws NumeroJugadoresInvalidoException {
        if (Juego.gestorJugadores.getJugadoresHumanos().isEmpty()) {
            throw new NumeroJugadoresInvalidoException("No hay jugadores registrados. Registra uno antes de jugar.");
        }
    }

    public void obtenerJugadores() {
        TerminalUtils.printHeader("Configuración de Partida");

        boolean jugadoresValidos = false;
        do {
            numJugadores = pedirEntero("¿Cuántos jugadores participarán? (" + Constantes.MIN_JUGADORES + "-"
                    + Constantes.MAX_JUGADORES + "): ");
            if (numJugadores < Constantes.MIN_JUGADORES || numJugadores > Constantes.MAX_JUGADORES) {
                TerminalUtils.printWarning("Por favor, introduce entre " + Constantes.MIN_JUGADORES + " y "
                        + Constantes.MAX_JUGADORES + " jugadores.");
            } else {
                jugadoresValidos = true;
            }
        } while (!jugadoresValidos);

        boolean humanosValidos = false;
        do {
            numHumanos = pedirEntero("¿Cuántos jugadores son humanos? (0-" + numJugadores + "): ");
            int registrados = Juego.gestorJugadores.getJugadoresHumanos().size();

            if (numHumanos < 0 || numHumanos > numJugadores) {
                TerminalUtils.printError("Número de humanos inválido.");
            } else if (numHumanos > registrados) {
                TerminalUtils
                        .printError("No hay suficientes jugadores registrados (Disponibles: " + registrados + ").");
            } else {
                humanosValidos = true;
            }
        } while (!humanosValidos);
        crearJugadores();
    }

    public void crearJugadores() {
        for (int i = 0; i < numHumanos; i++) {
            boolean nombreValido = false;
            do {
                System.out.print(TerminalUtils.CYAN + "Nombre del Jugador #" + (i + 1) + ": " + TerminalUtils.RESET);
                String nombre = Constantes.SCANNER.nextLine().trim();

                if (nombre.isEmpty()) {
                    TerminalUtils.printWarning("El nombre no puede estar vacío.");
                } else if (Juego.gestorJugadores.existeJugador(nombre)) {
                    if (jugadores.containsKey(new JugadorHumano(nombre))) {
                        TerminalUtils.printWarning("Este jugador ya está en la partida.");
                    } else {
                        jugadores.put(new JugadorHumano(nombre), 0);
                        nombreValido = true;
                    }
                } else {
                    TerminalUtils.printError("Jugador no registrado. Disponibles:");
                    Juego.gestorJugadores.listarJugadores();
                }
            } while (!nombreValido);
        }

        int numCPU = numJugadores - numHumanos;
        for (int i = 0; i < numCPU; i++) {
            JugadorCPU cpu = new JugadorCPU();
            jugadores.put(cpu, 0);
        }
    }

    public void jugarPartida() throws IOException {
        List<Jugador> jugadoresOrdenados = new ArrayList<>(jugadores.keySet());
        Collections.shuffle(jugadoresOrdenados);

        LoggerJuego
                .registrar("Inicio de partida: " + jugadoresOrdenados.size() + " jugadores, " + numRondas + " rondas.");

        for (int i = 0; i < numRondas; i++) {
            TerminalUtils.printHeader("Ronda " + (i + 1) + " / " + numRondas);
            List<Pregunta> preguntasRondaActual = GeneradorDePreguntas.generarPreguntaAleatoria(numJugadores);

            for (Jugador jugadorActual : jugadoresOrdenados) {
                Pregunta preguntaActual = preguntasRondaActual.remove(0);
                boolean correcto = false;

                System.out.println("\n" + TerminalUtils.PURPLE + TerminalUtils.BOLD + ">> TURNO DE: "
                        + jugadorActual.getNombre().toUpperCase() + " <<" + TerminalUtils.RESET);

                if (jugadorActual.isHuman()) {
                    preguntaActual.mostrarPregunta();
                    String respuesta = (preguntaActual instanceof PreguntaCronometro) ? ""
                            : Constantes.SCANNER.nextLine();
                    correcto = preguntaActual.validarRespuesta(respuesta);
                } else {
                    correcto = preguntaActual.respuestaCPU();
                }

                if (correcto) {
                    int puntos = preguntaActual.getPuntosOtorgados();
                    jugadores.put(jugadorActual, jugadores.get(jugadorActual) + puntos);
                    TerminalUtils.printSuccess("+ " + puntos + " puntos!");

                    jugadorActual.incrementaRacha();
                    if (jugadorActual.getRacha() >= 3) {
                        TerminalUtils.printInfo("¡RACHA ARDIENTE! +5 puntos extra.");
                        jugadores.put(jugadorActual, jugadores.get(jugadorActual) + 5);
                        jugadorActual.resetRacha();
                    }

                    if (preguntaActual instanceof PreguntaAzar) {
                        int dado = ((PreguntaAzar) preguntaActual).getNumeroDado();
                        jugadores.put(jugadorActual, jugadores.get(jugadorActual) + dado);
                    }
                } else {
                    jugadorActual.resetRacha();
                    TerminalUtils.printError("Respuesta incorrecta.");
                }
            }
        }

        TerminalUtils.printHeader("Fin de la Partida");
        Ranking.guardarRanking(jugadores);
        Historico.registrarPartida(jugadores);

        System.out.println(TerminalUtils.YELLOW + TerminalUtils.BOLD + "¡El ganador es: " + Ranking.ganador() + "!"
                + TerminalUtils.RESET);

        TerminalUtils.printInfo("Presiona ENTER para volver al menú...");
        Constantes.SCANNER.nextLine();
        LoggerJuego.registrar("Fin de la partida.");
    }

    public void elegirRonda() {
        boolean esValida = false;
        while (!esValida) {
            System.out.println("\nElija la duración de la partida:");
            System.out.println(TerminalUtils.CYAN + "   (RAPIDA) -> 3 Rondas");
            System.out.println("   (CORTA)  -> 5 Rondas");
            System.out.println("   (NORMAL) -> 10 Rondas");
            System.out.println("   (LARGA)  -> 20 Rondas" + TerminalUtils.RESET);
            System.out.print("\nOpción: ");

            String partida = Constantes.SCANNER.nextLine().toUpperCase().trim().replace("Á", "A");

            switch (partida) {
                case "RAPIDA" -> {
                    numRondas = 3;
                    esValida = true;
                }
                case "CORTA" -> {
                    numRondas = 5;
                    esValida = true;
                }
                case "NORMAL" -> {
                    numRondas = 10;
                    esValida = true;
                }
                case "LARGA" -> {
                    numRondas = 20;
                    esValida = true;
                }
                default -> TerminalUtils.printError("Opción inválida.");
            }
        }
        TerminalUtils.printInfo("Partida configurada a " + numRondas + " rondas.");
    }

    private int pedirEntero(String mensaje) {
        int numero = 0;
        boolean valido = false;
        do {
            try {
                System.out.print(TerminalUtils.BOLD + mensaje + TerminalUtils.RESET);
                numero = Constantes.SCANNER.nextInt();
                Constantes.SCANNER.nextLine();
                valido = true;
            } catch (InputMismatchException e) {
                TerminalUtils.printError("Debes introducir un número entero.");
                Constantes.SCANNER.nextLine();
            }
        } while (!valido);
        return numero;
    }
}
