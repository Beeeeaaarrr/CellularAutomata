package src.cell.state;

import src.cell.state.property.StateColor;
import src.cell.state.property.StateRules;

import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;

public enum GameOfLifeState implements State {

    ALIVE(StateColor.GOL_ALIVE.getColor()){

        /*Instance Variables*/
        private int MIN = (int)StateRules.GOL_MIN.getValue();
        private int MAX = (int)StateRules.GOL_MAX.getValue();

        /**
         * Checks neighboring <code>states</code> and returns the next <code>State</code>.
         * @currentState: <code>ALIVE</code>
         * @rules: Game of Life
         * @param neighborStates list of the <code>states</code> surrounding the current <code>State</code>
         * @return the next <code>State</code>
         */
        @Override
        public State neighborUpdate(List<State> neighborStates) {
            int count = this.getTypeCount(ALIVE, neighborStates);
            return (count >= this.MIN && count <= this.MAX) ? ALIVE : DEAD;
        }

        @Override
        public int getMin() {
            return MIN;
        }

        @Override
        public int getMax() {
            return MAX;
        }

        @Override
        public void setMin(int min) {
            MIN = min;
        }

        @Override
        public void setMax(int max) {
            MAX = max;
        }

    },

    DEAD(StateColor.GOL_DEAD.getColor()){

        /*Instance Variables*/
        private int CHECK = (int)StateRules.GOL_CHECK.getValue();

        /**
         * Checks neighboring <code>states</code> and returns the next <code>State</code>.
         * @currentState: <code>DEAD</code>)
         * @rules: Game of Life
         * @param neighborStates list of the <code>states</code> surrounding the current <code>State</code>
         * @return the next <code>State</code>
         */
        @Override
        public State neighborUpdate(List<State> neighborStates) {
            int count = this.getTypeCount(ALIVE, neighborStates);
            return (count == this.CHECK) ? ALIVE : DEAD;
        }

        @Override
        public int getMin() {
            return CHECK;
        }

        @Override
        public int getMax() {
            return CHECK;
        }

        @Override
        public void setMin(int MIN) { CHECK = MIN; }

        @Override
        public void setMax(int MAX) { CHECK = MAX; }

    };

    private Color stateColor;

    GameOfLifeState(Color stateColor){
        this.setColor(stateColor);
    }

    /*Primary Functions*/

    /**
     * Using <code>Collections.frequency</code>, returns the number of occurrences of a specified <code>State</code>
     * within a given list
     * @param checkState <code>State</code> to check the occurrences of
     * @param neighborStates List of <code>states</code>
     * @return frequency of <code>checkState</code> within <code>neighborStates</code>
     */
    protected int getTypeCount(GameOfLifeState checkState, List<State> neighborStates){
        return Collections.frequency(neighborStates, checkState);
    }

    public abstract int getMin();

    public abstract int getMax();

    public abstract void setMin(int MIN);

    public abstract void setMax(int MAX);

    @Override
    public void setColor(Color stateColor) {
        this.stateColor = stateColor;
    }

    @Override
    public Color getColor() {
        return this.stateColor;
    }

}