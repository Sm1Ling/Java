package ru.hse.edu.azamatarifkhanov198;

public class StringHolder{

    private int hashcode;
    private String string;
    private Byte type; //0 оригинал //1 удаленная //2 добавленная //3 измененная


    StringHolder(String str, byte type){
        string = str;
        hashcode = hashCode(str);
        this.type = type;
    }

    private int hashCode(String str){
        return str.hashCode(); //можно и лучше но мне лень
    }

    public int getHashcode() {
        return hashcode;
    }

    public String getString() {
        return string;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}