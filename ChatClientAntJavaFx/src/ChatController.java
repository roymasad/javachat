import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URISyntaxException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URLEncoder;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

// Controller class in charge of the chat fxml page
public class ChatController implements Initializable {

    //private static final String API_URL = "http://localhost:8080/chat/api/messages";
    
    // reduce this to get faster message updates on clients but increase load on server
    private static final long FETCH_INTERVAL = 5000; 

    // we are displaying in a webview the messages
    // TODO better security for html injection attacks?
    @FXML
    private WebView chatMessages;

    @FXML
    private TextField inputText;

    private List<String> messages;
    
    private Timer timer;
    
    private String lastTimeStamp;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Connect(); // wrote it as an external function that can be call from different places
    }
    

    // connect/load server room message on load
    public void Connect() {
                   
        Platform.runLater(() -> inputText.requestFocus());
        Platform.runLater(() -> getAllMessages());  // get all messages for the first time
        
        messages = new ArrayList<>();
                
        timer = new Timer();
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    
                    // load any new messages only each 5 secs
                    getNewMessages(); 
                    
                });
            }
        }, FETCH_INTERVAL, FETCH_INTERVAL);
        
    }
    
    // cancel the timer before going back to the settings/connect page
    @FXML 
    private void ExitChat() throws IOException{
        
        timer.cancel();
        
        Parent settingsPage = FXMLLoader.load(getClass().getResource("Settings.fxml"));      
    
        Scene settingsScene = new Scene(settingsPage, 450, 450);
        
        NewFXMain.primaryStage.setScene(settingsScene);
        
    }


    // send a message button action
    @FXML
    private void SendTextAction(ActionEvent event) {

        try {
            
            Send();
            inputText.setText(""); //reset input field
            
        } catch (Exception ex) {
            
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    // on keyboard ENTER send also
    // TODO is throwing a bunch of exceptions out better than putting a try catch ?
    @FXML
    private void onEnterKeyPressed(KeyEvent event) throws URISyntaxException, IOException, InterruptedException {
        
        if (event.getCode() == KeyCode.ENTER) Send();
         
    }

    // connect to http endpoing and send message as JSON
    private void Send() throws URISyntaxException, IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(NewFXMain.url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"user\": \"" + NewFXMain.user + "\", \"message\": \"" + inputText.getText()  + "\", \"roomName\": \"" + NewFXMain.room + "\"}"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        // TODO: do something with the response to validate reception
        //int statusCode = response.statusCode();
        //String responseBody = response.body();

        getNewMessages();

    }

    // get all messages for a certain room
    // TODO add auth secret/key
    public List<String> getAllMessages() {

        try {
            
            // format URL
            URL url = new URL(NewFXMain.url + "/" + NewFXMain.room);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            
            JsonReader reader = Json.createReader(inputStream);

            JsonArray jsonArray = reader.readArray();

            for (JsonValue jsonValue : jsonArray) {
                
                JsonObject jsonMessage = (JsonObject) jsonValue;

                String message = jsonMessage.getString("message");
                
                String user = jsonMessage.getString("user");
                
                lastTimeStamp = jsonMessage.getString("createdAt");

                // add formatting and concantinate 
                // TODO render timestamp also 
                messages.add("<br><strong>" + user + "</strong> : "+ message + "<br>");
                    
            }
            
            if (lastTimeStamp == null) lastTimeStamp = "0"; // in case no message loaded in a new room

            connection.disconnect();

        } catch (IOException e) {
            
            e.printStackTrace();
        }
        
        // sort them in reverse to put newer first in the webview
        StringBuilder sb = new StringBuilder();
        for (int i = messages.size() - 1; i >= 0; i--) {
            sb.append(messages.get(i));
        }

        String joinedMessages = sb.toString();

        // refresh the webview
        chatMessages.getEngine().loadContent(joinedMessages, "text/html");
        
        return messages;
        
    }
    
    // Get only new room messages
    public List<String> getNewMessages() {

        int counter = 0;
        
        try {
            
            String encodedTimestamp = URLEncoder.encode(lastTimeStamp, "UTF-8"); // remove any white spaces
            
            // format URL
            URL url = new URL(NewFXMain.url + "/new/"+NewFXMain.room+"?timeStamp=" + encodedTimestamp);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            
            JsonReader reader = Json.createReader(inputStream);

            JsonArray jsonArray = reader.readArray();

            // read new messages
            for (JsonValue jsonValue : jsonArray) {
                
                counter++; // keep a counter of new messages
                
                JsonObject jsonMessage = (JsonObject) jsonValue;

                String message = jsonMessage.getString("message");
                
                String user = jsonMessage.getString("user");
                
                lastTimeStamp = jsonMessage.getString("createdAt");

                messages.add("<br><strong>" + user + "</strong> : "+ message + "<br>");
            }
            
            //System.out.println("Retrieved  " + counter );
            
            connection.disconnect();

        } catch (IOException e) {
            
            e.printStackTrace();
        }
        
        // reverve messages to display last first
        StringBuilder sb = new StringBuilder();
        for (int i = messages.size() - 1; i >= 0; i--) {
            sb.append(messages.get(i));
        }

        String joinedMessages = sb.toString();

        // refresh webview if there were new messages
        if (counter != 0) chatMessages.getEngine().loadContent(joinedMessages, "text/html");
        
        //System.out.println("Message Length " + messages.toArray().length );
        
        return messages;
        
    }
}
