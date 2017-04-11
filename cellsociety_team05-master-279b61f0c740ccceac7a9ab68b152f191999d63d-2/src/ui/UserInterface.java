package ui;

import cell.Cell;
import cell.CellShape;
import cell.SegregationCell;
import cell.state.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import society.Society;
import society.SpreadingFireSociety;
import society.property.SocietySetting;
import society.property.SocietyType;
import xml.XMLParser;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EventListener;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Mina Mungekar
 */

public class UserInterface {

    private final String UIPropertiesLocation = "ui/property/UITags";
    private ResourceBundle UIProperties = ResourceBundle.getBundle(UIPropertiesLocation);
    private Scene UIScene;

    private final int HEIGHT = Integer.parseInt(UIProperties.getString("ScreenHeight"));
    private final int WIDTH = Integer.parseInt(UIProperties.getString("ScreenWidth"));

    private BorderPane borderPane;
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private HBox mainBox;

    private Pane topPane;
    private ToolBar topToolBar;

    private Button playButton, stopButton, speedButton, slowButton;
    private Slider animationSlider;
    private Label rateValue;
    private ComboBox simulationOptions;
    private Animator mainAnimator;
    private Map<SocietyType, Society> societyMap;

    private XMLParser xmlParser;


    public UserInterface() throws Exception{
        this.setup();
    }

    public void setup() throws Exception{

        this.initPane();
        this.initSocieties();
        this.initComboBox();
        this.initButtons();
        this.initHBox();
        this.initRateSlider();

        this.setCellScale((double)Integer.parseInt(UIProperties.getString("CellScale")));
        
    }

    private void initPane() {

        this.borderPane = new BorderPane(){{
            prefHeight(HEIGHT);
            prefWidth(WIDTH);
        }};
//        this.borderPane.prefWidth(800.0);
//        this.borderPane.prefHeight(1200.0);
        //this.borderPane.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, null)));
        this.UIScene = new Scene(this.borderPane, WIDTH, HEIGHT, BACKGROUND_COLOR);

    }

    public Scene getScene() {
        return this.UIScene;
    }

    /**
     *@return SimulationSettings object that contains information for its respective society
     *@param filePath leads the parser to the file which contains relevant information
     *to the simulation
     * Using the SimulationSettings method, all societies can be initialized
     *
     */
    private SocietySetting getSocietySettings(String filePath) throws Exception{
        this.xmlParser = new XMLParser(filePath);
        return xmlParser.getSocietySetting();
    }

    private Society createSociety(String filePath) throws Exception {
        SocietySetting societySetting = this.getSocietySettings(filePath);
        return societySetting.getSocietyType().createSociety(societySetting.getRow(), societySetting.getCol(), societySetting.getCellList(), CellShape.TRIANGLE);
    }

    /**
     * Adds societies to an Enum Map
     */
    private void initSocieties() throws Exception {

        this.societyMap = new EnumMap<SocietyType,Society>(SocietyType.class);
        this.societyMap.put(SocietyType.SPREADING_FIRE, this.createSociety(UIProperties.getString("FirePath")));
        this.societyMap.put(SocietyType.WATOR, this.createSociety(UIProperties.getString("WatorPath")));
        this.societyMap.put(SocietyType.SEGREGATION, this.createSociety(UIProperties.getString("SegregationPath")));
        this.societyMap.put(SocietyType.GAME_OF_LIFE, this.createSociety(UIProperties.getString("GameofLifePath")));

    }

    /**
     * Buttons which are later added to the Hbox call event handlers to play and stop the animation
     */
    private void initButtons() {

        this.playButton = createButton(UIProperties.getString("PlayButtonDisplay"), e ->playButtonEvent());
        this.stopButton = createButton(UIProperties.getString("StopButtonDisplay"), e ->stopButtonEvent());

    }

    private Button createButton(String buttonTitle, EventHandler<ActionEvent> eventHandler){

        return new Button(buttonTitle){{
            setOnAction(eventHandler);
        }};

    }

    private void playButtonEvent() {
        mainAnimator.play();
    }

    private void stopButtonEvent() {
        mainAnimator.stop();
    }

    private void initHBox(){

        this.mainBox = new HBox();
        this.mainBox.setSpacing(Integer.parseInt(UIProperties.getString("ButtonSpacing")));
        int insetSpaces = Integer.parseInt(UIProperties.getString("InsetSpacing"));
        this.mainBox.setPadding(new Insets(insetSpaces,insetSpaces,insetSpaces,insetSpaces));
        this.mainBox.getChildren().addAll(this.playButton, this.stopButton);
        this.borderPane.setBottom(this.mainBox);

    }

