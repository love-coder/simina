package org.xidian.model;

/**
 * ������ز���
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class Matrix {
	
	public int[][] matrix;
	public String matrixName;
	
	/**
	 * @param i place����
	 * @param j token����
	 * @param matrixName ��������
	 */
	public Matrix(int i, int j, String matrixName) {
		matrix = new int[i][j];
		this.matrixName = matrixName;
	}

	/**
	 * ָ��ĳ��Ԫ��ֵΪvalue
	 * @param i
	 * @param j
	 * @param value
	 */
	public void setMatrix(int i, int j, int value) {
		if(matrix != null) {
			matrix[i][j] = value; 
		}
	}
	
	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public String getMatrixName() {
		return matrixName;
	}

	public void setMatrixName(String matrixName) {
		this.matrixName = matrixName;
	}

}
