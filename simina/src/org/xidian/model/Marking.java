package org.xidian.model;

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
		 int total = 0;
		 for (int offset = 0; offset < marking.length; offset++) {
			 total = (2 * total);
			 for (int index = 0; index < (marking.length - offset); index++) {
				total += marking[index];
			 }
		 }
		 //处理hashcode过大的情况，
		 if (total < 0) {
			 total = Integer.MAX_VALUE + total;
		 }      
		 return total;
	}

}
