/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**Create a basic object call Record. Contains name String, type String, and 
 * value String. Name describes the name of the record. Type describes the type 
 * of the record. Value describes the value of the record. This object contain
 * constructor that will create the object, with user input of name, value and 
 * type. This class contain all the accessor and modifier.
 * @author mk
 */
public class Record {
    String name;
    String type;
    String value;

    public Record(String name, String value, String type) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
