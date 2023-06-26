/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.roymassaad.springchatapp;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

// Define the API endpoints

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    // save/post a message from a user to a room
    @PostMapping
    public void saveMessage(@RequestBody Message message) {
        DatabaseConnector.saveMessage(message);
    }

    // get all messages in a room
    @GetMapping("/{roomName}")
    public List<Message> getMessagesByRoomName(@PathVariable String roomName) throws SQLException {
                
        List<Message> messages = DatabaseConnector.getMessagesByRoomName(roomName);

        return messages;
    }
    
    // get new messages in a room
    // PS: clients send the last server side time stamp they received
    @GetMapping("/new/{roomName}")
    public List<Message> getNewMessagesByRoomName(@PathVariable String roomName, @RequestParam String timeStamp) throws SQLException {
                
        List<Message> messages = DatabaseConnector.getNewMessagesByRoomName(roomName, timeStamp);

        return messages;
    }
}
