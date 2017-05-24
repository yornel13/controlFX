/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Yornel
 */
public class GuardarText {
    
    public void saveFile(ArrayList<String> lineas, String path){
        BufferedWriter bufferedWriter = null;
            try {
            //Construct the BufferedWriter object
                bufferedWriter = new BufferedWriter(new FileWriter(path));
                //Start writing to the output stream
                for (String text: lineas) {
                    bufferedWriter.write(text);
                    bufferedWriter.newLine();
                }
                bufferedWriter.write("");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
            //Close the BufferedWriter
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
