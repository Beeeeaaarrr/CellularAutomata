/**
 * @authors christianmartindale and Justin Wang
 */

package src.cell;

import java.util.Map;

import src.cell.state.State;
import src.cell.state.WatorState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by Christian Martindale
 * Refactored by Justin Wang
 */

public class WatorCell extends Cell{

    private int energy;
    private int turnsToBreed;

    public WatorCell(State currentState) {
        super(currentState);
        initVar(currentState);
        stateSwapMap.put(WatorState.PREDATOR, WatorState.PREY);
        stateSwapMap.put(WatorState.PREY, WatorState.EMPTY);
        stateSwapMap.put(WatorState.EMPTY, WatorState.PREDATOR);
    }

    public WatorCell(State currentState, CellShape cellShape) {
        super(currentState, cellShape);
        initVar(currentState);
        stateSwapMap.put(WatorState.PREDATOR, WatorState.PREY);
        stateSwapMap.put(WatorState.PREY, WatorState.EMPTY);
        stateSwapMap.put(WatorState.EMPTY, WatorState.PREDATOR);
        this.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
        	public void handle(MouseEvent arg0){
        		WatorCell.this.setCurrentState(stateSwapMap.get(WatorCell.this.getCurrentState()));
        	}
        });
    }

    public void moveCell(WatorCell cellToMove){
        this.energy = cellToMove.getEnergy();
        this.turnsToBreed = cellToMove.getTurnsToBreed();
        this.setCurrentState(cellToMove.getCurrentState());
        this.setNextState(cellToMove.getNextState());
    }

    public void initVar(State currentState){
        this.energy = ((WatorState)currentState).getEnergy();
        this.turnsToBreed = ((WatorState)currentState).getBreedTime();
    }

    public boolean canBreed() {
        return (!this.isCurrentEqual(WatorState.EMPTY) && turnsToBreed <= 0);
    }

    public void setTurnsToBreed(int turnsToBreed) {
        this.turnsToBreed = turnsToBreed;
    }

    public void removeTurnsToBreed(int removeTurn){
        this.turnsToBreed -= removeTurn;
    }

    public int getEnergy(){
        return this.energy;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    public void removeEnergy(int removeEnergy) {
        this.energy -= removeEnergy;
    }

    public void addEnergy(int addEnergy) {
        this.energy = this.energy + addEnergy;
    }

    public void resetBreed(){
        this.setTurnsToBreed(((WatorState)this.getCurrentState()).getBreedTime());
    }

    public boolean isDead(){
        return (this.isCurrentEqual(WatorState.PREDATOR) && this.getEnergy() <= 0);
    }

    public boolean canMove(){
        return !this.isCurrentEqual(WatorState.EMPTY) && this.isNextEqual(WatorState.EMPTY);
    }

    public boolean isEmpty(){
        return this.isCurrentEqual(WatorState.EMPTY);
    }

    public int getTurnsToBreed(){
        return this.turnsToBreed;
    }

    public void setEmpty(){
        this.energy = WatorState.EMPTY.getEnergy();
        this.turnsToBreed = WatorState.EMPTY.getBreedTime();
        this.setCurrentState(WatorState.EMPTY);
        this.setNextState(WatorState.EMPTY);
    }

    public void resetStats(){
        this.energy = ((WatorState)this.getCurrentState()).getEnergy();
        this.turnsToBreed = ((WatorState)this.getCurrentState()).getBreedTime();
    }

}
