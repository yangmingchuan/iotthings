package com.ymc.iotthings.util;

/**
 * package name: com.yb.socket.util
 * date :2019/7/5
 * author : ymc
 **/

public class HexUtils {

    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        if ((hexString == null) || (hexString.equals(""))) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; ++i) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[(pos + 1)]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
