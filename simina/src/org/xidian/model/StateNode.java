package org.xidian.model;

import java.util.List;

/**
 * State状态节点
 * @author HanChun
 * @version 1.0 2016-5-18
 */
public class StateNode {
	
	public State state; //节点
	public List<State> ChildNodes;//孩子状态节点
	int deepth;  //状态深度
	
	public StateNode(State state, List<State> childNodes, int deepth) {
		super();
		this.state = state;
		ChildNodes = childNodes;
		this.deepth = deepth;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public List<State> getChildNodes() {
		return ChildNodes;
	}

	public void setChildNodes(List<State> childNodes) {
		ChildNodes = childNodes;
	}

	public int getDeepth() {
		return deepth;
	}

	public void setDeepth(int deepth) {
		this.deepth = deepth;
	}
	
}
