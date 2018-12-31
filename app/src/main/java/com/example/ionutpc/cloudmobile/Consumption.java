package com.example.ionutpc.cloudmobile;

public class Consumption {
    private double sms;
    private double smsmax;
    private double minutes;
    private double minutesmax;
    private double data;
    private double maxdata;

    public Consumption(double sms, double smsmax, double minutes, double minutesmax, double data, double maxdata) {
        this.sms = sms;
        this.smsmax = smsmax;
        this.minutes = minutes;
        this.minutesmax = minutesmax;
        this.data = data;
        this.maxdata = maxdata;
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
}
