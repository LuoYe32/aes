import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Генерация случайного мастер-ключа
            byte[] masterKey = generateRandomKey();
            String masterKey2 = Base64.getEncoder().encodeToString(masterKey);;
            System.out.println("Сгенерированный мастер-ключ (Base64): " + masterKey2.replace("=", ""));

            // Чтение S-Box из JSON файла
            String sBoxFilePath = "src/main/resources/sbox.json";
            String iSBoxFilePath = "src/main/resources/inversesbox.json";


            // Инициализация AES
            AES aes = new AES(masterKey, sBoxFilePath, iSBoxFilePath);

//            // Ввод данных для шифрования
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите данные для шифрования (максимум 16 символов): ");
            String inputData = scanner.nextLine();

            // Проверка длины строки
            if (inputData.length() < 16) {
                System.out.println("Ошибка: введено меньше 16 символов.");
                return;
            } else if (inputData.length() > 16) {
                // Обрезаем строку до 16 символов
                inputData = inputData.substring(0, 16);
            }
            //String inputData = "qwertyuiopasdfgh";
            byte[] dataBlock = inputData.getBytes();  // Преобразуем строку в байты

            // Шифрование данных
            byte[] encryptedData = aes.encrypt(dataBlock);

            // Расшифровка данных
            byte[] decryptedData = aes.decrypt(encryptedData);

            System.out.println("Оригинальные байты: " + Arrays.toString(dataBlock));
            System.out.println("Зашифрованные байты: " + Arrays.toString(encryptedData));
            System.out.println("Расшифрованные байты: " + Arrays.toString(decryptedData));

            String originalString = new String(dataBlock, StandardCharsets.UTF_8);
            String encryptedString = new String(encryptedData, StandardCharsets.UTF_8);
            String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);

            String encryptedString2 = Base64.getEncoder().encodeToString(encryptedData);

            // Выводим строки
            System.out.println("Оригинальная строка: " + originalString);
            System.out.println("Зашифрованная строка: " + encryptedString);
            System.out.println("Зашифрованная строка (Base64): " + encryptedString2.replace("=", ""));
            System.out.println("Расшифрованная строка: " + decryptedString);

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Метод для генерации случайного 128-битного ключа
    private static byte[] generateRandomKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);  // Генерация 128-битного ключа
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();  // Возвращаем байты, а не Base64 строку
    }
}
