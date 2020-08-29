package vip.youwe.shell.utils.http;

import vip.youwe.shell.core.shell.ShellEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private byte[] result;
    private ShellEntity shellEntity;
    private Map<String, List<String>> headerMap;
    private String message;

    public byte[] getResult() {
        return this.result;
    }

    public Map<String, List<String>> getHeaderMap() {
        return this.headerMap;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public void setHeaderMap(Map<String, List<String>> headerMap) {
        this.headerMap = headerMap;
    }

    public HttpResponse(HttpURLConnection http, ShellEntity shellEntity) throws IOException {
        this.shellEntity = shellEntity;
        handleHeader(http.getHeaderFields());
        ReadAllData(getInputStream(http));
    }

    protected void handleHeader(Map<String, List<String>> map) {
        this.headerMap = map;
        this.message = map.get(null).get(0);
        List<String> cookieList = map.getOrDefault("Set-Cookie", new ArrayList());
        StringBuffer buffer = new StringBuffer();
        for (String cookieValue : cookieList) {
            String[] _cookie = cookieValue.split(";");
            buffer.append(_cookie[0]);
            buffer.append(";");
        }
        if (buffer.length() > 1) {
            String oldCookie = this.shellEntity.getHeaders().getOrDefault("Cookie", "");
            this.shellEntity.getHeaders().put("Cookie", ((oldCookie.trim().length() > 0) ? oldCookie : "") + buffer.toString());
        }
    }

    protected InputStream getInputStream(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = httpURLConnection.getErrorStream();
        if (inputStream != null) {
            return inputStream;
        }
        return httpURLConnection.getInputStream();
    }

    protected void ReadAllData(InputStream inputStream) throws IOException {
        int maxLen = 0;
        try {
            if (this.headerMap.get("Content-Length") != null
                    && this.headerMap.get("Content-Length").size() > 0) {
                maxLen = Integer.parseInt(this.headerMap.get("Content-Length").get(0));
                this.result = ReadKnownNumData(inputStream, maxLen);
            } else {
                this.result = ReadUnknownNumData(inputStream);
            }
        } catch (NumberFormatException e) {
            this.result = ReadUnknownNumData(inputStream);
        }
        this.result = this.shellEntity.getCryptionModel().decode(this.result);
    }

    protected byte[] ReadKnownNumData(InputStream inputStream, int num) throws IOException {
        if (num > 0) {
            byte[] temp = new byte[5120];
            int readOneNum = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((readOneNum = inputStream.read(temp)) != -1) {
                bos.write(temp, 0, readOneNum);
            }
            return bos.toByteArray();
        }
        if (num == 0) {
            return ReadUnknownNumData(inputStream);
        }
        return null;
    }

    protected byte[] ReadUnknownNumData(InputStream inputStream) throws IOException {
        byte[] temp = new byte[5120];
        int readOneNum = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((readOneNum = inputStream.read(temp)) != -1) {
            bos.write(temp, 0, readOneNum);
        }
        return bos.toByteArray();
    }
}
