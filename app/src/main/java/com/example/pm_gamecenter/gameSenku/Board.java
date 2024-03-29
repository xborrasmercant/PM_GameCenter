package com.example.pm_gamecenter.gameSenku;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.pm_gamecenter.R;

import androidx.core.content.ContextCompat;

public class Board extends GridLayout {
    private MoveListener moveListener;
    private Cell[][] boardMatrix;
    private int[][] lastBoardStatus;
    private Cell selectedCell;
    private int[][] borders = new int[][]{ {0, 0}, {0, 1}, {1, 0}, {1, 1}, {0, 5}, {0, 6}, {1, 5}, {1, 6}, {5, 0}, {5, 1}, {6, 0}, {6, 1}, {5, 5}, {5, 6}, {6, 5}, {6, 6}}; // Cords to avoid checking borders of matrix
    public enum Direction { // Movement directions
        UP, DOWN, RIGHT, LEFT
    }

    public enum Status { // Current status of the board
        LOST, WIN, PLAYABLE
    }

    // CONSTRUCTOR
    public Board(Context context) {
        super(context);
        boardMatrix = new Cell[7][7];
        lastBoardStatus = new int[7][7];
        initBoard();
        copyMatrixContent(boardMatrix, lastBoardStatus);
    }

    // GAME STATUS Methods

    public Status getBoardStatus() {
        int pegCount = 0;

        for (Cell[] row : boardMatrix) {
            for (Cell cell : row) {
                if (cell.getValue() > 0 ) {
                    if (canMove(cell)) {return Status.PLAYABLE;};
                    pegCount++;
                }
            }
        }

        if (pegCount == 1) {return Status.WIN;}

        return Status.LOST;
    }
    public boolean canMove(Cell cell) {
        if (insideBounds(cell.getRowPos()+1, cell.getColPos(), cell.getRowPos()+2, cell.getColPos()) && boardMatrix[cell.getRowPos()+2][cell.getColPos()].getValue() == 0) { // Try down
            return true;
        } else if (insideBounds(cell.getRowPos(), cell.getColPos()+1, cell.getRowPos(), cell.getColPos()+2) && boardMatrix[cell.getRowPos()][cell.getColPos()+2].getValue() == 0) { // Try up
            return true;
        } else if (insideBounds(cell.getRowPos()-1, cell.getColPos(), cell.getRowPos()-2, cell.getColPos()) && boardMatrix[cell.getRowPos()-2][cell.getColPos()].getValue() == 0) { // Try left
            return true;
        } else if (insideBounds(cell.getRowPos(), cell.getColPos()-1, cell.getRowPos(), cell.getColPos()-2) && boardMatrix[cell.getRowPos()][cell.getColPos()-2].getValue() == 0) { // Try right
            return true;
        }

        return false;
    }
    private boolean insideBounds(int middleCellRow, int middleCellCol, int targetCellRow, int targetCellCol) {
        // Check the bounds for both the middle cell and the target cell
        if (middleCellRow < 0 || middleCellRow >= boardMatrix.length || middleCellCol < 0 || middleCellCol >= boardMatrix[0].length ||
                targetCellRow < 0 || targetCellRow >= boardMatrix.length || targetCellCol < 0 || targetCellCol >= boardMatrix[0].length) {
            return false;
        }

        return boardMatrix[middleCellRow][middleCellCol].getValue() == 1 && boardMatrix[targetCellRow][targetCellCol].getValue() == 0;
    }


