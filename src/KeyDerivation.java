import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

public class KeyDerivation {

    private static final String SALT_FILE = "salt.bin";
    private static final int SALT_LENGTH = 16; //128 bits
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String AUTH_FILE = "auth.dat";

    public static byte[] getOrCreateSalt() throws Exception{
        Path saltPath = Paths.get(SALT_FILE);
        if(Files.exists(saltPath)){
            return Files.readAllBytes(saltPath);
        }else {
            byte[] salt = new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);
            Files.write(saltPath, salt);
            return salt;
        }
    }

    public static SecretKey deriveKey(char[] masterPassword, byte[] salt) throws Exception{
        PBEKeySpec spec = new PBEKeySpec(masterPassword, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return factory.generateSecret(spec);
    }

    public static byte[] getKeyBytesFromMasterPassword(char[] masterPassword) throws Exception{
        byte[] salt = getOrCreateSalt();
        SecretKey key = deriveKey(masterPassword, salt);
        return key.getEncoded();
    }

    public static char[] getOrCreateMasterPassword() throws Exception {
        Scanner scanner = new Scanner(System.in);
        Path saltPath = Paths.get(SALT_FILE);
        Path authPath = Paths.get(AUTH_FILE);

        if (!Files.exists(saltPath)) {
            System.out.println("🔐 Primera vez. Asigná tu nueva clave maestra:");
            System.out.print("Clave maestra: ");
            char[] pass1 = scanner.nextLine().toCharArray();
            System.out.print("Confirmar clave maestra: ");
            char[] pass2 = scanner.nextLine().toCharArray();

            if (!Arrays.equals(pass1, pass2)) {
                throw new RuntimeException("❌ Las claves no coinciden. Abortando.");
            }

            // Crear salt y guardarlo
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            Files.write(saltPath, salt);

            // Derivar clave y guardar autenticación cifrada
            byte[] key = getKeyBytesFromMasterPassword(pass1);
            Encryptor encryptor = new Encryptor(key);
            String encrypted = encryptor.encrypt("VALID");
            Files.write(authPath, encrypted.getBytes());

            return pass1;
        } else {
            System.out.print("🔐 Ingresá tu clave maestra: ");
            char[] password = scanner.nextLine().toCharArray();

            byte[] key = getKeyBytesFromMasterPassword(password);
            Encryptor encryptor = new Encryptor(key);

            // Verificar archivo de autenticación
            try {
                String encryptedAuth = new String(Files.readAllBytes(authPath));
                String decrypted = encryptor.decrypt(encryptedAuth);
                if (!"VALID".equals(decrypted)) {
                    throw new SecurityException("❌ Clave maestra incorrecta.");
                }
            } catch (Exception e) {
                throw new SecurityException("❌ Clave maestra incorrecta.");
            }

            return password;
        }
    }

    public static void main(String[] args) {
        try {
            char[] masterPassword = KeyDerivation.getOrCreateMasterPassword();
            byte[] key = KeyDerivation.getKeyBytesFromMasterPassword(masterPassword);
            Encryptor encryptor = new Encryptor(key);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Texto a cifrar: ");
            String text = scanner.nextLine();

            String encrypted = encryptor.encrypt(text);
            System.out.println("🔒 Cifrado: " + encrypted);

            String decrypted = encryptor.decrypt(encrypted);
            System.out.println("🔓 Descifrado: " + decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
