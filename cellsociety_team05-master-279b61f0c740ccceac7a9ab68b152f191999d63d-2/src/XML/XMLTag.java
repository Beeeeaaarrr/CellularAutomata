package src.XML;

/**
 * Created by Justin Wang
 */

public enum XMLTag {

    OUTPUT_ENCODING("UTF-8"),
    SIMULATION("simulation"),
    PROPERTIES("properties"),
    INITIALIZE("initialize"),
    CELL("cell"),
    TYPE("type"),
    TITLE("title"),
    POPULATION("population"),
    DIMENSIONS("dimensions"),
    ROW("row"),
    COL("col");

    private String xmlTag;

    XMLTag(String xmlTag){
        this.xmlTag = xmlTag;
    }

    public String getTag(){
        return this.xmlTag;
    }

}
