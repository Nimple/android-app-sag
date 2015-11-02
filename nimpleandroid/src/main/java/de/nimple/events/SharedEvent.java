package de.nimple.events;

/**
 * Created by dennis on 29.11.2014.
 */
public class SharedEvent {

    private Type type;

    public SharedEvent(Type type){
        this.type = type;
    }

    public Type getType(){
        return type;
    }

    public enum Type{
        Card,
        Code,
        Contact,
        Contacts
    }
}
