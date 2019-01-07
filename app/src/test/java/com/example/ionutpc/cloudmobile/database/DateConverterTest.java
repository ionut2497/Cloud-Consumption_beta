package com.example.ionutpc.cloudmobile.database;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateConverterTest {

    @Test
    public void toDate() {
        Long input = Long.valueOf(1546895515);
        Date output;
        Date expected =new Date(1546895515);
        DateConverter converter = new DateConverter();
        output = DateConverter.toDate(input);
        assertEquals(expected,output);

    }

    @Test
    public void toTimestamp() {
        Date input = new Date();
        Long output;
        Long expected =input.getTime();
        DateConverter converter = new DateConverter();
        output = DateConverter.toTimestamp(input);
        assertEquals(expected,output);
    }
}