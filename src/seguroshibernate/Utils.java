/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguroshibernate;

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
}
