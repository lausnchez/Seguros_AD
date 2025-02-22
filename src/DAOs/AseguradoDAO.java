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
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import seguroshibernate.GestorDOM;
import seguroshibernate.HibernateUtil;
import seguroshibernate.Utils;





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
                datos[0] = linea.substring(0, 9).trim();              // dni
                datos[1] = linea.substring(9, 29).trim();             // nombre, agregar espacios
                datos[2] = linea.substring(29,79).trim();             // apellido1
                datos[3] = linea.substring(79,129).trim();            //apellido2
                datos[4] = linea.substring(129).trim();               //fecha
                
                
                /*
                System.out.println("dni:" + datos[0]);
                System.out.println("nombre:" + datos[1]);
                System.out.println("apellido1:" + datos[2]);
                System.out.println("apellido2:" + datos[3]);
                System.out.println("fecha:" + datos[4]);
                */
                
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
    
    /**
     * Encuentra un asegurado en la base de datos. Puede devolver null.
     * @param id
     * @return Asegurado / null;
     */
    public Asegurado encontrarAsegurado(int id){
        iniciarOperacion();
        Asegurado aseguradoEncontrado = null;
        aseguradoEncontrado = (Asegurado)sesion.createQuery("FROM Asegurado a WHERE id=:param").setInteger("param", id).uniqueResult();
        return aseguradoEncontrado;
    }
    
    /**
     * Pide un ID al usuario hasta que encuentra el asegurado en la base de datos
     * @return Asegurado.
     */
    public Asegurado pedirAsegurado(){
        AseguradoDAO aseguradoDAO = new AseguradoDAO();
        Scanner scan = new Scanner(System.in);
        
        Asegurado asegurado = null;
        do{
            System.out.print("Inserte el ID del asegurado que quiere encontrar: ");
            String respuestaUsuario = scan.nextLine();
            if(Utils.comprobarInt(respuestaUsuario)){
                asegurado = aseguradoDAO.encontrarAsegurado(Integer.parseInt(respuestaUsuario));
                if(asegurado == null){
                    System.out.println("Asegurado no encontrado en la base de datos");
                }else{
                    System.out.println("\nEncontrado asegurado con ID " + asegurado.getId());
                }
            }else{
                System.out.println("Valor no válido. Inserte un id numérico.");
            }
        }while(asegurado == null);
        return asegurado;
    }
    
    public void mostrarDatosAsegurado(Asegurado asegurado){
        System.out.println("\n...............................");
        System.out.println("Datos del asegurado: ");
        System.out.println("...............................");
        System.out.println("ID: " + asegurado.getId());
        System.out.println("DNI: " + asegurado.getDni());
        System.out.println("Nombre: " + asegurado.getNombre());
        System.out.println("Apellidos: " + asegurado.getApellido1() + " " + asegurado.getApellido2());
        System.out.println("Fecha de nacimiento: " + asegurado.getFechaNacimiento());
    }
    
    public void borrarDatosAsegurado(Asegurado asegurado){
        SubvencionDAO subvencionesDAO = new SubvencionDAO();
        PolizaDAO polizaDAO = new PolizaDAO();
        
        // Borrar subvenciones
        subvencionesDAO.borrarSubvenciones(asegurado);
        // Borrar polizas
        polizaDAO.borrarPolizasAsegurado(asegurado);
        // Borrar asegurado
        //borrarAsegurado(asegurado);
        
        System.out.println("\nSe ha eliminado el asegurado correctamente");
    }
    
    public void borrarAsegurado(Asegurado asegurado){
        System.out.println("");
        try{
            iniciarOperacion();
            sesion.delete(asegurado);
            tx.commit();
            System.out.println("Asegurado " + asegurado.getId() + " eliminado.");
        }catch(HibernateException he){
            manejarExcepcion(he);
            throw he;
        }finally{
            sesion.close();
        }
    }
    
    public void generarXML_Asegurados(){
        GestorDOM gDom = new GestorDOM();
        List<Asegurado> listadoAsegurados = recogerAsegurados();
        
        gDom.inicializarDocumento();
        for(Asegurado as: listadoAsegurados){
            gDom.generarAsegurado(as);
        }
        gDom.guardarDocumento();
        
    }
    
    public List<Asegurado> recogerAsegurados(){
        List<Asegurado> listadoAsegurados = null;
        try{
            iniciarOperacion();
            String query = "FROM Asegurado a";
            listadoAsegurados = sesion.createQuery(query).list();
            if(listadoAsegurados == null){
                System.out.println("\nNo se han encontrado pólizas.");
            }
        }catch(HibernateException he){
            manejarExcepcion(he);
            throw he;
        }finally{
            sesion.close();
        }  
        return listadoAsegurados;
    }
}
