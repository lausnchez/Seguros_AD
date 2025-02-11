/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguroshibernate;

import DAOs.AseguradoDAO;
import DAOs.LineaDAO;
import DAOs.SubvencionDAO;
import java.io.IOException;
import java.text.ParseException;
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
    public static void main(String[] args) throws ParseException {
        AseguradoDAO aseguradoDao = new AseguradoDAO();
        LineaDAO lineaDao = new LineaDAO();
        SubvencionDAO subDAO = new SubvencionDAO();
        
        try {
            subDAO.volcarFichero();
        } catch (IOException ex) {
            Logger.getLogger(SegurosHibernate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
