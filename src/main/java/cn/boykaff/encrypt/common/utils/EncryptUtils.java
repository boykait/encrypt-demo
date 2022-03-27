package cn.boykaff.encrypt.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: boykaff
 * @date: 2022-03-25-0025
 */
public class EncryptUtils {

    private EncryptUtils() {

    }

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String IV = "2233445566778899";

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        String data = Base64.encodeBase64String(bytes);
        if (!StringUtils.isEmpty(data)) {
            // 处理一行超过76个字符换行问题
            return data.replaceAll("[\\s*\t\n\r]", "");
        } else {
            return data;
        }
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        byte[] raw = encryptKey.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        byte[] raw = decryptKey.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }


    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    public static void main(String[] args) throws Exception {
        String timestamp = "1648308048";
        String salt = "123456";
        String data = "{\n" +
                "\t\"name\": \"boyka1\",\n" +
                "\t\"userType\": \"VIP\"\n" +
                "}";
        String privateKey = "8VBkO5amPcPrXV3m";
        String encryptData = aesEncrypt(data, privateKey);
        System.out.println(encryptData);
        encryptData = "fxHYvnIE54eAXDbErdrDryEsIYNvsOOkyEKYB1iBcre/QU1wMowHE2BNX/je6OP3NlsCtAeDqcp7J1N332el8r4JPvZmKlXKUUReD8ayUckrFQkSATLwASJmMTArIeit28+LbLFz9e9K0vdaRi/splThh1+7u5CDg/ixDWwVThOlZknRgP3RKar6KN7BJGFyCJYNqpYPkuapsa2B2B6DHQ==";
        String decryptData = aesDecrypt(encryptData, privateKey);
        System.out.println(decryptData);
        String signature = Md5Utils.genSignature(timestamp + salt + encryptData + privateKey);
        System.out.println(signature);
    }

    public static byte[] generateDesKey(int length) throws Exception {
        //实例化
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //设置密钥长度
        kgen.init(length);
        //生成密钥
        SecretKey skey = kgen.generateKey();
        //返回密钥的二进制编码
        return skey.getEncoded();
    }

    public static int genSalt() {
        int salt = 0;
        while (true) {
            salt = (int) (Math.random() * 1000000);
            if (salt < 1000000 && salt > 99999)
                break;
        }
        return salt;
    }
}
