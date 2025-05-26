package juego.model;

import juego.config.Constantes;
import juego.exceptions.NumeroJugadoresInvalidoException;
import juego.main.Juego;
import juego.questions.GeneradorDePreguntas;
import juego.questions.Pregunta;
import juego.questions.PreguntaCronometro;
import juego.questions.PreguntaAzar;
import juego.service.Historico;
import juego.service.LoggerJuego;
import juego.service.Ranking;
import java.io.IOException;
import java.util.*;
public class Partida {

    /** Número de rondas de la partida */
    private int numRondas;
    /** Número de jugadores humanos */
    private int numHumanos;
    /** Número total de jugadores */
    private int numJugadores;

    /** Mapa que almacena los jugadores y sus puntos */
    private Map<Jugador, Integer> jugadores = new HashMap<>();

    /**
     * Constructor de la clase Partida.
     * Valida los jugadores registrados, obtiene los jugadores y elige el número de rondas.
     * @throws NumeroJugadoresInvalidoException si no hay jugadores humanos registrados
     * @throws IOException si ocurre un error de entrada/salida
     */
    public Partida() throws NumeroJugadoresInvalidoException, IOException {
        this.validarJugadoresRegistrados();
        this.obtenerJugadores();
        this.elegirRonda();
    }

    /**
     * Valida que haya jugadores humanos registrados en el sistema.
     * @throws NumeroJugadoresInvalidoException si no hay jugadores humanos registrados
     */
    private void validarJugadoresRegistrados() throws NumeroJugadoresInvalidoException {
        if (Juego.gestorJugadores.getJugadoresHumanos().isEmpty()) {
            throw new NumeroJugadoresInvalidoException("[⚠️No hay jugadores registrados] -> Registra un jugador previamente");
        }
    }

    /**
     * Solicita al usuario el número de jugadores y humanos que participarán en la partida.
     * Valida que los datos sean correctos.
     */
    public void obtenerJugadores() {
        boolean jugadoresValidos = false;
        do {
            numJugadores = pedirEntero("¿Cuántos jugadores van a jugar? (" + Constantes.MIN_JUGADORES + "-" + Constantes.MAX_JUGADORES + "): ");
            if (numJugadores < Constantes.MIN_JUGADORES || numJugadores > Constantes.MAX_JUGADORES) {
                System.out.println("Introduce de " + Constantes.MIN_JUGADORES + " a " + Constantes.MAX_JUGADORES + " Jugadores");
            } else {
                jugadoresValidos = true;
            }
        } while (!jugadoresValidos);

        boolean humanosValidos = false;
        do {
            numHumanos = pedirEntero("¿Cuántos son humanos? (0-" + numJugadores + "): ");
            int registrados = Juego.gestorJugadores.getJugadoresHumanos().size();

            if (numHumanos < 0 || numHumanos > numJugadores) {
                System.out.println("❌Numero Inválido de Jugadores");
            } else if (numHumanos > registrados) {
                System.out.println("❌No hay suficientes jugadores");
            } else {
                humanosValidos = true;
            }
        } while (!humanosValidos);
        crearJugadores();
    }

