JavaFX client side application to complement the Spring Boot backend

#Features:

-Uses JavaFX
-Connects to server using HTTP REST
-Can connect to a chat room using different user names
-Sends and reads messages
-Uses Polling to get new messages

#Building Notes:

This was created using Netbeans 18, Scenebuilder and the default JavaFX Ant project template with JDK17, JavaFX 17

To run in the CLI using the java command the module path parameters must be set

Also Javafx must be downloaded and copied over somewhere if your JRE doesnt include it (only custom ones include it now like Zulu)

Another dependency file that is needed is javax.json-1.1.4.jar (should be in the lib folder)

#Example:

java --module-path /Users/user1/Documents/javafx-sdk-17.0.7/lib --add-modules javafx.controls,javafx.fxml,javafx.web -jar ChatClientAntJavaFx.jar

Without it it will throw an error about missing Javafx runtime

Netbeans 18 saves the FXML files with Javafx version 20 tags but it runs fine with JavaFX 17 libs also it seems

#License:

MIT