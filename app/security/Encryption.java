package security;

import org.apache.commons.codec.binary.Base64;

// TODO Use real encryption
public class Encryption {

    public static String encode(String original) {
        String result = null;
        if (original != null) {
            result = new String(Base64.encodeBase64(original.getBytes()));
        }
        return result;
    }

    public static String decode(String original) {
        String result = null;
        if (original != null) {
            result = new String(Base64.decodeBase64(original.getBytes()));
        }
        return result;
    }
}
