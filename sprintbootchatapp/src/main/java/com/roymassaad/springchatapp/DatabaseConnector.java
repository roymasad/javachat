/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.roymassaad.springchatapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    
    // using sqlite db in a data subfolder (copy it to dist)
    private static final String DATABASE_URL = "jdbc:sqlite:./data/database.db";

    // save client messsages
    // TODO: add some secret/key auth at least before accepting
    public static void saveMessage(Message message) {

        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL);

            PreparedStatement statement = connection.prepareStatement(
                 "INSERT INTO messages (room_name, user, message, createdAt) VALUES (?, ?, ?, ?)");

            statement.setString(1, message.getRoomName());
            statement.setString(2, message.getUser());
            statement.setString(3, message.getMessage());
            statement.setString(4, message.setCurrentTime()); //auto set server side

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error saving message: " + e.getMessage());
        }
    }

    // get all (up to limit 100) messages for a room
    // TODO: add auth secret/key, and maybe a variable for limit
    public static List getMessagesByRoomName(String roomName) {

        List<Message> messages = new ArrayList<>();

        try {        
            Connection connection = DriverManager.getConnection(DATABASE_URL);

            Statement statement = connection.createStatement();

            // preparing the query string without setstring (just easier to debug the string if need be)
            String query = "SELECT * FROM messages WHERE room_name = '" + roomName + "' ORDER BY id ASC LIMIT 100";

            ResultSet resultSet = statement.executeQuery(query);

            // load results into a list of messages
            while (resultSet.next()) {

                Message message = new Message();

                //message.setId(resultSet.getLong("id"));
                message.setRoomName(resultSet.getString("room_name"));
                message.setUser(resultSet.getString("user"));
                message.setMessage(resultSet.getString("message"));
                message.setCreatedAt(resultSet.getString("createdAt"));
                messages.add(message);

            }

            return messages;

        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }
        return null;
    }

    // only get new room messages as compared to the provided timestamp string
    // TODO: auth and limit variable
    public static List<Message> getNewMessagesByRoomName(String roomName, String lastTimestamp) {

        List<Message> messages = new ArrayList<>();

        try {

            Connection connection = DriverManager.getConnection(DATABASE_URL);

            PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM messages WHERE room_name = ? AND createdAt > ? ORDER BY id DESC LIMIT 100");

            statement.setString(1, roomName);
            statement.setString(2, lastTimestamp);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Message message = new Message();

                //message.setId(resultSet.getLong("id"));
                message.setRoomName(resultSet.getString("room_name"));
                message.setUser(resultSet.getString("user"));
                message.setMessage(resultSet.getString("message"));
                message.setCreatedAt(resultSet.getString("createdAt"));
                messages.add(message);
            }

            return messages;

        } catch (SQLException e) {
            System.err.println("Error retrieving new messages: " + e.getMessage());
        }

        return null;
    }


}
