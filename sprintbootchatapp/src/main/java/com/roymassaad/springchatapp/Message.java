/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.roymassaad.springchatapp;

import java.text.SimpleDateFormat;
import java.util.Date;

// message ORM class
public class Message {
    
    //TODO add type field (text is default, but we can use it for image links)
    private Long id;
    private String roomName;
    private String user;
    private String message;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // deprecated, keeping it for reference
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    // auto set the current server timestamp on creation
    public String setCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedTime = dateFormat.format(new Date(currentTimeMillis));
        this.createdAt = formattedTime;
        return this.createdAt;
    }

    
}