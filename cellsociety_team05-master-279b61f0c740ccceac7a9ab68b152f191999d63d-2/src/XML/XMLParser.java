/**
 * The <code>XML</code> class parses through an <code>XML Configuration</code> file and extrapolates the information
 * appropriately.
 *
 * Author: Cell Society Team 05
 * Project: Cell Society
 *
 */

package src.XML;

import src.cell.*;

import src.cell.state.GameOfLifeState;
import src.cell.state.SegregationState;
import src.cell.state.SpreadingFireState;
import src.cell.state.WatorState;
import org.w3c.dom.*;
import org.xml.sax.*;
import src.society.property.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * Created by Justin Wang
 */

public class XMLParser {

    private static class EHandler implements ErrorHandler {

        private static final String exceptionFormat = "URI = %s Line = %s : %s";
        private static final String warningMsg = "Warning: %s";
        private static final String errorMsg = "Error: %s";
        private static final String fatalMsg = "Fatal Error: %s";

        private PrintWriter printWriter;

        EHandler(PrintWriter printWriter){
            this.printWriter = printWriter;
        }

        private String getParseException(SAXParseException saxException){
            return String.format(exceptionFormat, saxException.getSystemId(), saxException.getLineNumber(), saxException.getException());
        }

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            System.out.printf(warningMsg, this.getParseException(exception));
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException(String.format(errorMsg, this.getParseException(exception)));
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException(String.format(fatalMsg, this.getParseException(exception)));
        }

    }

    /*Parser Documents*/
    private DocumentBuilderFactory DBFactory;
    private DocumentBuilder DBuilder;
    private Document currentDoc;

    private String filePath;

    private SocietySetting societySetting;
    private EnumMap<SocietyType, Map<String, Integer>> populationMap;

    public XMLParser(String filePath) throws Exception{
        this.initMap();
        this.initDoc(filePath);
    }

    /**
     * Initializes population map of all society types
     */
    private void initMap(){

        this.populationMap = new EnumMap<SocietyType, Map<String, Integer>>(SocietyType.class);

        for(SocietyType societyType : SocietyType.values()){
            populationMap.put(societyType, new HashMap<String, Integer>());
        }

    }

    /**
     * Initializes a document for DOM Parsing. Can be accessed later to reset XML parser.
     * @param filePath the <code>Path</code> of the <code>XML File</code>
     * @throws Exception error handling for parsing
     */
    public void initDoc(String filePath) throws Exception{

        this.filePath = filePath;
        this.DBFactory = DocumentBuilderFactory.newInstance();
        this.DBuilder = this.DBFactory.newDocumentBuilder();
        this.currentDoc = DBuilder.parse(new File(filePath));
        this.currentDoc.getDocumentElement().normalize();

        OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, XMLTag.OUTPUT_ENCODING.getTag());
        DBuilder.setErrorHandler(new EHandler(new PrintWriter(errorWriter, true)));

        this.parseDoc();
    }

    public String getFilePath(){
        return this.filePath;
    }

    /**
     * Returns an instance of the SocietySetting object which stores all pertinent simulation data
     * @return SocietySetting societySetting
     */
    public SocietySetting getSocietySetting(){
        return this.societySetting;
    }

    /**
     * Helper method that parses through an XML file via the initialized XML parser
     */
    private void parseDoc(){

        Element rootNode = this.currentDoc.getDocumentElement();
        Element propertiesNode = ((Element) this.baseNode(XMLTag.PROPERTIES.getTag()));

        int row = getDimension(propertiesNode, XMLTag.DIMENSIONS.getTag(), XMLTag.ROW.getTag());
        int col = getDimension(propertiesNode, XMLTag.DIMENSIONS.getTag(), XMLTag.COL.getTag());
        int numCells = this.getNumberOfCells(row, col);

        this.societySetting = new SocietySetting(rootNode.getAttribute(XMLTag.SIMULATION.getTag()));
        this.societySetting.setRow(row);
        this.societySetting.setCol(col);
        this.societySetting.setTitle(rootNode.getAttribute(XMLTag.TITLE.getTag()));

        NodeList popList = propertiesNode.getElementsByTagName(XMLTag.POPULATION.getTag());

        for(int xCount = 0; xCount < popList.getLength(); xCount ++){

            int value = ((int)Math.round(Double.parseDouble(popList.item(xCount).getTextContent()) * numCells));
            String key = popList.item(xCount).getAttributes().getNamedItem(XMLTag.TYPE.getTag()).getNodeValue();
            this.populationMap.get(this.societySetting.getSocietyType()).put(key, value);

        }

        this.setCellList(this.societySetting, numCells);

    }

    /**
     * Returns the total number of cells required based off of row/col information
     * @param row number of rows in the grid
     * @param col number of columns in the grid
     * @return total number of cells (row * columns)
     */
    private int getNumberOfCells(int row, int col){
        return row * col;
    }

    /**
     * Extracts a dimension from a node (row or col)
     * @param resourceNode node to extract information from
     * @param parentTag parent node tag
     * @param childTag child node tag (row or col)
     * @return number of rows or number of columns
     */
    private int getDimension(Element resourceNode, String parentTag, String childTag){
        return Integer.parseInt(resourceNode.getElementsByTagName(parentTag).item(0).getAttributes().getNamedItem(childTag).getNodeValue());
    }

    /**
     * Retrieves the parent node with the given tagName
     * @param tagName
     * @return
     */
    private Node baseNode(String tagName){
        return this.currentDoc.getElementsByTagName(tagName).item(0);
    }

    /**
     * Private helper method:
     * @param societySettings SocietySetting object to modify
     * @param numCells number of cells to generate
     */
    private void setCellList(SocietySetting societySettings, int numCells){

        List<Cell> cellList = new ArrayList<Cell>();

        SocietyType societyType = societySettings.getSocietyType();
        Map<String, Integer> tempPop = this.populationMap.get(societyType);

        for(String stateString : tempPop.keySet()){
            for(int xCount = 0; xCount < tempPop.get(stateString); xCount++){
                switch (societyType){
                    case SEGREGATION:
                        cellList.add(new SegregationCell(SegregationState.valueOf(stateString)));
                        break;
                    case SPREADING_FIRE:
                        cellList.add(new SpreadingFireCell(SpreadingFireState.valueOf(stateString)));
                        break;
                    case GAME_OF_LIFE:
                        cellList.add(new GameOfLifeCell(GameOfLifeState.valueOf(stateString)));
                        break;
                    case WATOR:
                        cellList.add(new WatorCell(WatorState.valueOf(stateString)));
                        break;
                    default:
                        break;
                }
            }
        }

        Collections.shuffle(cellList);
        societySettings.setCellList(cellList);

    }

}