/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import org.joda.time.DateTime;

/**
 *
 * @author user 01
 */
public class Fechas {
    
    public static LocalDate getLocalFromTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            DateTime dateTime = new DateTime(timestamp.getTime());
            return LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        }
    }
    
    public static LocalTime getLocalFromTime(Time time) {
        if (time == null) {
            return null;
        } else {
            DateTime dateTime = new DateTime(time.getTime());
            return LocalTime.of(dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
        }
    }
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
    public static String getFechaConMes(DateTime dateTime) {
        String fecha = dateTime.getDayOfMonth() + " de " 
                + getMonthName(dateTime.getMonthOfYear()) 
                + " " + dateTime.getYear();
        return fecha;
    }
    
    public static String getFechaConMes(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        String fecha = dateTime.getDayOfMonth() + " de " 
                + getMonthName(dateTime.getMonthOfYear()) 
                + " " + dateTime.getYear();
        return fecha;
    }
    
    public static String getFechaConMes(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp.getTime());
        String fecha = dateTime.getDayOfMonth() + " de " 
                + getMonthName(dateTime.getMonthOfYear()) 
                + " " + dateTime.getYear();
        return fecha;
    }
    
    public static String getFechaConMesYHora(DateTime dateTime) {
        String fecha = dateTime.getDayOfMonth() + " de " 
                + getMonthName(dateTime.getMonthOfYear()) 
                + " " + dateTime.getYear() + " a las "
                + dateTime.toString("HH:mm:ss");
        return fecha;
    }
    
    public static String getFechaConMesYHora(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp.getTime());
        String fecha = dateTime.getDayOfMonth() + " de " 
                + getMonthName(dateTime.getMonthOfYear()) 
                + " " + dateTime.getYear() + " a las " 
                + dateTime.toString("HH:mm:ss");
        return fecha;
    }
    
    public static String getHora(Time time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
        return formatter.format(getLocalFromTime(time));
    }
    
    public static String differenceBetweenHours(Time time1, Time time2) {
        DateTime dateTime1 = new DateTime(time1.getTime());
        dateTime1 = dateTime1.plus(1);
        DateTime dateTime2 = new DateTime(time2.getTime());
        dateTime2 = dateTime2.plus(1);
        long c = dateTime2.getMillis() - dateTime1.getMillis();
        DateTime dateTime = new DateTime(c);
        dateTime = dateTime.plusHours(4);
        Time diff = new Time(dateTime.getMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(getLocalFromTime(diff));
    }
    
    public static String getFechaCorta(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp.getTime());
        return dateTime.toString("dd/MM/yyyy");
    }
    
    public static String getFechaCorta(DateTime dateTime) {
        return dateTime.toString("dd/MM/yyyy");
    }
    
    public static Time getTimeFromLocalTime(LocalTime local) {
        Time time = new Time(local.getHour(), local.getMinute(), 0);
        return time;
    }
    
     public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        java.util.Date today = new java.util.Date();

        java.util.Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
}
