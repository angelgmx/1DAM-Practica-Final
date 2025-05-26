package juego.model;

import juego.questions.Pregunta;

public abstract class Jugador {

    protected String nombre;
    private int puntuacion;
    private boolean isHuman;
    private int racha;

    public Jugador(){
        this.isHuman = false;
    }

    public Jugador(String nombre,boolean isHuman) {
        this.nombre = nombre;
        this.isHuman = isHuman;
    }

    public void incrementaRacha() {
        this.racha++;
    }

    public void resetRacha() {
        this.racha = 0;
    }


    /**
     * Obtiene la respuesta del jugador a una pregunta
     * @param pregunta Pregunta a responder
     * @return Respuesta proporcionada
     */
    public abstract String responder(Pregunta pregunta);

    // incrementa la puntuación del jugador
    public void incrementaPuntos(int puntos){
        this.puntuacion += puntos;
    }


    // getters
    public String getNombre() {
        return nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", puntuacion=" + puntuacion +
                '}';
    }

    public boolean isHuman() {
        return isHuman;
    }

    public int getRacha() {
        return racha;
    }

    public void setRacha(int racha) {
        this.racha = racha;
    }
}
