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
import POJOs.Poliza;
import POJOs.Subvencion;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
            System.out.println("3. Mostrar datos de un asegurado");
            System.out.println("0. Salir");
            System.out.print("\nOpcion: ");

            String valor = scan.nextLine();
            if(Utils.comprobarInt(valor)){
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
                case 3: 
                    encontrarAsegurado();
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
        SubvencionDAO subDAO = new SubvencionDAO();
        
        // 1.0 Encontrar Asegurado y línea de seguro válidos
        Asegurado aseguradoContratacion = null;
        int idAsegurado;
        
        while(aseguradoContratacion == null){
            String linea = "a";
            do{
                System.out.print("Inserte un ID para encontrar al asegurado: ");
                linea = scan.nextLine();
            }while(!Utils.comprobarInt(linea));
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
                    }while(!Utils.comprobarInt(linea));
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
                            3. Se comprueba que el límite de la fecha de contratación
                            de la línea de seguro es mayor o igual que la fecha 
                            actual, si no se devuelve un error
                             **/ 
                            
                            if(lineaDAO.comprobarCaducidadLinea(lineaContratacion)){
                                System.out.println("Linea de seguro sin caducar");
                                
                                // 4. Se pide un importe numérico y mayor de 0
                                float importePoliza = polizaDAO.designarImportePoliza();
                                
                                /*
                                5. Se comprueba si el usuario tiene una subvención para la línea de seguro que se desea contratar, si
                                es así se resta al importe de la póliza el porcentaje correspondiente a la subvención
                                */
                                
                                float importeSubvencion = subDAO.comprobarExistenciaSubvencion(aseguradoContratacion, lineaContratacion);
                                System.out.println("Porcentaje de deducción de la póliza: " + importeSubvencion + "%");
                                float importeFinalPoliza = (float) (importePoliza - (importePoliza * (importeSubvencion * 0.01)));
                                System.out.println("Importe final de la póliza: " + importeFinalPoliza + "€");
                                
                                // 6. Se genera la póliza en la base de datos
                                int numReferencia = polizaDAO.encontrarMayorReferencia(lineaContratacion.getFamilia()) + 1;
                                String referencia = lineaContratacion.getFamilia() + numReferencia;
                                
                                System.out.println("Referencia: " + referencia);
                                
                                // digito de control -> resto de referencia entre 6
                                int digitoControl = polizaDAO.generarDigitoDeControl(numReferencia);
                                System.out.println("Número de control: " + digitoControl);
                                
                                // Calcular fecha de vencimiento para el detalle_poliza
                                Date fechaActual = new Date();
                                System.out.println("Fecha actual: " + fechaActual);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(fechaActual);
                                calendar.add(Calendar.YEAR, 1);
                                Date fechaVencimiento = calendar.getTime();
                                System.out.println("Fecha de vencimiento: " + fechaVencimiento);
                                
                                // Crear póliza y detalle de la póliza
                                polizaDAO.crearPoliza(referencia, aseguradoContratacion, lineaContratacion, digitoControl, importePoliza, fechaActual, fechaVencimiento);
                                
                            }else System.out.println("Línea de seguro caducada");
                        }else System.out.println("Ya existe una póliza con esa línea de seguro y asegurado.");
                        
                    }else System.out.println("No se ha encontrado la línea de seguro");
                }
            }
        }
    }
    
    public static void encontrarAsegurado(){
        AseguradoDAO aseguradoDAO = new AseguradoDAO();
        SubvencionDAO subvencionDAO = new SubvencionDAO();
        PolizaDAO polizaDAO = new PolizaDAO();
        
        Asegurado aseguradoEncontrado = aseguradoDAO.pedirAsegurado();
        aseguradoDAO.mostrarDatosAsegurado(aseguradoEncontrado);
        List<Subvencion> listadoSubvenciones = subvencionDAO.recogerSubvencionesDeUsuario(aseguradoEncontrado);
        if(listadoSubvenciones != null){
            subvencionDAO.mostrarVariasSubvenciones(listadoSubvenciones);
        }
        List<Poliza> listadoPolizas = polizaDAO.recogerPolizasDeUsuario(aseguradoEncontrado);
        if(listadoPolizas != null){
            polizaDAO.mostrarVariasPolizas(listadoPolizas);
        }
        
    }  
}
