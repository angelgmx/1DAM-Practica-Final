package juego.questions;

import juego.config.Configuracion;
import java.util.Random;

public class PreguntaMates extends Pregunta {

    private String expresion;
    private String respuestaCorrecta;
    private final Random random = new Random();

    public PreguntaMates() {
        super(1);
        generarRespuestaCorrecta();
    }

    private void generarRespuestaCorrecta() {
        generarNuevaExpresion();
        try {
            this.respuestaCorrecta = String.valueOf(eval(this.expresion));
        } catch (Exception e) {
            generarRespuestaCorrecta(); // Retry if error
        }
    }

    private int eval(String expression) {
        return (int) new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+'))
                        x += parseTerm();
                    else if (eat('-'))
                        x -= parseTerm();
                    else
                        return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*'))
                        x *= parseFactor();
                    else
                        return x;
                }
            }

            double parseFactor() {
                int startPos = this.pos;
                if ((ch >= '0' && ch <= '9')) {
                    while ((ch >= '0' && ch <= '9'))
                        nextChar();
                    return Double.parseDouble(expression.substring(startPos, this.pos));
                }
                return 0;
            }
        }.parse();
    }

    private void generarNuevaExpresion() {
        char[] operadores = { '+', '-', '*' };
        int cantidadNumero = random.nextInt(2, 4); // Limited for simplicity but expandable
        StringBuilder nuevaExpresion = new StringBuilder();

        for (int cont = 0; cont <= cantidadNumero; cont++) {
            int numero = random.nextInt(2, 13);
            nuevaExpresion.append(numero);
            if (cont < cantidadNumero) {
                char operador = operadores[random.nextInt(operadores.length)];
                nuevaExpresion.append(" ").append(operador).append(" ");
            }
        }
        this.expresion = nuevaExpresion.toString();
    }

    @Override
    public void mostrarPregunta() {
        System.out.println("\n" + "🔢 OPERACIÓN MATEMÁTICA:");
        System.out.println("   " + this.expresion + " = ?");
        System.out.print("\nRespuesta: ");

        if (Configuracion.estaDepuracionActiva()) {
            System.out.println("\n[DEBUG] Solución: " + respuestaCorrecta);
        }
    }

    @Override
    public boolean validarRespuesta(String respuesta) {
        try {
            int respuestaUsuario = Integer.parseInt(respuesta.trim());
            int respuestaCorrectaInt = Integer.parseInt(this.respuestaCorrecta);

            if (respuestaUsuario == respuestaCorrectaInt) {
                return true;
            } else {
                System.out.println("❌ Incorrecto. La solución era: " + this.respuestaCorrecta);
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("⚠️ ERROR: Introduce un número entero.");
            return false;
        }
    }

    @Override
    public boolean respuestaCPU() {
        System.out.println("La CPU está calculando...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
        System.out.println("CPU responde: " + this.respuestaCorrecta);
        return true;
    }

    @Override
    public boolean modoDepuracion() {
        return Configuracion.estaDepuracionActiva();
    }
}
