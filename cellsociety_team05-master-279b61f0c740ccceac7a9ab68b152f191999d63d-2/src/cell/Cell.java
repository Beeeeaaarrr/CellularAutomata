/**
 * @authors Christian Martindale & Justin Wang
 * @title Cellular Automota Object
 *
 */

package src.cell;

/**
 * Created by Christian Martindale
 * Refactored by Justin Wang
 */

/*JavaFX Imports*/
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

/*Package Imports*/
import src.cell.state.*;
import src.cell.location.Location;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

/*Util Imports*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.cell.CellShape;

public abstract class Cell extends Polygon{

    private Color cellColor;

    private State currentState;
    private State nextState;

    private CellShape cellShape;

    protected Map<State, State> stateSwapMap;
    
    private int ROTATION_FACTOR = 180;

    public Cell(){}

    public Cell(State currentState){
        this(currentState, CellShape.SQUARE);
        this.stateSwapMap = new HashMap<State, State>();
    }
    
    public Cell(State currentState, CellShape cellShape){
        this.currentState = currentState;
        this.setColor();
        this.setCellStroke();
        this.setCellShape(cellShape);
        this.stateSwapMap = new HashMap<State, State>();
    }

    private void setCellStroke(){
        this.setStroke(Color.DARKSLATEGRAY);
        this.setStrokeWidth(0.2);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeLineJoin(StrokeLineJoin.ROUND);
    }

    public void setCellColor(Color cellColor){
        this.cellColor = cellColor;
    }

    /**
     * Change the shape of the cell
     * @param cellShape new cell shape
     */
    public void setCellShape(CellShape cellShape){
        this.cellShape = cellShape;
        this.getPoints().setAll(this.cellShape.getShapeCoordinates());
    }

    /**
     * Retrieves the shape of the cell
     * @return CellShape
     */
    public CellShape getCellShape(){
        return this.cellShape;
    }

    /**
     * Changes the state based on the cell's neighbors
     * @param neighborCells list of neighboring cells
     */
    public void updateState(List<Cell> neighborCells){
        this.setNextState(this.currentState.neighborUpdate(this.getNeighborStates(neighborCells)));
    }

    /**
     * Retrieves a list of the States of the neighboring Cells
     * @param neighborCells the List of a Cell's neighboring Cells
     * @return the list of States
     */
    private List<State> getNeighborStates(List<Cell> neighborCells){

        List<State> stateList = new ArrayList<State>();

        for(Cell xCell : neighborCells){
            stateList.add(xCell.getCurrentState());
        }

        return stateList;

    }

    /*State Functions*/

    /**
     * Retrieve the current state of the cell
     * @return current state of the cell
     */
    public State getCurrentState(){
        return this.currentState;
    }

    /**
     * Set the current state of the cell
     * @param currentState new state
     */
    public void setCurrentState(State currentState){
        this.currentState = currentState;
        setColor();
    }

    /**
     * Retrieve the next state of the cell
     * @return next state of the cell
     */
    public State getNextState(){
        return this.nextState;
    }

    /**
     * Set the next state of the cell
     * @param nextState new state
     */
    public void setNextState(State nextState){
        this.nextState = nextState;
    }

    /**
     * Moves next state to current state and sets the next state to null
     */
    public void swapStates(){
        this.setCurrentState(this.getNextState());
        this.setNextState(null);
        this.setColor();
    }

    public boolean isCurrentEqual(State checkState){
        return (this.currentState == checkState);
    }

    public boolean isNextEqual(State checkState){
        return (this.nextState== checkState);
    }

    /*Color Functions*/

    /**
     * Retrieves the Cell's current color
     * @return the currentState color
     */
    public Color getColor(){
        this.setColor();
        return this.cellColor;
    }

    /**
     * Sets color based on the Cell's currentState
     */
    private void setColor(){
        this.cellColor = this.currentState.getColor();
        this.setFill(this.cellColor);
    }

    public double getWidth(){
        return this.getBoundsInParent().getWidth();
    }

    public double getHeight(){
        return this.getBoundsInParent().getHeight();
    }

    /*Location Functions*/
    public void setLocation(Location cellLocation){
        this.setTranslateY(this.cellShape.calculateY(cellLocation, this.getWidth(), this.getHeight()));
        this.setTranslateX(this.cellShape.calculateX(cellLocation, this.getWidth(), this.getHeight()));
        if(this.cellShape == CellShape.TRIANGLE && (cellLocation.getCol()+cellLocation.getRow())%2 == 0){
            this.setRotate(ROTATION_FACTOR);
        }
    }

    public void setScale(double xFactor) {
        this.setScaleX(xFactor);
        this.setScaleY(xFactor);
    }
    public Map<State, State> getStateSwapMap(){
    	return this.stateSwapMap;
    }
}