    /**
     *
     * Sliders that control grid transparency and animation rate are initialized and configured with
     * ChangeListeners
     */
    private void createSlider(Slider xSlider, Label xValue ){

        xSlider.setMin((double)Integer.parseInt(UIProperties.getString(xSlider.getId()+"BeginValue")));
        xSlider.setMax((double)Integer.parseInt(UIProperties.getString(xSlider.getId()+"EndValue")));
        xSlider.setValue((double) Integer.parseInt(UIProperties.getString(xSlider.getId()+"Default")));
        Label tempTitle = new Label(UIProperties.getString(xSlider.getId()+"Title"));
        xValue = new Label(Double.toString(xSlider.getValue()));
        this.mainBox.getChildren().addAll(tempTitle, xSlider, xValue);

    }

    private void initRateSlider(){

        this.animationSlider = new Slider();
        this.animationSlider.setId(UIProperties.getString("AnimationRateId"));
        createSlider(this.animationSlider, this.rateValue);

        try {
            this.animationSlider.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> obv,
                                    Number n1, Number n2) {
                    mainAnimator.changeRate((double) n2);
                    rateValue.setText(String.format("%.2f", n2));
                }
            });
        }
        catch (Exception E){

        }

    }
    

    private void setCellScale(double xFactor){
        for(Society xSociety : this.societyMap.values()){
            xSociety.setCellScale(xFactor);
        }
    }

    /**
     * The combo box presents a drop-down menu so that the user can select which simulation
     * to start with.
     * Once the user selects a simulation, a society is initialized, as well as an animator and a chart
     * tracking the number of cells in each of the simulation's states
     */
    private void initComboBox(){

        this.listSimulationOptions();
        this.simulationOptions.setPromptText(UIProperties.getString("ComboBoxPrompt"));
        this.simulationOptions.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                String tempSelection = arg2.toString();
                mainAnimator = new Animator(SocietyType.valueOf(tempSelection), societyMap.get(SocietyType.valueOf(tempSelection)));
                createSociety(mainAnimator.getSociety());
                positionAnimatorChart();
                mainAnimator.initialize();

            }

        });

        this.initTopPane();

        borderPane.setAlignment(simulationOptions, Pos.TOP_LEFT);
        borderPane.setTop(simulationOptions);
    }

    private void initTopPane(){

        this.topToolBar = new ToolBar(){{
            setBlendMode(BlendMode.SRC_ATOP);
            setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            prefHeight(Integer.parseInt(UIProperties.getString("ToolbarHeight")));
            prefWidth(Integer.parseInt(UIProperties.getString("ToolbarWidth")));
        }};
        this.borderPane.setTop(topToolBar);
        //this.borderPane.setAlignment(topPane, Pos.TOP_CENTER);
    }
    /**
     * Positions the chart
     */
    private void positionAnimatorChart() {
        borderPane.setAlignment(mainAnimator.getLineChart(),Pos.CENTER_RIGHT);
        borderPane.setRight(mainAnimator.getLineChart());
    }

    /**
     * List all simulation options
     */
    private void listSimulationOptions() {

        this.simulationOptions = new ComboBox<>();
        this.simulationOptions.getItems().add(UIProperties.getString("FireSimTitle"));
        this.simulationOptions.getItems().add(UIProperties.getString("PredatorSimTitle"));
        this.simulationOptions.getItems().add(UIProperties.getString("SegregationSimTitle"));
        this.simulationOptions.getItems().add(UIProperties.getString("GameofLifeSimTitle"));

    }

    /**
     * Sets all the cells into a gridpane to be displayed
     * @param Society calls the grid upon which the cells are built
     */
    private void createSociety(Society xSociety){

        Group viewSociety = new Group();
        xSociety.getSocietyGrid().renderCells();
        for(Cell xCell : xSociety.getSocietyGrid().getCellMap().values()){
        	System.out.print(xCell.getStateSwapMap().keySet());
        	  xCell.setOnMouseClicked(new EventHandler<MouseEvent>(){
              	@Override
              	public void handle(MouseEvent arg0){
              		xCell.setCurrentState(xCell.getStateSwapMap().get(xCell.getCurrentState()));
              	}
              });
            viewSociety.getChildren().add(xCell);       
        }

        this.borderPane.setAlignment(viewSociety, Pos.CENTER);
        this.borderPane.setCenter(viewSociety);

    }

    /**
     * Retrieves the UI Scene
     * @return UIScene
     */
    public Scene getBackground(){
        return this.UIScene;
    }

}