package src.cell.state;

import src.cell.state.property.StateColor;
import src.cell.state.property.StateRules;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;

public enum SpreadingFireState implements State{

    EMPTY(StateColor.SF_EMPTY.getColor()), BURNING(StateColor.SF_BURNING.getColor()),
    TREE(StateColor.SF_TREE.getColor()){

        /**
         * Special neighborUpdate method for tree.
         * @param neighborStates list of the <code>States</code> surrounding the current <code>State</code>
         * @return the next <code>State</code>
         */
        @Override
        public State neighborUpdate(List<State> neighborStates) {

            double randDouble = this.getRandGen().nextDouble();

            if(neighborStates.contains(BURNING) && (randDouble <= this.getCatchProb())){
                return BURNING;
            }

            return TREE;

        }

    };

    private double catchProb;
    private Random randGen;
    private Color stateColor;

    SpreadingFireState(Color stateColor){
        catchProb = StateRules.SF_PROBABILITY.getValue();
        randGen = new Random();
        this.setColor(stateColor);
    }

    /*Primary Functions*/

    /**
     * Checks neighboring <code>states</code> and returns the next <code>State</code>.
     * @param neighborStates list of the <code>states</code> surrounding the current <code>State</code>
     * @param probCatch probability of a <code>Tree</code> catching fire (value between 0.0 and 1.0)
     * @return the next <code>State</code>
     */
    public State neighborUpdate(List<State> neighborStates, double probCatch){
        this.setCatchProb(probCatch);
        return neighborUpdate(neighborStates);
    }

    /**
     * Retrieves the probability of catching fire
     * @return probability of catching fire
     */
    public double getCatchProb(){
        return this.catchProb;
    }

    /**
     * Retrieves the random number generator
     * @return random number generator
     */
    protected Random getRandGen(){
        return this.randGen;
    }

    /**
     * Sets the probability of catching fire
     * @param catchProb new probability
     */
    public void setCatchProb(double catchProb){
        this.catchProb = catchProb;
    }

    /*Parent Functions*/

    /**
     * Checks neighboring <code>states</code> and returns the next <code>State</code>. <code>EMPTY</code> is returned
     * by default.
     * @rules: Spreading of Fire
     * @param neighborStates list of the <code>states</code> surrounding the current <code>State</code>
     * @return the next <code>State</code>
     */
    @Override
    public State neighborUpdate(List<State> neighborStates) {
        return EMPTY;
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