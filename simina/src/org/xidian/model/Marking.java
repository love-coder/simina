package org.xidian.model;

/**
 * marking 类
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class Marking {
	
	private int[] initialMarking;
	private int[] currentMarking;
	
	public Marking(int placeCount) {
		initialMarking = new int[placeCount];
		currentMarking = new int[placeCount];
	}
	
	public Marking(int[] initialMarking) {
		this.initialMarking = initialMarking;
		this.currentMarking = initialMarking;
	}

	public int[] getInitialMarking() {
		return initialMarking;
	}

	public void setInitialMarking(int[] initialMarking) {
		this.initialMarking = initialMarking;
	}

	public int[] getCurrentMarking() {
		return currentMarking;
	}

	public void setCurrentMarking(int[] currentMarking) {
		this.currentMarking = currentMarking;
	}

}
