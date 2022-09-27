package com.zlk.design.creation.prototype.shallow;

public class School implements Cloneable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected School clone() throws CloneNotSupportedException {
        return (School)super.clone();
    }
}
