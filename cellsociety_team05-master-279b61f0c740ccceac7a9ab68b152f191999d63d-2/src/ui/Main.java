package src.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import src.ui.UserInterfaceX;

/**
 * Created by Mina Mungekar
 */

public class Main extends Application{

    private UserInterfaceX userInterface;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.userInterface = new UserInterfaceX();

        primaryStage.setScene(userInterface.getPrimaryScene());
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
