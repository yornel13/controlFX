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
    // Constantes de items de pago
    public static final String IP_DECIMO_TERCERO = "decimo_tercero";
    public static final String IP_DECIMO_CUARTO = "decimo_cuarto";
    public static final String IP_ADELANTO_QUINCENAL = "adelanto_quincenal";
    public static final String IP_HORAS_EXTRAS = "horas_extras";
    public static final String IP_BONOS = "bonos";
    public static final String IP_SUELDO = "sueldo";
    public static final String IP_IESS = "iess";
    public static final String IP_DEUDA = "deuda";
    
    // Reportes
    public static final String REPORTE_ROL_PAGO_INDIVIDUAL = "report/rol_pago_individual.jrxml";
    public static final String REPORTE_ROL_GENERAL_MENSUAL= "report/rol_general_mensual.jrxml";
    public static final String REPORTE_HORAS_TRABAJADAS = "report/historico_dias.jrxml";
    public static final String REPORTE_DEUDAS_EMPLEADO = "report/deudas_empleado.jrxml";
    public static final String REPORTE_DEUDAS_EMPLEADOS = "report/deudas_varios.jrxml";
    public static final String REPORTE_ADELANTO_QUINCENAL_EMPLEADOS = "report/adelanto_quincenal_varios.jrxml";
    public static final String REPORTE_SUELDO_EMPLEADOS = "report/sueldo_varios.jrxml";
    public static final String REPORTE_ACUMULACION_DECIMOS_EMPLEADOS = "report/acumulacion_decimos_varios.jrxml";
    public static final String REPORTE_DECIMOS_ACUMULADOS_POR_MES = "report/decimos_acumulados_por_mes.jrxml";
    public static final String REPORTE_ACTUARIALES_EMPLEADOS = "report/actuariales_varios.jrxml";
    public static final String REPORTE_IESS_EMPLEADOS = "report/iess_varios.jrxml";
    public static final String REPORTE_DESCUENTOS_EMPLEADOS = "report/descuentos_varios.jrxml";
    public static final String REPORTE_AUDITORIA = "report/auditoria.jrxml";
    public static final String REPORTE_ROL_CLIENTE = "report/rol_cliente.jrxml";
    public static final String REPORTE_ROL_INDIVIDUAL = "report/rol_individual.jrxml";
    public static final String REPORTE_ROL_VACACIONES = "report/rol_vacaciones.jrxml";
    public static final String REPORTE_ROL_UNITARIO = "report/rol_unitario.jrxml";
    public static final String REPORTE_PAGO_ADELANTO_QUINCENAL = "report/adelanto_quincenal_individual.jrxml";
    public static final String REPORTE_DEUDA_EMPLEADO_INDIVIDUAL = "report/deuda_empleado_individual.jrxml";
    public static final String REPORTE_DEUDA_EMPLEADO_INDIVIDUAL_SIN_ABONO = "report/deuda_empleado_individual_sin_abono.jrxml";
    public static final String REPORTE_DECIMOS_MES_EMPLEADO = "report/decimos_mes_empleado.jrxml";
    public static final String REPORTE_DECIMO_TERCERO_ACUMULADOS_POR_MES = "report/decimo_tercero_acumulados_por_mes.jrxml";
    public static final String REPORTE_DECIMO_CUARTO_ACUMULADOS_POR_MES = "report/decimo_cuarto_acumulados_por_mes.jrxml";
    public static final String REPORTE_PAGO_DECIMO_TERCERO = "report/pago_decimo_tercero.jrxml";
    public static final String REPORTE_PAGO_DECIMO_CUARTO = "report/pago_decimo_cuarto.jrxml";
    public static final String REPORTE_DECIMO_TERCERO_TOTAL_EMPLEADO = "report/decimo_tercero_total_empleado.jrxml";
    public static final String REPORTE_DECIMO_CUARTO_TOTAL_EMPLEADO = "report/decimo_cuarto_total_empleado.jrxml";
    public static final String REPORTE_PLANILLA_IESS_EMPLEADOS = "report/planilla_iess_varios.jrxml";
    public static final String REPORTE_DIAS_DERECHO_VACACIONES = "report/reporte_dias_vacaciones.jrxml";
    
    
    //Reportes nuevos de gestion
    public static final String REPORTE_SUMATORIA_HORAS = "report/reporte_horas_extras.jrxml";
    public static final String REPORTE_SUMATORIA_JUBILACION = "report/reporte_jubilacion_patronal.jrxml";
    public static final String REPORTE_SUMATORIA_APORTE = "report/reporte_aporte_patronal.jrxml";
    public static final String REPORTE_SUMATORIA_RESERVA = "report/reporte_fondo_reserva.jrxml";
    public static final String REPORTE_SUMATORIA_VACACIONES = "report/reporte_vacaciones.jrxml";
    public static final String REPORTE_SUMATORIA_UNIFORMES = "report/reporte_uniformes.jrxml";
    public static final String REPORTE_SUMATORIA_SEGUROS = "report/reporte_seguros.jrxml";
    public static final String REPORTE_SUMATORIA_BONOS = "report/reporte_bonos.jrxml";
    public static final String REPORTE_SUMATORIA_TRANSPORTE = "report/reporte_transporte.jrxml";
    public static final String REPORTE_SUMATORIA_DECIMO_TERCERO = "report/reporte_decimos_tercero.jrxml";
    public static final String REPORTE_SUMATORIA_DECIMO_CUARTO = "report/reporte_decimos_cuarto.jrxml";
    public static final String REPORTE_SUMATORIA_IESS = "report/reporte_iess.jrxml";
    
    /************* Correo **************/
    // Asunto
    public static final String ASUNTO_ROL_INDIVIDUAL = "Pago Mensual";
    public static final String ASUNTO_HORAS = "Horas Trabajadas en el mes";
    public static final String ASUNTO_ADELANTO_QUINCENAL = "Adelanto Quincenal";
    public static final String ASUNTO_DEUDAS = "Deudas pendientes";
    public static final String ASUNTO_VACACIONES = "Liquidacion de Vacaciones";
    public static final String ASUNTO_PAGO_DECIMO_TERCERO = "Pago Decimo Tercero";
    public static final String ASUNTO_PAGO_DECIMO_CUARTO = "Pago Decimo Cuarto";
    public static final String ASUNTO_DECIMOS_GENERADO = "Decimos Generados";
    public static final String ASUNTO_DECIMO_TERCERO_GENERADO = "Decimo Tercero Generado";
    public static final String ASUNTO_DECIMO_CUARTO_GENERADO = "Decimo Cuarto Generado";
    // Mensaje
    public static final String MENSAJE_ROL_INDIVIDUAL = "Recibo de rol de pago";
    
    //Casos en Horarios
    public static final String TRABAJO = "T"; 
    public static final String LIBRE = "L";
    public static final String FALTA = "F";
    public static final String VACACIONES = "V";
    public static final String PERMISO = "P";
    public static final String DM = "D";  // Descanso Medico
    public static final String CM = "C";  // Cita Medica
    
    // Dias de la semana
    public static final String LUNES = "Lunes"; 
    public static final String MARTES = "Martes"; 
    public static final String MIERCOLES = "Miercoles"; 
    public static final String JUEVES = "Jueves"; 
    public static final String VIERNES = "Viernes"; 
    public static final String SABADO = "Sabado"; 
    public static final String DOMINGO = "Domingo"; 
}
