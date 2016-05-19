package org.xidian.model;

/**
 * petri net 数学模型
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class PetriModel {
	
	private Matrix preMatrix; //前置矩阵
	private Matrix posMatrix; //后置矩阵
	private Transition transition; //变迁
	private Marking ininmarking; //初始marking
	public int transCount, placesCount;
	
	public PetriModel(Matrix preMatrix, Matrix posMatrix, Transition transition, Marking ininmarking) {
		this.preMatrix = preMatrix;
		this.posMatrix = posMatrix;
		this.transition = transition;
		this.ininmarking = ininmarking;
		transCount = transition.getTransition().length;
		placesCount = ininmarking.getMarking().length;
	}

	public Matrix getPreMatrix() {
		return preMatrix;
	}

	public void setPreMatrix(Matrix preMatrix) {
		this.preMatrix = preMatrix;
	}

	public Matrix getPosMatrix() {
		return posMatrix;
	}

	public void setPosMatrix(Matrix posMatrix) {
		this.posMatrix = posMatrix;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	public Marking getIninmarking() {
		return ininmarking;
	}

	public void setIninmarking(Marking ininmarking) {
		this.ininmarking = ininmarking;
	}
	
}
