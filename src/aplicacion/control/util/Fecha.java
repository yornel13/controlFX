/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import java.sql.Date;
import javafx.scene.control.ChoiceBox;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class Fecha {
    
    private String fecha;
    private String dia;
    private String mes;
    private String ano;

    public Fecha(String fecha) {
        this.fecha = fecha;
        this.dia = fecha.subSequence(6, 8).toString();
        this.mes = fecha.subSequence(4, 6).toString();
        this.ano = fecha.subSequence(0, 4).toString();
    }
    
    public Fecha(ChoiceBox selectorAno, ChoiceBox selectorMes, ChoiceBox selectorDia) {
        this.dia = (String) selectorDia.getSelectionModel().getSelectedItem();
        this.mes = (String) selectorMes.getSelectionModel().getSelectedItem();
        this.ano = (String) selectorAno.getSelectionModel().getSelectedItem();
        this.fecha = ano+mes+dia;
    }
    
    public Fecha(ChoiceBox selectorAno, ChoiceBox selectorMes, String dia) {
        this.dia = dia;
        this.mes = (String) selectorMes.getSelectionModel().getSelectedItem();
        this.ano = (String) selectorAno.getSelectionModel().getSelectedItem();
        this.fecha = ano+mes+dia;
    }
    
    public Fecha(String dia, String mes, String anio) {
        this.dia = dia;
        this.mes = mes;
        this.ano = anio;
        this.fecha = anio+mes+dia;
    }
    
    public String getFecha() {
        return fecha;
    }
    
    public Integer getFechaInt() {
        return Integer.valueOf(fecha);
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDia() {
        return dia;
    }
    
    public Integer getDiaInt() {
        return Integer.valueOf(dia);
    }

    public void setDia(String dia) {
        this.dia = dia;
        fecha = ano+mes+dia;
    }

    public String getMes() {
        return mes;
    }
    
    public Integer getMesInt() {
        return Integer.valueOf(mes);
    }

    public void setMes(String mes) {
        this.mes = mes;
        fecha = ano+mes+dia;
    }

    public String getAno() {
        return ano;
    }
    
    public Integer getAnoInt() {
        return Integer.valueOf(ano);
    }

    public void setAno(String ano) {
        this.ano = ano;
        fecha = ano+mes+dia;
    }
    
    @Override
    public String toString() {
        return fecha;
    }
    
    public String toStringInverse() {
        return dia+"-"+mes+"-"+ano;
    }
    
    public Fecha plusYears(int years) {
        Fecha newFecha = new Fecha(fecha);
        Integer newYears = newFecha.getAnoInt()+years;
        newFecha = new Fecha(
                newYears.toString()
                +newFecha.getMes()
                +newFecha.getDia());
        
        return newFecha;
    }
    
    public Fecha minusYears(int years) {
        Fecha newFecha = new Fecha(fecha);
        Integer newYears = newFecha.getAnoInt()-years;
        newFecha = new Fecha(
                newYears.toString()
                +newFecha.getMes()
                +newFecha.getDia());
        
        return newFecha;
    }
    
    public Fecha plusMonths(int months) {
        if (months == 0) {return this;}
        
        Fecha newFecha = new Fecha(fecha);
        Integer newMonth = newFecha.getMesInt()+months;
        if (newMonth > 12) {
            while (newMonth > 12) {
                newMonth += -12;
                newFecha = newFecha.plusYears(1);
            }
        }
        
        if (newMonth.toString().length() == 2) {
            newFecha = new Fecha(
                newFecha.getAno()
                +newMonth.toString()
                +newFecha.getDia()); 
        } else {
            newFecha = new Fecha(
                newFecha.getAno()
                +"0"+newMonth.toString()
                +newFecha.getDia());
        } 
        
        return newFecha;
    }
    
    public Fecha minusMonths(int months) {
        if (months == 0) {return this;}
        
        Fecha newFecha = new Fecha(fecha);
        Integer newMonth = newFecha.getMesInt()-months;
        if (newMonth < 1) {
            while (newMonth < 1) {
                newMonth += 12;
                newFecha = newFecha.minusYears(1);
            }
        }
        
        if (newMonth.toString().length() == 2) {
            newFecha = new Fecha(
                newFecha.getAno()
                +newMonth.toString()
                +newFecha.getDia()); 
        } else {
            newFecha = new Fecha(
                newFecha.getAno()
                +"0"+newMonth.toString()
                +newFecha.getDia());
        } 
        
        return newFecha;
    }
    
    public Fecha plusDays(int days) {
        if (days == 0) {return this;}
        
        Fecha newFecha = new Fecha(fecha);
        Integer newDay = newFecha.getDiaInt()+days;
        if (newDay > 30) {
            while (newDay > 30) {
                newDay += -30;
                newFecha = newFecha.plusMonths(1);
            }
        }
        
        if (newDay.toString().length() == 2) {
            newFecha = new Fecha(
                newFecha.getAno()
                +newFecha.getMes()
                +newDay.toString()); 
        } else {
            newFecha = new Fecha(
                newFecha.getAno()
                +newFecha.getMes()
                +"0"+newDay.toString());
        } 
        
        return newFecha;
    }
    
    public Fecha minusDays(int days) {
        if (days == 0) {return this;}
        
        Fecha newFecha = new Fecha(fecha);
        Integer newDay = newFecha.getDiaInt()-days;
        if (newDay < 1) {
            while (newDay < 1) {
                newDay += 30;
                newFecha = newFecha.minusMonths(1);
            }
        }
        
        if (newDay.toString().length() == 2) {
            newFecha = new Fecha(
                newFecha.getAno()
                +newFecha.getMes()
                +newDay.toString()); 
        } else {
            newFecha = new Fecha(
                newFecha.getAno()
                +newFecha.getMes()
                +"0"+newDay.toString());
        }  
        
        return newFecha;
    }
    
    public Boolean before(Fecha otherFecha) {
       
        return otherFecha.getFechaInt() > getFechaInt();
    }
    
    public Boolean beforeEquals(Fecha otherFecha) {
       
        return otherFecha.getFechaInt() >= getFechaInt();
    }
    
    public Boolean after(Fecha otherFecha) {
       
        return otherFecha.getFechaInt() < getFechaInt();
    }
    
    public Boolean afterEquals(Fecha otherFecha) {
       
        return otherFecha.getFechaInt() <= getFechaInt();
    }
    
    public int yearsDifference(Fecha nextFecha) {
        
       Fecha otherFecha = new Fecha(nextFecha.getFecha());
        
       int years = otherFecha.getAnoInt() - this.getAnoInt();
       otherFecha = otherFecha.minusYears(years);
       
       if (otherFecha.getFechaInt() < this.getFechaInt()) {
            years--; 
       }
       
       return years;
    }
    
    public int monthsDifference(Fecha nextFecha) {
        
        Fecha otherFecha = new Fecha(nextFecha.getFecha());
        
        if (otherFecha.getFechaInt() < this.getFechaInt()) {
            return 0;
        }
        
        if (otherFecha.getAnoInt().equals(this.getAnoInt())) {
            if (otherFecha.getMesInt().equals(this.getMesInt())) {
                return 0;
            } 
        }
        
        
        int years;
        int months = 0;
       
        years = yearsDifference(otherFecha);
        otherFecha = otherFecha.minusYears(years);

        if (!otherFecha.getAnoInt().equals(this.getAnoInt())) {
            months += otherFecha.getMesInt();
            otherFecha = otherFecha.minusMonths(months);
        }

        months += (otherFecha.getMesInt() - this.getMesInt());
        otherFecha = otherFecha.minusMonths(otherFecha.getMesInt() - this.getMesInt());

        if (otherFecha.getFechaInt() < this.getFechaInt()) {
            months--; 
        }
        
        months += (years*12);
        return months;
    }
    
    public int daysDifference(Fecha nextFecha) {
        
        Fecha otherFecha = new Fecha(nextFecha.getFecha());
        
        if (otherFecha.getFechaInt() <= this.getFechaInt()) {
            return 0;
        }
       
        int months;
        int days = 0;
       
        months = monthsDifference(otherFecha);
        otherFecha = otherFecha.minusMonths(months);
        
        if (!otherFecha.getMesInt().equals(this.getMesInt())) {
            days += otherFecha.getDiaInt();
            otherFecha = otherFecha.minusDays(days);
        }
        
        days += (otherFecha.getDiaInt()- this.getDiaInt());
        
        days += (months*30);
        
        return days;
    }
    
    
    public String getMonthName() {
        switch (getMesInt()) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
        }
        return null;
    }
    
    public String getMonthNameCort() {
        switch (getMesInt()) {
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
    
    public void setToSpinner(ChoiceBox selectorAno, 
            ChoiceBox selectorMes, 
            ChoiceBox selectorDia) {
         selectorAno.getSelectionModel().select(this.getAno());
         selectorMes.getSelectionModel().select(this.getMes());
         selectorDia.getSelectionModel().select(this.getDia());
    }
    
    public Date getDate() {
        DateTime dateTime = new DateTime(getAnoInt(), getMesInt(), getDiaInt(), 0, 0, 0);
        //dateTime = dateTime.withYear(getAnoInt());
        //dateTime = dateTime.withMonthOfYear(getMesInt());
        //dateTime = dateTime.withDayOfMonth(getDiaInt());
        return new Date(dateTime.getMillis());
    }
}