    // MOVEMENT Methods
    public void handleMovement(Cell selectedCell, Cell targetCell, Direction movementDirection) {
        int midCellRow, midCellCol;

        copyMatrixContent(boardMatrix, lastBoardStatus);

        // Get mid cell positions
        switch (movementDirection) {
            case DOWN:
                midCellRow = selectedCell.getRowPos()+1;
                midCellCol = selectedCell.getColPos();
                break;
            case UP:
                midCellRow = selectedCell.getRowPos()-1;
                midCellCol = selectedCell.getColPos();
                break;
            case RIGHT:
                midCellRow = selectedCell.getRowPos();
                midCellCol = selectedCell.getColPos()+1;
                break;
            case LEFT:
                midCellRow = selectedCell.getRowPos();
                midCellCol = selectedCell.getColPos()-1;
                break;
            default:
                midCellRow = 0;
                midCellCol = 0;
                break;
        }

        // Handle movement
        if (targetCell.getValue() == 0 && boardMatrix[midCellRow][midCellCol].getValue() == 1) {
            applyMovement(selectedCell, targetCell, midCellRow, midCellCol);
        }
    }
    public void applyMovement(Cell selectedCell, Cell targetCell, int row, int col) {
        selectedCell.updateCell(0);
        boardMatrix[row][col].updateCell(0); // Empty cell between selected and target cells
        targetCell.updateCell(1);

        // Call onMoveMade() to increment score
        if (moveListener != null) {
            moveListener.onMoveMade();
        }
    }
    public Direction getDirection(Cell selectedCell, Cell targetCell) {
        // Check the difference result of selected and target pegs.
        int dRow = selectedCell.getRowPos() - targetCell.getRowPos(),
            dCol = selectedCell.getColPos() - targetCell.getColPos();

        // Check that target cell is in valid range.
        if (dRow > 2 || dRow < -2 || dCol > 2 || dCol < -2) {
            return null;
        }

        if (dCol == 0) { // Making sure that delta col is 0 to prevent diagonal movement
            switch (dRow) {
                case -2: return Direction.DOWN;
                case 2: return Direction.UP;
            }
        }

        if (dRow == 0) { // Making sure that delta row is 0 to prevent diagonal movement
            switch (dCol) {
                case -2: return Direction.RIGHT;
                case 2: return Direction.LEFT;
            }
        }
        return null;
    }

    // INITIALIZATION Methods
    public void initBoard() {
        // Fill with pegs
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                Cell cell = new Cell(getContext(), col, row, 1);
                boardMatrix[row][col] = cell;

                GridLayout.LayoutParams params = createGridLayoutParams(row, col);
                addView(boardMatrix[row][col], params); // Add to Layout
            }
        }

        // Define borders of boardMatrix
        for (int[] pos : borders) {
            boardMatrix[pos[0]][pos[1]].updateCell(-1);
        }

        // Set the empty position of the board (center)
        boardMatrix[3][3].updateCell(0);
    }
    public void undoBoard(){
        for (int row = 0; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {
                boardMatrix[row][col].updateCell(lastBoardStatus[row][col]);
            }
        }
    }
    public void resetBoard(){
        for (Cell[] row : boardMatrix) {
            for (Cell c : row) {
                c.updateCell(1);
            }
        }

        // Define borders of boardMatrix
        for (int[] pos : borders) {
            boardMatrix[pos[0]][pos[1]].updateCell(-1);
        }

        // Set the empty position of the board (center)
        boardMatrix[3][3].updateCell(0);
    }

    public void copyMatrixContent(Cell[][] sourceMatrix, int[][] targetMatrix) {
        for (int row = 0; row < sourceMatrix.length ; row++) {
            for (int col = 0; col < sourceMatrix[row].length ; col++) {
                targetMatrix[row][col] = sourceMatrix[row][col].getValue();
            }
        }
    }

    public GridLayout.LayoutParams createGridLayoutParams(int row, int col) {

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(row);
        params.columnSpec = GridLayout.spec(col);

        return params;
    }

    // GETTERS and SETTERS
    public void setMoveListener(MoveListener listener) {
        this.moveListener = listener;
    }

    public Cell[][] getBoardMatrix() {
        return boardMatrix;
    }
    public void setBoardMatrix(Cell[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
    }
    public int[][] getBorders() {
        return borders;
    }
    public void setBorders(int[][] borders) {
        this.borders = borders;
    }

    public MoveListener getMoveListener() {
        return moveListener;
    }

    public int[][] getLastBoardStatus() {
        return lastBoardStatus;
    }

    public void setLastBoardStatus(int[][] lastBoardStatus) {
        this.lastBoardStatus = lastBoardStatus;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(Cell selectedCell) {
        this.selectedCell = selectedCell;
    }
}

