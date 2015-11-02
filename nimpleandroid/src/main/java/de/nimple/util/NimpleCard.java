package de.nimple.util;

/**
 * Created by dennis on 04.10.2014.
 */
public class NimpleCard {

    private int id;
    private String name;

    public NimpleCard(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString(){
        return name;
    }
}
