/**
 * The <code>Location</code> class provides the necessary positional information for a cell (i.e. relative <code>row</code>
 * and <code>column</code> coordinates). This class also provides the necessary functions for comparing locations to one
 * another
 *
 * Author: Cell Society Team 05
 * Project: Cell Society
 *
 */

package src.cell.location;

public final class Location implements Comparable<Location>{

    /*Row And Column Information For Cell*/
    private int row;
    private int col;

    /**
     * Creates a new <code>Location</code> object and instantiates its coordinates
     * @param row represents the <code>row</code> number
     * @param col represents the <code>column</code> number
     */
    public Location(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * Retrieves the <code>row</code> coordinate
     * @return the <code>row</code> number of this location
     */
    public int getRow(){
        return this.row;
    }

    /**
     * Retrieves the <code>column</code> coordinate
     * @return the <code>column</code> number of this location
     */
    public int getCol(){
        return this.col;
    }

    /**
     * Provides comparison between the rows of two <code>locations</code>
     * @param loc <code>Location</code> to compare against
     * @return row distance between the two <code>locations</code>
     */
    public int compareRow(Location loc) {
        return this.getRow() - loc.getRow();
    }

    /**
     * Provides comparison between the columns of two <code>locations</code>
     * @param loc <code>Location</code> to compare against
     * @return column distance between the two <code>locations</code>
     */
    public int compareCol(Location loc) {
        return this.getCol() - loc.getCol();
    }

    /*Parent Functions*/

    /**
     * Interprets relative position of this <code>Location</code> with respect to that of another <code>Location</code>
     * Note: Assuming row-major order
     * @param loc <code>Location</code> to compare against
     * @return <code>-1</code> if <code>loc</code> comes before the current <code>Location</code>; <code>1</code>
     * otherwise
     */
    @Override
    public int compareTo(Location loc) {
        return ((this.getRow() > loc.getRow()) || (this.getCol() < loc.getCol() && this.getRow() == loc.getRow())) ? -1 : 1;
    }

    /**
     * Indicates if another object is the equivalent to this location
     * @param obj <code>Object</code> to compare against
     * @return <code>true</code> if the object's row and column coordinates are the same as the row and column of this
     * location
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Location location = (Location) obj;

        if (row != location.row) return false;
        return col == location.col;

    }

    /**
     * Generates HashCode
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

}