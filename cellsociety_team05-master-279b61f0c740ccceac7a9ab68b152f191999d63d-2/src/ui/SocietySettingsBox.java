package src.ui;


import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import src.cell.state.*;
import src.society.property.SocietyType;
/**
 * The Society Settings Box sets individual society parameters for the simulation
 * Whenever a particular society type is chosen, the society settings box changes to 
 * allow the user to interact with parameters relevant to that particular society
 *By Justin Wang and Mina Mungekar
 *Refactored by Mina Mungekar
 */
public class SocietySettingsBox {
	private Animator mainAnimator;
	private ResourceBundle SettingsBoxProperties = ResourceBundle.getBundle("src.ui.property/UITags");
	private Integer insets = Integer.parseInt(SettingsBoxProperties.getString("TempBoxInsets"));
	private Integer spacing = Integer.parseInt(SettingsBoxProperties.getString("TempBoxSpacing"));
	private Map<SocietyType,VBox> settingsMap;
	private Slider aliveMaxRate, aliveMinRate;
	/**
     *Creates an instance of the settings box and initializes a map linking society type to the 
     *settings box that ought to be displayed
     *@param Animator takes in the UI's main animator and saves it
     */
	public SocietySettingsBox(Animator animator){
		this.mainAnimator = animator;
		settingsMap = new HashMap<SocietyType,VBox>();
		settingsMap.put(SocietyType.GAME_OF_LIFE, createGOLSettings());
		settingsMap.put(SocietyType.SEGREGATION, createSegregationSettings());
		settingsMap.put(SocietyType.WATOR, createWatorSettings());
		settingsMap.put(SocietyType.SPREADING_FIRE, createFireSettings());	
	}
	/**
     *Returns the settings box most relevant to the current society
     *@param societyType is the key in the map that holds the value of the Settings Box necessary for 
     *the simulation
     */
	public VBox getSettings(SocietyType societyType){
		return settingsMap.get(societyType);
	}

	private Slider configureSlider(){
		Slider slider = new Slider(){{
			setMin(Integer.parseInt(SettingsBoxProperties.getString("SliderMin")));
			setMax(Integer.parseInt(SettingsBoxProperties.getString("DefaultSliderMax"))); 
		}};
		return slider;
	}

