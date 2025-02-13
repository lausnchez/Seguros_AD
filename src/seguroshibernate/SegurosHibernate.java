/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguroshibernate;

import DAOs.AseguradoDAO;
import DAOs.LineaDAO;
import DAOs.PolizaDAO;
import DAOs.SubvencionDAO;
import POJOs.Asegurado;
import POJOs.Linea;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

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
        System.exit(0);
    }
    
    public static void contratacion(){
        Scanner scan = new Scanner(System.in);
        AseguradoDAO asegDAO = new AseguradoDAO();
        LineaDAO lineaDAO = new LineaDAO();
        PolizaDAO polizaDAO = new PolizaDAO();
        
        // 1.0 Encontrar Asegurado y línea de seguro válidos
        Asegurado aseguradoContratacion = null;
        int idAsegurado;
        
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
                System.out.println("Encontrado el asegurado con ID " + idAsegurado);
                
                // Encontrar linea de seguro
                Linea lineaContratacion = null;
                int idLinea = -1;

                while(lineaContratacion == null){
                    linea = "a";
                    do{
                        System.out.print("Inserte un código para encontrar la línea de seguro: ");
                        linea = scan.nextLine();
                    }while(!Utils.comprobarNumero(linea));
                    idLinea = Integer.parseInt(linea);
                    lineaContratacion = lineaDAO.encontrarLinea(idLinea);
                    if(lineaContratacion != null){
                        System.out.println("Encontrado la línea de seguro con código " + idLinea);
                        
                        /** 
                        2. Se comprueba que el asegurado no tiene ninguna póliza para
                        esa línea de seguro
                        **/ 
                        
                        if(!polizaDAO.existePoliza(aseguradoContratacion, lineaContratacion)){
                            /**
                            Se comprueba que el límite de la fecha de contratación
                            de la línea de seguro es mayor o igual que la fecha 
                            actual, si no se devuelve un error
                             **/ 
                            if(lineaDAO.comprobarCaducidadLinea(lineaContratacion)){
                                System.out.println("Linea de seguro sin caducar");
                            }else System.out.println("Línea de seguro caducada");
                        }else System.out.println("Ya existe una póliza con esa línea de seguro y asegurado.");
                        
                    }else System.out.println("No se ha encontrado la línea de seguro");
                }
            }
        }
    }
    
}
