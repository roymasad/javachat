
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author roymassaad
 */
public class SettingsController implements Initializable{
    
    // bind them to the fxml file
    @FXML
    private TextField userName;
    
    @FXML
    private TextField roomName;
        
    @FXML
    private TextField serverURL;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
    }
    
    // On Connect Button press
    @FXML
    private void ConnectAction(ActionEvent event)  throws IOException{ 
    
        // abort if any of the fields is empty
        if (userName.getText() == "" || roomName.getText() == "" || serverURL.getText() == "") return;
        
        // load the fields in the global app varaibles
        NewFXMain.user = userName.getText();
        
        NewFXMain.room = roomName.getText();
        
        NewFXMain.url = serverURL.getText() + "/chat/api/messages"; // append path context detail
        
        Parent chatPage = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        
        Scene chatScene = new Scene(chatPage, 450, 450);
        
        NewFXMain.primaryStage.setScene(chatScene);

        
    }

    
}