	private void configureSegregationListener(Slider slider,SegregationState state, Label rateLabel) {
		slider.setValue(state.getSatisfactionRate());
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(mainAnimator.getSocietyType() == SocietyType.SEGREGATION){
					state.setSatisfactionRate(newValue.doubleValue());
					rateLabel.setText(String.valueOf(slider.getValue()));
				}
			}
		});
	}

	private Slider configureSegregationSlider(SegregationState state,VBox tempbox){
		Slider segSlider = configureSlider();
		Label rateLabel = new Label();
		rateLabel.setText(String.valueOf(segSlider.getValue()));
		configureSegregationListener(segSlider,state,rateLabel);
		tempbox.getChildren().addAll(new Text(SettingsBoxProperties.getString("SegSliderTitle"+state.toString())), 
				segSlider, rateLabel);
		return segSlider;

	}


	private VBox createTempBox() {
		VBox tempBox = new VBox(){{
			setSpacing(Integer.parseInt(SettingsBoxProperties.getString("TempBoxSpacing")));
			setAlignment(Pos.CENTER);
			setPadding(new Insets(insets, insets, insets, insets));
		}};
		return tempBox;
	}
	/**
     *Creates the sliders associated with the Segregation society and stores them in a VBox. Each
     *slider is associated with a listener that changes the simulation parameter as the user moves the slider.
     */
	private VBox createSegregationSettings() {
		VBox tempBox = createTempBox();
		Slider satisfactionRate = configureSegregationSlider(SegregationState.AGENT_X,tempBox);
		Slider satisfactionRateB = configureSegregationSlider(SegregationState.AGENT_O,tempBox);
		return tempBox;

	}			 

	private void configureFireListener(Slider slider,SpreadingFireState state, Label rateLabel) {
		slider.setValue(state.getCatchProb());
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(mainAnimator.getSocietyType() == SocietyType.SPREADING_FIRE){
					state.setCatchProb(newValue.doubleValue());
					rateLabel.setText(String.valueOf(slider.getValue()));
				}
			}
		});
	}

	private Slider configureFireSlider(SpreadingFireState state,VBox tempbox){
		Slider fireSlider = configureSlider();
		Label rateLabel = new Label();
		rateLabel.setText(String.valueOf(fireSlider.getValue()));
		configureFireListener(fireSlider,state,rateLabel);
		tempbox.getChildren().addAll(new Text(SettingsBoxProperties.getString("FireSliderTitle"+state.toString())), 
				fireSlider, rateLabel);
		return fireSlider;	
	}
	/**
     *Initializes sliders associated with the Fire simulation and stores them
     */
	private VBox createFireSettings(){
		VBox tempBox = createTempBox();
		Slider catchRate = configureFireSlider(SpreadingFireState.TREE,tempBox);
		return tempBox;
	}

	private Slider configureGOLSlider(Slider slider, Label rateLabel){
		slider.setMin(Integer.parseInt(SettingsBoxProperties.getString("GOLSliderMin")));
		slider.setMax(mainAnimator.getSociety().getSocietyGrid().getCellShape().getNumSides());
		slider.setMajorTickUnit(Integer.parseInt(SettingsBoxProperties.getString("GOLSliderMin")));
		slider.setShowTickMarks(true);
		slider.setMinorTickCount(0);
		slider.setSnapToTicks(true);
		rateLabel.setText(String.valueOf((int)slider.getValue()));
		return slider;
	}
	/**
     *Sets listener for the slider that sets the maximum count of dead cells
     *@param slider takes in the slider the listener is being attached to
     *@param takes in the label that changes as the slider is updated
     */
	private void configureGOLListenerDead(Slider slider, Label rateLabel) {
		slider.setValue(GameOfLifeState.DEAD.getMax());
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(mainAnimator.getSocietyType() == SocietyType.GAME_OF_LIFE){
					GameOfLifeState.DEAD.setMax(newValue.intValue());
					rateLabel.setText(String.valueOf((int)slider.getValue()));
				}
			}
		});
	}
	/**
     *Sets listener for slider that sets count of alive neighbors
     */
	private void configureGOLListenerAlive(Slider slider, Label rateLabel) {
		slider.setValue(GameOfLifeState.ALIVE.getMax());
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(aliveMinRate.getValue() <= newValue.intValue()){
					GameOfLifeState.ALIVE.setMax(newValue.intValue());
					rateLabel.setText(String.valueOf((int)slider.getValue()));
				}
				else{
					slider.setValue(aliveMinRate.getValue());
				}
			}
		});
	}
	/**
     *Sets listener for slider that sets the minimum count of alive neighbors
     */
	private void configureGOLListenerMin(Slider slider, Label rateLabel){
		slider.setValue(GameOfLifeState.ALIVE.getMin());
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(aliveMaxRate.getValue() >= newValue.intValue()){
					GameOfLifeState.ALIVE.setMin(newValue.intValue());
					rateLabel.setText(String.valueOf((int)slider.getValue()));
				}
				else{
					slider.setValue(aliveMaxRate.getValue());
				}
			}
		});
	}
	/**
     *Creates all the sliders and adds them to a VBox
     */
	private Slider createDeadParameterSlider(VBox tempBox){
		Label rateLabel = new Label();
		Slider slider = new Slider(){{
			configureGOLSlider(this,rateLabel);
		}};
		configureGOLListenerDead(slider,rateLabel);
		tempBox.getChildren().addAll(new Text(SettingsBoxProperties.getString("DeadParameterSliderTitle")), slider, rateLabel);
		return slider;
	}
	private Slider aliveMaxRateSlider(VBox tempBox){
		Label rateLabel = new Label();
		aliveMaxRate = new Slider(){{
			configureGOLSlider(this,rateLabel);
		}};
		configureGOLListenerAlive(aliveMaxRate,rateLabel);
		tempBox.getChildren().addAll(new Text(SettingsBoxProperties.getString("AliveRateMaxTitle")), aliveMaxRate, rateLabel);
		return aliveMaxRate;
	}
	private Slider aliveMinRateSlider(VBox tempBox){
		Label rateLabel = new Label();
		aliveMinRate = new Slider(){{
			configureGOLSlider(this,rateLabel);
		}};
		configureGOLListenerMin(aliveMinRate,rateLabel);
		tempBox.getChildren().addAll(new Text(SettingsBoxProperties.getString("AliveRateMinTitle")), aliveMinRate, rateLabel);
		return aliveMinRate;
	}

	private VBox createGOLSettings() {
		VBox tempBox = createTempBox();
		tempBox.setPadding(new Insets(spacing, insets, 0, insets));
		Slider deadParameter = createDeadParameterSlider(tempBox);
		aliveMaxRate = aliveMaxRateSlider(tempBox);
		aliveMinRate = aliveMinRateSlider(tempBox);
		return tempBox;

	}

	private void configureWatorListener(Slider slider, WatorState state, Label rateLabel) {
		slider.setValue(state.getBreedTime());
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(mainAnimator.getSocietyType() == SocietyType.WATOR){
					state.setBreedTime(newValue.intValue());
					rateLabel.setText(String.valueOf(slider.getValue()));
				}
			}
		});
	}

	private Slider configureWatorSlider(WatorState state,VBox tempbox){
		Slider watorSlider = initWatorSlider();
		Label rateLabel = new Label();
		rateLabel.setText(String.valueOf(watorSlider.getValue()));
		configureWatorListener(watorSlider,state,rateLabel);
		tempbox.getChildren().addAll(new Text(SettingsBoxProperties.getString("WatorSliderTitle"+state.toString())), 
				watorSlider, rateLabel);
		return watorSlider;

	} 


	private Slider initWatorSlider(){
		Slider slider = new Slider(){{
			setMin(Integer.parseInt(SettingsBoxProperties.getString("GOLSliderMin")));
			setMax(Integer.parseInt(SettingsBoxProperties.getString("WatorSliderMax")));
			setMajorTickUnit(Integer.parseInt(SettingsBoxProperties.getString("GOLSliderTickUnit")));
			setShowTickMarks(true);
			setMinorTickCount(Integer.parseInt(SettingsBoxProperties.getString("SliderMin")));
			setSnapToTicks(true); 
		}};
		return slider;
	}

	/**
     *Initializes sliders associated with the Wator simulation which control predator and prey breeding rates. They are later
     *stored in a VBox
     */
	private VBox createWatorSettings() {
		VBox tempBox = createTempBox();
		Slider predatorBreedRate = configureWatorSlider(WatorState.PREDATOR,tempBox);
		Slider preyBreedRate = configureWatorSlider(WatorState.PREY,tempBox);
		return tempBox;

	}

}


