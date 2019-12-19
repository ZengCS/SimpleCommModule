package cn.sxw.android.base.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.sxw.android.base.provider.CommonProvider;

public class AESUtils {
    public static String Encrypt(String sSrc) throws Exception {
        return Encrypt(sSrc, new CommonProvider().getCommonArgs());
    }

    // 加密
    public static String Encrypt(String sSrc, String args) throws Exception {
        byte[] raw = args.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return Base64Util.encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    public static String Decrypt(String sSrc) {
        return Decrypt(sSrc, new CommonProvider().getCommonArgs());
    }

    // 解密
    public static String Decrypt(String sSrc, String args) {
        try {
            byte[] raw = args.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64Util.decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
