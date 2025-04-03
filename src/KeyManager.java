import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.util.Base64;

public class KeyManager {
    private static final String KEY_FILE = "key.aes";

    public static SecretKey generateKey() throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    public static void saveKey(SecretKey key) throws Exception{
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        try (FileWriter writer = new FileWriter(KEY_FILE)){
            writer.write(encodedKey);
        }
    }

    public static SecretKey loadKey() throws IOException {
        File file = new File(KEY_FILE);
        if (!file.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String encodedKey = reader.readLine();
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            return new SecretKeySpec(decodedKey, "AES");
        }
    }

    public static SecretKey getKey() throws Exception{
        SecretKey key = loadKey();
        if(key == null){
            key = generateKey();
            saveKey(key);
        }
        return key;
    }

    public static void main(String[] args) {
        try {
            SecretKey key = getKey();
            System.out.println("Clave AES cargada correctamente.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
