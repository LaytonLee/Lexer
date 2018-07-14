package com.lexer.analysis;
/**
 * 对输入的sentence进行分析并输出词法分析结果
 * @author 誠
 *
 */

import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import com.lexer.model.ErrorModel;
import com.lexer.model.WordModel;

public class LexicalAnalysis {
	/**
	 * 关键字数组
	 */
	private static final String[] keyword = {
			"boolean", "byte", "char", "double", "false", "float", "int", "long",
            "new", "null", "short", "true", "void", "instanceof", "break",
            "case", "catch", "continue", "default", "do", "else", "for", "if",
            "return", "switch", "try", "while", "finally", "throw", "this",
            "super", "abstract", "final", "namtive", "private", "protected",
            "public", "static", "synchronized", "transient", "volatile",
            "class", "extends", "implements", "interface", "package", "import",
            "throws"
	};
	
	/**
	 * 运算符数组
	 */
	private static final String[] oprator = {
			"+", "-", "*", "/", "<", ">", "=", "&", "|"
	};
	
	private static final String[] delimeter = {
			"{", "}", ",", ";", "(", ")", "[", "]"
	};
	
	/**
	 * 标识符 1
	 * 常数 2
	 * 保留字 3
	 * 运算符 4
	 * 界符 5
	 * 非法字符 -1
	 */
	private static final int IDENTIFIER = 1;
	private static final int NUMBER = 2;
	private static final int KEYWORD = 3;
	private static final int OPRATOR = 4;
	private static final int DELIMETER = 5;
	private static final int ILLEGALCHAR = -1;
	
	/**
	 * 存储标识符的链表
	 */
	List<String> list_identifier = new ArrayList<>();
	
	/**
	 * 存储划分结果的链表
	 */
	List<WordModel> list_result = new ArrayList<>();	
	
	/**
	 * 存储Error信息的链表 
	 */
	List<ErrorModel> list_error = new ArrayList<>();
	
	/**
	 * 分析并打印到控制台
	 * @param sentence
	 */
	public List<Object> wordAnalysis(String sentence, int line){
		List<Object> list = new ArrayList<>();
		//去掉首尾空格
		String sentence_tem = sentence.trim() ;
		//将字符串中的多个空格替换为一个空格
		sentence_tem = sentence_tem.replaceAll(" +", " ");
		//存储切分的单词
		String word = "";
		
		/**
		 * 切分字符串
		 */
		for(int i=0; i<sentence_tem.length(); i++){
			//第i个字符的类型
			int type_charI = wordType(String.valueOf(sentence_tem.charAt(i)));
			//切分的字符串或字符word的类型
			int type_word = wordType(word);
			
			/**
			 * 切分字符串
			 * > 空格
			 * > 运算符，界符
			 * > 非以上两种情况，不进行切分
			 */
			if(sentence_tem.charAt(i) == ' '){
				/**
				 * 空格情况下的切分
				 * > word 含非法字符，存入error链表
				 * > word 不含非法字符，
				 *   > word 非空，存入结果链表
				 */
				if (type_word == ILLEGALCHAR ) {
					ErrorModel error = new ErrorModel();
					error.setLine(line);
					error.setError("\"" + word + "\" invalid charactor");
					list_error.add(error);
				}
				else {
					/**
					 * 若word非空，加入链表，否则不加入链表
					 */
					if (!word.equals("")) {
						WordModel wordModel = new WordModel(type_word, word, line);
						//word 是标识符
						if(type_word == 1){
							list_result.add(wordModel);
							//标识符表中无 word，将word添加到标识符表中
							if(!list_identifier.contains(word)){
								list_identifier.add(word);
							}
						}
						//word 非标识符
						else{
							list_result.add(wordModel);
						}
						
					}
				}
				
				//重置word
				word = "";
				
			}
			else if (type_charI > 3) {
				/**
				 * > 有//， 过滤//注释
				 * > 无//， 存链表 
				 * 	 word 含非法字符
				 *     > 存入error链表
				 *   word 不含非法字符
				 *     > word为空，只需将运算符加入结果链表
				 *     > word非空，将word和运算符均加如链表
				 */
				//注释
				if(sentence_tem.charAt(i)=='/' && sentence_tem.charAt(i+1)=='/'){
					/**
					 * 将结果链表、标识符链表、error链表添加到对象链表
					 * > 0 list_result
					 * > 1 list_identifier
					 * > 2 list_error
					 */
					list.add(list_result);
					list.add(list_identifier);
					list.add(list_error);
					
					/**
					 * return 对象链表 
					 */
					return list;
				}
				//非法字符
				if (type_word == ILLEGALCHAR ) {
					ErrorModel error = new ErrorModel();
					error.setLine(line);
					error.setError("\"" + word + "\" invalid charactor");
					list_error.add(error);
				}
				//word 为空
				else if(word.equals("")){
					list_result.add(new WordModel(type_charI, String.valueOf(sentence_tem.charAt(i)), line));
				}
				//word 非空
				else{
					WordModel wordModel_word = new WordModel(type_word, word, line);
					WordModel wordModel_notWord = new WordModel
							(type_charI, String.valueOf(sentence_tem.charAt(i)), line);
					
					/**
					 * > word 是标识符
					 * > word 非标识符
					 */
					if(type_word == 1){
						list_result.add(wordModel_word);
						//标识符表中无 word，将word添加到标识符表中
						if(!list_identifier.contains(word)){
							list_identifier.add(word);
						}
					}
					else{
						list_result.add(wordModel_word);
					}
					list_result.add(wordModel_notWord);
				}
				//重置word
				word = "";
			}
			/**
			 * 没有遇到运算符、界符、空格，持续切分字符串
			 */
			else{
				word += sentence_tem.charAt(i);
			}
			
		}
		
		/**
		 * 将结果链表、标识符链表、error链表添加到对象链表
		 * > 0 list_result
		 * > 1 list_identifier
		 * > 2 list_error
		 */
		list.add(list_result);
		list.add(list_identifier);
		list.add(list_error);
		
		/**
		 * return 对象链表 
		 */
		return list;
	}
	
	
	
