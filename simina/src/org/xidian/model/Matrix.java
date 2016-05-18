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
	public static void printMatrix(int[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j< matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * 矩阵复制
	 * @param startRow 
	 * @param startCol
	 * @param endRow
	 * @param endCol
	 * @param matrix
	 * @return
	 */
	public static int[][] copyMatrix(int startRow, int startCol, int endRow, int endCol, int[][] matrix) {
		int[][] temMatrix = new int[endRow-startRow][endCol-startCol];
		for(int i = startRow; i < endRow; i++) {
			for(int j = startCol; j < endCol; i++){
				temMatrix[i-startRow][j-startCol] = matrix[i][j];
			}
		}
		return temMatrix;
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
