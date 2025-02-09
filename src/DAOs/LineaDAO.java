/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import POJOs.Linea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import seguroshibernate.HibernateUtil;

/**
 *
 * @author Laura Sánchez
 */
public class LineaDAO {
    private Session sesion;
    private Transaction tx;
    private String fichero = "src/ficheros/Lineas.txt";
    
    public void iniciarOperacion(){
        this.sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        this.tx = sesion.beginTransaction();
    }
    
    public void manejarExcepcion(HibernateException he){
        tx.rollback();
        throw new HibernateException("Error en el acceso a la capa de datos\n", he);
    }
    
    /*
    +-------------------------+--------------+------+-----+---------+-------+
    | Field                   | Type         | Null | Key | Default | Extra |
    +-------------------------+--------------+------+-----+---------+-------+
    | codigo                  | int(11)      | NO   | PRI | NULL    |       |
    | descriptivo             | varchar(100) | YES  |     | NULL    |       |
    | familia                 | varchar(1)   | YES  |     | NULL    |       |
    | fechaLimiteContratacion | date         | YES  |     | NULL    |       |
    +-------------------------+--------------+------+-----+---------+-------+
    */
    
    public void volcarFichero() throws IOException{
        File ficheroLineas = new File(fichero);
        FileReader fReader;
        
        try {
            fReader = new FileReader(ficheroLineas);
            BufferedReader bReader = new BufferedReader(fReader);
            String linea = "";
            
            while((linea = bReader.readLine()) != null){
                String[] datos = new String[4];
                datos[0] = linea.substring(0, 3);               // codigo
                datos[1] = linea.substring(4,103).trim();       // descriptivo
                datos[2] = linea.substring(102, 103);           // familia
                datos[3] = linea.substring(104);                // fechaLimiteContratacion
                
                // int codigo, String descriptivo, String familia, Date fechaLimiteContratacion, Set subvencions, Set polizas
            }
            
            bReader.close();
            fReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LineaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
}
