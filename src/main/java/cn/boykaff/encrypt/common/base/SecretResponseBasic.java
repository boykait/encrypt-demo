package cn.boykaff.encrypt.common.base;

import cn.boykaff.encrypt.common.utils.EncryptUtils;
import lombok.Data;

/**
 * @description:
 * @author: boykaff
 * @date: 2022-03-25-0025
 */
@Data
public class SecretResponseBasic {
    /**
     * 加密数据
     */
    private Object data;
    /**
     * 加密结果
     */
    private Integer code;
    /**
     * 签名，用于验签
     */
    private String signature;

    private String msg;

    private long timestamp;
    private int salt;

    public static SecretResponseBasic success(Object data) {
        return newInstance(data, 200, "success");
    }

    public static SecretResponseBasic success(Object data, long timestamp, int salt, String signature) {
        return newInstance(data, "", 200, timestamp, salt, signature);
    }

    public static SecretResponseBasic fail(Object data) {
        return newInstance(data, 400, "");
    }

    public static SecretResponseBasic fail(Integer code, Object data, String msg) {
        return newInstance(data, code, msg);
    }

    public static SecretResponseBasic fail(String msg, long timestamp, int salt, String signature) {
        return newInstance("", msg, 400, timestamp, salt, signature);
    }

    public static SecretResponseBasic failWithEncrypt(String msg, long timestamp, int salt, String signature) {
        return newInstance("", msg, 400, timestamp, salt, signature);
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private static SecretResponseBasic newInstance(Object data, Integer status, String msg) {
        long timestamp = System.currentTimeMillis() / 1000;
        int salt = EncryptUtils.genSalt();
        SecretResponseBasic secretResponseBasic = new SecretResponseBasic();
        secretResponseBasic.setData(data);
        secretResponseBasic.setMsg(msg);
        secretResponseBasic.setCode(status);
        secretResponseBasic.setSalt(salt);
        secretResponseBasic.setTimestamp(timestamp);
        return secretResponseBasic;
    }


    private static SecretResponseBasic newInstance(Object data, String msg, Integer status, long timestamp, int salt, String signature) {
        SecretResponseBasic secretResponseBasic = new SecretResponseBasic();
        secretResponseBasic.setData(data);
        secretResponseBasic.setMsg(msg);
        secretResponseBasic.setCode(status);
        secretResponseBasic.setTimestamp(timestamp);
        secretResponseBasic.setSalt(salt);
        secretResponseBasic.setSignature(signature);
        return secretResponseBasic;
    }
}
