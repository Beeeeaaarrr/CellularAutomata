/**
 * @author christianmartindale and Justin Wang
 */

package src.society;

import src.cell.Cell;
import src.cell.CellShape;
import src.cell.EdgeCell;
import src.cell.location.Location;
import src.cell.state.State;
import javafx.scene.Group;
import src.society.property.BorderType;

import java.util.*;

/**
 * Created by Christian Martindale
 * Refactored by Justin Wang
 */

public class Grid {

    private Map<Location, Cell> cellMap;
    private CellShape cellShape;

    public Grid(){
        this.cellMap = new HashMap<Location, Cell>();
    }

    public Grid(Map<Location, Cell> cellMap){
        this.setCellMap(cellMap);
    }

    public Grid(int row, int col, List<Cell> cellList, CellShape cellShape){

        this();

        Iterator<Cell> cellIterator = cellList.iterator();

        for(int xRow = 0; xRow < row; xRow ++){
            for(int yCol = 0; yCol < col; yCol ++){
                if(cellIterator.hasNext()){
                    this.cellMap.put(new Location(xRow, yCol), cellIterator.next());
                }
            }
        }

        this.setCellShape(cellShape);

    }

    public CellShape getCellShape(){
        return this.cellShape;
    }

    public Map<Location, Cell> getCellMap(){
        return this.cellMap;
    }

    public void setCellMap(Map<Location, Cell> cellMap){
        this.cellMap = cellMap;
    }

    public void setCellShape(CellShape cellShape){

        this.cellShape = cellShape;

        for(Cell xCell : this.cellMap.values()){
            xCell.setCellShape(cellShape);
        }

        this.renderCells();

    }

    public Cell getCell(Location cellLocation){

        try{
            return this.cellMap.get(cellLocation);
        }
        catch(Exception E){
            return null;
        }

    }

    public void setCell(Location cellLocation, Cell newCell){
        this.cellMap.put(cellLocation, newCell);
    }

    public boolean contains(Location cellLocation){
        return cellMap.containsKey(cellLocation);
    }

    public List<Cell> getCellsFromLocations(List<Location> locationList){

        List<Cell> cellList = new ArrayList<Cell>();

        for(Location cellLocation : locationList){

            if(this.contains(cellLocation)){
                cellList.add(this.cellMap.get(cellLocation));
            }

        }

        cellList.removeAll(Collections.singleton(null)); // ensure list does not contain any extraneous null nodes

        return cellList;

    }

    /**
     * Retrieves the neighboring cells at a given location
     * @param cellLocation location of cell being checked
     * @return list of cells surrounding the location being checked
     */
    public List<Cell> getNeighborCellList(Location cellLocation) {
        return new ArrayList<>(this.getNeighborCellMap(cellLocation).values());
    }

    public Map<Location, Cell> getNeighborCellMap(Location cellLocation){

        Map<Location, Cell> neighborCells = new HashMap<Location, Cell>();

        if(this.contains(cellLocation)){

            for(Location xLocation : this.getCell(cellLocation).getCellShape().getNeighborLocations(cellLocation)){

                if(this.contains(xLocation)){
                    neighborCells.put(xLocation, this.getCell(xLocation));
                }
                else if(cellShape.getBorderType() != BorderType.REGULAR){

                    switch(cellShape.getBorderType()){
                        case TOROIDAL:
                            break;
                        case INFINITE:
                            neighborCells.put(xLocation, new EdgeCell(xLocation));
                            break;
                    }

                }

            }

        }

        return neighborCells;

    }

    public Map<Location, Cell> getCellsOfType(State cellType){

        Map<Location, Cell> cellMapOfType = new HashMap<Location, Cell>();

        for(Location xLocation : this.cellMap.keySet()){
            if(cellMap.get(xLocation).isCurrentEqual(cellType)){
                cellMapOfType.put(xLocation, this.cellMap.get(xLocation));
            }
        }

        return cellMapOfType;

    }

    public List<Location> getGridLocations(){
        List<Location> tempList = new ArrayList<Location>(this.cellMap.keySet());
        Collections.shuffle(tempList);
        return tempList;
    }

    public void renderCells() {

        for(Location xLocation : this.getGridLocations()){
            Cell xCell = this.getCell(xLocation);
            xCell.setLocation(xLocation);
        }

    }

    public void setCellScale(double xFactor) {

        for(Cell xCell : this.cellMap.values()){
            xCell.setScale(xFactor);
        }

        this.renderCells();

    }
}