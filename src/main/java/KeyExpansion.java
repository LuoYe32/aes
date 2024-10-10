public class KeyExpansion {
    private byte[][] roundKeys;
    public static final int NUM_ROUNDS = 10; // Количество раундов
    private SubstitutionBox sBox;

    // Константы RCON
    private static final byte[] RCON = {
            (byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x10,
            (byte) 0x20, (byte) 0x40, (byte) 0x80, (byte) 0x1B, (byte) 0x36
    };

    // Изменяем конструктор, чтобы принимать байтовый массив
    public KeyExpansion(byte[] masterKey, SubstitutionBox sBox) {
        this.sBox = sBox;
        this.roundKeys = expandKey(masterKey);
    }

    private byte[][] expandKey(byte[] masterKey) {
        byte[][] expandedKeys = new byte[NUM_ROUNDS + 1][16];

        // Инициализация первых 16 байт ключа (первый раундовый ключ)
        System.arraycopy(masterKey, 0, expandedKeys[0], 0, 16);

        // Расширение ключей для всех раундов
        for (int i = 1; i < NUM_ROUNDS + 1; i++) {
            byte[] temp = new byte[4];

            // Берем последние 4 байта предыдущего раундового ключа
            System.arraycopy(expandedKeys[i - 1], 12, temp, 0, 4);

            // Применяем RotWord и SubWord для первого слова каждого раунда
            temp = rotWord(temp);
            temp = subWord(temp);

            // XOR с константой раунда (RCON)
            temp[0] ^= RCON[i - 1];

            // Генерация нового раундового ключа
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if (j == 0) {
                        expandedKeys[i][k + j * 4] = (byte) (expandedKeys[i - 1][k + j * 4] ^ temp[k]);
                    } else {
                        expandedKeys[i][k + j * 4] = (byte) (expandedKeys[i - 1][k + j * 4] ^ expandedKeys[i][k + (j - 1) * 4]);
                    }
                }
            }
        }

        return expandedKeys;
    }

    // Преобразование байтов через S-Box
    private byte[] subWord(byte[] word) {
        for (int i = 0; i < 4; i++) {
            word[i] = sBox.substitute(word[i]);
        }
        return word;
    }

    // Сдвиг байтов в слове
    private byte[] rotWord(byte[] word) {
        byte temp = word[0];
        for (int i = 0; i < 3; i++) {
            word[i] = word[i + 1];
        }
        word[3] = temp;
        return word;
    }

    // Получение раундового ключа
    public byte[] getRoundKey(int round) {
        return roundKeys[round];
    }
}
