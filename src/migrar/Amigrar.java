package migrar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import hibernate.dao.CargoDAO;
import hibernate.dao.DepartamentoDAO;
import hibernate.dao.DetallesEmpleadoDAO;
import hibernate.dao.EmpresaDAO;
import hibernate.dao.EstadoCivilDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.DetallesEmpleado;
import hibernate.model.Usuario;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Yornel
 */
public class Amigrar {

    private static Session session_1;
    
    
    public static void main(String[]args) {
        
        System.out.println("Migrando data:");
        
        /*
        System.out.println("Migrando cargos...");
        ArrayList<EmpTbcargoguardia> cargosViejos = new ArrayList<>();
        cargosViejos.addAll((List<EmpTbcargoguardia>) new EmpTbcargoguardiaDAO().findAll());
        
        for (EmpTbcargoguardia cargoViejo: cargosViejos) {
            Cargo cargo = new Cargo();
            cargo.setNombre(cargoViejo.getNombCargoguardia());
            cargo.setActivo(Boolean.TRUE);
            CargoDAO dao = new CargoDAO();
            dao.save(cargo);
            dao.changeId(cargo.getId(), cargoViejo.getCodiCargoguardia());
        } 
        
        System.out.println("Migrando empresas...");
        ArrayList<EmpTbempresasegu> empresasViejas = new ArrayList<>();
        empresasViejas.addAll(new EmpTbempresaseguDAO().findAllEmpresas());
        
        for (EmpTbempresasegu empresaVieja: empresasViejas) {
            Empresa empresa = new Empresa();
            empresa.setNombre(empresaVieja.getNombEmpresa());
            empresa.setSiglas(empresaVieja.getSiglEmpresa());
            empresa.setNumeracion(empresaVieja.getNrucEmpresa());
            empresa.setTelefono1(empresaVieja.getFon1Empresa());
            empresa.setTelefono2(empresaVieja.getFon2Empresa());
            empresa.setDireccion(empresaVieja.getDireEmpresa());
            empresa.setFax(empresaVieja.getNfaxEmpresa());
            empresa.setWeb(empresaVieja.getWebsEmpresa());
            empresa.setEmail(empresaVieja.getMailEmpresa());
            empresa.setObservacion(empresaVieja.getObseEmpresa());
            empresa.setActivo(Boolean.TRUE);
            empresa.setCreacion(new Timestamp(new Date().getTime()));
            empresa.setUltimaModificacion(new Timestamp(new Date().getTime()));
            empresa.setDiaCortePago(Integer.parseInt("23"));
            empresa.setTipo("Seguridad");
            EmpresaDAO empresaDAO = new EmpresaDAO();
            empresaDAO.save(empresa);
            empresaDAO.changeId(empresa.getId(), empresaVieja.getCodiEmpresa());
        }  
        
        System.out.println("Migrando clientes...");
        ArrayList<WebTblCliente> clientesViejos = new ArrayList<>();
        clientesViejos.addAll((List<WebTblCliente>) new WebTblClienteDAO().findAll());
        
        for (WebTblCliente clienteViejo: clientesViejos) {
            Cliente cliente = new Cliente();
            cliente.setNombre(clienteViejo.getRazonSocial());
            cliente.setDetalles("");
            cliente.setRuc("Falta Nruc.");
            cliente.setTelefono("Falta Tlf.");
            cliente.setDireccion("Falta direccion.");
            cliente.setActivo(Boolean.TRUE);
            cliente.setCreacion(new Timestamp(new Date().getTime()));
            cliente.setUltimaModificacion(new Timestamp(new Date().getTime()));
            ClienteDAO clienteDAO = new ClienteDAO();
            clienteDAO.save(cliente);
        } */ 
        
        System.out.println("Migrando empleados guardias...");
        ArrayList<SegTbguardia> empleadosViejos = new ArrayList<>();
        empleadosViejos.addAll((List<SegTbguardia>) new SegTbguardiaDAO().findAll());
        UsuarioDAO usuariosDAO = new UsuarioDAO();
        DetallesEmpleadoDAO detallesEmpleadoDAO = new DetallesEmpleadoDAO();
         
        for (SegTbguardia empleadoViejo: empleadosViejos) {
            DetallesEmpleado detallesEmpleado = new DetallesEmpleado();
            detallesEmpleado.setEmpresa(new EmpresaDAO().findById(empleadoViejo.getCodiEmpresa().intValue()));
            detallesEmpleado.setDepartamento(new DepartamentoDAO().findById(2));
            detallesEmpleado.setCargo(new CargoDAO().findById(empleadoViejo.getIdCargo()));
            detallesEmpleado.setFechaInicio(new Timestamp(empleadoViejo.getFingGuardia().getTime()));
            detallesEmpleado.setFechaContrato(new Timestamp(empleadoViejo.getFingGuardia().getTime()));
            detallesEmpleado.setExtra(empleadoViejo.getObseGuardia());
            detallesEmpleado.setNroCuenta("Sin especificar");
            detallesEmpleado.setSueldo(empleadoViejo.getSueldo());
            detallesEmpleado.setQuincena(0d);
            detallesEmpleado.setAcumulaDecimos(true);

            detallesEmpleadoDAO.save(detallesEmpleado);

            Usuario usuario = new Usuario();
            usuario.setNombre(empleadoViejo.getNombGuardia());
            usuario.setApellido(empleadoViejo.getApelGuardia() 
                    + " " + empleadoViejo.getApel2Guardia());
            usuario.setCedula(empleadoViejo.getCeduGuardia());
            usuario.setSexo(empleadoViejo.getSexoGuardia());
            usuario.setEmail(empleadoViejo.getMailGuardia());
            usuario.setNacimiento(new Timestamp(empleadoViejo.getFnacGuardia().getTime()));
            usuario.setDireccion(empleadoViejo.getDomiGuardia());
            usuario.setTelefono(empleadoViejo.getFon1Guardia());
            usuario.setDetallesEmpleado(detallesEmpleado);
            usuario.setFoto(empleadoViejo.getFoto());
            usuario.setEstadoCivil(new EstadoCivilDAO().findById(empleadoViejo.getEcivGuardia()));
            if (empleadoViejo.getStatGuardia().equalsIgnoreCase("A"))
                usuario.setActivo(Boolean.TRUE);
            else 
                usuario.setActivo(Boolean.FALSE);
            usuario.setCreacion(new Timestamp(new Date().getTime()));
            usuario.setUltimaModificacion(new Timestamp(new Date().getTime()));

            usuariosDAO.save(usuario);
            
            usuariosDAO.changeId(usuario.getId(), empleadoViejo.getCodiGuardia());
        }  
        
        
        
        
        System.out.println("Migracion completada.");
    } 

    
}
