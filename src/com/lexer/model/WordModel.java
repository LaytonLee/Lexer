package com.lexer.model;
/**
 * 词的bean类
 * @author 誠
 *
 */
public class WordModel {
	private int type;
	private String word;
	private int line;
	
	/**
	 * 构造方法
	 * @return
	 */
	public WordModel(){
		
	}
	
	public WordModel(int type, String word){
		this.type = type;
		this.word = word;
	}
	
	public WordModel(int type, String word, int line){
		this.type = type;
		this.word = word;
		this.line = line;
	}
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "line" + this.line + ": ( " + this.word + " , " + this.type + " )";
	}
	
	
}
