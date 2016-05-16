package org.xidian.model;

/**
 * 矩阵相关操作
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class Matrix {
	
	public int[][] matrix;
	public String matrixName;
	
	/**
	 * @param i place个数
	 * @param j token个数
	 * @param matrixName 矩阵名称
	 */
	public Matrix(int i, int j, String matrixName) {
		matrix = new int[i][j];
		this.matrixName = matrixName;
	}

	/**
	 * 指定某个元素值为value
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
