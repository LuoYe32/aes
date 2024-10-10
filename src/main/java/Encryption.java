public class Encryption {
    private SubstitutionBox sBox;
    private KeyExpansion keyExpansion;

    public Encryption(SubstitutionBox sBox, KeyExpansion keyExpansion) {
        this.sBox = sBox;
        this.keyExpansion = keyExpansion;
    }

    public byte[] encryptBlock (byte[] block) {
        StateMatrix state = new StateMatrix(block);

        // Начальный раунд
        addRoundKey(state, keyExpansion.getRoundKey(0));

        // Основные 9 ранудов
        for (int round = 1; round < 10; round++) {
            subBytes(state);
            state.shiftRows();
            state.mixColumns();
            addRoundKey(state, keyExpansion.getRoundKey(round));
        }

        // Финальный раунд
        subBytes(state);
        state.shiftRows();
        addRoundKey(state, keyExpansion.getRoundKey(10));

        return state.toByteArray();
    }

    private void subBytes(StateMatrix state) {
        byte[][] matrix = state.getState();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = sBox.substitute(matrix[i][j]);
            }
        }
    }

    private void addRoundKey(StateMatrix state, byte[] roundKey) {
        byte[][] matrix = state.getState();
        for (int i = 0; i < 16; i++) {
            matrix[i % 4][i / 4] ^= roundKey[i];
        }
    }
}
