import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PasswordManager {
    private static final String PASSWORD_FILE = "password.dat";
    private static final Map<String,String> passwordMap = new HashMap<>();

    private static void loadPassword(){
        File file = new File(PASSWORD_FILE);
        if(!file.exists()) return;

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine())!=null){
                String[] parts = line.split(":", 2);
                if (parts.length == 2){
                    passwordMap.put(parts[0],parts[1]);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void savePassword(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(PASSWORD_FILE))){
            for(Map.Entry<String,String> entry : passwordMap.entrySet()){
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addPassword(String service, String password){
        try {
            String encryptedPassword = Encryptor.encrypt(password);
            passwordMap.put(service, encryptedPassword);
            savePassword();
            System.out.println("✅ Contraseña guardada con éxito.");
        } catch (Exception e) {
            System.out.println("❌ Error al cifrar la contraseña.");
        }
    }

    public static void getPassword(String service){
        try{
            if(passwordMap.containsKey(service)){
                String decryptedPassword = Encryptor.decrypt(passwordMap.get(service));
                System.out.println("🔑 Contraseña para " + service + ":" + decryptedPassword);
            }else {
                System.out.println("⚠️ No hay contraseña guardada para " + service);
            }
        } catch (Exception e) {
            System.out.println("❌ Error al descrifrar la contraseña");
        }
    }

    public static void listServices(){
        if(passwordMap.isEmpty()){
            System.out.println("📂 No hay contraseñas guardadas.");
        }else {
            System.out.println("🔍 Servicios guardados:");
            for (String service : passwordMap.keySet()) {
                System.out.println("- " + service);
            }
        }
    }

    public static void main(String[] args) {
        loadPassword();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n🔐 GESTOR DE CONTRASEÑAS");
            System.out.println("1. Agregar contraseña");
            System.out.println("2. Recuperar contraseña");
            System.out.println("3. Listar servicios");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("🔹 Ingresa el servicio: ");
                    String service = scanner.nextLine();
                    System.out.print("🔹 Ingresa la contraseña: ");
                    String password = scanner.nextLine();
                    addPassword(service, password);
                    break;
                case 2:
                    System.out.print("🔍 Ingresa el servicio: ");
                    String searchService = scanner.nextLine();
                    getPassword(searchService);
                    break;
                case 3:
                    listServices();
                    break;
                case 4:
                    System.out.println("👋 Saliendo del gestor. ¡Hasta luego!");
                    scanner.close();
                    return;
                default:
                    System.out.println("⚠️ Opción no válida. Intenta de nuevo.");
            }
        }
    }
}
