import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create Alice and Bob
        User alice = new User("Alice");
        User bob = new User("Bob");

        // Symmetric Encryption and Decryption (AES-256)
        System.out.println("=== Symmetric Encryption (AES-256) ===");
        SecretKey aesKey = KeyGenerator.getInstance("AES").generateKey();
        String message = "Hello Bob, this is Alice!";
        System.out.println("Original Message: " + message);

        // Alice encrypts the message
        byte[] encryptedMessage = alice.encryptSymmetric(message, aesKey);
        System.out.println("Encrypted Message: " + Base64.getEncoder().encodeToString(encryptedMessage));

        // Bob decrypts the message
        String decryptedMessage = bob.decryptSymmetric(encryptedMessage, aesKey);
        System.out.println("Decrypted Message: " + decryptedMessage);

        // Asymmetric Encryption and Decryption (RSA-2048)
        System.out.println("\n=== Asymmetric Encryption (RSA-2048) ===");
        String asymmetricMessage = "Hello Alice, this is Bob!";
        System.out.println("Original Message: " + asymmetricMessage);

        // Bob encrypts the message using Alice's public key
        byte[] encryptedAsymmetricMessage = bob.encryptAsymmetric(asymmetricMessage, alice.getPublicKey());
        System.out.println("Encrypted Message: " + Base64.getEncoder().encodeToString(encryptedAsymmetricMessage));

        // Alice decrypts the message using her private key
        String decryptedAsymmetricMessage = alice.decryptAsymmetric(encryptedAsymmetricMessage);
        System.out.println("Decrypted Message: " + decryptedAsymmetricMessage);

        // Signing and Validating a Message
        System.out.println("\n=== Signing and Validating a Message (RSA-2048) ===");
        String messageToSign = "This is a signed message from Alice.";
        System.out.println("Message to Sign: " + messageToSign);

        // Alice signs the message
        byte[] signature = alice.signMessage(messageToSign);
        System.out.println("Signature: " + Base64.getEncoder().encodeToString(signature));

        // Bob validates the signature using Alice's public key
        boolean isSignatureValid = bob.validateSignature(messageToSign, signature, alice.getPublicKey());
        System.out.println("Is Signature Valid? " + isSignatureValid);
    }
}

class User {
    private final String name;
    private final KeyPair rsaKeyPair;

    public User(String name) throws Exception {
        this.name = name;
        this.rsaKeyPair = generateRSAKeyPair();
    }

    public PublicKey getPublicKey() {
        return rsaKeyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return rsaKeyPair.getPrivate();
    }

    // Symmetric Encryption (AES-256)
    public byte[] encryptSymmetric(String message, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12]; // 12-byte IV for GCM
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] ciphertext = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        byte[] encryptedMessage = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encryptedMessage, 0, iv.length);
        System.arraycopy(ciphertext, 0, encryptedMessage, iv.length, ciphertext.length);
        return encryptedMessage;
    }

    public String decryptSymmetric(byte[] encryptedMessage, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12];
        System.arraycopy(encryptedMessage, 0, iv, 0, iv.length);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] ciphertext = new byte[encryptedMessage.length - iv.length];
        System.arraycopy(encryptedMessage, iv.length, ciphertext, 0, ciphertext.length);
        byte[] plaintext = cipher.doFinal(ciphertext);
        return new String(plaintext, StandardCharsets.UTF_8);
    }

    // Asymmetric Encryption (RSA-2048)
    public byte[] encryptAsymmetric(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

    public String decryptAsymmetric(byte[] encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
        byte[] plaintext = cipher.doFinal(encryptedMessage);
        return new String(plaintext, StandardCharsets.UTF_8);
    }

    // Signing a Message
    public byte[] signMessage(String message) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(rsaKeyPair.getPrivate());
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    // Validating a Signature
    public boolean validateSignature(String message, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return signature.verify(signatureBytes);
    }

    // Generate RSA Key Pair
    private KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}