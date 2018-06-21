/**
 * 
 */
package models;

import java.util.Arrays;
import java.util.Map;

/**
 * @author kevinlpd
 *
 */
public class BinaryModel extends Model{
	private Integer id;
	private String name, binary, program_lang, os;

	public String get_binary(String command) {
		Map<String, Object> result = this.select_one("Binaries",
				Arrays.asList(
						Arrays.asList("name", "=", "'" + command + "'")
				));
		return (String) result.get("binary");
	}
	
	
	/*
	 * GETTERS AND SETTERS
	 */	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBinary() {
		return binary;
	}
	public void setBinary(String binary) {
		this.binary = binary;
	}
	public String getProgram_lang() {
		return program_lang;
	}
	public void setProgram_lang(String program_lang) {
		this.program_lang = program_lang;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	
	
}
