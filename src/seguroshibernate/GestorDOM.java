/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguroshibernate;

import DAOs.PolizaDAO;
import DAOs.SubvencionDAO;
import POJOs.Asegurado;
import POJOs.Linea;
import POJOs.Poliza;
import POJOs.Subvencion;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 *
 * @author Laura Sánchez
 */
public class GestorDOM {
    private static String direccionDoc = "./asegurados.xml";
    private static DocumentBuilder dBuilder;
    private static Document doc;
    
    public GestorDOM() {
		DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			this.dBuilder = dBuilderFactory.newDocumentBuilder();
			this.doc = dBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
    }
    
    public void guardarDocumento() {
		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer trans = transFactory.newTransformer();	
			
			Source origen =  new DOMSource(doc);
			Result resultado = new StreamResult(new File("asegurados.xml"));	
			
			trans.setOutputProperty(OutputKeys.INDENT,"yes");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			trans.transform(origen, resultado);
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void inicializarDocumento() {
		try {
			this.doc = dBuilder.parse(direccionDoc);
		} catch (SAXException | IOException e) {
			this.doc = dBuilder.newDocument();
			Element raiz = doc.createElement("asegurados");
			doc.appendChild(raiz);
			guardarDocumento();
		}
    }
    
    public void generarAsegurado(Asegurado asegurado){
        // Elemento principal
        Element elementoAsegurado = doc.createElement("asegurado");
        elementoAsegurado.setAttribute("id", asegurado.getId().toString());
        
        // DNI
        Element elementoDNI = doc.createElement("dni");
        elementoDNI.setTextContent(asegurado.getDni());
        elementoAsegurado.appendChild(elementoDNI);
        
        // NOMBRE
        Element elementoNombre = doc.createElement("nombre");
        elementoNombre.setTextContent(asegurado.getNombre());
        elementoAsegurado.appendChild(elementoNombre);
        
        // APELLIDOS
        Element elementoApellido1 = doc.createElement("apellido1");
        elementoApellido1.setTextContent(asegurado.getApellido1());
        elementoAsegurado.appendChild(elementoApellido1);
        
        Element elementoApellido2 = doc.createElement("apellido2");
        elementoApellido2.setTextContent(asegurado.getApellido2());
        elementoAsegurado.appendChild(elementoApellido2);
        
        // FECHA
        Element elementoFecha = doc.createElement("fecha");
        elementoFecha.setTextContent(asegurado.getFechaNacimiento().toString());
        elementoAsegurado.appendChild(elementoFecha);
        
        // PÓLIZAS
        PolizaDAO poliDAO = new PolizaDAO();
        List<Poliza> polizasListado = poliDAO.recogerPolizasDeUsuario(asegurado);
        for(Poliza poli: polizasListado){
            // Poliza
            Element elementoPoliza = doc.createElement("poliza");
            elementoPoliza.setAttribute("referencia", poli.getReferencia());
            elementoPoliza.setAttribute("digitoControl", String.valueOf(poli.getDigitoControl()));
            
            Linea linea = poli.getLinea();
            
            // Linea
            Element elementoLinea = doc.createElement("linea");
            elementoLinea.setAttribute("codigo", String.valueOf(linea.getCodigo()));
            
            Element elementoDescriptivo = doc.createElement("descriptivo");
            elementoDescriptivo.setTextContent(linea.getDescriptivo());
            elementoLinea.appendChild(elementoDescriptivo);
            
            Element elementoFamilia = doc.createElement("familia");
            elementoFamilia.setTextContent(linea.getFamilia());
            elementoLinea.appendChild(elementoFamilia);
            
            Element elementoFechaLimite = doc.createElement("FechaLimiteContratacion");
            elementoFechaLimite.setTextContent(String.valueOf(linea.getFechaLimiteContratacion().toString()));
            elementoLinea.appendChild(elementoFechaLimite);
            
            elementoPoliza.appendChild(elementoLinea);
            
            // Importe
            Element elementoImporte = doc.createElement("importe");
            elementoImporte.setTextContent(String.valueOf(poli.getImporte()));
            elementoPoliza.appendChild(elementoImporte);
            
            elementoAsegurado.appendChild(elementoPoliza);
        }
        
        // SUBVENCIONES
        SubvencionDAO subDAO = new SubvencionDAO();
        List<Subvencion> subvencionesListado = subDAO.recogerSubvencionesDeUsuario(asegurado);
        for(Subvencion sub: subvencionesListado){
            Element elementoSubvencion = doc.createElement("subvencion");
            elementoSubvencion.setAttribute("asegurado", String.valueOf(sub.getAsegurado().getId()));
            elementoSubvencion.setAttribute("linea", String.valueOf(sub.getLinea().getCodigo()));
            
            Element elementoImporteSubvencion = doc.createElement("importe");
            elementoImporteSubvencion.setTextContent(String.valueOf(sub.getImporte()));
            elementoSubvencion.appendChild(elementoImporteSubvencion);
            
            Element elementoAsuntoSub = doc.createElement("asunto");
            elementoAsuntoSub.setTextContent(sub.getAsunto());
            elementoSubvencion.appendChild(elementoAsuntoSub);
            
            elementoAsegurado.appendChild(elementoSubvencion);
        }
          
        Element elementoRaiz = (Element) doc.getElementsByTagName("asegurados").item(0);
        elementoRaiz.appendChild(elementoAsegurado);
        
    }
}
