package src.cell;

import java.util.Map;

import src.cell.state.SegregationState;
import src.cell.state.SpreadingFireState;
import src.cell.state.State;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class SpreadingFireCell extends Cell{

	
    public SpreadingFireCell(State currentState) {
        super(currentState);
        stateSwapMap.put(SpreadingFireState.BURNING, SpreadingFireState.TREE);
        stateSwapMap.put(SpreadingFireState.TREE, SpreadingFireState.EMPTY);
        stateSwapMap.put(SpreadingFireState.EMPTY, SpreadingFireState.BURNING);
    }

    public SpreadingFireCell(State currentState, CellShape cellShape) {
        super(currentState, cellShape);
        stateSwapMap.put(SpreadingFireState.BURNING, SpreadingFireState.TREE);
        stateSwapMap.put(SpreadingFireState.TREE, SpreadingFireState.EMPTY);
        stateSwapMap.put(SpreadingFireState.EMPTY, SpreadingFireState.BURNING);
        this.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
        	public void handle(MouseEvent arg0){
        		SpreadingFireCell.this.setCurrentState(stateSwapMap.get(SpreadingFireCell.this.getCurrentState()));
        	}
        });
    }

}
