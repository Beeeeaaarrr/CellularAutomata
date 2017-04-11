package src.cell.state.property;

import javafx.scene.paint.Color;

/**
 * Created by Justin Wang
 */

public enum StateColor {

    /*Game of Life*/
    GOL_ALIVE(Color.BLACK), GOL_DEAD(Color.WHITE),

    /*Spreading of Fire*/
    SF_EMPTY(Color.GOLD), SF_TREE(Color.DARKSEAGREEN), SF_BURNING(Color.DARKRED),

    /*Segregation*/
    SG_EMPTY(Color.WHITE), SG_AGENTX(Color.GOLD), SG_AGENTO(Color.SILVER),

    /*Wator*/
    WT_EMPTY(Color.STEELBLUE), WT_PREY(Color.GREENYELLOW), WT_PREDATOR(Color.ORANGE);

    private Color stateColor;

    StateColor(Color stateColor){
        this.stateColor = stateColor;
    }

    public Color getColor(){
        return this.stateColor;
    }

}