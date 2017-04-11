package src.cell;

import java.util.Map;

import src.cell.state.GameOfLifeState;
import src.cell.state.SegregationState;
import src.cell.state.State;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created by Christian Martindale
 * Refactored by Justin Wang
 */

public class SegregationCell extends Cell{

	
    public SegregationCell(State currentState) {
        super(currentState);
        stateSwapMap.put(SegregationState.AGENT_X, SegregationState.AGENT_O);
        stateSwapMap.put(SegregationState.AGENT_O, SegregationState.EMPTY);
        stateSwapMap.put(SegregationState.EMPTY, SegregationState.AGENT_X);
    }

    public SegregationCell(State currentState, CellShape cellShape) {
        super(currentState, cellShape);
        stateSwapMap.put(SegregationState.AGENT_X, SegregationState.AGENT_O);
        stateSwapMap.put(SegregationState.AGENT_O, SegregationState.EMPTY);
        stateSwapMap.put(SegregationState.EMPTY, SegregationState.AGENT_X);
      
    }
    
}
