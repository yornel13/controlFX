/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import aplicacion.control.AplicacionControl;

/**
 *
 * @author Yornel
 */
public class Roboto {
    public static javafx.scene.text.Font MEDIUM(Integer size) {
       return javafx.scene.text.Font.loadFont(AplicacionControl.class
               .getResource("font/Roboto-Medium.ttf").toExternalForm(), size);
    }
    
    public static javafx.scene.text.Font REGULAR(Integer size) {
       return javafx.scene.text.Font.loadFont(AplicacionControl.class
               .getResource("font/Roboto-Regular.ttf").toExternalForm(), size);
    }
}
