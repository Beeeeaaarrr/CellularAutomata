package src.society.property;

import src.cell.Cell;
import src.cell.CellShape;
import src.society.*;

import java.util.List;

/**
 * Created by Justin Wang
 */

public enum SocietyType {

    GAME_OF_LIFE("Game Of Life"){
        @Override
        public Society createSociety(int row, int col, List<Cell> cellList, CellShape cellShape) {
            return new GameOfLifeSociety(row, col, cellList, cellShape);
        }
    },
    SEGREGATION("Segregation"){
        @Override
        public Society createSociety(int row, int col, List<Cell> cellList, CellShape cellShape) {
            return new SegregationSociety(row, col, cellList, cellShape);
        }
    },
    SPREADING_FIRE("Spreading Of Fire"){
        @Override
        public Society createSociety(int row, int col, List<Cell> cellList, CellShape cellShape) {
            return new SpreadingFireSociety(row, col, cellList, cellShape);
        }
    },
    WATOR("WaTor World"){
        @Override
        public Society createSociety(int row, int col, List<Cell> cellList, CellShape cellShape) {
            return new WatorSociety(row, col, cellList, cellShape);
        }
    };

    private String nameOfSociety;

    SocietyType(String nameOfSociety){
        this.nameOfSociety = nameOfSociety;
    }

    public String getNameOfSociety(){
        return this.nameOfSociety;
    }

    public abstract Society createSociety(int row, int col, List<Cell> cellList, CellShape cellShape);

    public static SocietyType fromString(String text) {

        for (SocietyType sType : SocietyType.values()) {
            if (sType.nameOfSociety.equals(text)) {
                return sType;
            }
        }

        throw new IllegalArgumentException("No society type with text " + text + " found");

    }

}
