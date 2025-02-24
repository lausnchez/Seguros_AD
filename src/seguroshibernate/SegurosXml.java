/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SegurosHibernate;

import POJOs.Asegurado;
import POJOs.DetallePoliza;
import POJOs.Linea;
import POJOs.Poliza;
import POJOs.Subvencion;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 *
 * @author javier.saugar
 */
public class SegurosXml {
    
    private Document doc, doc1;
    private Element datos, asegurados, polizas, detallepolizas, subvenciones;
   //---------------------------------------------------------------------------
   
    public SegurosXml(){

        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbfactory.newDocumentBuilder();
            doc = db.newDocument();
           //doc1 = db.parse("asegurado.xml");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void guardarDatos(Document d) {
	try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("f.xslt")));
            Source source = new DOMSource(d);   
            Result result = new StreamResult(new File("asegurado.xml"));
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");                    
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
	}catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
    public void raiz(){
        Element asegurados = doc.createElement("asegurados");
        doc.appendChild(asegurados);             
    }
    
     public void asegurados(Asegurado asegur){
        
        this.asegurados = (Element)doc.getElementsByTagName("asegurados").item(0);
        
        Element asegurado = doc.createElement("asegurado");
        asegurado.setAttribute("id", asegur.getId().toString());
        asegurados.appendChild(asegurado);  
        
        Element dni = doc.createElement("dni");
        asegurado.appendChild(dni);
        dni.appendChild(doc.createTextNode(asegur.getDni()));
        Element nombre = doc.createElement("nombre");
        asegurado.appendChild(nombre);
        nombre.appendChild(doc.createTextNode(asegur.getNombre()));
        Element apellido1 = doc.createElement("apellido1");
        asegurado.appendChild(apellido1);
        apellido1.appendChild(doc.createTextNode(asegur.getApellido1()));
        Element apellido2 = doc.createElement("apellido2");
        asegurado.appendChild(apellido2);
        apellido2.appendChild(doc.createTextNode(asegur.getApellido2()));
        Element fechanacimiento = doc.createElement("fechanacimiento");
        asegurado.appendChild(fechanacimiento);
        fechanacimiento.appendChild(doc.createTextNode(asegur.getFechaNacimiento().toString()));
        guardarDatos(doc);
     }
     
       public void polizas(Poliza pol, DetallePoliza dp){
        Element asegurado = (Element) doc.getElementsByTagName("asegurado").item(0);
        Element poliza = doc.createElement("poliza");
        asegurado.appendChild(poliza);
        poliza.setAttribute("referencia", pol.getReferencia());
        Element digitocontrol = doc.createElement("digitocontrol");
        poliza.appendChild(digitocontrol);
        digitocontrol.appendChild(doc.createTextNode(String.valueOf(pol.getDigitoControl())));
        Element importe = doc.createElement("importe");
        poliza.appendChild(importe);     
        importe.appendChild(doc.createTextNode(String.valueOf(pol.getImporte())));
        Element fechaalta = doc.createElement("fechaalta");
        poliza.appendChild(fechaalta);
        fechaalta.appendChild(doc.createTextNode(String.valueOf(dp.getFechaAlta())));
        Element fechavencimiento = doc.createElement("fechavencimiento");
        poliza.appendChild(fechavencimiento);
        fechaalta.appendChild(doc.createTextNode(String.valueOf(dp.getFechaVencimiento())));
           guardarDatos(doc);
       }
    
    public void subvencion(Subvencion sub){
        Element asegurado = (Element) doc.getElementsByTagName("asegurado").item(0);
        Element subvencion = doc.createElement("subvencion");
        asegurado.appendChild(subvencion);
        Element importe = doc.createElement("importe");
        subvencion.appendChild(importe);
        importe.appendChild(doc.createTextNode(String.valueOf(sub.getImporte())));
        Element asunto = doc.createElement("asunto");
        subvencion.appendChild(asunto);
        asunto.appendChild(doc.createTextNode(sub.getAsunto()));
        guardarDatos(doc);
    }
    
    public void linea(Linea line){
        Element asegurado = (Element) doc.getElementsByTagName("asegurado").item(0);
        Element linea = doc.createElement("linea");
        asegurado.appendChild(linea);
        linea.setAttribute("codigo", String.valueOf(line.getCodigo()));
        Element descriptivo = doc.createElement("descriptivo");
        linea.appendChild(descriptivo);
        descriptivo.appendChild(doc.createTextNode(line.getDescriptivo()));
        Element familia = doc.createElement("familia");
        linea.appendChild(familia);
        familia.appendChild(doc.createTextNode(line.getFamilia()));
        Element fecha_lim = doc.createElement("fecha_lim");
        linea.appendChild(fecha_lim);
        fecha_lim.appendChild(doc.createTextNode(String.valueOf(line.getFechaLimiteContratacion())));
        guardarDatos(doc);
    } 
}
