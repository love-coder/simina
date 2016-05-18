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
	 * 
	 */
	public Matrix() {
		
	}
	
	/**
	 * @param i place个数
	 * @param j token个数
	 * @param matrixName 矩阵名称
	 */
	public Matrix(int rows, int cols, String matrixName) {
		matrix = new int[rows][cols];
		this.matrixName = matrixName;
	}
	
	//for debug 
	public void printMatrix(int[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j< matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
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
