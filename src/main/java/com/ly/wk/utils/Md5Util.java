package com.ly.wk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Md5加盐加密
 */
public class Md5Util {
    private static Random random=new Random();

    public static  String digest(String password,String salt)  {
        String text=password+salt;
        MessageDigest digest= null;
        try {
            digest = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] b=text.getBytes();
        digest.update(b);
        b=digest.digest(b);

        return byteArrayToHex(b);
    }

    /**
     * 获取盐
     * @return
     */
    public static String getSalt(){
        StringBuffer salt=new StringBuffer();
        salt.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        if(salt.length()<16){
            for(int i=0;i<16-salt.length();i++)
                salt.append(0);
        }
        return salt.toString();
    }

    /**
     * 将字节数组转成16进制
     * @param byteArray
     * @return
     */
    private static String byteArrayToHex(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (int n=0; n < byteArray.length; n++) {
            int v = byteArray[n];
            if (v < 0)v += 256;
            if (v < 16) sb.append(0);
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }
}
