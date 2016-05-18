package org.xidian.model;

/**
 * petri net 数学模型
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class PetriModel {
	
	public Matrix preMatrix; //前置矩阵
	public Matrix posMatrix; //后置矩阵
	public Place place; //扩所
	public Marking ininmarking; //初始marking
	
	public PetriModel(Matrix preMatrix, Matrix posMatrix, Place place, Marking ininmarking) {
		super();
		this.preMatrix = preMatrix;
		this.posMatrix = posMatrix;
		this.place = place;
		this.ininmarking = ininmarking;
	}
	
	
	
	
	

}
