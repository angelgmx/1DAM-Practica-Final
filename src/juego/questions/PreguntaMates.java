package juego.questions;


import juego.config.Configuracion;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * ExpressionBuilder: Clase de la librería exp4j para construir expresiones.
 *
 * this.expresion: El String con la operación generada (ej: "10 * 2 + 5").
 *
 * .build(): Metodo que finaliza la construcción de la expresión.
 */

import java.util.Random;

public class PreguntaMates extends Pregunta{

    private String expresion;// Almacena la cadena con la expresión matemática
    private String respuestaCorrecta;
    private final Random random = new Random();

    public PreguntaMates() {
        super(1);
       generarRespuestaCorrecta();
    }

    private void generarRespuestaCorrecta(){
        boolean generado;
        generarNuevaExpresion();
        do {
            try {
                // Crear una expresión matemática a partir del String

                Expression exp = new ExpressionBuilder(this.expresion).build();

                // Calculamos el resultado y lo convertimos a entero
                this.respuestaCorrecta = String.valueOf((int)exp.evaluate());
                generado = true;
            } catch (Exception e) {
                generarNuevaExpresion(); // Si hay error que genere otra nueva expresión
                generado = false;
            }
        }while (!generado);
    }
    private void generarNuevaExpresion() {
        // Creamos unos operadores
        char[] operadores = {'+', '-', '*'};
        // La cantidad de números que tiene nuesta expresión está entre 4 y 8
        int cantidadNumero = random.nextInt(4,9);
        String nuevaExpresion = "";

        // Crear cada elemento de la expresión
        for (int cont = 0; cont <= cantidadNumero; cont++) {
            int numero = random.nextInt(2,13);  // Generar un número entre 2 y 12
            nuevaExpresion += numero; // Añadimos el número a la expresión
            if (cont < cantidadNumero) { // La posición 0 cuenta
                // Operador aleatorio
                char operador = operadores[random.nextInt(operadores.length)];
                nuevaExpresion += " " + operador + " "; // añadimos ahora un operador a la expresión
            }
        }
        this.expresion = nuevaExpresion;
    }

    @Override
    public void mostrarPregunta() {
        System.out.println("Resuelve la expresión>  " + this.expresion);
        System.out.print("Tu respuesta: ");

        if (modoDepuracion()) {
            System.out.println(" ⚠️[DEBUG] Respuesta correcta: " + respuestaCorrecta);
        }

    }

    @Override
    public boolean validarRespuesta(String respuesta) {

        try {

            int respuestaUsuario = Integer.parseInt(respuesta);
            int respuestaCorrectaInt = Integer.parseInt(this.respuestaCorrecta);

            if (respuestaUsuario == respuestaCorrectaInt) {
                System.out.println("-✅[número correcto]");
                return true;
            } else {
                System.out.println("-❌[número incorrecto]");
                System.out.println("-> La respuesta correcta era " + this.respuestaCorrecta);
                return false;
            }

        } catch (NumberFormatException e) {
            System.err.println("⚠️ Respuesta inválida. Por favor, introduce un número.");
            return false;
        }

    }



    @Override
    public boolean respuestaCPU() {
        mostrarPregunta();
        String respuestaCPU = this.respuestaCorrecta;
        System.out.println("calculando...");
        System.out.println("La respuesta de la CPU es: " + respuestaCPU); // La cpu siempre acierta
        System.out.println("La CPU acierta!!");
        return true;
    }


    @Override
    public boolean modoDepuracion() {
        return Configuracion.estaDepuracionActiva();
    }

}
