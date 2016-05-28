package org.xidian.model;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * 变迁类
 * @author HanChun
 * @version 1.0 2016-5-16
 */
@SuppressWarnings("serial")
public class Transition extends DefaultWeightedEdge{
	
	private int[] Transition;

	private String tranName; 
	
	public Transition() {
		
	}

	public Transition(int[] transition) {
		Transition = transition;
	}

	public int[] getTransition() {
		return Transition;
	}

	public void setTransition(int[] transition) {
		Transition = transition;
	}

	public String getTranName() {
		return tranName;
	}

	public void setTranName(String tranName) {
		this.tranName = tranName;
	}

	@Override
	public String toString() {
		return tranName;
	}

}
