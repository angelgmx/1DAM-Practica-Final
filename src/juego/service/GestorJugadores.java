package juego.service;

import juego.model.JugadorHumano;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GestorJugadores {
    private static final Scanner scanner = new Scanner(System.in);
    private Set<JugadorHumano> jugadoresHumanos;

    public GestorJugadores() {
        this.jugadoresHumanos = new HashSet<>();
    }

    public void menuJugadores() {

        int opcion;
        do {

            System.out.println("== SUBMENU JUGADORES ==");
            System.out.println("1. Ver jugadores registrados");
            System.out.println("2. Añadir jugador");
            System.out.println("3. Eliminar Jugador");
            System.out.println("4. Volver");
            System.out.print("Elige una opcion -> ");

           opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer


            switch (opcion) {
                case 1:
                    this.listarJugadores();
                    break;
                case 2:
                    anodirJugador();
                    break;
                case 3:
                   eliminarJugador();
                   break;
                case 4:
                    System.out.println("Hasta pronto!");

            }
        } while (opcion != 4);

    }

    public void anodirJugador() {
        Scanner scanner = new Scanner(System.in);
        boolean nombreValido;
        String nombre = "";

        do {
            nombreValido = true;

            System.out.print("-> Ingrese el nombre del Jugador: ");
            nombre = scanner.nextLine().trim();

            // Validación 1: Nombre no vacío
            if (nombre.isEmpty()) {
                System.out.println("¡Error! El nombre no puede estar vacío");
                nombreValido = false;
            }

            // Validación 2: Sin números
            if (nombre.matches(".*\\d.*")) {
                System.out.println("¡Error! El nombre no puede contener números");
                nombreValido = false;
            }

            // Validación 3: Nombre único
            if (existeJugador(nombre)) {
                System.out.println("[" + nombre + "]" + " ya existe");
               nombreValido = false;
            }



        } while (!nombreValido);

        jugadoresHumanos.add(new JugadorHumano(nombre));
        System.out.println("\n[✅Jugador " + nombre + " añadido con éxito]\n");
        LoggerJuego.registrar("jugador " + nombre + "añadido con éxito");

    }


    public void eliminarJugador() {
        System.out.print("-> Ingrese el nombre del jugador a eliminar: ");
        String nombre = scanner.nextLine();

        if (existeJugador(nombre)) {
            eliminarJugador(nombre);
            System.out.println("\n[❌Jugador " + nombre + " eliminado]\n");
            LoggerJuego.registrar("Jugador " + nombre + "eliminado");
        } else {
            System.out.println("⚠️[Jugador no encontrado]\n");
        }

    }


    // metodo que busca al el nombre el jugador para ver si existe
    public boolean existeJugador(String nombre) {
        String nombreBuscado = nombre.trim().toLowerCase();

        for (JugadorHumano j : this.jugadoresHumanos) {
            if (j.getNombre().equalsIgnoreCase(nombre.trim())) {
                return true;
            }
        }
        return false;
    }

    public void eliminarJugador(String nombre) {
        for (JugadorHumano j : jugadoresHumanos) {
            if (j.getNombre().equalsIgnoreCase(nombre.trim())) {
                jugadoresHumanos.remove(j);
            }
        }
    }

    public void listarJugadores() {
        // si la lista está vacía
        if (jugadoresHumanos.isEmpty()) {
            System.out.println("\n[No hay jugadores registrados]");
        } else {
            for (JugadorHumano j : jugadoresHumanos) {
                System.out.println("- nombre: " + "[" + j.getNombre() +  " ]" + " - " + j.getPuntuacion() + " Puntos");
            }
        }
    }

    private void  importarJugadoresRanking() {

    }

    public Set<JugadorHumano> getJugadoresHumanos() {
        return jugadoresHumanos;
    }

    public void setJugadoresHumanos(Set<JugadorHumano> jugadoresHumanos) {
        this.jugadoresHumanos = jugadoresHumanos;
    }

}
