package com.example.ionutpc.cloudmobile.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "consumptions")
public class ConsumptionEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double sms;
    private double smsmax;
    private double minutes;
    private double minutesmax;
    private double data;
    private double maxdata;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @Ignore
    public ConsumptionEntry(double sms, double smsmax, double minutes, double minutesmax, double data, double maxdata, Date updatedAt) {
        this.sms = sms;
        this.smsmax = smsmax;
        this.minutes = minutes;
        this.minutesmax = minutesmax;
        this.data = data;
        this.maxdata = maxdata;
        this.updatedAt = updatedAt;
    }

    public ConsumptionEntry(int id, double sms, double smsmax, double minutes, double minutesmax, double data, double maxdata, Date updatedAt) {
        this.id = id;
        this.sms = sms;
        this.smsmax = smsmax;
        this.minutes = minutes;
        this.minutesmax = minutesmax;
        this.data = data;
        this.maxdata = maxdata;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ConsumptionEntry{" +
                "id=" + id +
                ", sms=" + sms +
                ", smsmax=" + smsmax +
                ", minutes=" + minutes +
                ", minutesmax=" + minutesmax +
                ", data=" + data +
                ", maxdata=" + maxdata +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSms() {
        return sms;
    }

    public void setSms(double sms) {
        this.sms = sms;
    }

    public double getSmsmax() {
        return smsmax;
    }

    public void setSmsmax(double smsmax) {
        this.smsmax = smsmax;
    }

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(double minutes) {
        this.minutes = minutes;
    }

    public double getMinutesmax() {
        return minutesmax;
    }

    public void setMinutesmax(double minutesmax) {
        this.minutesmax = minutesmax;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public double getMaxdata() {
        return maxdata;
    }

    public void setMaxdata(double maxdata) {
        this.maxdata = maxdata;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
