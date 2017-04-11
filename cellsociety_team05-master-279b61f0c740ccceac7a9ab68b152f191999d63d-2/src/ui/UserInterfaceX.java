package src.ui;

import src.cell.*;
import src.cell.Cell;
import src.cell.state.GameOfLifeState;
import src.cell.state.SegregationState;
import src.cell.state.SpreadingFireState;
import src.cell.state.WatorState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import src.society.Society;
import src.society.property.SocietySetting;
import src.society.property.SocietyType;
import src.XML.XMLParser;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The User Interface initializes and displays all graphical tools necessary for viewing and interacting 
 * with the simulation.
 * Created by Mina Mungekar and Justin Wang
 */

public class UserInterfaceX {

    private final String UIPropertiesLocation = "src.ui.property/UITags";
    private ResourceBundle UIProperties = ResourceBundle.getBundle(UIPropertiesLocation);

    private Animator mainAnimator;

    private BorderPane borderPane;
    private Scene primaryScene;

    private ToolBar topPane;
    private ToolBar bottomPane;
    private VBox menuPane;
    private ScrollPane societyPane;

    private Text titleText;

    private Button selectFile;
    private Button resetSociety;
    private ComboBox simulationOptions;
    private ComboBox changeShape;

    private ToggleButton startStopButton;
    private Slider animationSlider;
    private Slider cellSlider;

    private TabPane settingsPaneA;
    private TabPane settingsPaneB;
    private Tab settingsTab;
    private Tab graphTab;

    private HBox mainBox;

    private Button nextStep;

    private Map<SocietyType, String> fileMap;

    private XMLParser xmlParser;

    public UserInterfaceX(){

        initFilePaths();
        initBorderPane();

    }
   /**
    * Map the different society types to their filepaths
    */
    private void initFilePaths(){
        fileMap = new EnumMap<SocietyType, String>(SocietyType.class){{
            put(SocietyType.SPREADING_FIRE, UIProperties.getString("FirePath")+".xml");
            put(SocietyType.WATOR, UIProperties.getString("WatorPath")+".xml");
            put(SocietyType.SEGREGATION, UIProperties.getString("SegregationPath")+".xml");
            put(SocietyType.GAME_OF_LIFE, UIProperties.getString("GameofLifePath")+".xml");
        }};

    }

    private SocietySetting getSettings(String filePath) throws Exception{
        this.xmlParser = new XMLParser(filePath);
        return xmlParser.getSocietySetting();
    }

    private Society createSociety(String filePath, CellShape cellShape) throws Exception {
        SocietySetting societySetting = this.getSettings(filePath);
        Society newSociety = societySetting.getSocietyType().createSociety(societySetting.getRow(), societySetting.getCol(), societySetting.getCellList(), cellShape);
        setCellScale(((double)Integer.parseInt(UIProperties.getString("CellScale"))), newSociety);
        return newSociety;
    }

    private void initBorderPane() {

        borderPane = new BorderPane(){{
        	setPrefWidth(Integer.parseInt(UIProperties.getString("BorderPaneWidth")));
            setPrefHeight(Integer.parseInt(UIProperties.getString("BorderPaneHeight")));
        }};
        primaryScene = new Scene(borderPane);
        initTopPane();
        initBottomPane();
        initMenuPane();
        initSocietyPane();

    }
    /**
     * The top pane contains the combobox presenting options to initialize the simulation and
     * a button which allows the user to select files to initialize simulations
     */
    private void initTopPane() {

    	int topPanePadding = Integer.parseInt(UIProperties.getString("TopPanePadding"));
        Insets xPadding = new Insets(topPanePadding, topPanePadding, topPanePadding, topPanePadding);
        configureTopPaneToolbar();
        configureTopPaneLeftBox(xPadding);
        configureSelectFileButton();
        simulationOptions = listSimulationOptions();
        configureTopPaneRightBox(xPadding);
        borderPane.setTop(topPane);
    }
	private void configureSelectFileButton() {
		selectFile = new Button(){{
        	setText(UIProperties.getString("FileButtonDisplay"));
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        createFileChooser();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }};
	}

	private void configureTopPaneRightBox(Insets xPadding) {
		HBox RightBox = new HBox(){{
            setAlignment(Pos.CENTER_RIGHT);
            getChildren().addAll(selectFile, simulationOptions);
            setMargin(selectFile, xPadding);
            setMargin(simulationOptions, xPadding);
            setPrefWidth(Integer.parseInt(UIProperties.getString("RightBoxWidth")));

        }};
		topPane.getItems().add(RightBox);
	}

