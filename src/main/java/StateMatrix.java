public class StateMatrix {
    private byte[][] state;

    public StateMatrix(byte[] block) {
        this.state = new byte[4][4];
        loadBlock(block);
    }

    private void loadBlock(byte[] block) {
        for (int i = 0; i < 16; i++) {
            state[i % 4][i / 4] = block[i];
        }
    }

    public byte[][] getState() {
        return state;
    }

    public byte[] toByteArray() {
        byte[] block = new byte[16];
        for (int i = 0; i < 16; i++) {
            block[i] = state[i % 4][i / 4];
        }
        return block;
    }

    public void shiftRows() {
        // Сдвиг строк
        for (int i = 0; i < 4; i++) {
            byte[] temp = new byte[4];
            for (int j = 0; j < 4; j++) {
                temp[j] = state[i][(j + i) % 4];
            }
            state[i] = temp;
        }
    }


    public void mixColumns() {
        for (int j = 0; j < 4; j++) { // для каждого столбца
            byte[] col = new byte[4];

            // Копируем текущий столбец
            for (int i = 0; i < 4; i++) {
                col[i] = state[i][j];
            }

            // Применяем матричное умножение
            state[0][j] = (byte) (mul(2, col[0]) ^ mul(3, col[1]) ^ col[2] ^ col[3]);
            state[1][j] = (byte) (col[0] ^ mul(2, col[1]) ^ mul(3, col[2]) ^ col[3]);
            state[2][j] = (byte) (col[0] ^ col[1] ^ mul(2, col[2]) ^ mul(3, col[3]));
            state[3][j] = (byte) (mul(3, col[0]) ^ col[1] ^ col[2] ^ mul(2, col[3]));
        }
    }

    // Метод для умножения в поле GF(2^8)
    private byte mul(int a, byte b) {
        if (b == 0) return 0;
        byte result = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) result ^= a; // добавляем a
            boolean highBit = (a & 0x80) != 0; // проверяем старший бит
            a <<= 1; // сдвигаем a
            if (highBit) a ^= 0x1b; // если старший бит был 1, выполняем сокращение
            b >>= 1; // сдвигаем b
        }
        return result;
    }


}
