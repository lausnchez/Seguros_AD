/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import POJOs.DetallePoliza;
import POJOs.Poliza;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import seguroshibernate.HibernateUtil;



/**
 *
 * @author Laura Sánchez
 */
public class DetallePolizaDAO {
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
    +------------------+------------+------+-----+---------+-------+
    | Field            | Type       | Null | Key | Default | Extra |
    +------------------+------------+------+-----+---------+-------+
    | referencia       | varchar(9) | NO   | PRI | NULL    |       |
    | digitoControl    | int(11)    | NO   |     | NULL    |       |
    | fechaAlta        | date       | YES  |     | NULL    |       |
    | fechaVencimiento | date       | YES  |     | NULL    |       |
    +------------------+------------+------+-----+---------+-------+
    */
    
    public DetallePoliza generarDetallePoliza(String referencia, Poliza poliza, int digitoControl, Date alta, Date vencimiento){
        DetallePoliza detalle = new DetallePoliza(referencia, poliza, digitoControl, alta, vencimiento);
        try{
            iniciarOperacion();
            sesion.save(detalle);
            tx.commit();
            System.out.println("Detalle creado con éxito.");
            return detalle;
        }catch(HibernateException he){
            manejarExcepcion(he);
            System.err.println(he);
            System.out.println("Error creando el detalle de la póliza");
            return null;
        }finally{
            sesion.close();
        }  
    }
    
    public DetallePoliza buscarDetallePoliza(Poliza poliza){
        DetallePoliza resultado = null;
        String query = "FROM DetallePoliza dp WHERE referencia=:refPoliza";
        try{
            iniciarOperacion();
            resultado = (DetallePoliza)sesion.createQuery(query).setString("refPoliza", poliza.getReferencia()).uniqueResult();
            if(resultado == null){
                System.out.println("No se encontraron resultados.");
            }
        }catch(HibernateException he){
            manejarExcepcion(he);
            throw he;
        }finally{
            sesion.close();
        }
        return resultado;
    }
    
    public void borrarDetallePoliza(DetallePoliza detalle){
        try{
            iniciarOperacion();
            sesion.delete(detalle);
            tx.commit();
            System.out.println("Detalle de póliza " + detalle.getReferencia() + " eliminada.");
        }catch(HibernateException he){
            manejarExcepcion(he);
            throw he;
        }finally{
            sesion.close();
        }
    }
}
