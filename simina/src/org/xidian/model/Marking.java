package org.xidian.model;

import java.util.Arrays;

/**
 * marking 类
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class Marking {
	
	public int[] marking;
	
	public Marking(int n) {
		marking = new int[n];
	}
	
	public Marking(int[] marking) {
		this.marking = marking;
	}
	
	public int[] getMarking() {
		return marking;
	}

	/**
	  * hashCode()
	  * 计算hashcode，用来比较两个对象是否相等
	  */
	@Override
	 public int hashCode(){
		return  Arrays.hashCode(marking);
	}

}
