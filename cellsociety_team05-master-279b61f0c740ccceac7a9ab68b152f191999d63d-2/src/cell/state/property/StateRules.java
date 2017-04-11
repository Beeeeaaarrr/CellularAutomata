package src.cell.state.property;

/**
 * Created by Justin Wang
 */

public enum StateRules {

    /*Game of Life*/
    GOL_MIN(2), GOL_MAX(3), GOL_CHECK(3),

    /*Segregation*/
    SG_SATISFACTION(0.25),

    /*Spreading of Fire*/
    SF_PROBABILITY(0.4),

    /*Wator*/
    WT_ZERO(0), WT_PREYBREED(2), WT_PREDATORBREED(16),
    WT_PREDATORENERGY(5);

    private double statValue;

    StateRules(double statValue){
        this.statValue = statValue;
    }

    public double getValue(){
        return this.statValue;
    }

}