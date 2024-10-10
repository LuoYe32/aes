public class Decryption {
    private InverseSubstitutionBox inverseSBox;
    private KeyExpansion keyExpansion;

    public Decryption(InverseSubstitutionBox inverseSBox, KeyExpansion keyExpansion) {
        this.inverseSBox = inverseSBox;
        this.keyExpansion = keyExpansion;
    }

    public byte[] decryptBlock(byte[] block) {
        InverseStateMatrix state = new InverseStateMatrix(block);

        // Начальный раунд
        addRoundKey(state, keyExpansion.getRoundKey(10));
        state.invShiftRows();
        invSubBytes(state);

        // Основные 9 раундов
        for (int round = 9; round > 0; round--) {
            addRoundKey(state, keyExpansion.getRoundKey(round));
            state.invMixColumns();
            state.invShiftRows();
            invSubBytes(state);

        }

        // Финальный раунд
        addRoundKey(state, keyExpansion.getRoundKey(0));


        return state.toByteArray();
    }

    private void invSubBytes(InverseStateMatrix state) {
        byte[][] matrix = state.getState();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = inverseSBox.inverseSubstitute(matrix[i][j]);
            }
        }
    }

    private void addRoundKey(InverseStateMatrix state, byte[] roundKey) {
        byte[][] matrix = state.getState();
        for (int i = 0; i < 16; i++) {
            matrix[i % 4][i / 4] ^= roundKey[i];
        }
    }
}
