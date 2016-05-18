package org.xidian.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.xidian.model.Matrix;
import org.xidian.model.PetriModel;

/**
 * 加载model
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class LoadModel {

	int[][] preMatrix;	//前置矩阵
	int[][] posMatrix;	//后置矩阵
	List<Integer> iniMarking;  //初始marking
	
	int defaultTranCount = 1000; //默认最大变迁数量为1000
	int trueMaxTran = 0; //实际最大的变迁编号
	
	/**
	 * @param filePath 文件路径
	 */
	public void loadResource(String filePath) {
		//1.读文件
		String resource = FileUtil.read(filePath, null);
		Pattern pattern = Pattern.compile("\r|\n");
    	String[] strs = pattern.split(resource);
    	preMatrix = new int[strs.length-2][defaultTranCount];
    	posMatrix = new int[strs.length-2][defaultTranCount];
        //2.解析
    	for(int i = 1; i < strs.length; i++) {
    		parseModelLine(strs[i]);
    	}
    	//3.得到模型
    	preMatrix = Matrix.copyMatrix(0, 0, strs.length-3, trueMaxTran, preMatrix);
    	posMatrix = Matrix.copyMatrix(0, 0, strs.length-3, trueMaxTran, posMatrix);
    	
    	Matrix.printMatrix(preMatrix);
    	
    	
    	
	}
	
//	@Test
//	public void testS(){
//		parseModelLine("");
//	}
	
	/**
	 * 解析模型
	 * @param str pnt文件的一行
	 */
	public void parseModelLine(String str){
		System.out.println("正在解析##" + str);
		
		String[] strArr = str.split(","); 
		String preStr = strArr[0].replaceAll("\\s{1,}", " ");
		String posStr = strArr[1].replaceAll("\\s{1,}", " ") + " ";
		
		iniMarking = new LinkedList<Integer>();
		char[] preCharArr = preStr.toCharArray();
		
		//String[] preStringArry =
		
		//初始矩阵
		iniMarking.add((int)preCharArr[2]-49);
		//得到前置矩阵
		for(int i = 4; i < preCharArr.length; ){
			if((i+1)>(preCharArr.length-1)) break;
			if(preCharArr[i] != ' '){
				//更新最大变迁数
				if((int)preCharArr[i]-49 > trueMaxTran) {
					trueMaxTran = (int)preCharArr[i]-49; 
				}
				//弧权值为1的情况
				if(preCharArr[i+1] !=':') {
					preMatrix[(int)preCharArr[0]-49][(int)preCharArr[i]-49] = 1;
					if(preMatrix.length>=(i+2)) {
						i += 2;
					}
				//不为1的情况
				}else{
					preMatrix[(int)preCharArr[0]-49][(int)preCharArr[i]-49] = (int)preCharArr[i+2]-48;
					if(preMatrix.length>=(i+4)) {
						i += 4;
					}
				}
			}
		}
		
		char[] posCharArr = posStr.toCharArray();
		
		//得到后置矩阵
		for(int i = 0; i < posCharArr.length; ){
			if((i+1)>(posCharArr.length-1)) break;
			if(posCharArr[i] != ' '){
				//更新最大变迁数
				if((int)posCharArr[i]-49 > trueMaxTran) {
					trueMaxTran = (int)posCharArr[i]-49; 
				}
				//弧权值为1的情况
				if(posCharArr[i+1] !=':') {
					posMatrix[(int)preCharArr[0]-49][(int)posCharArr[i]-49] = 1;
					if(posMatrix.length>=(i+2)) {
						i += 2;
					}
				//不为1的情况
				}else{
					posMatrix[(int)preCharArr[0]-49][(int)posCharArr[i]-49] = (int)posCharArr[i+2]-48;
					if(posMatrix.length>=(i+4)) {
						i += 4;
					}
				}
			}else{
				i++;
			}
		}
		//System.out.println("####"+ trueMaxTran);
	}
	
	/**
	 * 为了解决编号超过1位数情况
	 * @param charArray
	 * @return
	 */
	public String[] parseCharToStringArr(char[] charArray){
		List<String> temList =  new LinkedList<String>();
		
		for(int i = 0; i < charArray.length;) {
			if(i+1 > charArray.length-1) break;
			//有连续数字情况
			if(48 <= charArray[i+1] && charArray[i+1] <= 57) {
				StringBuffer sb = null;
				while(48 <= charArray[i+1] && charArray[i+1] <= 57) {
					sb = new StringBuffer();
					sb.append(String.valueOf(charArray[i]));
					i++;
				}
				temList.add(sb.toString());
			}else{
				temList.add(String.valueOf(charArray[i]));
				i++;
			}
		}
		return (String[]) temList.toArray();
	}
	
	@Test
	public void testMedel(){
		
		String s = "1 2 98 56 1 ";
		parseCharToStringArr(s.toCharArray());
		System.out.println();
		
	}
	
	
	
//	@Test
//	public void testMedel(){
//		loadResource(Constant.rootPath+"resources"+File.separator+"test.pnt");
//	}
	
}
