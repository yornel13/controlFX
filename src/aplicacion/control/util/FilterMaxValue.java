/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


/**
 *
 * @author Yornel
 */

public class FilterMaxValue implements EventHandler<KeyEvent> {

        private int maxVal;

        public FilterMaxValue (int i) {
            this.maxVal= i;
        }

        public void handle(KeyEvent arg0) {

            TextField tx = (TextField) arg0.getSource();
            String chara = arg0.getCharacter();
            if (tx.getText().equals(""))
                return;

            Double valor;
            if (chara.equals(".")) {
                valor = Double.parseDouble(tx.getText() + chara + "0");
            } else {
                try {
                    valor = Double.parseDouble(tx.getText() + chara);
                } catch (NumberFormatException e) {
                    //The other filter will prevent this from hapening
                    return;
                }
            }
            if (valor > maxVal) {
                arg0.consume();
            }

        }
    }
