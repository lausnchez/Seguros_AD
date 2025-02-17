/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import POJOs.Asegurado;
import POJOs.DetallePoliza;
import POJOs.Linea;
import POJOs.Poliza;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import seguroshibernate.HibernateUtil;
import seguroshibernate.Utils;

/**
 *
 * @author Laura Sánchez
 */
public class PolizaDAO {
    private Session sesion;
    private Transaction tx;
    
    public void iniciarOperacion(){
        this.sesion = HibernateUtil.getSessionFactory().openSession();
        this.tx = sesion.beginTransaction();
    }
    
    public void manejarExcepcion(HibernateException he){
        tx.rollback();
        throw new HibernateException("Error en el acceso a la capa de datos\n", he);
    }
    
    /*
    +---------------+---------------+------+-----+---------+-------+
    | Field         | Type          | Null | Key | Default | Extra |
    +---------------+---------------+------+-----+---------+-------+
    | referencia    | varchar(9)    | NO   | PRI | NULL    |       |
    | digitoControl | int(11)       | NO   |     | NULL    |       |
    | asegurado     | int(11)       | NO   | MUL | NULL    |       |
    | linea         | int(11)       | NO   | MUL | NULL    |       |
    | importe       | decimal(13,2) | YES  |     | NULL    |       |
    +---------------+---------------+------+-----+---------+-------+
    
    Cada póliza (tabla "Poliza") se contrata para un asegurado (tabla "Asegurado")
    y una línea de seguro (tabla "Linea"). Las líneas de seguro indican una serie
    de condiciones específicas para contratar una póliza.

    */
    
    /**
     * Busca una póliza con un asegurado y una línea en específico. En caso de
     * no existir devolverá true y en caso negativo devolverá false.
     * 
     * @param asegurado
     * @param linea
     * @return 
     */
    public boolean existePoliza(Asegurado asegurado, Linea linea){
        boolean polizaEncontrada = false;
        Poliza poliza = null;
        String query = "FROM Poliza p WHERE linea=:paramLinea AND asegurado=:paramAsegurado";
        
        try{
            iniciarOperacion();
            poliza = (Poliza)sesion.createQuery(query)
                    .setInteger("paramLinea", linea.getCodigo())
                    .setInteger("paramAsegurado", asegurado.getId())
                    .uniqueResult();
        }catch(HibernateException he){
            manejarExcepcion(he);
            throw he;
        }finally{
            sesion.close();
        }
        
        if(poliza == null){ return false;}
        else return true;
    }
    
    
    public float designarImportePoliza(){
        // Se debe pedir un importe numérico y que sea mayor que 0
        float importe;
        Scanner scan = new Scanner(System.in);
        Boolean valido = true;
        String peticion;
        
        do{
            valido = true;
            System.out.print("Inserte un valor para el importe de la póliza: ");
            peticion = scan.nextLine();
            if(!Utils.comprobarFloat(peticion)){
               valido = false; 
                System.out.println("Debe insertar un número con decimales (usando una coma).");
            }else{
                if(Float.parseFloat(peticion) < 0){
                    valido = false;
                    System.out.println("Debe insertar un número positivo.");
                }
            }
        }while(!valido);
        System.out.println("Importe de póliza: " + peticion + "€");
        return Float.parseFloat(peticion);
    }
    
    public void generarPoliza(Asegurado asegurado, Linea linea, Float importePoliza){
        String referencia = linea.getFamilia();
    }

    
    public int encontrarMayorReferencia(String familia){
        String resultado;
        
        try{
           iniciarOperacion();
           
           String query = "SELECT MAX(SUBSTRING(referencia,2,8)) FROM Poliza p WHERE referencia LIKE :letra";
           resultado = (String)sesion.createQuery(query).setString("letra", familia + '%').uniqueResult();
        }catch(HibernateException he){
            manejarExcepcion(he);
            throw he;
        }finally{
            sesion.close();
        }
        if(resultado == null){ resultado = "0";}
        return Integer.parseInt(resultado);
    }
    
    public int generarDigitoDeControl(int referencia){
        return referencia%6;
    }
    
    public void crearPoliza(String referencia, Asegurado asegurado, Linea linea, int digitoControl, float importe ,Date alta, Date vencimiento){
        DetallePolizaDAO detalleDAO = new DetallePolizaDAO();
        
        Poliza nuevaPoliza = new Poliza(referencia, asegurado, linea, digitoControl);
        Boolean polizaCreada;
        try{
            iniciarOperacion();
            sesion.save(nuevaPoliza);
            tx.commit();
            polizaCreada = true;
            System.out.println("Póliza creada con éxito. Procede a crear el detalle de la póliza y el importe.");
        }catch(HibernateException he){
            System.out.println("Error al crear la póliza base");
            manejarExcepcion(he);
            polizaCreada = false;
            throw he;
        }finally{
            sesion.close();
        }
        
        // En caso de que se haya creado la póliza sin problemas se crea el detalle
        if(polizaCreada){
            DetallePoliza detallePoliza = detalleDAO.generarDetallePoliza(referencia, nuevaPoliza, digitoControl, alta, vencimiento);
            nuevaPoliza.setDetallePoliza(detallePoliza);
            nuevaPoliza.setImporte(BigDecimal.valueOf(Double.parseDouble(String.valueOf(importe))));
            
            try{
                iniciarOperacion();
                sesion.update(nuevaPoliza);
                tx.commit();
                System.out.println("Importe y detalles asignados a la póliza.");
                System.out.println("Póliza " + nuevaPoliza.getReferencia() + " creada.");
            }catch(HibernateException he){
                manejarExcepcion(he);
                throw he;
            }finally{
                sesion.close();
            }
            
        }
    }
    
    public List<Poliza> recogerPolizasDeUsuario(Asegurado asegurado){
        List<Poliza> polizasUsuario = null;
        try{
            iniciarOperacion();
            String query = "FROM Poliza p WHERE asegurado =:asegurado";
            polizasUsuario = sesion.createQuery(query).setInteger("asegurado", asegurado.getId()).list();
            if(polizasUsuario != null){
                System.out.println("\nSe han encontrado " + polizasUsuario.size() + " pólizas del usuario " + asegurado.getId());
            }else System.out.println("\nNo se han encontrado pólizas.");
        }catch(HibernateException he){
            manejarExcepcion(he);
            throw he;
        }finally{
            sesion.close();
        }
        
        return polizasUsuario;
    }
    
    public void mostrarVariasPolizas(List<Poliza> subvencionesUsuario){
        for(Poliza pol: subvencionesUsuario){
                    mostrarPoliza(pol);
                }
    }
    
    public void mostrarPoliza(Poliza poliza){
        System.out.println("----------------------------------------------------");
        System.out.println("Referencia: " + poliza.getReferencia());
        System.out.println("Digito de control: " + poliza.getDigitoControl());
        System.out.println("Asegurado: " + poliza.getAsegurado().getId());
        System.out.println("Linea de seguro: " + poliza.getLinea().getCodigo());
        System.out.println("Importe: " + poliza.getImporte());
        
        System.out.println("\nDetalles de la póliza: ");
        System.out.println("Referencia: " + poliza.getDetallePoliza().getReferencia());
        System.out.println("Dígito de control: " + poliza.getDetallePoliza().getDigitoControl());
        System.out.println("Fecha de Alta: " + poliza.getDetallePoliza().getFechaAlta());
        System.out.println("Fecha de Vencimiento: " + poliza.getDetallePoliza().getFechaVencimiento());
        
    }
    
}
