/**
 * @author christianmartindale
 */

package src.society;

import src.cell.Cell;
import src.cell.CellShape;
import src.cell.location.Location;
import src.cell.state.SegregationState;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Christian Martindale
 * Refactored by Justin Wang
 */

public class SegregationSociety extends Society {

    public SegregationSociety(int row, int col, List<Cell> cellList, CellShape cellShape) {
        super(row, col, cellList, cellShape);
    }

    /**
     * Updates the next states of all cells, then moves cells accordingly
     * @param societyGrid grid of cells
     */
    @Override
    public void updateNextState(Grid societyGrid){

        super.updateNextState(societyGrid);
        Map<Location, Cell> emptyCells = getEmptyCells(societyGrid);

        List<Location> tempSet = new ArrayList<>(societyGrid.getGridLocations());
        Collections.shuffle(tempSet);

        for(Location xLocation : tempSet){

            //Collections.shuffle(tempSet);
            //tempSet.remove(xLocation);
            Cell tempCell = societyGrid.getCell(xLocation);

            if(this.willMove(tempCell) && emptyCells.size() > 0){

                List<Location> emptyCellLocations = new ArrayList<Location>(emptyCells.keySet());
                Collections.shuffle(emptyCellLocations);
                int randomIndex = ThreadLocalRandom.current().nextInt(emptyCellLocations.size());
                Location randomLocation = emptyCellLocations.get(randomIndex);

                Cell newCell = emptyCells.remove(randomLocation);
                newCell.setNextState(tempCell.getCurrentState());

                societyGrid.setCell(randomLocation, newCell);

            }

            else if(this.willMove(tempCell) && emptyCells.size() <= 0){
                tempCell.setNextState(tempCell.getCurrentState());
                societyGrid.setCell(xLocation, tempCell);
            }

        }

    }

    /**
     * Checks to see if the cell is in the process of moving
     * @param xCell cell to check
     * @return true if cell is moving; false otherwise
     */
    public boolean willMove(Cell xCell){
        return (!xCell.isCurrentEqual(SegregationState.EMPTY) && xCell.isNextEqual(SegregationState.EMPTY));
    }

    /**
     * Retrieves a map of all empty cell locations
     * @param societyGrid
     * @return
     */
    public Map<Location, Cell> getEmptyCells(Grid societyGrid){
        return societyGrid.getCellsOfType(SegregationState.EMPTY);
    }

}