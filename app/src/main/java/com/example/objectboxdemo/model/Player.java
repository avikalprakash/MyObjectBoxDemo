package com.example.objectboxdemo.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by AsifMoinul on 1/13/2018.
 */

@Entity
public class Player {
    @Id
    private long id;
    private String name;
    private int jerseyNumber;

    public Player() {

    }

    public Player(long id, String name, int jerseyNumber) {
        this.id = id;
        this.name = name;
        this.jerseyNumber = jerseyNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
}