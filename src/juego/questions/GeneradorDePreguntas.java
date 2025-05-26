package juego.questions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorDePreguntas {

    public static List<Pregunta> generarPreguntaAleatoria(int numJugadores) throws IOException {
        Random random = new Random();
        List<Pregunta> lista = new ArrayList<>();
        int tipo = random.nextInt(5);
        for (int i = 0; i < numJugadores; i++) {
            Pregunta pregunta;
            switch (tipo) {
                case 0 -> pregunta = new PreguntaGeografia();
                case 1 -> pregunta = new PreguntaCronometro();
                case 2 -> pregunta = new PreguntaMastermind();
                case 3 -> pregunta = new PreguntaMates();
                case 4 -> pregunta = new PreguntaAzar();
                default -> throw new IllegalStateException("Tipo de pregunta inválido");
            }
            lista.add(pregunta);
        }
        return lista;
    }
}

