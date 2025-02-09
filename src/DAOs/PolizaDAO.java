/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import seguroshibernate.HibernateUtil;

/**
 *
 * @author Laura Sánchez
 */
public class PolizaDAO {
    private Session sesion;
    private Transaction tx;
    
    public void iniciarOperacion(){
        this.sesion = HibernateUtil.getSessionFactory().getCurrentSession();
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
    
}