	private void configureTopPaneLeftBox(Insets xPadding) {
		HBox LeftBox = new HBox(){{
            setPadding(xPadding);
            getChildren().add(titleText);
            setPrefWidth(Integer.parseInt(UIProperties.getString("LeftBoxWidth")));
        }};
        topPane.getItems().add(LeftBox);
	}

	private void configureTopPaneToolbar() {
		topPane = new ToolBar(){{
            setBlendMode(BlendMode.SRC_ATOP);
            setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            prefWidth(Integer.parseInt(UIProperties.getString("TopPaneWidth")));
        }};

        titleText = new Text(UIProperties.getString("PaneTitle")){{
            setFont(Font.font(UIProperties.getString("FontName"), 
            		Integer.parseInt(UIProperties.getString("FontSize"))));
        }};
	}
	/**
     * Creates file-choosing button
     */
    private void createFileChooser() throws Exception {
        File selectedFile = configureFileChooser();
        if(selectedFile != null){
            String filePath = selectedFile.getPath();
            SocietySetting socSetting = getSettings(filePath);
            Society xSociety = createSociety(selectedFile.getPath(), CellShape.SQUARE);
            mainAnimator = new Animator(socSetting.getSocietyType(), xSociety);
            changeShape.getSelectionModel().select(xSociety.getSocietyGrid().getCellShape().toString());
            enableBottom();
            renderSociety(mainAnimator.getSociety());
            positionAnimatorChart();
            //mainAnimator.initialize();
            setSettings(socSetting.getSocietyType());
            startStopButton.setSelected(false);
        }

    }
	private File configureFileChooser() {
		FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter(UIProperties.getString("Settings"), 
        		UIProperties.getString("FileType"));

        fileChooser.getExtensionFilters().add(extentionFilter);

        String userDirectoryString = System.getProperty(UIProperties.getString("userDirectoryString"));
        File userDirectory = new File(userDirectoryString);
        fileChooser.setInitialDirectory(userDirectory);

        fileChooser.setTitle(UIProperties.getString("FileChooserTitle"));
        File selectedFile = fileChooser.showOpenDialog(null);
		return selectedFile;
	}
	/**
     * The society pane contains the societies initialized in the simulation
     */
    private void initSocietyPane() {
        societyPane = new ScrollPane(){{
            setPannable(true);
        }};
        BorderPane.setAlignment(societyPane, Pos.CENTER);
        borderPane.setCenter(societyPane);
    }

    private void initMenuPane() {
        configureMenuPane();
        configureSettingsTab();
        configureGraphTab();
        configureSettingsPaneA();
        configureSettingsPaneB();
        menuPane.getChildren().addAll(settingsPaneA, settingsPaneB);

    }

	private void configureSettingsPaneB() {
		settingsPaneB = new TabPane(){{
        	setPrefHeight(Integer.parseInt(UIProperties.getString("PaneBHeight")));
            setPrefWidth(Integer.parseInt(UIProperties.getString("PaneBWidth")));
            getTabs().add(graphTab);
        }};
	}

	private void configureSettingsPaneA() {
		settingsPaneA = new TabPane(){{
        	setPrefHeight(Integer.parseInt(UIProperties.getString("PaneAWidth")));
           getTabs().add(settingsTab);
        }};
	}

	private void configureGraphTab() {
		graphTab = new Tab(){{
            setText(UIProperties.getString("GraphsTabTitle"));
            setClosable(false);
        }};
	}

	private void configureSettingsTab() {
		settingsTab = new Tab(){{
            setText(UIProperties.getString("SettingsTabTitle"));
            setClosable(false);
        }};
	}

	private void configureMenuPane() {
		menuPane = new VBox(){{
        	setPrefWidth(Integer.parseInt(UIProperties.getString("MenuPaneWidth")));
        }};
        BorderPane.setAlignment(menuPane, Pos.CENTER);
        borderPane.setLeft(menuPane);
	}
	/**
     * The bottom pane contains the buttons which control general animation and simulation parameters,
     * such a start/stop button, buttons to change unit cell shape and reset the society, and sliders 
     * controlling animation rate and cell size
     */
    private void initBottomPane() {

    	int insetSpacing = Integer.parseInt(UIProperties.getString("InsetSpacing")); 
        Insets xPadding = new Insets(insetSpacing, insetSpacing, insetSpacing, insetSpacing);
        configureBottomPane();
        configureNextStepButton();
        VBox animationSliderBox = configureAnimationSliderBox(xPadding);
        VBox cellSliderBox = configureCellSliderBox(xPadding);
        changeShape = listShapeOptions();
        configureStartStopButton();
        configureResetSocietyButton();
        configureMainBox(xPadding, animationSliderBox, cellSliderBox);
        bottomPane.getItems().add(mainBox);
        borderPane.setBottom(bottomPane);

    }

