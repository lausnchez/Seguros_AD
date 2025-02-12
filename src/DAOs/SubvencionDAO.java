/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import POJOs.Asegurado;
import POJOs.Subvencion;
import POJOs.SubvencionId;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public class SubvencionDAO {
    private Session sesion;
    private Transaction tx;
    private String fichero = "src/ficheros/Subvenciones.txt";
    
    public void iniciarOperacion(){
        this.sesion = HibernateUtil.getSessionFactory().openSession();
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
            
            while((linea = bReader.readLine()) != null){
                String[] datos = new String[4];
                datos[0] = linea.substring(0, 10).trim();  // Asegurado -ID
                datos[1] = linea.substring(10, 13); // Linea -ID
                datos[2] = linea.substring(13, 16); // Importe
                datos[3] = linea.substring(16).trim();     // Asunto
                
                
                System.out.println("Asegurado: " + datos[0] + ". L:" + datos[0].length());
                System.out.println("Linea: " + datos[1] + ". L:" + datos[1].length());
                System.out.println("Importe: " + datos[2] + ". L:" + datos[2].length());
                System.out.println("Asunto: " + datos[3] + ". L:" + datos[3].length() + "\n");
                
                
                SubvencionId subID = new SubvencionId(Integer.parseInt(datos[0]), Integer.parseInt(datos[1]));
                
                // Encontrar elementos del ID
                AseguradoDAO asDAO = new AseguradoDAO();
                LineaDAO lineaDAO = new LineaDAO();
                Asegurado  aseguradoEncontrado = asDAO.encontrarAsegurado(Integer.parseInt(datos[0]));
                Boolean valoresValidos = true;
                
                if(aseguradoEncontrado == null){
                    System.out.println("Asegurado no encontrado");
                    valoresValidos = false;
                }else {
                    System.out.println(aseguradoEncontrado.getNombre());
                }
                
                /*
                Subvencion nuevaSub = new Subvencion(
                        Integer.parseInt(datos[0]),
                        Integer.parseInt(datos[1]),
                        aseguradoEncontrado,
                        linea,
                        Short.parseShort(datos[2]),
                        datos[3]);
                try{
                    iniciarOperacion();
                    //int id = (int)sesion.save(nuevaLinea);
                    //System.out.println("Nueva línea: " + nuevaLinea.getCodigo());
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
                */
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
}
