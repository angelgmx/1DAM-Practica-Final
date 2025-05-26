package juego.questions;
import juego.config.Configuracion;
import juego.config.Constantes;

import java.util.Random;


public class PreguntaMastermind extends Pregunta{
    String numeroSecreto;
    private final Random random = new Random();
    private int intentos = 3;

    public PreguntaMastermind() {
        super(1);
        this.respuestaCorrecta = generarNumeroSecreto();
    }

    private String generarNumeroSecreto() {
        String numeroSecreto = "";
        while (numeroSecreto.length() < 3) {
            int digito = random.nextInt(1, 10);
            String numero = String.valueOf(digito);
            if (!numeroSecreto.contains(numero)) {
                numeroSecreto += numero;
            }
        }
        return numeroSecreto;
    }

    @Override
    public boolean validarRespuesta(String respuesta) {

        boolean correcto = false;
        int intentosRestantes = 3;

        while (intentosRestantes > 0) {
            String respuestaJugador = respuesta;

            if ( respuestaJugador.length() != 3 || !respuestaJugador.matches("\\d{3}")){
                System.out.println("Entrada inválida. Debes introducir un número de 3 dígitos");
                System.out.println("Intentalo de nuevo!");
                respuestaJugador = Constantes.SCANNER.nextLine();
                continue;
            }

            int bienUbicadas = 0;
            int malUbicadas = 0;

            for (int i = 0; i < respuesta.length(); i++) {
                for (int j = 0; j < this.respuestaCorrecta.length(); j++) {
                    // Comparar en la misma posición el número secreto con la del jugador
                    if (i == j && respuesta.charAt(i) == this.respuestaCorrecta.charAt(j)) {
                        bienUbicadas++;
                        break;
                        // Si el dígito coincide pero no está en la misma posición, se considera mal ubicado.
                    } else if (respuesta.charAt(i) == this.respuestaCorrecta.charAt(j)) {
                        malUbicadas++;
                        break;
                    }
                }
            }

            System.out.println("-> Bien ubicadas: " + bienUbicadas);
            System.out.println("-> Mal ubicadas: " + malUbicadas);

            if (bienUbicadas == 3) {
                System.out.println("¡Has adivinado el número!");
                correcto = true;
                break;
            } else {
                intentosRestantes--;
                if (intentosRestantes > 0) {
                    System.out.println("-> Tienes " + intentosRestantes + "intentos disponibles");
                    respuesta = Constantes.SCANNER.nextLine().trim();
                }
            }
        }

        if (!correcto) {
            System.out.println("❌ Se acabaron los intentos. La respuesta correcta era: " + this.respuestaCorrecta);
        }
        return correcto;
    }


    @Override
    public boolean respuestaCPU() {
        mostrarPregunta();
      int  intentosCPU = 3;


      while (intentosCPU > 0) {
       String numeroCPU = "";
        while (numeroCPU.length() < 3) {
            int digito = random.nextInt(1,10);
            String numero = Integer.toString(digito);

            if (!numeroCPU.contains(numero)) {
               numeroCPU += numero;
            }
        }
          System.out.println("CPU prueba con: " + numeroCPU);
          if (numeroCPU == this.respuestaCorrecta) {
              return true;
          }
          intentosCPU--;
      }
      return false;
    }



    @Override
    public void mostrarPregunta() {
        if (this.intentos == 3) System.out.println("-> Pregunta Mastermind <-");
        System.out.println("Adivina el número secreto de 3 cifras (dígitos únicos). Te quedan " + this.intentos+ " intentos.");
        if (modoDepuracion()) {
            System.out.println(" ⚠️[DEBUG] Respuesta correcta: " + respuestaCorrecta);
        }
    }

    public int getIntentos() {
        return intentos;
    }

    @Override
    public boolean modoDepuracion() {
        return Configuracion.estaDepuracionActiva();
    }

}
