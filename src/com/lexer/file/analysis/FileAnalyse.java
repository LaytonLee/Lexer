package com.lexer.file.analysis;
/**
 * 文件操作
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.List;

import com.lexer.analysis.LexicalAnalysis;
import com.lexer.model.ErrorModel;
import com.lexer.model.WordModel;
public class FileAnalyse {
	/**
	 * 按行从文件中读取 
	 * @param path
	 */
	public List<Object> fileAnalyse(String path){
		List<Object> list = null;
		LexicalAnalysis lexer = new LexicalAnalysis();	
		//读入文件
		FileInputStream file = null;
		BufferedReader bufferedReader = null;
		
		try {
			file = new FileInputStream(path);
			bufferedReader = new BufferedReader(new InputStreamReader(file, "utf-8"));
			String string;
			int line = 1;
			while((string = bufferedReader.readLine()) != null){
				//System.out.println("line" + line + ": " + string);
				list = lexer.wordAnalysis(string,line);
				line++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 将分析结果写入文件
	 * @param path
	 */
	public void FileAnalyseWrite(String path, List<Object> list){
		
		//System.out.println(list.size());
		
		/**
		 * 获取结果、标识符、error链表
		 */
		List<WordModel> list_result = (List<WordModel>)list.get(0);
		List<String> list_identifier = (List<String>)list.get(1);
		List<ErrorModel> list_error = (List<ErrorModel>)list.get(2);
		
		//System.out.println(list_result.size());
		//System.out.println(list_result.get(0).toString());
		
		FileOutputStream outputStream = null;
		BufferedWriter filewriter = null;
		
		/**
		 * 将链表中的数据写入文件
		 * > 文件不存在，创建文件
		 * > 文件存在，写入分析结果
		 */
		try {
			 File file = new File(path);
			 if (!file.exists()) {
				 file.createNewFile();
			}
			 outputStream = new FileOutputStream(file);
			 filewriter = new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));
			 /**
			  * 打印错误
			  */
			 if(list_error != null && list_error.size() != 0){
				 //控制台输出
				 System.out.println("Error:");
				 filewriter.write("ERROR:");
				 filewriter.flush();
				 filewriter.newLine();
				 for(int i=0; i<list_error.size(); i++){
					 //控制台
					 System.out.println(list_error.get(i).toString());
					 //文件
					 filewriter.write(list_error.get(i).toString());
					 //换行
					 filewriter.newLine();
				 }
			 }
			 
			 /**
			  * 打印词法分析结果
			  * > result链表为空，无处理结果
			  * > result链表不空，向文件打印结果
			  */
			 if(list_result.size() == 0){
				 filewriter.write("Error: 程序代码为空，无分析结果");
			 }
			 else{
				 for(int i=0; i<list_result.size(); i++){
					 WordModel wordModel = list_result.get(i);
					 //输出到控制台
					 System.out.println(wordModel.toString());
					 /**
					  * 将分析的结果写入问价
					  * > 写入分析结果
					  * > 若为标识符，写入标识符入口
					  */
					 filewriter.write(wordModel.toString());
					 /**
					  * 必须要有flush方法用以刷新缓冲流
					  * 需要注意flush方法的位置，否则可能导致数据输出不全
					  */
					 filewriter.flush();
					 if(wordModel.getType() == 1){
						 //写入标识符入口
						 filewriter.write("  //标识符入口为：" + list_identifier.indexOf(wordModel.getWord()));
					 }
					 //换行
					 filewriter.newLine();
				 }
			 }
			 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			/**
			 * 关闭输入流
			 */
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args){
		FileAnalyse fileAnalyse = new FileAnalyse();
		fileAnalyse.fileAnalyse("E://logs//example_lexer.txt");
		String path = "E://logs//a.txt";
		fileAnalyse.FileAnalyseWrite(path, fileAnalyse.fileAnalyse("E://logs//example_lexer.txt"));
		
	}
}
