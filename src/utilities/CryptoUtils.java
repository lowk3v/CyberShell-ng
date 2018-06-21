/**
 * 
 */
package utilities;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author kevinlpd
 *
 */
public class CryptoUtils {
	private String rot13(String str) {
		String result = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            result += c;
        }
        System.out.println(result);
		return result;
	}
	
	private String base64_encode(String plaintext) {
		try {
			return Base64.getEncoder().encodeToString(plaintext.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String base64_decode(String cipher) {
		try {
			return new String(Base64.getDecoder().decode(cipher.getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String encrypt(String plaintext) {
		return this.rot13(this.base64_encode(plaintext));
	}
	
	public String decrypt(String cipher) {
		return this.base64_decode(this.rot13(cipher));
	}
}
