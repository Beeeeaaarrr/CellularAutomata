package src.society.property;

import src.cell.Cell;
import src.cell.state.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin Wang
 */

public class SocietySetting {

    private int rowCount;
    private int colCount;

    private String simulationTitle;
    private SocietyType societyType;

    private List<Cell> cellList;

    public SocietySetting(String societyString){

        try{
            this.societyType = SocietyType.valueOf(societyString);
        }
        catch(Exception E){
            E.getStackTrace();
        }

        this.cellList = new ArrayList<Cell>();

    }

    /**
     * Get the title of the specified simulation
     * @return the title of the simulation
     */
    public String getTitle(){
        return this.simulationTitle;
    }

    /**
     * Get the number of rows
     * @return the number of rows
     */
    public int getRow(){
        return this.rowCount;
    }

    /**
     * Get the number of columns
     * @return the number of columns
     */
    public int getCol(){
        return this.colCount;
    }

    /**
     * Get the enumeration for the simulation
     * @return enum of type SocietyType
     */
    public SocietyType getSocietyType(){
        return this.societyType;
    }

    /**
     * Get the number of cells for the society
     * @return
     */
    public int getNumCells(){
        return this.cellList.size();
    }

    /**
     * Set the number of rows
     * @param rowCount number of rows
     */
    public void setRow(int rowCount){
        this.rowCount = rowCount;
    }

    /**
     * Set the number of columns
     * @param colCount number of columns
     */
    public void setCol(int colCount){
        this.colCount = colCount;
    }

    /**
     * Set the title of the simulation
     * @param simulationTitle title of simulation
     */
    public void setTitle(String simulationTitle){
        this.simulationTitle = simulationTitle;
    }

    /**
     * Set the list of Cells for the simulation
     * @param cellList list of Cells
     */
    public void setCellList(List<Cell> cellList){
        this.cellList = new ArrayList<Cell>(cellList);
    }

    /**
     * Get the list of Cells for the simulation
     * @return list of Cells
     */
    public List<Cell> getCellList(){
        return new ArrayList<Cell>(this.cellList);
    }

}
