package org.xidian.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import org.xidian.model.Matrix;

public class TestMain {
	public static void main(String[] args) {
		int[][] pre = new int[10][10];
		int[][] pos = new int[10][10];
		String s = "9       2      8:2 2 , 7:2 1";
		//String s = "8       0      7 , 8";
		String[] strArr = s.split(","); 
		String preStr = strArr[0].replaceAll("\\s{1,}", " ");
		String posStr = strArr[1].replaceAll("\\s{1,}", " ") + " ";
		//9 2 8:2 2 , 7:2 1 
		//9 2 8 2 , 7 1 
		System.out.println("preStr:"+preStr);
		System.out.println("posStr:"+posStr);
		
		List<Integer> ininMarking = new LinkedList<Integer>();
		char[] preCharArr = preStr.toCharArray();
		//初始矩阵
		ininMarking.add((int)preCharArr[2]-49);
		//得到前置矩阵
		for(int i = 4; i < preCharArr.length; ){
			if((i+1)>(preCharArr.length-1)) break;
			if(preCharArr[i] != ' '){
				//弧权值为1的情况
				if(preCharArr[i+1] !=':') {
					pre[(int)preCharArr[0]-49][(int)preCharArr[i]-49] = 1;
					if(pre.length>=(i+2)) {
						i += 2;
					}
				//不为1的情况
				}else{
					pre[(int)preCharArr[0]-49][(int)preCharArr[i]-49] = (int)preCharArr[i+2]-48;
					if(pre.length>=(i+4)) {
						i += 4;
					}
				}
			}
		}
		new Matrix().printMatrix(pre);
		System.out.println();
		char[] posCharArr = posStr.toCharArray();
		
		//得到后置矩阵
		for(int i = 0; i < posCharArr.length; ){
			if((i+1)>(posCharArr.length-1)) break;
			if(posCharArr[i] != ' '){
				//弧权值为1的情况
				if(posCharArr[i+1] !=':') {
					pos[(int)preCharArr[0]-49][(int)posCharArr[i]-49] = 1;
					if(pos.length>=(i+2)) {
						i += 2;
					}
				//不为1的情况
				}else{
					pos[(int)preCharArr[0]-49][(int)posCharArr[i]-49] = (int)posCharArr[i+2]-48;
					if(pos.length>=(i+4)) {
						i += 4;
					}
				}
			}else{
				i++;
			}
		}
		
		new Matrix().printMatrix(pos);
	}
	
	//测试矩阵输出
//	@Test
//	public void testM(){
//		new Matrix().printMatrix(new int[10][10]);;
//	}

}
