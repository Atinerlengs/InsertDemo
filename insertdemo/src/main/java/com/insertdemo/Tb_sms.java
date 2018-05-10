package com.insertdemo;

/**
 * Created by liqiang on 18-4-18.
 */

public class Tb_sms {
    private String number;
    private int type;
    private boolean isRead;

    public Tb_sms() {
        super();
    }

    public Tb_sms(String number, int type, boolean isRead) {
        super();
        this.number = number;
        this.type = type;
        this.isRead = isRead;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Tb_contacts [number=" + number + "]";
    }
}
