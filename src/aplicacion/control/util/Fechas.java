/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import org.joda.time.DateTime;

/**
 *
 * @author user 01
 */
public class Fechas {
    
    public static LocalDate getDateFromTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            DateTime dateTime = new DateTime(timestamp.getTime());
            return LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
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
    
    public static String getFechaCorta(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp.getTime());
        return dateTime.toString("dd/MM/yyyy");
    }
    
    public static String getFechaCorta(DateTime dateTime) {
        return dateTime.toString("dd/MM/yyyy");
    }
}
