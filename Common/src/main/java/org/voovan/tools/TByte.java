package org.voovan.tools;

import java.nio.charset.Charset;

/**
 * 基本类型转换为字节
 *
 * @author: helyho
 * DBase Framework.
 * WebSite: https://github.com/helyho/DBase
 * Licence: Apache v2 License
 */
public class TByte {

    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "UTF-8");
    }


    public static short getShort(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static char getChar(byte[] bytes) {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public static long getLong(byte[] bytes) {
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16)) | (0xff000000L & ((long) bytes[3] << 24))
                | (0xff00000000L & ((long) bytes[4] << 32)) | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48)) | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        System.out.println(l);
        return Double.longBitsToDouble(l);
    }

    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, "UTF-8");
    }


    /**
     * 字节数组拼接
     * @param firstBytes		   首个字节数组
     * @param firstBytesLength     首个字节数组长度
     * @param lastBytes			   拼接在后的字节数组
     * @param lastBytesLength      拼接在后的字节数组长度
     * @return 字节数组
     */
    public static byte[] byteArrayConcat(byte[] firstBytes,int firstBytesLength, byte[] lastBytes,int lastBytesLength) {
        if (lastBytes.length == 0)
            return firstBytes;
        byte[] target = new byte[firstBytesLength + lastBytesLength];
        System.arraycopy(firstBytes, 0, target, 0, firstBytesLength);
        System.arraycopy(lastBytes, 0, target, firstBytes.length,lastBytesLength);
        return target;
    }

    /**
     * 定位某个字节在字节数组中的位置
     * @param source 待检索的数组
     * @param mark 被检索的字节
     * @return 字节位置 -1: 没有找到, 大于0: 被检索的字节的位置
     */
    public static int byteIndexOf(byte[] source, byte mark){
        for(int i = source.length-1 ; i >= 0; i--){
            if(source[i] == mark){
                return i ;
            }
        }
        return -1;
    }

    /**
     * 定位某个字节数组在字节数组中的位置
     * @param source 待检索的数组
     * @param mark 被检索的字节数组
     * @return 字节位置 -1: 没有找到, 大于0: 被检索的字节第一个元组的位置
     */
    public static int byteArrayIndexOf(byte[] source, byte[] mark){
        if(source.length == 0){
            return -1;
        }

        if(source.length < mark.length){
            return -1;
        }

        int index = -1;

        int i = 0;
        int j = 0;

        while(i <= (source.length - mark.length + j )  ){
            if(source[i]!=mark[j]){
                if(i == (source.length - mark.length + j )){
                    break;
                }

                int pos = (int) byteIndexOf(mark, source[i+mark.length-j]);

                if( pos== -1){
                    i = i + mark.length + 1 - j;
                    j = 0 ;
                }else{
                    i = i + mark.length - pos - j;
                    j = 0;
                }
            }else{
                if(j == (mark.length - 1)){
                    i = i - j + 1 ;
                    j = 0;
                    index  = i-j - 1;
                    break;
                }else{
                    i++;
                    j++;
                }
            }
        }

        return index;
    }

    public static int byteArrayCompare(byte[] byte1, byte[] byte2){
        for(int i = 0; i < byte1.length; i++) {
            if (i + 1 > byte2.length){
                break;
            }

            if (byte1[i] < byte2[i]){
                return -1;
            }

            if (byte1[i] > byte2[i]){
                return 1;
            }
        }

        return 0;
    }
}
