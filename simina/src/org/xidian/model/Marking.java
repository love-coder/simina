package org.xidian.model;

/**
 * marking 类
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class Marking {
	
	private int[] marking;
	
	public Marking(int[] marking) {
		this.marking = marking;
	}

	public int[] getMarking() {
		return marking;
	}

	public void setMarking(int[] marking) {
		this.marking = marking;
	}

	/**
	 * 判断两状态是否相等，抄写hashcode()方法
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	
	
	

}
