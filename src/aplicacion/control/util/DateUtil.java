/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import java.util.Calendar;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Locale;

/**
 *
 * @author Yornel
 */
public class DateUtil {
    
    public static Date addDays(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    public static Date addMonths(Date date, int months){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    public static Date addYears(Date date, int years){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    public static Date removeDays(Date date, int days){ // no probado
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days); //minus number would decrement the days
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    public static Date removeMonths(Date date, int months){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -months);
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    public static Date removeYears(Date date, int years){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -years);
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    public static String getShortDate(Date date){
        
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        
        return df.format(date);
    }
    
    public static String getLongDate(Date date){
  
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        String stringDate = day + " de " 
                + getMonthName(month) + " " +year;
     
        return stringDate;
    }
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month);
        return cal.getDisplayName(Calendar.MONTH, 
                Calendar.LONG, 
                Locale.getDefault());
    }
}
