import netscape.javascript.JSException;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SubstitutionBox {
    private int[] sBox;

    public SubstitutionBox(String filePath) throws Exception {
        loadBoxFromJson(filePath);
    }

    private void loadBoxFromJson(String filePath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject json = new JSONObject(content);
        this.sBox = new int[256];

        for (int i = 0; i < 256; i++) {
            String hexValue = json.getString(String.format("%02x", i)); // Получаем строку в шестнадцатеричном формате
            sBox[i] = Integer.parseInt(hexValue, 16); // Преобразуем шестнадцатеричную строку в десятичное целое число
        }
    }

    public byte substitute(byte value) {
        return (byte) sBox[value & 0xFF];
    }


}
