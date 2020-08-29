package vip.youwe.shell.utils.http;

import vip.youwe.shell.utils.functions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReqParameter {

    Map<String, Object> hashMap = new HashMap();

    public void add(String name, byte[] value) {
        this.hashMap.put(name, value);
    }

    public void add(String name, String value) {
        this.hashMap.put(name, value);
    }

    public Object get(String name) {
        return this.hashMap.get(name);
    }

    public String format() {
        Iterator<String> keys = this.hashMap.keySet().iterator();
        StringBuffer buffer = new StringBuffer();

        while (keys.hasNext()) {
            String key = keys.next();
            buffer.append(key);
            buffer.append("=");
            Object valueObject = this.hashMap.get(key);
            if (valueObject.getClass().isAssignableFrom(byte[].class)) {
                buffer.append(functions.base64Encode((byte[]) valueObject));
            } else {
                buffer.append(functions.base64Encode(((String) valueObject).getBytes()));
            }
            buffer.append("&");
        }
        String temString = buffer.delete(buffer.length() - 1, buffer.length()).toString();
        return temString;
    }
}
