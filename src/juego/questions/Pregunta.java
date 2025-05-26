package juego.questions;

import java.util.Scanner;

/**
 * Clase abstacta base que representa una pregunta genérica en el sistema
 * Define la estructura que debe implementar toos los tipos específicos de preguntas de jugo
 */

public abstract class Pregunta {


    // Atributos protegidos para herencia
    protected String respuestaCorrecta;
    private int puntosOtorgados;

    public Pregunta(int puntos) {
        puntosOtorgados = puntos;
    }

    public abstract boolean validarRespuesta(String respuesta);

    public abstract boolean respuestaCPU();


    // modo depuración
    public boolean modoDepuracion() {
        return false;
    }


    // mostrar la pregunta
    public abstract void mostrarPregunta();

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public int getPuntosOtorgados() {
        return puntosOtorgados;
    }





}
