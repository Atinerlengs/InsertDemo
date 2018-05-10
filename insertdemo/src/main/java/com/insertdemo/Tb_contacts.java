package com.insertdemo;

/**
 * Created by liqiang on 18-4-18.
 */

public class Tb_contacts {
    private String name;
    private String number;

    public Tb_contacts() {
        super();
    }

    public Tb_contacts(String name, String number) {
        super();
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Tb_contacts [name=" + name + ", number=" + number + "]";
    }

}
