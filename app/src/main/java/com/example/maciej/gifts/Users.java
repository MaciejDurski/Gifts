package com.example.maciej.gifts;

public class Users {

    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int status;
    private String name;

    public Users(int ID, String name, int status){
        this.setID(ID);
        this.setName(name);
        this.setStatus(status);
    }
}
