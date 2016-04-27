/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

/**
 *
 * @author Yornel
 */
import java.util.Enumeration;
import javafx.scene.control.ToggleGroup;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

public class GroupButtonUtils {

    public static String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }

    
}
