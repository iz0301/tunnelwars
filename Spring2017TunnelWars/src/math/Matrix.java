package math;

/**
 * Defines a class for a 2D matrix and matrix math
 * @author Isaac Zachmann
 *
 */
//TODO: add determinant and inverse functions
public class Matrix {
	
	private double[][] self;
	
	/**
	 * Creates a new matrix with the specified number of rows and columns. All values default to 0.
	 * @param rows the number of rows in the matrix
	 * @param columns the number of columns in the matrix
	 */
	public Matrix(int rows, int columns){
		self = new double[rows][columns];
	}
	
	/**
	 * Creates a new matrix from the two dimensional array, rows first then columns 
	 * @param matrix A rectangular two dimensional array
	 */
	public Matrix(double[][] matrix){
		int numOfColumns = 0;
		for(int i = 0; i < matrix.length; i++){
			Math.max(numOfColumns, matrix[i].length);
		}
		self = new double[matrix.length][numOfColumns];
		for(int r = 0; r < matrix.length; r++){
			for(int c = 0; c < matrix[r].length; c++){
				try {
					self[r][c] = matrix[r][c];
				} catch (IndexOutOfBoundsException e){
					self[r][c] = 0;
				}
			}
		}
	}
	
	/**
	 * Returns the value at the specified position of the matrix
	 * @param row the row of the value to return
	 * @param column the column of the value to return
	 * @return the value at the row and column
	 * @throws IndexOutOfBoundsException of there is no item at the location
	 */
	public double getItemAt(int row, int column){
		return self[row][column];
	}
	
	/**
	 * Sets a value at a specific position in the matrix
	 * @param value the value to set
	 * @param row the row of the value to set
	 * @param column the column of the value to set
	 * @throws IndexOutOfBoundsException if there is no item at the location
	 */
	public void setItemAt(double value, int row, int column){
		self[row][column] = value;
	}
	
	/**
	 * Gets the number of columns in the matrix
	 * @return the number of columns
	 */
	public int getNumberOfColumns(){
		return self[0].length;
	}
	
	/**
	 * Gets the number of rows in the matrix
	 * @return the number of rows
	 */
	public int getNumberOfRows(){
		return self.length;
	}
	
	/**
	 * Adds the two matrices and returns the result
	 * @param matrixA the first matrix to add
	 * @param matrixB the second matrix to add
	 * @return the result matrix. Null if matrices cannot be added
	 */
	public static Matrix addMatrices(Matrix matrixA, Matrix matrixB){
		if(matrixA.getNumberOfRows() == matrixB.getNumberOfRows() &&
				matrixA.getNumberOfColumns() == matrixB.getNumberOfColumns()){
			Matrix result = new Matrix(matrixA.getNumberOfRows(), matrixA.getNumberOfColumns());
			for(int r = 0; r < matrixA.getNumberOfRows(); r++){
				for(int c = 0; c < matrixA.getNumberOfColumns(); c++){
					result.setItemAt(matrixA.getItemAt(r, c)+matrixB.getItemAt(r, c), r, c);
				}
			}
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * Subtracts the two matrices and returns the result
	 * @param matrixA the first matrix
	 * @param matrixB the second matrix
	 * @return the result matrix. Null if matrices cannot be subtracted
	 */
	public static Matrix subtractMatrices(Matrix matrixA, Matrix matrixB){
		if(matrixA.getNumberOfRows() == matrixB.getNumberOfRows() &&
				matrixA.getNumberOfColumns() == matrixB.getNumberOfColumns()){
			Matrix result = new Matrix(matrixA.getNumberOfRows(), matrixA.getNumberOfColumns());
			for(int r = 0; r < matrixA.getNumberOfRows(); r++){
				for(int c = 0; c < matrixA.getNumberOfColumns(); c++){
					result.setItemAt(matrixA.getItemAt(r, c)-matrixB.getItemAt(r, c), r, c);
				}
			}
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * Multiplies the specified matrix by a scalar
	 * @param matrix the matrix to be scaled
	 * @param scalar the scalar
	 * @return the result matrix
	 */
	public static Matrix multiplyMatrixByScalar(Matrix matrix, double scalar){
		Matrix product = new Matrix(matrix.getNumberOfRows(), matrix.getNumberOfColumns());
		for(int r = 0; r < matrix.getNumberOfRows(); r++){
			for(int c = 0; c < matrix.getNumberOfColumns(); c++){
				product.setItemAt(matrix.getItemAt(r, c)*scalar, r, c);
			}
		}
		return product;
	}
	
	/**
	 * Multiplies the two matrices. Order matters.
	 * @param matrixA the first matrix
	 * @param metrixB the second matrix
	 * @return the result matrix, null if matrices cannot be multiplied
	 */
	public static Matrix multiplyMatrices(Matrix matrixA, Matrix matrixB){
		if(matrixA.getNumberOfColumns() == matrixB.getNumberOfRows()){
			Matrix product = new Matrix(matrixA.getNumberOfRows(), matrixB.getNumberOfColumns());
			for(int ar = 0; ar < matrixA.getNumberOfRows(); ar++){
				for(int bc = 0; bc < matrixB.getNumberOfColumns(); bc++){
					double value = 0;
					for(int acbr = 0; acbr < matrixA.getNumberOfColumns(); acbr++){
						value += matrixA.getItemAt(ar, acbr)*matrixB.getItemAt(acbr, bc);
					}
					product.setItemAt(value, ar, bc);
				}
			}
			return product;
		} else {
			return null;
		}
	}
	
	/**
	 * Transposes the matrix and returns the result. Transpose switches the rows and columns
	 * @param matrix the matrix to transpose
	 * @return the transposition of the matrix
	 */
	public static Matrix transposeMatrix(Matrix matrix){
		Matrix result = new Matrix(matrix.getNumberOfColumns(), matrix.getNumberOfRows());
		for(int r = 0; r < matrix.getNumberOfRows(); r++){
			for(int c = 0; c < matrix.getNumberOfColumns(); c++){
				result.setItemAt(matrix.getItemAt(r, c), c, r);
			}
		}
		return result;
	}

	/**
	 * Returns the identity matrix with the specified number of rows/columns 
	 * @param size the number of rows and columns
	 * @return the identity matrix
	 */
	public static Matrix getIdentityMatrix(int size){
		Matrix result = new Matrix(size, size);
		for(int r = 0; r < size; r++){
			for(int c = 0; c < size; c++){
				if(r == c){
					result.setItemAt(1, r, c);
				} else {
					result.setItemAt(0, r, c);
				}
			}
		}
		return result;
	}
}