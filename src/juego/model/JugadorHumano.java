package juego.model;

import juego.questions.Pregunta;

import java.nio.file.DirectoryNotEmptyException;
import java.util.*;

public class JugadorHumano extends Jugador {


    // Constructor por defecto que inicializa el jugador como humano
    public JugadorHumano(String nombre){
        super(nombre,true);
    }


    @Override
    public String responder(Pregunta pregunta) {
        System.out.println("Pregunta para " + getNombre());
        return "";
    }

}
