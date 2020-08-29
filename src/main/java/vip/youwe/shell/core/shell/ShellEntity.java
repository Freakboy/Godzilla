package vip.youwe.shell.core.shell;

import vip.youwe.shell.core.ApplicationContext;
import vip.youwe.shell.core.imp.Cryption;
import vip.youwe.shell.core.imp.Payload;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.functions;
import vip.youwe.shell.utils.http.Http;

import java.awt.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class ShellEntity {

    private String url = "";
    private String password = "";
    private String secretKey = "";
    private String payload = "";
    private String cryption = "";
    private String remark = "";
    private String encoding = "";
    private Map<String, String> headers = new HashMap();
    private String reqLeft = "";
    private String reqRight = "";
    private int connTimeout = 60000;
    private int readTimeout = 60000;
    private String proxyType = "";
    private String proxyHost = "";
    private Cryption cryptionModel;
    private int proxyPort = 8888;
    private Payload payloadModel;
    private String id = "";
    private Frame frame;
    private Http http;
    private boolean isSendLRReqData;

    public boolean initShellOpertion() {
        boolean state = false;
        try {
            this.http = ApplicationContext.getHttp(this);
            this.payloadModel = ApplicationContext.getPayload(this.payload);
            this.cryptionModel = ApplicationContext.getCryption(this.payload, this.cryption);
            this.cryptionModel.init(this);
            if (this.cryptionModel.check()) {
                this.payloadModel.init(this);
                if (this.payloadModel.test()) {
                    state = true;
                } else {
                    Log.error("payload Initialize Fail !");
                }
            } else {
                Log.error("cryption Initialize Fail !");
            }

        } catch (Exception e) {
            Log.error(e);
            return state;
        }
        return state;
    }

    public Http getHttp() {
        return this.http;
    }


    public Cryption getCryptionModel() {
        return this.cryptionModel;
    }


    public Payload getPayloadModel() {
        return this.payloadModel;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getSecretKeyX() {
        return functions.md5(this.getSecretKey()).substring(0, 16);
    }

    public String getPayload() {
        return this.payload;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public String getProxyType() {
        return this.proxyType;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public String getCryption() {
        return this.cryption;
    }

    public void setCryption(String cryption) {
        this.cryption = cryption;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setPayload(String Payload) {
        this.payload = Payload;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getConnTimeout() {
        return this.connTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getHeaderS() {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = this.headers.keySet().iterator();


        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = this.headers.get(key);
            builder.append(key);
            builder.append(": ");
            builder.append(value);
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Frame getFrame() {
        return this.frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public void setHeader(String reqString) {
        if (reqString != null) {
            String[] reqLines = reqString.split("\n");
            this.headers = new Hashtable();
            for (int i = 0; i < reqLines.length; i++) {
                if (!reqLines[i].trim().isEmpty()) {
                    int index = reqLines[i].indexOf(":");
                    if (index > 1) {
                        String keyName = reqLines[i].substring(0, index).trim();
                        String keyValue = reqLines[i].substring(index + 1, reqLines[i].length()).trim();
                        this.headers.put(keyName, keyValue);
                    }
                }
            }
        }
    }

    public String getReqLeft() {
        return this.reqLeft;
    }

    public void setReqLeft(String reqLeft) {
        this.reqLeft = reqLeft;
    }

    public String getReqRight() {
        return this.reqRight;
    }

    public void setReqRight(String reqRight) {
        this.reqRight = reqRight;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSendLRReqData() {
        return this.cryptionModel.isSendRLData();
    }
}
