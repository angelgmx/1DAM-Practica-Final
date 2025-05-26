package juego.questions;

import juego.config.Configuracion;

public class PreguntaAzar extends Pregunta {

    private int numeroDado;
    private int  respuestaUsuario;

    public PreguntaAzar() {
        super(1);
        this.numeroDado = (int) (Math.random() * 10) + 1;
    }

    @Override
    public boolean validarRespuesta(String respuesta) {
        try {
            this.respuestaUsuario = Integer.parseInt(respuesta.trim());
            return respuestaUsuario == numeroDado;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean respuestaCPU() {
        // Simula que la CPU tiene un 10% de acertar
        int intentoCPU = (int) (Math.random() * 10) + 1;
        return intentoCPU == numeroDado;
    }

    @Override
    public void mostrarPregunta() {
        System.out.println("Adivina el número aleatorio de 1 a 10: ");
        if (modoDepuracion()) {
            System.out.println(" ⚠️[DEBUG] Respuesta correcta: " + numeroDado);
        }
    }

    @Override
    public boolean modoDepuracion() {
        return Configuracion.estaDepuracionActiva();
    }

    public int getNumeroDado() {
        return numeroDado;
    }

    public void setNumeroDado(int numeroDado) {
        this.numeroDado = numeroDado;
    }

    public int getRespuestaUsuario() {
        return respuestaUsuario;
    }

    public void setRespuestaUsuario(int respuestaUsuario) {
        this.respuestaUsuario = respuestaUsuario;
    }
}
