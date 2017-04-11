package src.cell.state;

import javafx.scene.paint.Color;

import java.util.List;

public interface State {

    /**
     * Checks neighboring <code>States</code> and returns the next <code>State</code>.
     * @param neighborStates list of the <code>States</code> surrounding the current <code>State</code>
     * @return the next <code>State</code>
     */
    public State neighborUpdate(List<State> neighborStates);

    /**
     * Retrieve the <code>Color</code> of the <code>State</code>
     * @return the <code>State's</code> current <code>Color</code>
     */
    public Color getColor();

    /**
     * Set the <code>Color</code> of the <code>State</code>
     * @param stateColor <code>Color</code> to set the <code>State</code> to
     */
    public void setColor(Color stateColor);

}