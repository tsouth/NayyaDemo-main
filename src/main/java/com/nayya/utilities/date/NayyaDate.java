package com.nayya.utilities.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NayyaDate {
    private final Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    public NayyaDate() {
        this(new Date());
    }

    public NayyaDate(Date date) {
        calendar.setTime(date);
    }

    public NayyaDate dateAndTime() {
        this.format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            NayyaDate date = (NayyaDate) obj;

            return toString().equals(date.toString());
        }
        return false;
    }

    @Override
    public String toString() {
        Date date = calendar.getTime();

        return format.format(date);
    }
}