    /**
     * Crea y añade los jugadores humanos y CPUs a la partida.
     * Solicita los nombres de los jugadores humanos y valida que existan y no estén repetidos.
     */
    public void crearJugadores() {
        // Crear jugadores Humanos
        for (int i = 0; i < numHumanos; i++) {
            boolean nombreValido = false;
            do {
                System.out.print("Nombre del jugador humano numero#" + (i + 1) + ": ");
                String nombre = Constantes.SCANNER.nextLine().trim();

                if (nombre.isEmpty()) {
                    System.out.println("El nombre no puede estar vacío");
                } else if (Juego.gestorJugadores.existeJugador(nombre)) {
                    if (jugadores.containsKey(new JugadorHumano(nombre))) {
                        System.out.println("Este jugador ya está en la partida");
                    } else {
                        jugadores.put(new JugadorHumano(nombre), 0);
                        nombreValido = true;
                    }
                } else {
                    System.out.println("[⚠️ Juador no registrado] -> Jugadores disponibles...");
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

    /**
     * Ejecuta el desarrollo de la partida, gestionando las rondas y turnos de los jugadores.
     * Registra los resultados y muestra el ranking final.
     * @throws IOException si ocurre un error de entrada/salida
     */
    public void jugarPartida() throws IOException {
        //1.  Ordenamos los jugadores extraidos del mapa
        List<Jugador> jugadoresOrdenados = new ArrayList<>(jugadores.keySet());
        Collections.shuffle(jugadoresOrdenados);

        LoggerJuego.registrar("Inicio de la partida con " + jugadoresOrdenados.size() + " jugadores y " + numRondas + " rondas");

        //2.  Controlamos el número de rondas del juego
        for (int i = 0; i < numRondas; i++) {
            System.out.println("\n=== RONDA " + (i + 1) + " ===");
            List<Pregunta> preguntasRondaActual = GeneradorDePreguntas.generarPreguntaAleatoria(numJugadores);

            //4 Itera sobre cada jugador en el orden aleatorio establecido anteriormente (Hacemos las preguntas)
            for (int j = 0; j < jugadoresOrdenados.size(); j++) {
                Jugador jugadorActual = jugadoresOrdenados.get(j);
                Pregunta preguntaActual = preguntasRondaActual.removeFirst();
                boolean correcto = false;

                System.out.println("\n-> Turno de " + "[" + jugadorActual.getNombre() + "]\n");
                if (jugadorActual.isHuman()) {
                    String respuesta = "";
                    preguntaActual.mostrarPregunta();

                    if (preguntaActual instanceof PreguntaCronometro) {
                        respuesta = "";
                    } else {
                        respuesta = Constantes.SCANNER.nextLine();
                    }

                    correcto = preguntaActual.validarRespuesta(respuesta);
                } else {
                    correcto = preguntaActual.respuestaCPU();
                }

                if (correcto) {
                    jugadores.put(jugadorActual, jugadores.get(jugadorActual) + preguntaActual.getPuntosOtorgados());
                    System.out.println("[✅+ " + preguntaActual.getPuntosOtorgados() + " Puntos]");

                    // Se le incrementa la racha al jugador actual si acierta
                    jugadorActual.incrementaRacha();
                    // Si el jugador actual tiene una racha de 3 se le suman +3 puntos

                    if (jugadorActual.getRacha() >= 3){
                        System.out.println("Has realizado 3 respuestas correctas consecutivas, ¡+5 Puntos extra!");
                        jugadores.put(jugadorActual, jugadores.get(jugadorActual) + 5);
                        // se le resetea la racha
                        jugadorActual.resetRacha();
                    }
                    // Si la pregunta actual es de pregunta dados se le suma al jugdor el número adivinado
                    if (preguntaActual instanceof PreguntaAzar) {
                        jugadores.put(jugadorActual, jugadores.get(jugadorActual) + ((PreguntaAzar) preguntaActual).getNumeroDado());
                    }
                } else {
                    // Si falla se le resetea la racha
                    jugadorActual.resetRacha();
                    System.out.println("[No sumas Puntos]");
                }
            }
            System.out.println("--- Fin de la ronda " + (i + 1) + " ---\n");
        }

        System.out.println("--FIN DE LA PARTIDA--");

        Ranking.guardarRanking(jugadores);
        Historico.registrarPartida(jugadores);
        Ranking.mostrarRanking();

        String ganador = Ranking.ganador();
        System.out.println("El ganador es: " + ganador);
        LoggerJuego.registrar("Fin de la partida con " + jugadores.size() + " jugadores "+ " y " +  numRondas + "rondas");
    }

    /**
     * Permite al usuario elegir el tipo de partida y asigna el número de rondas correspondiente.
     */
    public void elegirRonda() {
        boolean esValida = false;

        while (!esValida) {
            String partida = " ";
            System.out.println("¿Qué tipo de partida quieres jugar?\n -> (Rápida) (Corta) (Normal) (Larga) <- ");
            partida = Constantes.SCANNER.nextLine().toUpperCase().trim().replace("Á", "A");

            if (partida.isEmpty()) {
                System.out.println("¡Error! Entrada vacía");
                continue;
            }

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
                default -> {
                    System.out.println("\nOpción inválida! Intenta nuevamente");
                }
            }
        }
        System.out.println("[ ⚠️La partida elegida tiene " + "- " + numRondas + " Rondas] \n");
    }

    /**
     * Solicita al usuario un número entero mostrando un mensaje.
     * Repite hasta que la entrada sea válida.
     * @param mensaje Mensaje a mostrar al usuario
     * @return Número entero introducido por el usuario
     */
    private int pedirEntero(String mensaje) {
        int numero = 0;
        boolean valido = false;
        do {
            try {
                System.out.print(mensaje);
                numero = Constantes.SCANNER.nextInt();
                Constantes.SCANNER.nextLine(); // Limpiar el buffer
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("¡Error! Debes introducir un número entero.");
                Constantes.SCANNER.nextLine(); // Limpiar el buffer
            }
        } while (!valido);
        return numero;
    }

    /**
     * Muestra por pantalla los jugadores añadidos a la partida.
     */
    private void mostrarJugadoresPartida() {
        System.out.println("\nJugadores añadidos a la partida:");
        for (Jugador j : jugadores.keySet()) {
            System.out.println("- " + j.getNombre());
        }
    }
}