	private void configureMainBox(Insets xPadding, VBox animationSliderBox, VBox cellSliderBox) {
		mainBox = new HBox(){{
            getChildren().addAll(startStopButton, nextStep, changeShape, resetSociety, animationSliderBox, cellSliderBox);
            setPrefWidth(Integer.parseInt(UIProperties.getString("MainBoxWidth")));
            setAlignment(Pos.CENTER);
            setMargin(startStopButton, xPadding);
            setMargin(animationSlider, xPadding);
            setMargin(changeShape, xPadding);
            setMargin(resetSociety, xPadding);
            setMargin(cellSlider, xPadding);
            setMargin(nextStep, xPadding);
            setDisable(true);
        }};
	}

	private void configureResetSocietyButton() {
		resetSociety = new Button(){{
            setText(UIProperties.getString("ResetSocietyDisplay"));
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }};
	}

	private void configureStartStopButton() {
		startStopButton = new ToggleButton(){{
            setText(UIProperties.getString("StartStopDisplay"));
            selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(!oldValue){
                        start();
                    }
                    else{
                        stop();
                    }
                }

            });
        }};
	}

	private void addCellSliderListener() {
		cellSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mainAnimator.getSociety().setCellScale(newValue.doubleValue());
            }
        });
	}

	private void addAnimationSliderListener() {
		animationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mainAnimator.changeRate(newValue.doubleValue());
            }
        });
	}

	private VBox configureCellSliderBox(Insets xPadding) {
		VBox cellSliderBox = new VBox(){{
        	cellSlider = new Slider();
        	getChildren().addAll(new Text(UIProperties.getString("cellSliderTitle")), cellSlider);
            setPadding(xPadding);
            setSpacing(Integer.parseInt(UIProperties.getString("InsetSpacing")));
            setAlignment(Pos.CENTER);
            initSizeSlider(cellSlider);
        }};

        addCellSliderListener();
		return cellSliderBox;
	}

	private VBox configureAnimationSliderBox(Insets xPadding) {
		VBox animationSliderBox = new VBox(){{
        	animationSlider = new Slider(){{
                setId(UIProperties.getString("AnimationRateId"));
            }};
        	getChildren().addAll(new Text(UIProperties.getString("animationRateTitle")), animationSlider);
            setPadding(xPadding);
            setSpacing(Integer.parseInt(UIProperties.getString("InsetSpacing")));
            setAlignment(Pos.CENTER);
            initSlider(animationSlider);
        }};
        addAnimationSliderListener();
		return animationSliderBox;
	}

	private void configureNextStepButton() {
		nextStep = new Button(){{
        	setText(UIProperties.getString("NextStepDisplay"));
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mainAnimator.nextStep();
                }
            });
        }};
	}
	/**
     *Initializes the bottom pane
     */
	private void configureBottomPane() {
		bottomPane = new ToolBar(){{
            setBlendMode(BlendMode.SRC_ATOP);
            setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            prefWidth(Integer.parseInt(UIProperties.getString("BottomPaneWidth")));
            prefHeight(Integer.parseInt(UIProperties.getString("BottomPaneHeight")));

        }};
	}
	/**
     *Contains the method called by the reset button (i.e., resets the simulation)
     */
	private void reset() throws Exception{
        String xShape = changeShape.getValue().toString();
        mainAnimator = new Animator(mainAnimator.getSocietyType(), createSociety(xmlParser.getFilePath(), CellShape.valueOf(xShape)));
        mainAnimator.getSociety().setCellScale(cellSlider.getValue());
        cellSlider.setValue(mainAnimator.getSociety().getCellScale());
        changeShape.getSelectionModel().select(mainAnimator.getSociety().getSocietyGrid().getCellShape().toString());
        renderSociety(mainAnimator.getSociety());
        positionAnimatorChart();
        startStopButton.setSelected(false);
    }
	
    private void initSizeSlider(Slider cellSlider) {
        cellSlider.setMin(Integer.parseInt(UIProperties.getString("CellSliderMin")));
        cellSlider.setMax(Integer.parseInt(UIProperties.getString("CellSliderMax")));
    }
    private void initSlider(Slider xSlider){
        xSlider.setMin(Integer.parseInt(UIProperties.getString("SliderMin")));
        xSlider.setMax(Integer.parseInt(UIProperties.getString("SliderMax")));
        xSlider.setValue((xSlider.getMax()-xSlider.getMin())/2);
    }

    /**
     * List all simulation options in a drop-down menu
     */
    private ComboBox listSimulationOptions() {

        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(UIProperties.getString("FireSimTitle"), UIProperties.getString("PredatorSimTitle"),
                UIProperties.getString("SegregationSimTitle"), UIProperties.getString("GameofLifeSimTitle"));

        initSimOptions(comboBox);

        return comboBox;
    }
    /**
     *List all shape options for the cell in a drop-down menu
     */
    private ComboBox listShapeOptions() {

        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(UIProperties.getString("SQUARE"), UIProperties.getString("HEXAGON_POINT"),
                UIProperties.getString("HEXAGON_EDGE"), UIProperties.getString("TRIANGLE"));

        comboBox.setPromptText(UIProperties.getString("ComboBoxPromptShape"));
        configureShapeComboBoxListener(comboBox);

        return comboBox;
    }

	private void configureShapeComboBoxListener(ComboBox comboBox) {
		comboBox.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                CellShape cellShape = CellShape.valueOf(arg2.toString());

                try {
                    setCellShape(cellShape, mainAnimator.getSociety());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
	}

    private void setCellShape(CellShape cellShape, Society xSociety) {
        xSociety.setCellShape(cellShape);
    }

    private void initSimOptions(ComboBox comboBox) {

        comboBox.setPromptText(UIProperties.getString("ComboBoxPrompt"));
        comboBox.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                SocietyType sType = SocietyType.fromString(arg2.toString());

                configureSimExceptionHandler(sType);

                enableBottom();

                renderSociety(mainAnimator.getSociety());
                positionAnimatorChart();
                startStopButton.setSelected(false);
                setSettings(sType);
            }

			private void configureSimExceptionHandler(SocietyType sType) {
				try {
                    mainAnimator = new Animator(sType, createSociety(fileMap.get(sType), CellShape.SQUARE));
                    changeShape.getSelectionModel().select(mainAnimator.getSociety().getSocietyGrid().getCellShape().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
			}

        });

    }

    public void enableBottom(){
        mainBox.setDisable(false);
    }

    /**
     * Sets all the cells into a gridpane to be displayed
     * @param Society calls the grid upon which the cells are built
     */
    private void renderSociety(Society xSociety){

        Group viewSociety = extractCells(xSociety);
        double tempScale = xSociety.getCellScale();
        while(viewSociety.getBoundsInParent().getWidth() > (societyPane.getWidth())){
            setCellScale(tempScale-=Double.parseDouble(UIProperties.getString("TempScale")), xSociety);
        }
        cellSlider.setValue(mainAnimator.getSociety().getCellScale());
        StackPane groupHolder = new StackPane(viewSociety);
        societyPane.setContent(groupHolder);
        societyPane.setFitToWidth(true);
        societyPane.setFitToHeight(true);

    }

	private Group extractCells(Society xSociety) {
		Group viewSociety = new Group();
        xSociety.getSocietyGrid().renderCells();
        for(Cell xCell : xSociety.getSocietyGrid().getCellMap().values()){
            viewSociety.getChildren().add(xCell);
            xCell.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent arg0){
                    xCell.setCurrentState(xCell.getStateSwapMap().get(xCell.getCurrentState()));
                }
            });
        }
		return viewSociety;
	}
	/**
     *The start, stop, and reset methods all call methods which require action on part of the animator.
     */
    private void start(){
        if(mainAnimator.isInitialized()){
            mainAnimator.play();
        }
        else{
            mainAnimator.initialize();
            mainAnimator.changeRate(animationSlider.getValue());
        }
        nextStep.setDisable(true);
    }

    private void stop(){
        if(mainAnimator.isInitialized()) {
            mainAnimator.stop();
        }
        nextStep.setDisable(false);
    }

    public Scene getPrimaryScene() {
        return primaryScene;
    }

    /**
     * Positions the chart
     */
    private void positionAnimatorChart() {
        graphTab.setContent(mainAnimator.getLineChart());
    }

    private void setCellScale(double xFactor, Society xSociety){
        xSociety.setCellScale(xFactor);
    }

    private void swapSettings(VBox vBox){
        settingsTab.setContent(vBox);
    }

    private void setSettings(SocietyType societyType){
    	SocietySettingsBox socBox = new SocietySettingsBox(mainAnimator);
        VBox societySettingsBox = socBox.getSettings(societyType);
        swapSettings(societySettingsBox);

    }

}