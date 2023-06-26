/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author roymassaad
 * Main chat client side app class
 */

public class NewFXMain extends Application {
    
    static public Stage primaryStage;
     
    // main app variables
    // TODO: save them in app settings/properties
    static public String user;
    
    static public String room;
    
    static public String url;
    
    @Override
    public void start(Stage stage) throws IOException {
         
        // Show settings page before connecting
        Parent settingsPage = FXMLLoader.load(getClass().getResource("Settings.fxml"));      
    
        Scene settingsScene = new Scene(settingsPage, 450, 450);
        
        // TODO, not sure using Static refs is the best way to nagivate
        NewFXMain.primaryStage = stage;
        
        NewFXMain.primaryStage.setTitle("JavaFX Chat Ant");
        NewFXMain.primaryStage.setScene(settingsScene);
        NewFXMain.primaryStage.setResizable(false);
        NewFXMain.primaryStage.show();
        
        NewFXMain.primaryStage.setOnCloseRequest(event -> {
            
            System.out.println("Closing application...");
            System.exit(0);
            
        });
        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
