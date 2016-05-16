package org.xidian.model;

/**
 * petri net 数学模型
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class PetriModel {
	
	public Matrix preMatrix; //前置矩阵
	public Matrix posMatrix; //后置矩阵
	public Place place;
	public Marking marking;
	
	/**
	 * @param placesCount 扩所个数
	 * @param trasCount 变迁个数
	 */
	public PetriModel(int placesCount, int trasCount) {
		preMatrix = new Matrix(placesCount, trasCount, "preMatrix");
		posMatrix = new Matrix(placesCount, trasCount, "posMatrix");
		marking = new Marking(placesCount);
		place = new Place(placesCount);
	} 
	
	
	
	
	
	
	
	
	
	
	

}
