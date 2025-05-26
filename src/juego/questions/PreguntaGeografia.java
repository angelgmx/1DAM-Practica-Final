package juego.questions;

import juego.config.Configuracion;
import juego.config.Constantes;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class PreguntaGeografia extends Pregunta {
    // Mapa estático compartido por todas las instancias para evitar múltiples cargas de ciudades
    // Si se desea que cada pregunta tenga su propio conjunto de ciudades, se debe hacer no-estático
    private final Map<String,List<Double>> ciudades = new HashMap<>();

    private Random random = new Random();
    private final String respuestaCorrecta;
    private final List<String> listaCiudades;


    private String comodinUsado = "";
    private Set<String> opcionesEliminadas = new HashSet<>();

    public PreguntaGeografia() throws IOException {
        super(1);
        if (ciudades.isEmpty()) {
            ImportarCiudades(); // Lanza IOException si hay error
        }
        listaCiudades = generarCiudades();
        respuestaCorrecta = ciudadGanadora();
    }

    // main pa probar

    /*
      public static void main(String[] args) throws IOException {
              PreguntaGeografia pregunta = new PreguntaGeografia();
             pregunta.mostrarPregunta();
              Scanner scanner = new Scanner(System.in);
              pregunta.validarRespuesta(scanner.nextLine());
         }

    */
    private void ImportarCiudades(){
        try {
            List<String> ciudadesFichero = Files.readAllLines(Constantes.RUTACIUDADES_PATH);
            for (String linea : ciudadesFichero){
                String[] datos = linea.split(",");
                if (datos.length == 3){
                    List<Double> coords = new ArrayList<>();
                    double lat = Double.parseDouble(datos[1]);
                    double lon = Double.parseDouble(datos[2]);
                    coords.add(lat);
                    coords.add(lon);

                    ciudades.put(datos[0],coords);
                }else {
                    System.err.println("Saltando línea por mala construcción");
                }
            }

        } catch (IOException e) {
            System.out.println("error al leer el archivo de ciudades: " + e);
        }
    }

    /**
     *
     * @return
     */
    //  Generar 5 ciudades Aleatorias
    private List<String> generarCiudades() {
        List<String> nomCiud = new ArrayList<>(ciudades.keySet());
        List<String> ciudadesGeneradas =  new ArrayList<>();
        do {
            int indice = random.nextInt(nomCiud.size());
            String ciudadActual = nomCiud.get(indice);
            if (!ciudadesGeneradas.contains(ciudadActual)){
                ciudadesGeneradas.add(ciudadActual);
            }
        } while (ciudadesGeneradas.size() < 5);
        return ciudadesGeneradas;
    }


     // Calcula las distancias en kilómetros entre la ciudad objetivo (primera de la lista)
     // y todas las ciudades disponibles en el mapa, utilizando la fórmula de Haversine.
    private Map<String, Double> calcularDistancias() {

        // Ciudad objetivo
        String ciudadObjetivo = listaCiudades.getFirst(); // devuelve el primer elemento
        List<Double> coordCiudadObjetivo = ciudades.get(ciudadObjetivo);
        Map<String, Double> ciudadDistancia = new HashMap<>();

        // Solo calcular la distancia contra las otras 4 ciudades generadas
        for (int i = 1; i < listaCiudades.size(); i++) {
            String ciudadComparada = listaCiudades.get(i);
            List<Double> coordComparada = ciudades.get(ciudadComparada);

            double distancia = calcularDistanciaHaversine(
                    coordCiudadObjetivo.get(0), coordCiudadObjetivo.get(1),
                    coordComparada.get(0), coordComparada.get(1)
            );

            ciudadDistancia.put(ciudadComparada, distancia);
        }

        return ciudadDistancia;
    }

    /**
     *
     * @return
     */
    private String ciudadGanadora() {
        Map<String,Double> ciudadesDistancias = calcularDistancias();
        String ciudadMinima = "";
        double distanciaMasCorta = Double.MAX_VALUE;

        for (Map.Entry<String,Double> entrada : ciudadesDistancias.entrySet()) {
            double valor = entrada.getValue(); // extraemos el valor actual del mapa
            if (valor < distanciaMasCorta) {
                distanciaMasCorta = valor; // guardamos siempre el valor más pequeño
                ciudadMinima = entrada.getKey(); // guardamos el nombre de la ciudad más pequeña
            }
        }

        return ciudadMinima;
    }

    public void mostrarRespuesta() {

        Map<String,Double> ciudadDistancias = calcularDistancias();

        int cont = 0;
       for (Map.Entry<String,Double> entrada : ciudadDistancias.entrySet()) {
           String letra = "";
           switch (cont){
               case 0 -> letra = "A";
               case 1 -> letra = "B";
               case 2 -> letra = "C";
               case 3 -> letra = "D";
           }

           // Formatear a dos decimales
           String distancia = String.format("%.2f", entrada.getValue());

           System.out.println(letra + ") " + entrada.getKey() + " -> " + distancia);
           cont++;
       }
        System.out.println("❌ Respuesta correcta: " + this.respuestaCorrecta);
    }

    /**
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int RADIO_TIERRA_KM = 6371; // Radio de la Tierra en kilómetros

        // Convertir grados a radianes
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Diferencias
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;


        // Fórmula de Haversine implementado con IA
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_KM * c;
    }

    /**
     *
     * @param respuesta
     * @return true si la respuesta es correcta, false en caso contrario.
     */
    @Override
    public boolean validarRespuesta(String respuesta) {
        boolean esValido;

        boolean esCorrecto = false;
        do {
            esValido = true;
            String opcion = respuesta.trim().toUpperCase();
            int indiceCiudad = -1;

            switch (opcion) {
                case "A" -> indiceCiudad = 1;
                case "B" -> indiceCiudad = 2;
                case "C" -> indiceCiudad = 3;
                case "D" -> indiceCiudad = 4;
                default -> {
                    System.out.println("❌ Opción inválida. Por favor elige A, B, C o D.");
                    esValido = false;
                }
            }
            if (esValido) {
                // Obtener ciudad seleccionada
                String ciudadElegida = listaCiudades.get(indiceCiudad);
                String ciudadCorrecta = ciudadGanadora();

                esCorrecto = ciudadElegida.equalsIgnoreCase(ciudadCorrecta);

                if (esCorrecto) {
                    System.out.println("✅[Correcto]");
                } else {
                    mostrarRespuesta();
                }
            } else {
                this.mostrarPregunta();
                System.out.print("\nElige una opción válida POR FAVOR\n> ");
                respuesta = Constantes.SCANNER.nextLine();
            }
        } while (!esValido);
        return esCorrecto;
    }
    // metodo para mostrar la pregunta
    @Override
    public void mostrarPregunta() {

        System.out.println("-> Pregunta Geografía: Elige la ciudad más cercana a la ciudad Objetivo!");

        List<String> ciudades = listaCiudades;

        System.out.println("Ciudad Objetivo: " + ciudades.getFirst());
        for (int i = 1; i < ciudades.size(); i++) {
            String letra = "";
            switch (i){
                case 1 -> letra = "A";
                case 2 -> letra = "B";
                case 3 -> letra = "C";
                case 4 -> letra = "D";
            }
            System.out.println(letra + ") " + ciudades.get(i));
        }
        if (modoDepuracion()) {
            System.out.println(" ⚠️[DEBUG] Respuesta correcta: " + respuestaCorrecta);
        }
        System.out.print("¿Cual está mas cerca? (A,B,C o D): ");


    }

    @Override
    public boolean respuestaCPU() {
        System.out.println("-> Pregunta Geografía: Elige la ciudad más cercana a la ciudad Objetivo!");
        int respuesta = random.nextInt(4);
        String letra = "";
        switch (respuesta){
            case 0 -> letra = "A";
            case 1 -> letra = "B";
            case 2 -> letra = "C";
            case 3 -> letra = "D";
        }
        return validarRespuesta(letra);
    }

    @Override
    public boolean modoDepuracion() {
        return Configuracion.estaDepuracionActiva();
    }
}
