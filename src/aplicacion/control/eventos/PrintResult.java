/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.eventos;

/**
 *
 * @author Yornel
 */
public interface PrintResult {

    void onPrintUpdate(int current, int total);

    void onPrintSuccessful();

    void onPrintFailure(String error);
}
