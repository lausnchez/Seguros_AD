/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguroshibernate;

import java.util.Scanner;

/**
 *
 * @author Laura Sánchez
 */
public class Utils {
    public static boolean comprobarInt(String valor){
        try{
            Integer.parseInt(valor);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public static boolean comprobarFloat(String valor){
        try{
            Float.parseFloat(valor);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public static boolean pedirConfirmacion(String pregunta){
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("\n" + pregunta);
            System.out.println(" - Opcion 1: Confirmar");
            System.out.println(" - Opcion 2: Cancelar");
            System.out.print("Respuesta: ");
            String respuesta = scan.nextLine();
            switch(respuesta){
                case "1":
                    return true;
                case "2":
                    return false;
                default:
                    System.out.println("Valor no válido\n");
                    break;
            }
        }
    }
}
