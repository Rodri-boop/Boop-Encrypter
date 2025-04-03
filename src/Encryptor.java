import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Base64;

public class Encryptor {

    public static String encrypt(String data) throws Exception{
        SecretKey key = KeyManager.getKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData) throws Exception{
        SecretKey key = KeyManager.getKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try{

            String originalText = "Clave SECRETA 123";

            String encryptedText = encrypt(originalText);
            System.out.println("Texto cifrado: " + encryptedText);

            String decryptedText = decrypt(encryptedText);
            System.out.println("Texto Descrifrado: " + decryptedText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
