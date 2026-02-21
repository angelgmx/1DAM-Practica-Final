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
            TerminalUtils.printHeader("Gestión de Jugadores");
            System.out.println(TerminalUtils.YELLOW + "1." + TerminalUtils.RESET + " 📋 Ver registrados");
            System.out.println(TerminalUtils.YELLOW + "2." + TerminalUtils.RESET + " ➕ Añadir jugador");
            System.out.println(TerminalUtils.YELLOW + "3." + TerminalUtils.RESET + " ➖ Eliminar jugador");
            System.out.println(TerminalUtils.YELLOW + "4." + TerminalUtils.RESET + " 🔙 Volver");
            System.out.print("\nOpción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch (opcion) {
                case 1 -> listarJugadores();
                case 2 -> anodirJugador();
                case 3 -> eliminarJugador();
                case 4 -> TerminalUtils.printInfo("Regresando al menú principal...");
                default -> TerminalUtils.printError("Opción no válida.");
            }
        } while (opcion != 4);
    }

    public void anodirJugador() {
        boolean nombreValido;
        String nombre;

        do {
            nombreValido = true;
            System.out.print("-> Ingrese el nombre del nuevo jugador: ");
            nombre = scanner.nextLine().trim();

            if (nombre.isEmpty()) {
                TerminalUtils.printWarning("El nombre no puede estar vacío.");
                nombreValido = false;
            } else if (nombre.matches(".*\\d.*")) {
                TerminalUtils.printWarning("El nombre no puede contener números.");
                nombreValido = false;
            } else if (existeJugador(nombre)) {
                TerminalUtils.printWarning("El jugador [" + nombre + "] ya existe.");
                nombreValido = false;
            }
        } while (!nombreValido);

        jugadoresHumanos.add(new JugadorHumano(nombre));
        TerminalUtils.printSuccess("Jugador '" + nombre + "' registrado con éxito.");
        LoggerJuego.registrar("Jugador añadido: " + nombre);
    }

    public void eliminarJugador() {
        System.out.print("-> Ingrese el nombre del jugador a eliminar: ");
        String nombre = scanner.nextLine().trim();

        if (existeJugador(nombre)) {
            eliminarPorNombre(nombre);
            TerminalUtils.printSuccess("Jugador '" + nombre + "' eliminado.");
            LoggerJuego.registrar("Jugador eliminado: " + nombre);
        } else {
            TerminalUtils.printError("Jugador no encontrado.");
        }
    }

    public boolean existeJugador(String nombre) {
        for (JugadorHumano j : this.jugadoresHumanos) {
            if (j.getNombre().equalsIgnoreCase(nombre.trim())) {
                return true;
            }
        }
        return false;
    }

    private void eliminarPorNombre(String nombre) {
        jugadoresHumanos.removeIf(j -> j.getNombre().equalsIgnoreCase(nombre.trim()));
    }

    public void listarJugadores() {
        if (jugadoresHumanos.isEmpty()) {
            TerminalUtils.printInfo("No hay jugadores registrados.");
        } else {
            System.out.println("\n--- LISTA DE JUGADORES ---");
            for (JugadorHumano j : jugadoresHumanos) {
                System.out.println(TerminalUtils.CYAN + " • " + TerminalUtils.RESET + j.getNombre() + " ("
                        + j.getPuntuacion() + " pts)");
            }
        }
    }

    public Set<JugadorHumano> getJugadoresHumanos() {
        return jugadoresHumanos;
    }
}
