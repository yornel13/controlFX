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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    public static String getFechaConMesSinAno(DateTime dateTime) {
        String fecha = dateTime.getDayOfMonth() + " de " 
                + getMonthName(dateTime.getMonthOfYear());
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
    
    public static String getFechaConMesCorto(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp.getTime());
        String fecha = dateTime.getDayOfMonth() + " de " 
                + getMonthName(dateTime.getMonthOfYear()).substring(0,3)
                + " " + dateTime.getYear();
        return fecha;
    }
    
    public static String getFechaConMes(Fecha fecha) {
        String fechaString = fecha.getDiaInt()+" de " 
                + fecha.getMonthName() 
                + " " + fecha.getAnoInt();
        return fechaString;
    }
    
    public static String getFechaSoloMesYAno(Fecha fecha) {
        String fechaString = fecha.getMonthName()+" "+fecha.getAnoInt();
        return fechaString;
    }
    
    public static String getFechaConMes(String fechaFecha) {
        Fecha fecha = new Fecha(fechaFecha);
        String fechaString = fecha.getDiaInt()+" de " 
                + fecha.getMonthName() 
                + " " + fecha.getAnoInt();
        return fechaString;
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
    
    public static String getFechaCorta(Date date) {
        DateTime dateTime = new DateTime(date.getTime()).plusHours(1); // con una hora adicional porque pone un dia anterior erroneo por el cambio horario
        return dateTime.toString("dd/MM/yyyy");
    }
    
    public static String getFechaCorta(DateTime dateTime) {
        return dateTime.toString("dd/MM/yyyy");
    }
    
    public static Time getTimeFromLocalTime(LocalTime local) {
        Time time = new Time(local.getHour(), local.getMinute(), 0);
        return time;
    }
    
    public static Timestamp getToday() {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        java.util.Date today = new java.util.Date();

        java.util.Date todayWithZeroTime = null;
        try {
            todayWithZeroTime = formatter.parse(formatter.format(today));
        } catch (ParseException ex) {
            Logger.getLogger(Fechas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
     
    public static ObservableList<String> arraySpinnerDia() {
        String[] items = new String[30];
        items[0] = "01";
        items[1] = "02";
        items[2] = "03";
        items[3] = "04";
        items[4] = "05";
        items[5] = "06";
        items[6] = "07";
        items[7] = "08";
        items[8] = "09";
        items[9] = "10";
        items[10] = "11";
        items[11] = "12";
        items[12] = "13";
        items[13] = "14";
        items[14] = "15";
        items[15] = "16";
        items[16] = "17";
        items[17] = "18";
        items[18] = "19";
        items[19] = "20";
        items[20] = "21";
        items[21] = "22";
        items[22] = "23";
        items[23] = "24";
        items[24] = "25";
        items[25] = "26";
        items[26] = "27";
        items[27] = "28";
        items[28] = "29";
        items[29] = "30";
        return FXCollections.observableArrayList(items);
    }
    
    public static ObservableList<String> arraySpinnerMes() {
        String[] items = new String[12];
        items[0] = "01";
        items[1] = "02";
        items[2] = "03";
        items[3] = "04";
        items[4] = "05";
        items[5] = "06";
        items[6] = "07";
        items[7] = "08";
        items[8] = "09";
        items[9] = "10";
        items[10] = "11";
        items[11] = "12";
        return FXCollections.observableArrayList(items);
    }
    
    public static ObservableList<String> arraySpinnerMesText() {
        String[] items = new String[12];
        items[0] = "Ene";
        items[1] = "Feb";
        items[2] = "Mar";
        items[3] = "Abr";
        items[4] = "May";
        items[5] = "Jun";
        items[6] = "Jul";
        items[7] = "Ago";
        items[8] = "Sep";
        items[9] = "Oct";
        items[10] = "Nov";
        items[11] = "Dic";
        return FXCollections.observableArrayList(items);
    }
    
    public static ObservableList<String> arraySpinnerAno() {
        String[] items = new String[20];
        Integer count = 0;
        
        Integer secuencia = (new DateTime().getYear()) - 10;
        for (String number : items) {
            items[count] = secuencia.toString();
            secuencia++;
            count++;
        }
        return FXCollections.observableArrayList(items);
    }
    
    public static ObservableList<String> arraySpinnerAnoCorto(int year) {
        String[] items = new String[3];
        Integer count = 0;
        
        Integer secuencia = year - 1;
        for (String number : items) {
            items[count] = secuencia.toString();
            secuencia++;
            count++;
        }
        return FXCollections.observableArrayList(items);
    }
    
    public static Fecha getFechaActual() {
        
        String dia;
        String mes;
        String ano;
        ano = String.valueOf(new DateTime().getYear());
        mes = String.valueOf(new DateTime().getMonthOfYear());
        if (new DateTime().getDayOfMonth() > 30) {
            dia = "30";
        } else {
            dia = String.valueOf(new DateTime().getDayOfMonth());
        }
        
        if (mes.length() == 1) {
            mes = "0"+mes;
        }
        if (dia.length() == 1) {
            dia = "0"+dia;
        }
        
        return new Fecha(ano+mes+dia);
    }
    
    public static Fecha getFecha(DateTime dateTime) {
        
        String dia;
        String mes;
        String ano;
        ano = String.valueOf(dateTime.getYear());
        mes = String.valueOf(dateTime.getMonthOfYear());
        if (dateTime.getDayOfMonth() > 30) {
            dia = "30";
        } else {
            dia = String.valueOf(dateTime.getDayOfMonth());
        }
        
        if (mes.length() == 1) {
            mes = "0"+mes;
        }
        if (dia.length() == 1) {
            dia = "0"+dia;
        }
        
        return new Fecha(ano+mes+dia);
    }
    
    public static Fecha getFecha(Timestamp timestamp) {
        
        String dia;
        String mes;
        String ano;
        ano = String.valueOf((timestamp.getYear()+1900));
        mes = String.valueOf((timestamp.getMonth()+1));
        if (timestamp.getDate() > 30) {
            dia = "30";
        } else {
            dia = String.valueOf(timestamp.getDate());
        }
        
        if (mes.length() == 1) {
            mes = "0"+mes;
        }
        if (dia.length() == 1) {
            dia = "0"+dia;
        }
        
        return new Fecha(ano+mes+dia);
    }
    
    public static Fecha getFechaStart(Timestamp timestamp) {
        
        String dia;
        String mes;
        String ano;
        ano = String.valueOf((timestamp.getYear()+1900));
        mes = String.valueOf((timestamp.getMonth()+1));
        dia = "01";
        
        if (mes.length() == 1) {
            mes = "0"+mes;
        }
        
        return new Fecha(ano+mes+dia);
    }
    
    public static String getMonthNameCort(Integer mes) {
        switch (mes) {
            case 1:
                return "Ene";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Abr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Ago";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dic";
        }
        return null;
    }
}
