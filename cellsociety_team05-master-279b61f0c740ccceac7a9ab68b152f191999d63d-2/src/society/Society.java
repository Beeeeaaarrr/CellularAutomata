/**
 * @author christianmartindale
 */

package src.society;

import src.cell.Cell;
import src.cell.CellShape;
import src.cell.location.Location;
import src.cell.state.SpreadingFireState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.*;

/**
 * Created by Christian Martindale
 * Refactored by Justin Wang
 */

public abstract class Society {

    private Grid societyGrid;
    double cellScale;

    public Society(int row, int col, List<Cell> cellList, CellShape cellShape) {
        this.societyGrid = new Grid(row, col, cellList, cellShape);
    }

    public void setCellShape(CellShape cellShape){
        this.societyGrid.setCellShape(cellShape);
    }

    /**
     * Returns the grid of cells that represent the society
     * @return grid of cells
     */
    public Grid getSocietyGrid(){
        return this.societyGrid;
    }

    /**
     * Sets the society's grid of cells to another grid
     * @param societyGrid new grid of cells
     */
    public void setSocietyGrid(Grid societyGrid){
        this.societyGrid = societyGrid;
    }

    /**
     * Determines the cells' next states
     * @param societyGrid
     */
    public void updateNextState(Grid societyGrid){

        for(Location xLocation : societyGrid.getGridLocations()){
            List<Cell> neighborCells = societyGrid.getNeighborCellList(xLocation);
            societyGrid.getCell(xLocation).updateState(neighborCells);
        }

    }

    /**
     * Changes current states of all cells to their next states
     * @param societyGrid
     */
    public void advanceNextState(Grid societyGrid){

        for(Location xLocation : societyGrid.getGridLocations()){
            societyGrid.getCell(xLocation).swapStates();
        }

    }

    public void setCellScale(double xFactor) {
        this.cellScale = xFactor;
        this.societyGrid.setCellScale(xFactor);
    }

    public double getCellScale(){
        return (cellScale == 0.0) ? 1 : cellScale;
    }
}
