package juego.questions;

import java.util.Random;
import java.util.Scanner;

public class PreguntaCronometro extends Pregunta {

    private final int tiempoObjetivo;
    private final double margenError = 0.5;
    private double diferencia;
    private static final Scanner scanner = new Scanner(System.in);

    public PreguntaCronometro() {
        this(false);
    }

    // Constructor general
    public PreguntaCronometro(boolean esCPU) {
        super(1);
        this.tiempoObjetivo = new Random().nextInt(1, 6); // 1 a 5 segundos
    }

    @Override
    public void mostrarPregunta() {
        System.out.println("-> Pregunta de Cronómetro Mental <-");
        System.out.println("Objetivo: Contar " + tiempoObjetivo + " segundos exactos.");
        System.out.println("Presiona ENTER para comenzar...");
        scanner.nextLine(); // Consume el ENTER inicial

        long inicio = System.currentTimeMillis();
        System.out.println("¡Contando! Presiona ENTER cuando termines...");
        scanner.nextLine(); // Consume el ENTER final
        long fin = System.currentTimeMillis();
        diferencia = Math.abs((fin - inicio) / 1000.0 - tiempoObjetivo);

        System.out.printf("[Tiempo esperado]: %.1f s\n", (double) tiempoObjetivo);
        System.out.printf("[Tiempo medido]: %.2f s\n", (fin - inicio) / 1000.0);

    }

    @Override
    public boolean validarRespuesta(String respuesta) {
        if (diferencia <= margenError) {
            System.out.println("✅ ¡Correcto! Has acertado dentro del margen permitido.");
            return true;
        } else {
            System.out.println("❌ Has fallado. Te pasaste o te quedaste corto.");
            return false;
        }
    }

    @Override
    public boolean respuestaCPU() {
        System.out.println("La CPU intenta medir el tiempo... ¡Pero siempre falla! ❌");
        return false;
    }

}
