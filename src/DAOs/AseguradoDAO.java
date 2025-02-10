/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import POJOs.Asegurado;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class AseguradoDAO {
    private Session sesion;
    private Transaction tx;
    private String fichero = "src/ficheros/Asegurados.txt";
    
    public void iniciarOperacion(){
        this.sesion = HibernateUtil.getSessionFactory().openSession();
        this.tx = sesion.beginTransaction();
    }
    
    public void manejarExcepcion(HibernateException he){
        tx.rollback();
        throw new HibernateException("Error en el acceso a la capa de datos\n", he);
    }
    
    /*
    +-----------------+-------------+------+-----+---------+----------------+
    | Field           | Type        | Null | Key | Default | Extra          |
    +-----------------+-------------+------+-----+---------+----------------+
    | id              | int(11)     | NO   | PRI | NULL    | auto_increment |
    | DNI             | varchar(9)  | NO   |     | NULL    |                |
    | nombre          | varchar(20) | YES  |     | NULL    |                |
    | apellido1       | varchar(50) | YES  |     | NULL    |                |
    | apellido2       | varchar(50) | YES  |     | NULL    |                |
    | fechaNacimiento | date        | YES  |     | NULL    |                |
    +-----------------+-------------+------+-----+---------+----------------+   
    */
    
    public void volcarFichero(){
        File ficheroLineas = new File(fichero);
        FileReader fReader;
        
        try {
            fReader = new FileReader(ficheroLineas);
            BufferedReader bReader = new BufferedReader(fReader);
            String linea = "";
            
            while((linea = bReader.readLine()) != null){
                String[] datos = new String[5];
                datos[0] = linea.substring(0, 9);              // dni
                datos[1] = linea.substring(9, 29);             // nombre, agregar espacios
                datos[2] = linea.substring(29,79);             // apellido1
                datos[3] = linea.substring(79,129);            //apellido2
                datos[4] = linea.substring(129);               //fecha
                
                System.out.println("dni:" + datos[0]);
                System.out.println("nombre:" + datos[1]);
                System.out.println("apellido1:" + datos[2]);
                System.out.println("apellido2:" + datos[3]);
                System.out.println("fecha:" + datos[4]);
                
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                Date fecha = format.parse(datos[4]);
                
                // String dni, String nombre, String apellido1, String apellido2, Date fechaNacimiento
                Asegurado nuevoAsegurado = new Asegurado(datos[0], datos[1], datos[2], datos[3], fecha, null, null);
             
                try{
                    iniciarOperacion();
                    int id = (int)sesion.save(nuevoAsegurado);
                    System.out.println("Nuevo asegurado: " + nuevoAsegurado.getDni() + "; ID: " + id);
                    tx.commit();
                }catch(HibernateException he){
                    manejarExcepcion(he);
                    System.out.println("Error:" + he.getMessage());
                    throw he;   
                }catch(Exception e){
                    //manejarExcepcion(e);
                    System.out.println("Error:" + e.getMessage());
                    throw e;                   
                }finally{
                    sesion.close();
                }
            }
            bReader.close();
            fReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LineaDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(AseguradoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(AseguradoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
