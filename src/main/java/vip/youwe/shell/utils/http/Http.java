package vip.youwe.shell.utils.http;

import vip.youwe.shell.core.ApplicationContext;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.functions;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.Iterator;
import java.util.Map;

public class Http {

    private Proxy proxy;
    private ShellEntity shellContext;

    public Http(ShellEntity shellContext) {
        this.shellContext = shellContext;
        this.proxy = ApplicationContext.getProxy(this.shellContext);
    }

    static {
        trustAllHttpsCertificates();
    }

    public HttpResponse SendHttpConn(String urlString, String method, Map<String, String> header, byte[] requestData, int connTimeOut, int readTimeOut, Proxy proxy) {
        try {
            URL url = new URL(urlString);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection(proxy);
            if (urlString.indexOf("https://") != -1) {
                ((HttpsURLConnection) httpConn).setHostnameVerifier(new TrustAnyHostnameVerifier());
            }
            httpConn.setDoInput(true);
            httpConn.setDoOutput(!"GET".equals(method.toUpperCase()));
            if (connTimeOut > 0) {
                httpConn.setConnectTimeout(connTimeOut);
            }
            if (readTimeOut > 0) {
                httpConn.setReadTimeout(readTimeOut);
            }
            httpConn.setRequestMethod(method.toUpperCase());
            addHttpHeader(httpConn, ApplicationContext.getGloballHttpHeaderX());
            addHttpHeader(httpConn, header);
            if (httpConn.getDoOutput()) {
                httpConn.getOutputStream().write(requestData);
            }

            return new HttpResponse(httpConn, this.shellContext);
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

    public HttpResponse sendHttpResponse(Map<String, String> header, byte[] requestData, int connTimeOut, int readTimeOut) {
        requestData = this.shellContext.getCryptionModel().encode(requestData);
        if (this.shellContext.isSendLRReqData()) {
            byte[] leftData = this.shellContext.getReqLeft().getBytes();
            byte[] rightData = this.shellContext.getReqRight().getBytes();
            requestData = (byte[]) functions.concatArrays(functions.concatArrays(leftData, 0, ((leftData.length > 0) ? leftData.length : 1) - 1, requestData, 0, requestData.length - 1), 0, leftData.length + requestData.length - 1, rightData, 0, ((rightData.length > 0) ? rightData.length : 1) - 1);
        }

        return SendHttpConn(this.shellContext.getUrl(), "POST", header, requestData, connTimeOut, readTimeOut, this.proxy);
    }

    public HttpResponse sendHttpResponse(byte[] requestData, int connTimeOut, int readTimeOut) {
        return sendHttpResponse(this.shellContext.getHeaders(), requestData, connTimeOut, readTimeOut);
    }

    public HttpResponse sendHttpResponse(byte[] requestData) {
        return sendHttpResponse(requestData, this.shellContext.getConnTimeout(), this.shellContext.getReadTimeout());
    }

    public static void addHttpHeader(HttpURLConnection connection, Map<String, String> headerMap) {
        if (headerMap != null) {
            Iterator<String> names = headerMap.keySet().iterator();
            String name = "";
            while (names.hasNext()) {
                name = names.next();
                connection.setRequestProperty(name, headerMap.get(name));
            }
        }
    }

    private static void trustAllHttpsCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[1];
            // TrustManager tm = new miTM(null);
            TrustManager tm = new miTM();
            trustAllCerts[0] = tm;
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            SSLContext sc2 = SSLContext.getInstance("TLS");
            sc2.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc2.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class miTM implements TrustManager, X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        private miTM() {
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }
    }

    public class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
