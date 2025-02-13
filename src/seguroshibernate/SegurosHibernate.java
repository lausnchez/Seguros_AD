/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguroshibernate;

import DAOs.AseguradoDAO;
import DAOs.LineaDAO;
import DAOs.SubvencionDAO;
import POJOs.Asegurado;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laura Sánchez
 */
public class SegurosHibernate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException, IOException {
        menu();
    }
    
    private static void menu() throws IOException, ParseException{
        AseguradoDAO aseguradoDao = new AseguradoDAO();
        LineaDAO lineaDao = new LineaDAO();
        SubvencionDAO subDAO = new SubvencionDAO();
        Scanner scan = new Scanner(System.in);
        
        int opcion = -1;
        while(opcion != 0){
            System.out.println("\nAplicación de Seguros");
            System.out.println("------------------------------------------------");
            System.out.println("1. Volcar ficheros en la base de datos");
            System.out.println("2. Contratar póliza");
            System.out.println("0. Salir");

            String valor = scan.nextLine();
            if(Utils.comprobarNumero(valor)){
                opcion = Integer.parseInt(valor);
            }else opcion = -1;
            
            switch(opcion){
                case 0:
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Inserte un número válido");
                    break;
                case 1:
                    System.out.println("Volcar ficheros ---");
                    
                    aseguradoDao.volcarFichero();
                    System.out.println("");
                    
                    lineaDao.volcarFichero();
                    System.out.println("");
                    
                    subDAO.volcarFichero();
                    System.out.println("");
                    break;
                case 2:
                    System.out.println("Contrataciones ---");
                    contratacion();
                    break;
            }
        }
    }
    
    public static void contratacion(){
        Scanner scan = new Scanner(System.in);
        AseguradoDAO asegDAO = new AseguradoDAO();
        
        // 1.0 Encontrar Asegurado
        Asegurado aseguradoContratacion = null;
        int idAsegurado = -1;
        
        while(aseguradoContratacion == null){
            String linea = "a";
            do{
                System.out.print("Inserte un ID para encontrar al asegurado: ");
                linea = scan.nextLine();
            }while(!Utils.comprobarNumero(linea));
            idAsegurado = Integer.parseInt(linea);
            aseguradoContratacion = asegDAO.encontrarAsegurado(idAsegurado);
            if(aseguradoContratacion == null){
                System.out.println("No se ha encontrado un asegurado con ese ID");
            }else {
                // 2. Se comprueba que el asegurado no tiene ninguna póliza para
                // esa línea de seguro
                
            }
        }
    }
    
}
