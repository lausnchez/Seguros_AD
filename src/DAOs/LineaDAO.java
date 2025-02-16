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
public class LineaDAO {
    private Session sesion;
    private Transaction tx;
    private String fichero = "src/ficheros/Lineas.txt";
    
    public void iniciarOperacion(){
        this.sesion = HibernateUtil.getSessionFactory().openSession();
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
    
    public void volcarFichero() throws IOException, ParseException{
        File ficheroLineas = new File(fichero);
        FileReader fReader;
        
        try {
            fReader = new FileReader(ficheroLineas);
            BufferedReader bReader = new BufferedReader(fReader);
            String linea = "";
            
            while((linea = bReader.readLine()) != null){
                String[] datos = new String[4];
                datos[0] = linea.substring(0,3).trim();        // Codigo
                datos[1] = linea.substring(3, 103).trim();     // descriptivo
                datos[2] = linea.substring(103, 104).trim();    // familia
                datos[3] = linea.substring(104).trim();         // fechaLimiteContratacion
                
                /*
                System.out.println("Codigo: " + datos[0] + ". L: " + datos[0].length());
                System.out.println("Descriptivo: " + datos[1] + ". L: " + datos[1].length());
                System.out.println("Familia: " + datos[2] + ". L: " + datos[2].length());
                System.out.println("Fecha: " + datos[3] + ". L: " + datos[3] + "\n");
                */
                
               SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
               Date fecha = format.parse(datos[3]); 
                
               Linea nuevaLinea = new Linea(Integer.parseInt(datos[0]), datos[1], datos[2], fecha, null, null);
                try{
                    iniciarOperacion();
                    int id = (int)sesion.save(nuevaLinea);
                    System.out.println("Nueva línea: " + nuevaLinea.getCodigo());
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
        } 
    }
    
    public Linea encontrarLinea(int codigo){
        iniciarOperacion();
        Linea lineaEncontrada = null;
        lineaEncontrada = (Linea)sesion.createQuery("FROM Linea l WHERE id=:param").setInteger("param", codigo).uniqueResult();
        return lineaEncontrada;
    }
   
    /**
     * Si la linea de seguro no está caducada, devuelve true.
     * @param linea
     * @return 
     */
    public boolean comprobarCaducidadLinea(Linea linea){
        Date currentDate = new Date();   
        if(linea.getFechaLimiteContratacion().after(currentDate) 
                || linea.getFechaLimiteContratacion().equals(currentDate)){
            return true;
        }else return false;
    }
}
