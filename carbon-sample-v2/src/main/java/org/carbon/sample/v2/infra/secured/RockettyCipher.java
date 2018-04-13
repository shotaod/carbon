package org.carbon.sample.v2.infra.secured;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.NonNull;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;

/**
 * @author garden 2018/03/29.
 */
@Component
public class RockettyCipher {

    private static final Base64.Decoder base64Decoder = Base64.getDecoder();

    private Cipher decipher;
    @Assemble
    private RockettyCipherProperty property;

    public RockettyCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    public String decrypt(@NonNull String base64Message) throws GeneralSecurityException, DecoderException {
        // decode base64
        byte[] cipherData = base64Decoder.decode(base64Message);
        String cipherStr = new String(cipherData);
        // parse encrypt info(Key and Iv)
        String[] iv_text = cipherStr.split("_");
        byte[] iv = Hex.decodeHex(iv_text[0].toCharArray());
        byte[] text = Hex.decodeHex(iv_text[1].toCharArray());
        // initialize cipher
        SecretKey key = new SecretKeySpec(property.getPassphrase().getBytes(), "AES");
        IvParameterSpec ivs = new IvParameterSpec(iv);
        decipher.init(Cipher.DECRYPT_MODE, key, ivs);
        // do decrypt
        byte[] decryptedData = decipher.doFinal(text);
        return new String(decryptedData);
    }

    // public String 断念したdecrypt() {
    //        // get need info
    //        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);
    //        Hex.decodeHex()
    //        MessageDigest md5 = MessageDigest.getInstance("MD5");
    //        Pair<byte[], byte[]> keyAndIV = generateKeyAndIV(16, 16, 500, saltData, property.getPassphrase().getBytes(), md5);
    //        SecretKeySpec key = new SecretKeySpec(keyAndIV.getLeft(), "AES");
    //        IvParameterSpec iv = new IvParameterSpec(keyAndIV.getRight());
    //
    //        byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
    //        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
    //        aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
    //        byte[] decryptedData = aesCBC.doFinal(encrypted);
    //        return new String(decryptedData);
    //        byte[] salt = Hex.decodeHex(iv_salt_text[1].toCharArray());
    //        byte[] encrypted = Hex.decodeHex(iv_salt_text[1].toCharArray());
    //        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    //        KeySpec spec = new PBEKeySpec(property.getPassphrase().toCharArray(), salt, 1, 128);
    //        SecretKey tmp = factory.generateSecret(spec);
    // }

    //    /**
    //     * Generates a key and an initialization vector (IV) with the given salt and password.
    //     * <p>
    //     * This method is equivalent to OpenSSL's EVP_BytesToKey function
    //     * (see https://github.com/openssl/openssl/blob/master/crypto/evp/evp_key.c).
    //     * By default, OpenSSL uses a single iteration, MD5 as the algorithm and UTF-8 encoded password data.
    //     * </p>
    //     *
    //     * @param keyLength  the length of the generated key (in bytes)
    //     * @param ivLength   the length of the generated IV (in bytes)
    //     * @param iterations the number of digestion rounds
    //     * @param salt       the salt data (8 bytes of data or <code>null</code>)
    //     * @param password   the password data (optional)
    //     * @param md         the message digest algorithm to use
    //     * @return an two-element array with the generated key and IV
    //     */
    //    public Pair<byte[], byte[]> generateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) throws DigestException {
    //
    //        int digestLength = md.getDigestLength();
    //        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
    //        byte[] generatedData = new byte[requiredLength];
    //        int generatedLength = 0;
    //
    //        md.reset();
    //
    //        // Repeat process until sufficient data has been generated
    //        while (generatedLength < keyLength + ivLength) {
    //
    //            // Digest data (last digest if available, password data, salt if available)
    //            if (generatedLength > 0)
    //                md.update(generatedData, generatedLength - digestLength, digestLength);
    //            md.update(password);
    //            if (salt != null)
    //                md.update(salt, 0, 8);
    //            md.digest(generatedData, generatedLength, digestLength);
    //
    //            // additional rounds
    //            for (int i = 1; i < iterations; i++) {
    //                md.update(generatedData, generatedLength, digestLength);
    //                md.digest(generatedData, generatedLength, digestLength);
    //            }
    //
    //            generatedLength += digestLength;
    //        }
    //
    //        // Copy key and IV into separate byte arrays
    //        return Pair.of(
    //                // key
    //                Arrays.copyOfRange(generatedData, 0, keyLength),
    //                // iv
    //                Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength)
    //        );
    //    }
}
