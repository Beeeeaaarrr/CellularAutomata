package src.ui;

import src.cell.location.Location;
import src.cell.state.SpreadingFireState;
import src.cell.state.State;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import src.society.Grid;
import src.society.Society;
import src.society.property.SocietyType;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Mina Mungekar
 * Refactored by Justin Wang
 */

public class Chart {

    private String UIPropertiesLocation = "src.ui.property/UITags";
    private ResourceBundle ChartProperties = ResourceBundle.getBundle(UIPropertiesLocation);
    private final Integer INIT_VALUE = Integer.parseInt(ChartProperties.getString("InitValue"));
    
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    private Society chartSociety;
    private Map<State,Integer> stateMap;

    private LineChart<Number,Number> lineChart;

    private long startTime = System.currentTimeMillis();

    /**
     * The enum serves as a label for the chart, which matches the option presented on the
     * ComboBox. The chartSociety is given to the Chart object so that it can count the number of cells in
     * different states.
     * The method generates a line chart with the x-axis as time and y-axis as cell count. Different
     * series correspond to different states.
     * @param SocietyType type of chartSociety
     * @param chartSociety chartSociety to run
     */
    public Chart(SocietyType societyType, Society chartSociety){

        this.chartSociety = chartSociety;
        this.initAxes();
        this.initLineChart(societyType);
        this.updateStateCount();
        this.initSeries();

    }

    /*Primary Functions*/

    /**
     * @return the line chart generated
     */
    public LineChart getChart(){
        return this.lineChart;
    }

    /**
     * Updates the Chart by recounting the number of cells in each state and adding an updated
     * data point to the line chart
     */
    public void updateChart(){

        this.updateStateCount();

        for(XYChart.Series xSeries : this.lineChart.getData()){

            for(State xState : this.stateMap.keySet()){

                if(xSeries.getName().equals(xState.toString())){
                    xSeries.getData().add(new XYChart.Data((System.currentTimeMillis()- startTime), stateMap.get(xState)));
                }

            }

        }

    }

    /*Helper Methods*/

    /**
     * Initializes the series in the graph
     */
    private void initSeries() {

        for(State xState : this.stateMap.keySet()){

            XYChart.Series xSeries = new XYChart.Series();
            xSeries.setName(xState.toString());
            xSeries.getData().add(new XYChart.Data(((System.currentTimeMillis()- startTime)), stateMap.get(xState)));
            this.lineChart.getData().add(xSeries);

        }

    }

    /**
     * Initializes the axes of the graph
     */
    private void initAxes(){
        this.xAxis = new NumberAxis();
        this.yAxis = new NumberAxis();
        this.xAxis.setLabel(ChartProperties.getString("xLabel"));
        this.yAxis.setLabel(ChartProperties.getString("yLabel"));
    }

    /**
     * Initializes the line chart
     */
    private void initLineChart(SocietyType societyType){
        this.lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        this.lineChart.setCreateSymbols(false);
        this.lineChart.setTitle(societyType.getNameOfSociety());
    }

    /**
     * Creates a map with the key being the different states and value being the number
     * of cells present in that state.
     */
    private void updateStateCount() {

        stateMap = new HashMap<State,Integer>();

        Grid societyGrid = this.chartSociety.getSocietyGrid();

        for(Location xLocation : societyGrid.getGridLocations()){
            State xState = societyGrid.getCell(xLocation).getCurrentState();
            if(!this.stateMap.containsKey(xState)){
                this.stateMap.put(xState, this.INIT_VALUE);
            }
            this.stateMap.put(xState, stateMap.get(xState) + 1);

        }

    }

    public void setStartTime(double startTime) {
        this.startTime = (long) startTime;
    }
}