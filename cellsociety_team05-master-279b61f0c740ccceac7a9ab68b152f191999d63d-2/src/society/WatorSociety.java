/**
 * @author christianmartindale and Justin Wang
 */

package src.society;

import src.cell.Cell;
import src.cell.CellShape;
import src.cell.WatorCell;
import src.cell.location.Location;
import src.cell.state.WatorState;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Christian Martindale
 * Refactored by Justin Wang
 */

public class WatorSociety extends Society {

    private static final int REMOVE_TURNS = 1;
    private static final int DELTA_ENERGY = 1;

    public WatorSociety(int row, int col, List<Cell> cellList, CellShape cellShape) {
        super(row, col, cellList, cellShape);
    }

    /**
     * Initializes breeding of non-empty cells
     * @param societyGrid grid of cells to check
     */
    public void initBreed(Grid societyGrid){

        for(Location xLocation : societyGrid.getGridLocations()){

            WatorCell tempCell = ((WatorCell)societyGrid.getCell(xLocation));

            if(tempCell.canBreed()){

                Map<Location, Cell> emptyNeighbors = this.getNeighborCellsOfType(societyGrid, xLocation, WatorState.EMPTY);

                this.moveToNeighborCell(societyGrid, tempCell, emptyNeighbors, true);

                tempCell.resetBreed();

            }

        }

    }

    @Override
    public void updateNextState(Grid societyGrid){

        super.updateNextState(societyGrid);
        this.initBreed(societyGrid);
        this.updateCellAttributes(societyGrid);

        for(Location xLocation : societyGrid.getGridLocations()){

            WatorCell tempCell = ((WatorCell) societyGrid.getCell(xLocation));

            if(!tempCell.isEmpty()) {

                if (tempCell.isDead()) {
                    tempCell.setEmpty();
                    //societyGrid.setCell(xLocation, tempCell);
                    continue;

                }

                Map<Location, Cell> neighborMap = this.getNeighborCellsOfType(societyGrid, xLocation, WatorState.EMPTY);

                if (tempCell.isCurrentEqual(WatorState.PREDATOR)) {
                    neighborMap.putAll(this.getNeighborCellsOfType(societyGrid, xLocation, WatorState.PREY));
                }

                tempCell.setNextState(tempCell.getCurrentState());
                this.moveToNeighborCell(societyGrid, tempCell, neighborMap, false);

                if(neighborMap.size() > 0){
                    tempCell.setEmpty();
                    //societyGrid.setCell(xLocation, tempCell);
                }

            }

        }

    }

    /*Helper Methods*/

    /**
     * 
     * @param societyGrid the Grid representing a Society
     * @param xCell a Cell to be moved
     * @param neighborMap key is location, value is the Cell
     * @param isBreed true if the animal can breed, false otherwise
     */
    private void moveToNeighborCell(Grid societyGrid, WatorCell xCell, Map<Location, Cell> neighborMap, boolean isBreed) {

        if(neighborMap.size() > 0){

            List<Location> emptyCellLocations = new ArrayList<Location>(neighborMap.keySet());
            int randIndex = ThreadLocalRandom.current().nextInt(neighborMap.size());
            Location randomAdjacentLocation = emptyCellLocations.get(randIndex);

            WatorCell tempCell = (WatorCell) societyGrid.getCell(randomAdjacentLocation);

            if(tempCell.isCurrentEqual(WatorState.PREY) && xCell.isCurrentEqual(WatorState.PREDATOR)){
                xCell.addEnergy(this.DELTA_ENERGY);
            }

            if(isBreed){
                xCell.resetStats();
            }

            tempCell.moveCell(xCell);
            //societyGrid.setCell(randomAdjacentLocation, xCell);

        }

    }

    /**
     * Returns a collection of empty cells neighboring a specified cell
     * @param societyGrid grid of cells to check
     * @param cellLocation location of the cell to check
     * @return map of empty cell neighbors
     */
    private Map<Location, Cell> getNeighborCellsOfType(Grid societyGrid, Location cellLocation, WatorState checkState){

        Map<Location, Cell> emptyNeighbors = societyGrid.getNeighborCellMap(cellLocation);

        Iterator mapIterator = emptyNeighbors.entrySet().iterator();
        while(mapIterator.hasNext()){
            Map.Entry<Location, Cell> mapEntry = ((Map.Entry<Location, Cell>) mapIterator.next());
            Location xLocation = mapEntry.getKey();

            if(!mapEntry.getValue().isCurrentEqual(checkState)){
                mapIterator.remove();
            }
        }

        return emptyNeighbors;

    }

    /**
     * Updates the energy and breedtime attributes of cells in the grid
     * @param societyGrid grid of cells to update
     */
    private void updateCellAttributes(Grid societyGrid){

        for(Location xLocation : societyGrid.getGridLocations()){

            WatorCell tempCell = ((WatorCell)societyGrid.getCell(xLocation));

            if(tempCell.isCurrentEqual(WatorState.PREDATOR) || tempCell.isCurrentEqual(WatorState.PREY)){
                tempCell.removeTurnsToBreed(this.REMOVE_TURNS);
            }

            if(tempCell.isCurrentEqual(WatorState.PREDATOR)){
                tempCell.removeEnergy(this.DELTA_ENERGY);
            }

        }

    }

}