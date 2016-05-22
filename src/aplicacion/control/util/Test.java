/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import java.util.Date;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DateTime date = new DateTime();
        System.err.println(date.toString("dd/MM/yyyy"));
        
    }
    
}
