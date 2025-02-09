/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public class SubvencionDAO {
    private Session sesion;
    private Transaction tx;
    private String fichero = "src/ficheros/Subvenciones.txt";
    
    public void iniciarOperacion(){
        this.sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        this.tx = sesion.beginTransaction();
    }
    
    public void manejarExcepcion(HibernateException he){
        tx.rollback();
        throw new HibernateException("Error en el acceso a la capa de datos\n", he);
    }
    
    /*
    +-----------+--------------+------+-----+---------+-------+
    | Field     | Type         | Null | Key | Default | Extra |
    +-----------+--------------+------+-----+---------+-------+
    | asegurado | int(11)      | NO   | PRI | NULL    |       |
    | linea     | int(11)      | NO   | PRI | NULL    |       |
    | importe   | decimal(3,0) | YES  |     | NULL    |       |
    | asunto    | varchar(200) | NO   |     | NULL    |       |
    +-----------+--------------+------+-----+---------+-------+
    */
    
    public void volcarFichero() throws IOException{
        File ficheroLineas = new File(fichero);
        FileReader fReader;
        
        try {
            fReader = new FileReader(ficheroLineas);
            BufferedReader bReader = new BufferedReader(fReader);
            String linea = "";
            
            while((linea = bReader.readLine()) != null){    // linea.length(116)
                String[] datos = new String[4];
                datos[0] = linea.substring(0, 12);               // asegurado
                 System.out.println(linea.length());               
                // SubvencionId id, Asegurado asegurado, Linea linea, String asunto
            }
            
            bReader.close();
            fReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LineaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
}
