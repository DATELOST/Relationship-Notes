package com.jnu.relationshipnotes.dataprocesser;

import java.io.Serializable;

public class Gift implements Serializable {
    private String name;
    private String date;
    private String reason;
    private int money;
    public Gift(String name, String date, String reason, int money) {
        this.name = name;
        this.date = date;
        this.reason = reason;
        this.money = money;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }
}
