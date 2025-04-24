import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class Encryptor {

    private  final SecretKeySpec keySpec;

    public Encryptor(byte[] keyBytes) {
        this.keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String data) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String base64Data) throws Exception{
        byte[] combined = Base64.getDecoder().decode(base64Data);
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[combined.length - 16];
        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, "UTF-8");
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("🔐 Ingresá tu clave maestra: ");
            char[] masterPassword = scanner.nextLine().toCharArray();

            byte[] key = KeyDerivation.getKeyBytesFromMasterPassword(masterPassword);
            Encryptor encryptor = new Encryptor(key);

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
