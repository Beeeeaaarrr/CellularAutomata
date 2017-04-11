/**
 * @author christianmartindale
 */

package src.society;

import src.cell.Cell;
import src.cell.CellShape;

import java.util.List;

/**
 * Created by Christian Martindale
 * Refactored by Justin Wang
 */

public class SpreadingFireSociety extends Society {

    public SpreadingFireSociety(int row, int col, List<Cell> cellList, CellShape cellShape) {
        super(row, col, cellList, cellShape);
    }

}
