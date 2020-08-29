package vip.youwe.shell.core;

import vip.youwe.shell.core.ui.component.dialog.ImageShowDialog;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.functions;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig {

    private static final String GITEE_CONFIG_URL = "https://gitee.com/beichendram/Godzilla/raw/master/application.config";
    private static final String GIT_CONFIG_URL = "https://raw.githubusercontent.com/BeichenDream/Godzilla/master/application.config";
    public static final String GIT = "https://github.com/BeichenDream/Godzilla";
    private static final Map<String, String> headers = new HashMap();

    static {
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
    }

    public static void invoke() {
        Map<String, String> configMap = null;

        try {
            configMap = getAppConfig(GITEE_CONFIG_URL);
        } catch (Exception e) {
            try {
                configMap = getAppConfig(GIT_CONFIG_URL);
            } catch (Exception e2) {
                Log.error("Network connection failure");
            }
        }
        if (configMap != null && configMap.size() > 0) {
            String version = configMap.get("currentVersion");
            boolean isShowGroup = Boolean.valueOf(configMap.get("isShowGroup"));
            // todo 不显示群组
            isShowGroup = false;
            String wxGroupImageUrl = configMap.get("wxGroupImageUrl");
            String showGroupTitle = configMap.get("showGroupTitle");
            String gitUrl = configMap.get("gitUrl");
            boolean isShowAppTip = Boolean.valueOf(configMap.get("isShowAppTip"));
            String appTip = configMap.get("appTip");
            if (version != null && wxGroupImageUrl != null && appTip != null && gitUrl != null) {
                if (functions.stringToint(version.replace(".", "")) > functions.stringToint("1.00".replace(".", ""))) {
                    JOptionPane.showMessageDialog(null, String.format("新版本已经发布\n当前版本:%s\n最新版本:%s", "1.00", version), "message", 2);
                    functions.openBrowseUrl(gitUrl);
                }

                if (isShowAppTip) {
                    JOptionPane.showMessageDialog(null, appTip, "message", 1);
                }

                if (isShowGroup) {
                    try {
                        ImageIcon imageIcon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(functions.httpReqest(wxGroupImageUrl, "GET", headers, null))));
                        ImageShowDialog.showImageDiaolog(imageIcon, showGroupTitle);
                    } catch (IOException e) {
                        Log.error(e);
                        Log.error("showGroup fail!");
                    }
                }
            }
        }
    }

    private static Map<String, String> getAppConfig(String configUrl) throws Exception {
        byte[] result = functions.httpReqest(configUrl, "GET", headers, null);
        if (result == null) {
            throw new Exception("readApplication Fail!");
        }
        String configString;
        try {
            configString = new String(result, "utf-8");
        } catch (UnsupportedEncodingException e) {
            configString = new String(result);
        }
        Map<String, String> hashMap = new HashMap<>();
        String[] lines = configString.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int index = line.indexOf(':');
            if (index != -1) {
                hashMap.put(line.substring(0, index).trim(), line.substring(index + 1).trim());
            }
        }
        return hashMap;
    }
}
