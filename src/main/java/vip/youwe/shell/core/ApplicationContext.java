package vip.youwe.shell.core;

import vip.youwe.shell.core.annotation.CryptionAnnotation;
import vip.youwe.shell.core.annotation.PayloadAnnotation;
import vip.youwe.shell.core.annotation.PluginnAnnotation;
import vip.youwe.shell.core.imp.Cryption;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.core.imp.Plugin;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.http.Http;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ApplicationContext {

    public static final String VERSION = "1.00";
    private static Map<String, Class<?>> payloadMap;
    private static Map<String, Class<?>> cryptionMap;
    private static Map<String, Class<?>> pluginMap;
    private static File[] pluginJarFiles;
    public static int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int windowsHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static boolean easterEgg = true;

    private static Font font;
    private static Map<String, String> headerMap;

    static {
        payloadMap = new HashMap();
        cryptionMap = new HashMap();
        pluginMap = new HashMap();
    }

    public static void init() {
        initFont();
        initHttpHeader();
        scanPluginJar();
        scanPayload();
        scanCryption();
        scanPlugin();
    }

    private static void initFont() {
        String fontName = Db.getSetingValue("font-name");
        String fontType = Db.getSetingValue("font-type");
        String fontSize = Db.getSetingValue("font-size");
        if (fontName != null && fontType != null && fontSize != null) {
            font = new Font(fontName, Integer.parseInt(fontType), Integer.parseInt(fontSize));
            InitGlobalFont(font);
        }
    }

    private static void initHttpHeader() {
        String headerString = getGloballHttpHeader();
        if (headerString != null) {
            String[] reqLines = headerString.split("\n");

            headerMap = new Hashtable();
            for (int i = 0; i < reqLines.length; i++) {
                if (!reqLines[i].trim().isEmpty()) {
                    int index = reqLines[i].indexOf(":");
                    if (index > 1) {
                        String keyName = reqLines[i].substring(0, index).trim();
                        String keyValue = reqLines[i].substring(index + 1, reqLines[i].length()).trim();
                        headerMap.put(keyName, keyValue);
                    }
                }
            }
        }
    }

    private static void scanPayload() {
        try {
            // URL url = ApplicationContext.class.getResource("/shells/payloads/");
            URL url = ApplicationContext.class.getResource("/vip/youwe/shell/shells/payloads/");
            // int loadNum = scanClass(url.toURI(), "shells.payloads", Payload.class, PayloadAnnotation.class, payloadMap);
            int loadNum = scanClass(url.toURI(), "vip.youwe.shell.shells.payloads", Payload.class, PayloadAnnotation.class, payloadMap);
            Log.log(String.format("load payload success! payloadMaxNum:%s onceLoadPayloadNum:%s", payloadMap.size(), loadNum));
        } catch (Exception e) {
            Log.error(e);
        }
    }

    private static void scanCryption() {
        try {
            URL url = ApplicationContext.class.getResource("/vip/youwe/shell/shells/cryptions/");
            int loadNum = scanClass(url.toURI(), "vip.youwe.shell.shells.cryptions", Cryption.class, CryptionAnnotation.class, cryptionMap);
            Log.log(String.format("load cryption success! cryptionMaxNum:%s onceLoadCryptionNum:%s", cryptionMap.size(), loadNum));
        } catch (Exception e) {
            Log.error(e);
        }
    }

    private static void scanPlugin() {
        try {
            URL url = ApplicationContext.class.getResource("/vip/youwe/shell/shells/plugins/");
            int loadNum = scanClass(url.toURI(), "vip.youwe.shell.shells.plugins", Plugin.class, PluginnAnnotation.class, pluginMap);
            Log.log(String.format("load plugin success! pluginMaxNum:%s onceLoadPluginNum:%s", pluginMap.size(), loadNum));
        } catch (Exception e) {
            Log.error(e);
        }
    }

    private static void scanPluginJar() {
        String[] pluginJars = Db.getAllPlugin();
        ArrayList list = new ArrayList();

        for (int i = 0; i < pluginJars.length; i++) {
            File jarFile = new File(pluginJars[i]);
            if (jarFile.exists() && jarFile.isFile()) {
                addJar(jarFile);
                list.add(jarFile);
            } else {
                Log.error(String.format("PluginJarFile : %s no found", pluginJars[i]));
            }
        }
        pluginJarFiles = (File[]) list.toArray(new File[0]);
        Log.log(String.format("load pluginJar success! pluginJarNum:%s LoadPluginJarSuccessNum:%s", pluginJars.length, pluginJars.length));
    }

    private static int scanClass(URI uri, String packageName, Class<?> parentClass, Class<?> annotationClass, Map<String, Class<?>> destMap) {
        int num = scanClassX(uri, packageName, parentClass, annotationClass, destMap);

        for (int i = 0; i < pluginJarFiles.length; i++) {
            File jarFile = pluginJarFiles[i];
            num += scanClassByJar(jarFile, packageName, parentClass, annotationClass, destMap);
        }

        return num;
    }

    private static int scanClassX(URI uri, String packageName, Class<?> parentClass, Class<?> annotationClass, Map<String, Class<?>> destMap) {
        if (System.getProperty("java.class.path").endsWith(".jar")
                && System.getProperty("java.class.path").indexOf(';') == -1) {
            return scanClassByJar(new File(System.getProperty("java.class.path")), packageName, parentClass, annotationClass, destMap);
        }
        int addNum = 0;
        try {
            File file = new File(uri);
            File[] file2 = file.listFiles();

            for (int i = 0; i < file2.length; i++) {
                File objectFile = file2[i];
                if (objectFile.isDirectory()) {
                    File[] objectFiles = objectFile.listFiles();
                    for (int j = 0; j < objectFiles.length; j++) {
                        File objectClassFile = objectFiles[j];
                        if (objectClassFile.getPath().endsWith(".class")) {
                            try {
                                String objectClassName = String.format("%s.%s.%s", packageName, objectFile.getName(), objectClassFile.getName().substring(0, objectClassFile.getName().length() - ".class".length()));
                                Class objectClass = Class.forName(objectClassName);
                                if (parentClass.isAssignableFrom(objectClass) && objectClass.isAnnotationPresent(annotationClass)) {
                                    Annotation annotation = objectClass.getAnnotation(annotationClass);
                                    String name = (String) annotation.annotationType().getMethod("Name", new Class[0]).invoke(annotation, null);
                                    destMap.put(name, objectClass);
                                    addNum++;
                                }
                            } catch (Exception e) {
                                Log.error(e);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.error(e);
        }
        return addNum;
    }

    private static int scanClassByJar(File srcJarFile, String packageName, Class<?> parentClass, Class<?> annotationClass, Map<String, Class<?>> destMap) {
        int addNum = 0;
        try {
            JarFile jarFile = new JarFile(srcJarFile);
            Enumeration<JarEntry> jarFiles = jarFile.entries();


            packageName = packageName.replace(".", "/").substring(0);
            while (jarFiles.hasMoreElements()) {
                JarEntry jarEntry = jarFiles.nextElement();
                String name = jarEntry.getName();
                if (name.startsWith(packageName) && name.endsWith(".class")) {
                    name = name.replace("/", ".");
                    name = name.substring(0, name.length() - 6);
                    try {
                        Class objectClass = Class.forName(name);
                        if (parentClass.isAssignableFrom(objectClass) && objectClass.isAnnotationPresent(annotationClass)) {
                            Annotation annotation = objectClass.getAnnotation(annotationClass);
                            name = (String) annotation.annotationType().getMethod("Name").invoke(annotation, null);
                            destMap.put(name, objectClass);
                            addNum++;
                        }
                    } catch (Exception e) {
                        Log.error(e);
                    }
                }
            }

            jarFile.close();
        } catch (Exception e) {
            Log.error(e);
        }
        return addNum;
    }

    public static String[] getAllPayload() {
        Set<String>keys = payloadMap.keySet();
        return keys.toArray(new String[0]);
    }

    public static Payload getPayload(String payloadName) {
        Class<?> payloadClass = payloadMap.get(payloadName);
        Payload payload = null;
        if (payloadClass != null) {
            try {
                payload = (Payload) payloadClass.newInstance();
            } catch (Exception e) {
                Log.error(e);
            }
        }
        return payload;
    }

    public static Plugin[] getAllPlugin(String payloadName) {
        Iterator<String> keys = pluginMap.keySet().iterator();
        ArrayList<Plugin> list = new ArrayList<Plugin>();


        while (keys.hasNext()) {
            String cryptionName = keys.next();
            Class<?> pluginClass = pluginMap.get(cryptionName);
            if (pluginClass != null) {
                PluginnAnnotation pluginAnnotation = pluginClass.getAnnotation(PluginnAnnotation.class);
                if (pluginAnnotation.payloadName().equals(payloadName)) {
                    try {
                        Plugin plugin = (Plugin) pluginClass.newInstance();
                        list.add(plugin);
                    } catch (Exception e) {
                        Log.error(e);
                    }
                }
            }
        }

        return list.toArray(new Plugin[0]);
    }

    public static String[] getAllCryption(String payloadName) {
        Iterator<String> keys = cryptionMap.keySet().iterator();
        ArrayList<String> list = new ArrayList<String>();

        while (keys.hasNext()) {
            String cryptionName = keys.next();
            Class<?> cryptionClass = cryptionMap.get(cryptionName);
            if (cryptionClass != null) {
                CryptionAnnotation cryptionAnnotation = cryptionClass.getAnnotation(CryptionAnnotation.class);
                if (cryptionAnnotation.payloadName().equals(payloadName)) {
                    list.add(cryptionName);
                }
            }
        }

        return list.toArray(new String[0]);
    }

    public static Cryption getCryption(String payloadName, String crytionName) {
        Class<?> cryptionClass = cryptionMap.get(crytionName);
        if (cryptionMap != null) {
            CryptionAnnotation cryptionAnnotation = cryptionClass.getAnnotation(CryptionAnnotation.class);
            if (cryptionAnnotation.payloadName().equals(payloadName)) {
                Cryption cryption = null;
                try {
                    return (Cryption) cryptionClass.newInstance();
                } catch (Exception e) {
                    Log.error(e);
                    return null;
                }
            }
        }
        return null;
    }

    private static void addJar(File jarPath) {
        try {
            URLClassLoader classLoader = (URLClassLoader) ApplicationContext.class.getClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            URL url = jarPath.toURI().toURL();
            method.invoke(classLoader, url);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    public static Proxy getProxy(ShellEntity shellContext) {
        return ProxyT.getProxy(shellContext);
    }

    public static String[] getAllProxy() {
        return ProxyT.getAllProxyType();
    }

    public static String[] getAllEncodingTypes() {
        return Encoding.getAllEncodingTypes();
    }

    public static Http getHttp(ShellEntity shellEntity) {
        Http httpx = new Http(shellEntity);
        return httpx;
    }

    public static Font getFont() {
        return font;
    }

    public static void setFont(Font font) {
        Db.updateSetingKV("font-name", font.getName());
        Db.updateSetingKV("font-type", Integer.toString(font.getStyle()));
        Db.updateSetingKV("font-size", Integer.toString(font.getSize()));
        ApplicationContext.font = font;
    }

    public static void resetFont() {
        Db.removeSetingK("font-name");
        Db.removeSetingK("font-type");
        Db.removeSetingK("font-size");
    }

    public static String getGloballHttpHeader() {
        return Db.getSetingValue("globallHttpHeader");
    }

    public static Map<String, String> getGloballHttpHeaderX() {
        return headerMap;
    }

    public static boolean updateGloballHttpHeader(String header) {
        boolean state = Db.updateSetingKV("globallHttpHeader", header);
        initHttpHeader();
        return state;
    }

    public static boolean isGodMode() {
        return Boolean.valueOf(Db.getSetingValue("godMode"));
    }

    public static boolean setGodMode(boolean state) {
        return Db.updateSetingKV("godMode", String.valueOf(state));
    }
}
