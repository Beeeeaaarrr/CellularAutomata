package src.cell.state;

import src.cell.state.property.StateColor;
import src.cell.state.property.StateRules;

import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;

public enum SegregationState implements State {

    EMPTY(StateColor.SG_EMPTY.getColor()), AGENT_X(StateColor.SG_AGENTX.getColor()), AGENT_O(StateColor.SG_AGENTO.getColor());

    private double satisfactionRate;
    private Color stateColor;

    SegregationState(Color stateColor){
        this.satisfactionRate = StateRules.SG_SATISFACTION.getValue();
        this.setColor(stateColor);
    }

    /*Primary Functions*/

    /**
     * Retrieves the satisfaction rate for this <code>State</code>
     * @return the satisfaction rate (0.0 to 1.0)
     */
    public double getSatisfactionRate(){
        return this.satisfactionRate;
    }

    /**
     * Sets the satisfaction rate for this <code>State</code>
     * @param satisfactionRate new satisfaction rate (0.0 to 1.0)
     */
    public void setSatisfactionRate(double satisfactionRate){
        this.satisfactionRate = satisfactionRate;
    }

    /**
     * Using <code>Collections.frequency</code>, returns the percentage of occurrences of a specified <code>State</code>
     * within a given list
     * @param checkState <code>State</code> to check the occurrences of
     * @param neighborStates List of <code>states</code>
     * @return frequency of <code>checkState</code> within <code>neighborStates</code> as a decimal percentage (0.0 to 1.0)
     */
    protected double getTypeFreq(SegregationState checkState, List<State> neighborStates){
        return ((double) Collections.frequency(neighborStates, checkState))/((double) neighborStates.size());
    }

    /**
     * Indicates whether or not a <code>State</code> is satisfied with the neighbors surrounding it
     * @param neighborStates list of the <code>states</code> surrounding the current <code>State</code>
     * @return true if threshold is met; false otherwise
     */
    public boolean isSatisfied(List<State> neighborStates){
        return (getTypeFreq(this, neighborStates) >= this.satisfactionRate);
    }

    /*Parent Functions*/

    /**
     * Checks neighboring <code>states</code> and returns the next <code>State</code>.
     * @rules: Segregation
     * @param neighborStates list of the <code>states</code> surrounding the current <code>State</code>
     * @return the next <code>State</code>
     */
    @Override
    public State neighborUpdate(List<State> neighborStates) {
        return (isSatisfied(neighborStates)) ? this : EMPTY;
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