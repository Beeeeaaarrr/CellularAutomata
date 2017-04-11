package src.cell;

import java.util.ArrayList;
import java.util.List;

import src.cell.location.Location;
import src.society.property.BorderType;

/**
 * Created by Justin Wang
 */

public enum CellShape {

    SQUARE(4, new Double[]{0.0, 0.0, 0.0, 10.0, 10.0, 10.0, 10.0, 0.0},
            BorderType.REGULAR
    ){

        @Override
        public List<Location> getNeighborLocations(Location cellLocation) {

            int x = cellLocation.getRow();
            int y = cellLocation.getCol();

            List<Location> neighborList = new ArrayList<Location>(){{

                add(new Location(x - 1, y - 1));
                add(new Location(x - 1, y));
                add(new Location(x - 1, y + 1));
                add(new Location(x, y - 1));
                add(new Location(x, y + 1));
                add(new Location(x + 1, y - 1));
                add(new Location(x + 1, y));
                add(new Location(x + 1, y + 1));

            }};

            return neighborList;

        }

        @Override
        public double calculateX(Location cellLocation, double width, double height) {
            return cellLocation.getCol() * width;
        }

        @Override
        public double calculateY(Location cellLocation, double width, double height) {
            return cellLocation.getRow() * height;
        }

    },
    HEXAGON_POINT(6, new Double[]{0.0, 2.5, 0.0, 7.5, Math.sin(Math.toRadians(60.0))*5.0, 10.0, Math.sin(Math.toRadians(60.0))*10.0, 7.5, Math.sin(Math.toRadians(60.0))*10.0, 2.5, Math.sin(Math.toRadians(60.0))*5.0, 0.0},
            BorderType.REGULAR
    ){

        @Override
        public List<Location> getNeighborLocations(Location cellLocation) {

            /*Convert from Offset to Cubic*/
            int x = cellLocation.getCol() - (cellLocation.getRow() - (cellLocation.getRow() & 1)) / 2;
            int z = cellLocation.getRow();

            List<Location> neighborList = new ArrayList<Location>(){{

                add(new Location(z, getColFromCubic(x + 1, z)));
                add(new Location(z, getColFromCubic(x - 1, z)));
                add(new Location(z - 1, getColFromCubic(x + 1, z - 1)));
                add(new Location(z + 1, getColFromCubic(x - 1, z + 1)));
                add(new Location(z - 1, getColFromCubic(x, z - 1)));
                add(new Location(z + 1, getColFromCubic(x, z + 1)));

            }};

            return neighborList;

        }

        @Override
        public double calculateX(Location cellLocation, double width, double height) {
            return width * ((cellLocation.getRow() & 1)* 0.5 + cellLocation.getCol());
        }

        @Override
        public double calculateY(Location cellLocation, double width, double height) {
            return cellLocation.getRow() * ((height * 3.0)/4.0);
        }

        private int getColFromCubic(int x, int z){
            return x + (z - (z & 1)) / 2;
        }

    },
    HEXAGON_EDGE(6, new Double[]{0.0, Math.sin(Math.toRadians(60.0))*5.0, 2.5, Math.sin(Math.toRadians(60.0))*10.0, 7.5, Math.sin(Math.toRadians(60.0))*10.0, 10.0, Math.sin(Math.toRadians(60.0))*5.0, 7.5, 0.0, 2.5, 0.0},
            BorderType.REGULAR
    ){

        @Override
        public List<Location> getNeighborLocations(Location cellLocation) {

            /*Convert from Offset to Cubic*/
            int x = cellLocation.getCol();
            int z = cellLocation.getRow()  - (cellLocation.getCol() - (cellLocation.getCol() & 1)) / 2;

            List<Location> neighborList = new ArrayList<Location>(){{

                add(new Location(getRowFromCubic(x, z + 1), x));
                add(new Location(getRowFromCubic(x, z - 1), x));
                add(new Location(getRowFromCubic(x - 1, z + 1), x - 1));
                add(new Location(getRowFromCubic(x + 1, z - 1), x + 1));
                add(new Location(getRowFromCubic(x - 1, z), x - 1));
                add(new Location(getRowFromCubic(x + 1, z), x + 1));

            }};
            return neighborList;

        }

        @Override
        public double calculateX(Location cellLocation, double width, double height) {
            return cellLocation.getRow() * ((width * 3.0)/4.0);
        }

        //TODO
        @Override
        public double calculateY(Location cellLocation, double width, double height) {
            return height * ((cellLocation.getRow() & 1) * 0.5 + cellLocation.getCol());
        }

        private int getRowFromCubic(int x, int z){
            return z + (x - (x % 1)) / 2;
        }

    },
    TRIANGLE(3, new Double[]{0.0, 10.0, 10.0, 10.0, 5.0, 0.0},
            BorderType.REGULAR
    ){

        @Override
        public List<Location> getNeighborLocations(Location cellLocation) {

            int x = cellLocation.getRow();
            int y = cellLocation.getCol();

            int offset = (((x + y) % 2) == 0) ? -1 : 1;

            List<Location> neighborList = new ArrayList<Location>(){{

                add(new Location(x + offset, y));
                add(new Location(x, y + 1));
                add(new Location(x, y - 1));

            }};

            return neighborList;

        }

        @Override
        public double calculateX(Location cellLocation, double width, double height) {
            return cellLocation.getCol() * width/2;
        }

        @Override
        public double calculateY(Location cellLocation, double width, double height) {
            return cellLocation.getRow() * height;
        }

    };

    private int numSides;
    private int numNeighbors;
    private Double[] shapeCoordinates;
    private BorderType borderType;

    CellShape(int numSides, Double[] shapeCoordinates, BorderType borderType){
        this.numSides = numSides;
        this.borderType = borderType;
        this.setShapeCoordinates(shapeCoordinates);
    }

    public int getNumSides(){
        return this.numSides;
    }

    public BorderType getBorderType(){
        return this.borderType;
    }

    /**
     * Retrieves the coordinates of the shape
     * @return list of x and y coordinates for use in Polygon
     */
    public Double[] getShapeCoordinates(){
        return this.shapeCoordinates;
    }

    /**
     * Reset the coordinates of a specific shape
     * @param shapeCoordinates new coordinates for shape
     */
    private void setShapeCoordinates(Double[] shapeCoordinates){
        this.shapeCoordinates = shapeCoordinates;
    }

    /**
     * Extracts a list of locations surrounding the current cell. This calculation varies based on the type of cell shape.
     * @param cellLocation location of the cell to be checked
     * @return list of neighboring locations
     */
    public abstract List<Location> getNeighborLocations(Location cellLocation);

    /**
     * Calculate the x-coordinate based off of grid location and shape dimensions
     * @return x-coordinate
     */
    public abstract double calculateX(Location cellLocation, double width, double height);

    /**
     * Calculate the y-coordinate based off of grid location and shape dimensions
     * @return y-coordinate
     */
    public abstract double calculateY(Location cellLocation, double width, double height);

}