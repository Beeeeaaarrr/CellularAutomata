package src.cell;

import src.cell.location.Location;

public class EdgeCell extends Cell {

    private Location xLocation;

    public EdgeCell(Location xLocation){
        this.xLocation = xLocation;
    }

    public Location getxLocation(){
        return this.xLocation;
    }

}
