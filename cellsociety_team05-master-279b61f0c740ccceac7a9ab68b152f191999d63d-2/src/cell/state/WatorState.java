package src.cell.state;

import src.cell.state.property.StateColor;
import src.cell.state.property.StateRules;

import javafx.scene.paint.Color;

import java.util.List;

public enum WatorState implements State {

    EMPTY(StateColor.WT_EMPTY.getColor(), (int)StateRules.WT_ZERO.getValue(), (int)StateRules.WT_ZERO.getValue()),
    PREY(StateColor.WT_PREY.getColor(), (int)StateRules.WT_PREYBREED.getValue(), (int)StateRules.WT_ZERO.getValue()),
    PREDATOR(StateColor.WT_PREDATOR.getColor(), (int)StateRules.WT_PREDATORBREED.getValue(), (int)StateRules.WT_PREDATORENERGY.getValue()){

        @Override
        public State neighborUpdate(List<State> neighborStates) {
            return (this.isStateAvailable(neighborStates, EMPTY) || this.isStateAvailable(neighborStates, PREY))? EMPTY : this;
        }

    };

    private Color stateColor;
    private int breedTime;
    private int energy;

    WatorState(Color stateColor, int breedTime, int energy){
        this.setColor(stateColor);
        this.setBreedTime(breedTime);
        this.setEnergy(energy);
    }

    /*Primary Functions*/

    /**
     * Reports if a specified<code>State</code> is available within the immediate vicinity
     * @param neighborStates <code>List</code> of neighboring <code>states</code>
     * @param checkState <code>State</code> to check for
     * @return true if <code>EMPTY Cell</code> is available; false otherwise
     */
    protected boolean isStateAvailable(List<State> neighborStates, WatorState checkState){
        return (neighborStates.contains(EMPTY));
    }

    /**
     * Retrieves the number of turns it takes to breed
     * @return breed time
     */
    public int getBreedTime(){
        return this.breedTime;
    }

    /**
     * Retrieves the default energy setting
     * @return
     */
    public int getEnergy(){
        return this.energy;
    }

    /**
     * Set the number of turns it takes to breed.
     * @param breedTime new breed time
     */
    public void setBreedTime(int breedTime){
        this.breedTime = breedTime;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    /*Parent Functions*/

    /**
     * Checks neighboring <code>states</code> and returns the next <code>State</code>.
     * @Rules: WaTor
     * @param neighborStates list of the <code>states</code> surrounding the current <code>State</code>
     * @return the next <code>State</code>
     */
    @Override
    public State neighborUpdate(List<State> neighborStates) {
        return (this.isStateAvailable(neighborStates, EMPTY))? EMPTY : this;
    }

    @Override
    public void setColor(Color stateColor) {
        this.stateColor = stateColor;
    }

    @Override
    public Color getColor() {
        return this.stateColor;
    }

}