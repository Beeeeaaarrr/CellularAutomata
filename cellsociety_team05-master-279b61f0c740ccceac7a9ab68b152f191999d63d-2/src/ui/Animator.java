package src.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.util.Duration;
import src.society.Society;
import src.society.property.SocietyType;

import java.util.ResourceBundle;

/**
 * Created by Mina Mungekar
 */

public class Animator {

    private final String UITAGS_FILEPATH = "src.ui.property/UITags";
    private ResourceBundle AnimatorProperties = ResourceBundle.getBundle(this.UITAGS_FILEPATH);;

    private final int FRAMES_PER_SECOND = Integer.parseInt(AnimatorProperties.getString("FramesPerSecond"));
    private final int MILLISECOND_DELAY = Integer.parseInt(AnimatorProperties.getString("DelayFactor")) / FRAMES_PER_SECOND;
    private Timeline mainTimeline;

    private Society currentSociety;
    private SocietyType societyType;
    private Chart animatorChart;
    private boolean isInitialized;
    private boolean isStopped;

    /**
     * @param currentSociety takes in a Society and set of rules for which the Animator
     * needs to simulate
     */
    public Animator(SocietyType societyType, Society currentSociety){
        this.currentSociety = currentSociety;
        this.societyType = societyType;
        this.animatorChart =  new Chart(societyType, this.currentSociety);
        this.isInitialized = false;
        this.isStopped = true;
    }

    /**
     * @return returns the society the animator is simulating
     */
    public Society getSociety(){
        return this.currentSociety;
    }

    /**
     * @return returns the chart the animator generates
     */
    public LineChart getLineChart(){
        return this.animatorChart.getChart();
    }

    /**
     * Initializes and plays animation with default parameters
     */
    public void initialize(){
        this.mainTimeline = new Timeline();
        this.mainTimeline.setCycleCount(Timeline.INDEFINITE);
        this.changeFrames(MILLISECOND_DELAY);
        this.isInitialized = true;
        this.isStopped = false;
    }

    public boolean isInitialized(){
        return this.isInitialized;
    }

    public SocietyType getSocietyType(){
        return societyType;
    }

    /**
     * Plays animation
     */
    public void play(){
        this.mainTimeline.play();
        this.isStopped = false;
    }

    /**
     * Stops animation
     */
    public void stop(){
        this.mainTimeline.pause();
        this.isStopped = true;
    }

    public boolean isStopped(){
        return isStopped;
    }

    /**
     * Sets the rate of animation
     * @param rate
     */
    public void changeRate(double rate){
        if(isInitialized){
            this.mainTimeline.setRate(rate);
        }
    }

    /**
     * Retrieves the main timeline for use in parallel transitions
     * @return the main timeline
     */
    public Timeline getMainTimeline(){
        return this.mainTimeline;
    }

    public void nextStep(){
        updateSociety();
    }

    /**
     * The method changes the delay between keyframes to slow or speed them up
     * @param duration gives a keyframe to be added to the timeline;
     * duration specifies the time delay between keyframes
     */
    private void changeFrames(double duration){

        this.mainTimeline.stop();

        KeyFrame frame = new KeyFrame(Duration.millis(duration),
                g -> updateSociety());

        this.mainTimeline.getKeyFrames().add(frame);
        this.mainTimeline.play();

    }

    /**
     * Calls the two methods in Society which update all cells, then simultaneously
     * swap them to their next state. The chart with the counts of the different
     * states is updated as well.
     */
    private void updateSociety(){

        this.currentSociety.updateNextState(this.currentSociety.getSocietyGrid());
        this.currentSociety.advanceNextState(this.currentSociety.getSocietyGrid());
        this.animatorChart.updateChart();

    }

}
