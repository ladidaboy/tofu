package cn.hl.ox.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptUtil {

    /**
     * 根据publicKey来解密字符串
     * @param txt
     * @param publicKey
     * @return 解密后字符串
     */
    public static String decrypt(String txt, Key publicKey) throws Exception {
        if (txt == null || "".equals(txt) || publicKey == null) {
            throw new Exception("参数为空!");
        }

        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(txt);
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 根据key字符串获取PublicKey
     * @param key
     * @return PublicKey
     * @throws Exception
     */
    public static Key getPubKeyByStr(String key) throws Exception {
        if (key == null || "".equals(key)) {
            throw new Exception("参数为空");
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(key));
        return keyFactory.generatePublic(publicKeySpec);
    }

    /**
     * 根据privateKey来加密字符串
     * @param txt
     * @param privateKey
     * @return 加密后字符串
     */
    public static String ecrypt(String txt, Key privateKey) throws Exception {
        if (txt == null || "".equals(txt) || privateKey == null) {
            throw new Exception("参数为空");
        }

        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] b = txt.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b1);
    }
}
