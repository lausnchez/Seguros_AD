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
public class SubvencionIdDAO {
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
}
