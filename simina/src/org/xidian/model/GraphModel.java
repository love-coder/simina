package org.xidian.model;

/**
 * petri model完全转化为 graph model
 * @author HanChun
 * @since 2016-6-4
 * @version 1.0
 */
public class GraphModel {
	
	public Matrix costMatrix; //邻接矩阵
	public Matrix arcMatrix;  //弧即变迁名称，如2:t2
	public int statesAmount;
	
	/**
	 * @param statesAmount 节点个数
	 */
	public GraphModel(int statesAmount) {
		this.statesAmount = statesAmount;
		costMatrix = new Matrix(statesAmount, statesAmount, "costMatrix");
		arcMatrix = new Matrix(statesAmount, statesAmount, "arcMatrix", true);
	}

	public Matrix getCostMatrix() {
		return costMatrix;
	}

	public void setCostMatrix(Matrix costMatrix) {
		this.costMatrix = costMatrix;
	}

	public Matrix getArcMatrix() {
		return arcMatrix;
	}

	public void setArcMatrix(Matrix arcMatrix) {
		this.arcMatrix = arcMatrix;
	}

	public int getStatesAmount() {
		return statesAmount;
	}

	public void setStatesAmount(int statesAmount) {
		this.statesAmount = statesAmount;
	}
	
}
