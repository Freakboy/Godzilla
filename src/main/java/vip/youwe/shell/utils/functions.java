package vip.youwe.shell.utils;

import vip.youwe.shell.utils.http.Http;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class functions {

    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '+', '/'};
    private static final char[] toBase64URL = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '-', '_'};


    public static void concatMap(Map<String, List<String>> receiveMap, Map<String, List<String>> map) {
        Iterator<String> iterator = map.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            receiveMap.put(key, map.get(key));
        }
    }

    public static void fireActionEventByJComboBox(JComboBox comboBox) {
        try {
            Method method = comboBox.getClass().getDeclaredMethod("fireActionEvent", null);
            method.setAccessible(true);
            method.invoke(comboBox, null);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public static boolean toBoolean(String s) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            Log.error(e);
            return false;
        }
    }

    public static byte[] ipToByteArray(String paramString) {
        String[] array2 = paramString.split("\\.");
        byte[] array = new byte[4];
        for (int i = 0; i < array2.length; i++) {
            array[i] = (byte) Integer.parseInt(array2[i]);
        }
        return array;
    }

    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) (s >>> offset & 0xFF);
        }
        return targets;
    }

    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) (value & 0xFF);
        src[1] = (byte) (value >> 8 & 0xFF);
        src[2] = (byte) (value >> 16 & 0xFF);
        src[3] = (byte) (value >> 24 & 0xFF);
        return src;
    }

    public static String byteArrayToHex(byte[] bytes) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? ("0" + strHex) : strHex);
        }
        return sb.toString().trim();
    }

    public static byte[] hexToByte(String hex) {
        int m = 0, n = 0;
        int byteLen = hex.length() / 2;
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = (byte) intVal;
        }
        return ret;
    }

    public static Object concatArrays(Object array1, int array1_Start, int array1_End, Object array2, int array2_Start, int array2_End) {
        if (array1.getClass().isArray() && array2.getClass().isArray()) {
            if (array1_Start >= 0 && array1_Start >= 0 && array2_End >= 0 && array2_Start >= 0) {
                int array1len = (array1_Start != array1_End) ? (array1_End - array1_Start + 1) : 0;
                int array2len = (array2_Start != array2_End) ? (array2_End - array2_Start + 1) : 0;
                int maxLen = array1len + array2len;
                byte[] data = new byte[maxLen];
                System.arraycopy(array1, array1_Start, data, 0, array1len);
                System.arraycopy(array2, array2_Start, data, array1len, array2len);
                return data;
            }
            return null;
        }
        return null;
    }

    public static void addShutdownHook(Class<?> cls, Object object) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    cls.getMethod("Tclose", null).invoke(object, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public static short bytesToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static int stringToint(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (Exception e) {
            return 0;
        }
    }

    public static byte[] aes(int opmode, byte[] key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(opmode, new SecretKeySpec(key, "AES"), new IvParameterSpec(key));
            return cipher.doFinal(data);
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

    public static void openBrowseUrl(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                URI uri = URI.create(url);
                Desktop dp = Desktop.getDesktop();
                if (dp.isSupported(Desktop.Action.BROWSE)) {
                    dp.browse(uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] temp = new byte[5120];
        int readOneNum = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((readOneNum = inputStream.read(temp)) != -1) {
            bos.write(temp, 0, readOneNum);
        }
        return bos.toByteArray();
    }

    public static Map<String, String> matcherTwoChild(String data, String regex) {
        String rexString = regex;
        Pattern pattern = Pattern.compile(rexString);
        Matcher m = pattern.matcher(data);
        Map<String, String> hashMap = new HashMap<>();
        while (m.find()) {
            try {
                String v1 = m.group(1);
                String v2 = m.group(2);
                hashMap.put(v1, v2);
            } catch (Exception e) {
                Log.error(e);
            }
        }
        return hashMap;
    }

    public static short[] toShortArray(byte[] src) {
        int count = src.length >> 1;
        short[] dest = new short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) (src[i * 2] << 8 | src[2 * i + 1] & 0xFF);
        }
        return dest;
    }

    public static byte[] stringToByteArray(String data, String encodng) {
        try {
            return data.getBytes(encodng);
        } catch (Exception e) {
            return data.getBytes();
        }
    }

    public static byte[] httpReqest(String urlString, String method, Map<String, String> headers, byte[] data) {
        byte[] result = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoInput(true);
            httpConn.setDoOutput(!"GET".equals(method.toUpperCase()));
            httpConn.setConnectTimeout(3000);
            httpConn.setReadTimeout(3000);
            httpConn.setRequestMethod(method.toUpperCase());
            Http.addHttpHeader(httpConn, headers);
            if (httpConn.getDoOutput() && data != null) {
                httpConn.getOutputStream().write(data);
            }
            InputStream inputStream = httpConn.getInputStream();
            result = readInputStream(inputStream);
        } catch (Exception e) {
            Log.error(e);
        }

        return result;
    }

    public static String formatDir(String dirString) {
        if (dirString != null && dirString.length() > 0) {
            dirString = dirString.trim();
            dirString = dirString.replaceAll("\\\\+", "/").replaceAll("/+", "/").trim();
            if (!dirString.substring(dirString.length() - 1, dirString.length()).equals("/")) {
                dirString = dirString + "/";
            }
            return dirString;
        }
        return "";
    }

    public static boolean filePutContent(String file, byte[] data) {
        return filePutContent(new File(file), data);
    }

    public static boolean filePutContent(File file, byte[] data) {
        boolean state = false;
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();
            state = true;
        } catch (Exception e) {
            Log.error(e);
            state = false;
        }
        return state;
    }

    public static String concatCookie(String oldCookie, String newCookie) {
        oldCookie = oldCookie + ";";
        newCookie = newCookie + ";";
        StringBuffer cookieBuffer = new StringBuffer();
        Map<String, String> cookieMap = new HashMap<>();
        String[] tmpA = oldCookie.split(";");

        for (int i = 0; i < tmpA.length; i++) {
            String[] temB = tmpA[i].split("=");
            cookieMap.put(temB[0], temB[1]);
        }
        tmpA = newCookie.split(";");
        for (int i = 0; i < tmpA.length; i++) {
            String[] temB = tmpA[i].split("=");
            cookieMap.put(temB[0], temB[1]);
        }
        Iterator<String> iterator = cookieMap.keySet().iterator();

        while (iterator.hasNext()) {
            String keyString = iterator.next();
            cookieBuffer.append(keyString);
            cookieBuffer.append("=");
            cookieBuffer.append(cookieMap.get(keyString));
            cookieBuffer.append(";");
        }
        return cookieBuffer.toString();
    }

    public static String md5(String s) {
        String ret = null;


        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            ret = (new BigInteger(1, m.digest())).toString(16);
        } catch (NoSuchAlgorithmException e) {
            Log.error(e);
        }
        return ret;
    }

    public static String base64Encode(byte[] src) {
        int off = 0;
        int end = src.length;
        byte[] dst = new byte[4 * ((src.length + 2) / 3)];
        int linemax = -1;
        boolean doPadding = true;
        char[] base64 = toBase64;
        int sp = off;
        int slen = (end - off) / 3 * 3;
        int sl = off + slen;
        if (linemax > 0 && slen > linemax / 4 * 3) {
            slen = linemax / 4 * 3;
        }
        int dp = 0;
        while (sp < sl) {
            int sl0 = Math.min(sp + slen, sl);
            int sp0 = sp;
            int dp0 = dp;
            while (sp0 < sl0) {
                int bits = (src[sp0++] & 0xFF) << 16 | (src[sp0++] & 0xFF) << 8 | src[sp0++] & 0xFF;
                dst[dp0++] = (byte) base64[bits >>> 18 & 0x3F];
                dst[dp0++] = (byte) base64[bits >>> 12 & 0x3F];
                dst[dp0++] = (byte) base64[bits >>> 6 & 0x3F];
                dst[dp0++] = (byte) base64[bits & 0x3F];
            }
            int dlen = (sl0 - sp) / 3 * 4;
            dp += dlen;
            sp = sl0;
        }
        if (sp < end) {
            // 0xFF -> 255, 0x3F -> 63
            int b0 = src[sp++] & 0xFF;
            dst[dp++] = (byte) base64[b0 >> 2];
            if (sp == end) {
                dst[dp++] = (byte) base64[b0 << 4 & 0x3F];
                if (doPadding) {
                    dst[dp++] = 61;
                    dst[dp++] = 61;
                }
            } else {
                int b1 = src[sp++] & 0xFF;
                dst[dp++] = (byte) base64[b0 << 4 & 0x3F | b1 >> 4];
                dst[dp++] = (byte) base64[b1 << 2 & 0x3F];
                if (doPadding) {
                    dst[dp++] = 61;
                }
            }
        }
        return new String(dst);
    }

    public static byte[] base64Decode(String base64Str) {
        if (base64Str == null || base64Str.isEmpty()) {
            return new byte[0];
        }
        byte[] src = base64Str.getBytes();
        if (src.length == 0) {
            return src;
        }
        int sp = 0;
        int sl = src.length;
        int paddings = 0;
        int len = sl - sp;
        if (src[sl - 1] == 61) {
            paddings++;
            if (src[sl - 2] == 61) {
                paddings++;
            }
        }
        if (paddings == 0 && (len & 0x3) != 0) {
            paddings = 4 - (len & 0x3);
        }
        byte[] dst = new byte[3 * (len + 3) / 4 - paddings];
        int[] base64 = new int[256];
        Arrays.fill(base64, -1);

        for (int i = 0; i < toBase64.length; i++) {
            base64[toBase64[i]] = i;
        }
        base64[61] = -2;
        int dp = 0;
        int bits = 0;
        int shiftto = 18;
        while (sp < sl) {
            int b = src[sp++] & 0xFF;
            if ((b = base64[b]) < 0 && b == -2) {
                if ((shiftto == 6 && (sp == sl || src[sp++] != 61)) || shiftto == 18) {
                    throw new IllegalArgumentException("Input byte array has wrong 4-byte ending unit");
                }
                break;
            }
            bits |= b << shiftto;
            shiftto -= 6;
            if (shiftto < 0) {
                dst[dp++] = (byte) (bits >> 16);
                dst[dp++] = (byte) (bits >> 8);
                dst[dp++] = (byte) bits;
                shiftto = 18;
                bits = 0;
            }
        }

        if (shiftto == 6) {
            dst[dp++] = (byte) (bits >> 16);
        } else if (shiftto == 0) {
            dst[dp++] = (byte) (bits >> 16);
            dst[dp++] = (byte) (bits >> 8);
        } else if (shiftto == 12) {
            throw new IllegalArgumentException("Last unit does not have enough valid bits");
        }
        if (dp != dst.length) {
            byte[] arrayOfByte = new byte[dp];
            System.arraycopy(dst, 0, arrayOfByte, 0, Math.min(dst.length, dp));
            dst = arrayOfByte;
        }
        return dst;
    }

    public static String subMiddleStr(String data, String leftStr, String rightStr) {
        int leftIndex = data.indexOf(leftStr);
        leftIndex += leftStr.length();
        int rightIndex = data.indexOf(rightStr);
        if (leftIndex != -1 && rightIndex != -1) {
            return data.substring(leftIndex, rightIndex);
        }
        return null;
    }

    public static byte[] getResourceAsByteArray(Class cl, String name) {
        InputStream inputStream = cl.getResourceAsStream(name);
        byte[] data = null;
        try {
            data = readInputStream(inputStream);
        } catch (IOException e) {
            Log.error(e);
        }
        try {
            inputStream.close();
        } catch (Exception e) {
            Log.error(e);
        }
        return data;
    }

    public static byte[] getResourceAsByteArray(Object o, String name) {
        return getResourceAsByteArray(o.getClass(), name);
    }

    public static boolean saveDataViewToCsv(Vector columnVector, Vector dataRows, String saveFile) {
        boolean state = false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            int columnNum = columnVector.size();
            byte cob = 44;
            byte newLine = 10;
            int rowNum = dataRows.size();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < columnNum - 1; i++) {
                Object valueObject = columnVector.get(i);
                fileOutputStream.write(formatStringByCsv(valueObject.toString()).getBytes());
                fileOutputStream.write(cob);
            }
            Object valueObject = columnVector.get(columnNum - 1);
            fileOutputStream.write(formatStringByCsv(valueObject.toString()).getBytes());
            fileOutputStream.write(newLine);
            for (int i = 0; i < rowNum; i++) {
                Vector row = (Vector) dataRows.get(i);
                for (int j = 0; j < columnNum - 1; j++) {
                    valueObject = row.get(j);
                    fileOutputStream.write(formatStringByCsv(String.valueOf(valueObject)).getBytes());
                    fileOutputStream.write(cob);
                }
                valueObject = row.get(columnNum - 1);
                fileOutputStream.write(formatStringByCsv(String.valueOf(valueObject)).getBytes());
                fileOutputStream.write(newLine);
            }
            fileOutputStream.close();
            state = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    public static String stringToUnicode(String unicode) {
        char[] chars = unicode.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {

            builder.append("\\u");
            String hx = Integer.toString(chars[i], 16);
            if (hx.length() < 4) {
                builder.append("0000".substring(hx.length())).append(hx);
            } else {
                builder.append(hx);
            }
        }
        return builder.toString();
    }

    public static String unicodeToString(String s) {
        String[] split = s.split("\\\\");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith("u")) {
                builder.append((char) Integer.parseInt(split[i].substring(1, 5), 16));
                if (split[i].length() > 5) {
                    builder.append(split[i].substring(5));
                }
            } else {
                builder.append(split[i]);
            }
        }

        return builder.toString();
    }

    public static boolean sleep(int time) {
        boolean state = false;
        try {
            Thread.sleep(time);
            state = true;
        } catch (InterruptedException e) {
            Log.error(e);
        }
        return state;
    }

    public static String toString(Object object) {
        return (object == null) ? "null" : object.toString();
    }

    public static String getLastFileName(String file) {
        String[] fs = formatDir(file).split("/");
        return fs[fs.length - 1];
    }

    private static String formatStringByCsv(String string) {
        string = string.replace("\"", "\"\"");
        return "\"" + string + "\"";
    }

}
