/**
 * 
 */
package utilities;

/**
 * @author Kevinlpd
 *
 */
public class ValidUtils {
	/*
	 * Check user supplied's data for security
	 * PARAMETER:
	 * * input: user suppled's data
	 * * type: in list {email, alpha, numberic}
	 * RETURN:
	 * * True if it is safe
	 * * otherwise, False 
	 */
	public Boolean check(String input, String type){
		String regex = null;
		input = input.trim();
	
		if (input.equals("") || input.isEmpty()){
			return false;
		}
		switch (type) {
			case "email":
				regex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9]{2,4}$";
				break;
			case "alpha":
				regex = "[a-zA-Z0-9._-]+";
				break;
			case "numberic":
				regex = "^[0-9]+$";
				break;
			default:
				System.out.print("1");
				return false;
		}
		if (input.matches(regex)) return true;
		return false;
	}
}
