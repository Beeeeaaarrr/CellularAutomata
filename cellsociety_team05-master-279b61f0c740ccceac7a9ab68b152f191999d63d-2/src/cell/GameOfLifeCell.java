package src.cell;

import java.util.Map;

import src.cell.state.GameOfLifeState;
import src.cell.state.State;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class GameOfLifeCell extends Cell{

	
    public GameOfLifeCell(State currentState) {
        super(currentState);
        stateSwapMap.put(GameOfLifeState.ALIVE, GameOfLifeState.DEAD);
        stateSwapMap.put(GameOfLifeState.DEAD, GameOfLifeState.ALIVE);
    }

    public GameOfLifeCell(State currentState, CellShape cellShape) {
        super(currentState, cellShape);
        stateSwapMap.put(GameOfLifeState.ALIVE, GameOfLifeState.DEAD);
        stateSwapMap.put(GameOfLifeState.DEAD, GameOfLifeState.ALIVE);
        this.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
        	public void handle(MouseEvent arg0){
        		GameOfLifeCell.this.setCurrentState(stateSwapMap.get(GameOfLifeCell.this.getCurrentState()));
        	}
        });
        
    }

}
