package juego.service;

public class TerminalUtils {
    // ANSI Color Codes
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    // Bold ANSI Codes
    public static final String BOLD = "\u001B[1m";
    
    public static void printHeader(String title) {
        String border = "=".repeat(title.length() + 8);
        System.out.println("\n" + CYAN + BOLD + border + RESET);
        System.out.println(CYAN + BOLD + "    " + title.toUpperCase() + RESET);
        System.out.println(CYAN + BOLD + border + RESET);
    }
    
    public static void printSuccess(String message) {
        System.out.println(GREEN + "✅ " + message + RESET);
    }
    
    public static void printError(String message) {
        System.out.println(RED + "❌ " + message + RESET);
    }
    
    public static void printInfo(String message) {
        System.out.println(BLUE + "ℹ️ " + message + RESET);
    }
    
    public static void printWarning(String message) {
        System.out.println(YELLOW + "⚠️ " + message + RESET);
    }
    
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
