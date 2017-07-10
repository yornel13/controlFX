package migrar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.Numeros;
import hibernate.dao.AbonoDeudaDAO;
import hibernate.dao.CuotaDeudaDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.DeudaTipoDAO;
import hibernate.model.AbonoDeuda;
import hibernate.model.CuotaDeuda;
import hibernate.model.Deuda;
import hibernate.model.DeudaTipo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yornel
 */
public class MigrasDeudas {

    public static void main(String[]args) {
        
        System.out.println("Migrando data:");
        
        System.out.println("Migrando deudas...");
        
        ArrayList<Deuda> deudas = new ArrayList<>();
        deudas.addAll((List<Deuda>) new DeudaDAO().findAll());
        
        /*for (Deuda deuda: deudas) {
            
            System.out.println("Deuda de "+deuda.getUsuario().getApellido()+" "+deuda.getUsuario().getNombre()+". Nro: "+deuda.getId());
            
            ArrayList<AbonoDeuda> abonos = new ArrayList<>();
            abonos.addAll((List<AbonoDeuda>) new AbonoDeudaDAO().findAllByDeudaId(deuda.getId()));

            for (AbonoDeuda abono: abonos) {
                CuotaDeuda cuota = new CuotaDeuda();
                cuota.setFecha(abono.getPagoMes().getFinMes());
                cuota.setMonto(abono.getMonto());
                cuota.setPagoMes(abono.getPagoMes());
                cuota.setCreado(Fechas.getToday());
                cuota.setEditado(Fechas.getToday());
                cuota.setAplazado(Boolean.FALSE);
                cuota.setDeuda(deuda);
                
                new CuotaDeudaDAO().save(cuota);
            }
            
            if (!deuda.getPagada()) {
                
                int numeroC = deuda.getCuotas();
                
                Double monto = deuda.getRestante() / numeroC;
                monto = Numeros.round(monto);
                
                Fecha fecha = Fechas.getFechaActual();
                if (fecha.getDiaInt() > 28) {
                    fecha = fecha.plusMonths(1);
                }
                fecha.setDia("30");
                
                for (int number = 0; numeroC > number; number++) {
                    
                    CuotaDeuda cuota = new CuotaDeuda();
                    cuota.setFecha(fecha.plusMonths(number).getFecha());
                    cuota.setMonto(monto);
                    cuota.setPagoMes(null);
                    cuota.setCreado(Fechas.getToday());
                    cuota.setEditado(Fechas.getToday());
                    cuota.setAplazado(Boolean.FALSE);
                    cuota.setDeuda(deuda);
                    
                    new CuotaDeudaDAO().save(cuota);
                    
                }
            }
        } 
        
        System.out.println("Migrando id tipo a deudas...");
        
        for (Deuda deuda: deudas) {
            DeudaTipo tipo = (DeudaTipo) new DeudaTipoDAO().findByNombre(deuda.getTipo()).get(0);
            if (tipo != null) {
                deuda.setTipoDeuda(tipo);
                hibernate.HibernateSessionFactory.getSession().flush();
            }
        }*/
        
        
        for (Deuda deuda: deudas) {
            ArrayList<CuotaDeuda> cuotaDeuda = (ArrayList<CuotaDeuda>) new CuotaDeudaDAO().findAllByDeudaId(deuda.getId());
            deuda.setCuotasTotal(cuotaDeuda.size());
            hibernate.HibernateSessionFactory.getSession().flush();
        }
        
        
        
        System.out.println("Migracion finalizada.");
    } 

    
}
