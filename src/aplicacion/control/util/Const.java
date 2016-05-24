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
public class Const {
    
    public static final String BACKGROUND_COLOR_SEMI_TRANSPARENT = "-fx-background-color: linear-gradient(rgba(255, 255, 255, 0.3), rgba(0, 0, 0, 0.2));";
    public static final String BACKGROUND_COLOR_TRANSPARENT = "-fx-background-color: transparent;";
    
    public static final String DECIMO_CUARTO = "decimo_cuarto";
    public static final String DECIMO_TERCERO = "decimo_tercero";
    public static final String IESS = "iess";
    
    
   // Pago tipos
    public static final String ROL_PAGO_INDIVIDUAL = "rol_pago_individual";
    public static final String ROL_PAGO_CLIENTE = "rol_pago_cliente";
    public static final String ROL_PAGO_ADMINISTRATIVO = "rol_pago_administrativo";
    
    // Reportes
    public static final String REPORTE_ROL_INDIVIDUAL = "rol_pago_individual.jrxml";
    public static final String REPORTE_DEUDAS_EMPLEADO = "deudas_empleado.jrxml";
    public static final String REPORTE_DEUDAS_EMPLEADOS = "deudas_varios.jrxml";
    public static final String REPORTE_ADELANTO_QUINCENAL_EMPLEADOS = "adelanto_quincenal_varios.jrxml";
    public static final String REPORTE_ACUMULACION_DECIMOS_EMPLEADOS = "acumulacion_decimos_varios.jrxml";
    public static final String REPORTE_ACTUARIALES_EMPLEADOS = "actuariales_varios.jrxml";
    
    /************* Correo **************/
    // Asunto
    public static final String ASUNTO_ROL_INDIVIDUAL = "Pago mensual";
    public static final String ASUNTO_DEUDAS = "Deudas pendientes";
    // Mensaje
    public static final String MENSAJE_ROL_INDIVIDUAL = "Recibo de rol de pago";
   
}
