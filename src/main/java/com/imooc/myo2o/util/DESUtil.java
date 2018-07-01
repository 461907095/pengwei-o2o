package com.imooc.myo2o.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameterGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class DESUtil {
    private static Key key;
    private static String KEY_STR="myKey";
    private static String CHARSETNAME="UTF-8";
    private static String ALGORITHM="DES";

    static {
        try{
            //生成des算法对象
            KeyGenerator generator=KeyGenerator.getInstance(ALGORITHM);
            //运用SHA1安全策略
            SecureRandom secureRandom=SecureRandom.getInstance("SHA1PRNG");
            //色和智商密钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            //初始化基于SHA1的算法对象
            generator.init(secureRandom);
            //生成秒哟对象
            key=generator.generateKey();
            generator=null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取加密之后的信息
     * @param str
     * @return
     */
    public static String getEncryptString(String str){
        //基于base64编码，接受byt【】并转传承String
        BASE64Encoder base64Encoder=new BASE64Encoder();
        try{
            //utf8编码
            byte[]  bytes=str.getBytes(CHARSETNAME);
            //获取加密对象
            Cipher cipher=Cipher.getInstance(ALGORITHM);
            //初始化加密信息
            cipher.init(Cipher.ENCRYPT_MODE,key);
            //加密
            byte[] doFinal=cipher.doFinal(bytes);
            //byte[] to encode好的
            return base64Encoder.encode(doFinal);

        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public static String getDecryptString(String str){
        //基于base64编码，接受byte【】并转化un成String
        BASE64Decoder base64Decoder=new BASE64Decoder();
        try{
            //按UTF8编码
            byte[] bytes=base64Decoder.decodeBuffer(str);
            //初始化揭秘对象
            Cipher cipher=Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] doFinal=cipher.doFinal(bytes);
            //返回解密之后的信息
            return new String(doFinal,CHARSETNAME);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        System.out.println(getEncryptString("root"));
        System.out.println(getEncryptString("123456"));

    }

}
