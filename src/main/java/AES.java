public class AES {
    private KeyExpansion keyExpansion;
    private SubstitutionBox sBox;
    private InverseSubstitutionBox iSBox;
    private Encryption encryption;
    private Decryption decryption;


    public AES(byte[] masterKey, String sBoxFilePath, String iSBoxFilePath) throws Exception {
        // Инициализация компонентов
        this.sBox = new SubstitutionBox(sBoxFilePath);
        this.iSBox = new InverseSubstitutionBox(iSBoxFilePath);
        this.keyExpansion = new KeyExpansion(masterKey, sBox);
        this.encryption = new Encryption(sBox, keyExpansion);
        this.decryption = new Decryption(iSBox, keyExpansion);
    }

    public byte[] encrypt(byte[] plainText) throws Exception {
        return encryption.encryptBlock(plainText);
    }

    public byte[] decrypt(byte[] ciphertext) {
        return decryption.decryptBlock(ciphertext);
    }
}
