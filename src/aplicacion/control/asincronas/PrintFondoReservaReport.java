/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.asincronas;

import aplicacion.control.eventos.PrintResult;
import aplicacion.control.reports.ReporteRolDePagoFondoReserva;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Numeros.round;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Yornel
 */
public class PrintFondoReservaReport implements Runnable {

    private final List<EmpleadoTable> empleadoTables;
    private final Fecha inicio;
    private final Fecha fin;
    private final PrintResult listener;

    public PrintFondoReservaReport(List<EmpleadoTable> empleadoTables, 
            Fecha inicio, Fecha fin, PrintResult listener) {
     
        this.empleadoTables = empleadoTables;
        this.inicio = inicio;
        this.fin = fin;
        this.listener = listener;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            print();
        } catch (InterruptedException ex) {
            listener.onPrintFailure("Cancecelado");
        }
    }
    
    private void print() {
        try {
            for (EmpleadoTable empleadoTable: empleadoTables) {
                
                int pos = empleadoTables.indexOf(empleadoTable);
                
                Platform.runLater(() -> {
                    listener.onPrintUpdate(pos+1, empleadoTables.size());
                });
                
                Usuario user = empleadoTable.getUsuario();

                ReporteRolDePagoFondoReserva datasource = new ReporteRolDePagoFondoReserva();
                datasource.add(empleadoTable);
                
                InputStream inputStream = new FileInputStream(Const.REPORTE_PAGO_FONDO_RESERVA);

                Map<String, Object> parametros = new HashMap();
                parametros.put("empleado", user.getNombre() + " " + user.getApellido());
                parametros.put("cedula", user.getCedula());
                parametros.put("cargo", user.getDetallesEmpleado().getCargo().getNombre());
                parametros.put("empresa", user.getDetallesEmpleado().getEmpresa().getNombre());
                parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
                parametros.put("total", round(empleadoTable.getMonto()).toString());

                JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                String filename = "pago_fondo_reserva";

                File pdf = File.createTempFile(filename, ".pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                        user.getEmail(), Const.ASUNTO_FONDO_RESERVA, 
                        "Recibo de fondo de reserva del mes que empieza el " 
                                + getFechaConMes(inicio) 
                                + " y termina el " 
                                + getFechaConMes(fin), 
                        pdf.getPath(), filename + ".pdf");
            }
            Platform.runLater(() -> {
                listener.onPrintSuccessful();
            });

        } catch (JRException | IOException ex) {
            ex.printStackTrace();
            Platform.runLater(() -> {
                listener.onPrintFailure("Error de impresion");
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Platform.runLater(() -> {
                listener.onPrintFailure("Error de impresion");
            });
        } 
    }
}
