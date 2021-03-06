package org.xidian.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.xidian.alg.BaseData;
import org.xidian.model.Marking;
import org.xidian.model.Matrix;
import org.xidian.model.PetriModel;
import org.xidian.model.StateNode;
import org.xidian.model.Transition;

/**
 * 加载 PN model
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class LoadModel {
	
	static BaseData baseData;

	static int[][] preMatrix;	//前置矩阵
	static int[][] posMatrix;	//后置矩阵
	static List<Integer> iniMarking;  //初始marking
	
	static int defaultTranCount = 1000; //默认最大变迁数量为1000
	static int trueMaxTran = 0; //实际最大的变迁编号
	
	static String filePath; //文件位置
	
	/**
	 * @param filePath 文件路径
	 */
	public static void loadResource(String filePath) {
		//1.读文件
		String resource = FileUtil.read(filePath, null);
		Pattern pattern = Pattern.compile("\r|\n");
    	String[] strs = pattern.split(resource);
    	preMatrix = new int[strs.length-2][defaultTranCount];
    	posMatrix = new int[strs.length-2][defaultTranCount];
    	iniMarking = new LinkedList<Integer>();
        //2.解析
    	for(int i = 1; i < strs.length; i++) {
    		if(strs[i].equals("@")) break;
    		parseModelLine(strs[i]);
    	}
    	//3.得到模型
    	preMatrix = Matrix.copyMatrix(0, 0, strs.length-2, trueMaxTran, preMatrix);
    	posMatrix = Matrix.copyMatrix(0, 0, strs.length-2, trueMaxTran, posMatrix);
    	
    	int[] transation = new int[trueMaxTran];
    	for(int i = 0; i<transation.length;i++) {
    		transation[i] = i; 
    	}
    	int[] marking = new int[iniMarking.size()];
    	for(int i = 0; i<iniMarking.size();i++) {
    		marking[i] = iniMarking.get(i); 
    	}
    	//4.清空已存在基础模型数据
		if(baseData != null) {
			clearBaseData();
		}
    	//初始化变量
    	new PetriModel(new Matrix(preMatrix, "preMatrix"), new Matrix(posMatrix, "posMatrix"), new Transition(transation), new Marking(marking));
	}
	
	/**
	 * 解析模型
	 * @param str pnt文件的一行
	 */
	public static void parseModelLine(String str){
		if(str==""||str==null) return;
		String[] strArr = str.split(","); 
		//前置矩阵
		String preStr = strArr[0].replaceAll("\\s{1,}", " ").trim();
		String[] preArr = preStr.split(" ");
		iniMarking.add(Integer.parseInt(preArr[1]));
		for(int i = 2; i < preArr.length; i++) {
			if(preArr[i].contains(":")) {
				String[] tem = preArr[i].split(":");
				if((Integer.parseInt(tem[0]))>trueMaxTran) trueMaxTran = Integer.parseInt(tem[0]);
				preMatrix[Integer.parseInt(preArr[0])-1][Integer.parseInt(tem[0])-1] = Integer.parseInt(tem[1]);
			}else{
				if(Integer.parseInt(preArr[i])>trueMaxTran) trueMaxTran = Integer.parseInt(preArr[i]);
				preMatrix[Integer.parseInt(preArr[0])-1][Integer.parseInt(preArr[i])-1] = 1;
			}
		}
		//后置矩阵
		String posStr = strArr[1].replaceAll("\\s{1,}", " ").trim();
		String[] posArr = posStr.split(" ");
		for(int i = 0; i < posArr.length; i++) {
			if(posArr[i] == null) continue;
			if(posArr[i].contains(":")) {
				String[] tem2 = posArr[i].split(":");
				if((Integer.parseInt(tem2[0]))>trueMaxTran) trueMaxTran = Integer.parseInt(tem2[0]);
				posMatrix[Integer.parseInt(preArr[0])-1][Integer.parseInt(tem2[0])-1] = Integer.parseInt(tem2[1]);
			}else{
				if(Integer.parseInt(posArr[i])>trueMaxTran) trueMaxTran = Integer.parseInt(posArr[i]);
				posMatrix[Integer.parseInt(preArr[0])-1][Integer.parseInt(posArr[i])-1] = 1;
			}
		}
	}
	
	/**
	 * 为了解决编号超过1位数情况
	 * @param charArray
	 * @return
	 */
	public static String[] parseCharToStringArr(char[] charArray){
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
	
	public static void clearBaseData() {
		BaseData.deadStates = null;
		BaseData.graphModel = null;
		BaseData.grapht = null;
		BaseData.rootState = new StateNode(PetriModel.ininmarking.getMarking(), 1, 1);
	}
	
}
