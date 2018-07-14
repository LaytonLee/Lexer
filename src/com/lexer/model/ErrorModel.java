package com.lexer.model;
/**
 * 关于代码error信息的model类
 * @author 誠
 *
 */
public class ErrorModel {
	private int line;
	private String error;
	
	/**
	 * 构造方法
	 */
	public ErrorModel(){
		super();
	}
	
	public ErrorModel(int line, String error){
		this.line = line;
		this.error = error;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "error: " + "line" + this.line + ": " + this.error;
	}
	
	
}
