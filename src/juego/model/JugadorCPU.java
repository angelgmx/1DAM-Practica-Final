package juego.model;

import juego.questions.Pregunta;

public class JugadorCPU extends Jugador {

    private static int numCPUS = 0;
    private int puntosCPU;

    public JugadorCPU() {
        this.puntosCPU = 0;
        numCPUS++;
        this.nombre = generarNombre(); // this.nombre viene de Jugador
    }

    private String generarNombre() {
        return "CPU" + numCPUS;
    }

    @Override
    public String responder(Pregunta pregunta) {
        // Implementa la lógica de respuesta según tipo de pregunta
        return "";
    }
}