	/**
	 * 将分析结果打印到控制台
	 */
	public void printResult(List<Object> list){
		List<WordModel> list_result_print = (List<WordModel>)list.get(0);
		List<String> list_identifier_print = (List<String>)list.get(1);
		List<ErrorModel> list_error_print = (List<ErrorModel>)list.get(2);
		//判空
		if(list_result_print == null || list_result_print.size() == 0){
			System.out.println("Error: 程序代码为空");
		} 
		else{
			//输出程序错误
			if (list_error_print != null && list_error_print.size() != 0) {
				System.out.println("Error: ");
				for (int i = 0; i < list_error_print.size(); i++) {
					System.out.println(list_error_print.get(i).toString());
				}
			}
			//输出词法分析结果
			for (int i = 0; i < list_result_print.size(); i++) {
				WordModel wordModel = list_result_print.get(i);
				if(wordModel.getType() == IDENTIFIER){
					System.out.println(wordModel.toString() + "  //标识符入口为：" + 
							list_identifier_print.indexOf(wordModel.getWord()));
				}
				else{
					System.out.println(wordModel.toString());
				}
			}
		}	
	}
	
	/**
	 * 判断字符类型
	 * @param word
	 * @return
	 */
	public int wordType(String word){
		int type = 0;  //无可识别类型
		
		/**
         * 判断是否为标识符, 是则返回 IDENTIFIER = 1;
         * 注：这里应先将标识符的判断放于关键字判断之前，避免关键字
         * 被判断为标识符
         */
        Pattern pattern_identifier = Pattern.compile("^[A-Za-z_$]+[A-Za-z_$\\d]+$|[A-Za-z_$]");
        Matcher isIdentifier = pattern_identifier.matcher(word);
        if( isIdentifier.matches()){
        	type = IDENTIFIER;
        }
        
        /**
         * 判断是否为保留字，是则返回 KEYWORD = 3
         */
        List<String> list_keyword = Arrays.asList(keyword);
        
        if(list_keyword.contains(word)){
        	type = KEYWORD;
        }
        
        /**
         * 判断是否为运算符，是则返回 OPRATOR = 4
         */
        List<String> list_oprator = Arrays.asList(oprator);
        if(list_oprator.contains(word)){
        	type = OPRATOR;
        }
        
        /**
         * 判断是否为界符，是则返回 DELIMETER = 5
         */
        List<String> list_delimeter = Arrays.asList(delimeter);
        if(list_delimeter.contains(word)){
        	type = DELIMETER;
        } 
        
        /**
         * 检测非法字符，若包含非法字符，ILLEGALCHAR = -1
         * 注：将此判断至于数字判断之前，避免误判
         */
        Pattern pattern_illegalChar = Pattern.compile(".*[`~!@#$%^&*(){}':;',\\[\\].<>/?~！"
        		+ "@#￥%……&*（）——+|{}【】‘；：”“’。，、？].*");
        Matcher isIllegalChar = pattern_illegalChar.matcher(word);
        if (word.length()>1 && isIllegalChar.matches()) {
			type = ILLEGALCHAR;
		}
        
        /**
		 * 判断是否为数字(含小数，负数)，是则返回 NUMBER = 2
		 */
		Pattern pattern_number = Pattern.compile("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?");
        Matcher isNum = pattern_number.matcher(word);
        if( isNum.matches()){
        	type = NUMBER;
        }
        
        return type;
	}
	  
	//main 函数
	public static void main(String[] args){
		LexicalAnalysis leAnalysis = new LexicalAnalysis();
		//System.out.println(leAnalysis.wordType("-456.051$346"));
		leAnalysis.printResult(leAnalysis.wordAnalysis("if(i > 3){", 1));;
	}
}









