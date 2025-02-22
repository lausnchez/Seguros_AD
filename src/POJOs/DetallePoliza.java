package POJOs;
// Generated 10-feb-2025 17:59:58 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * DetallePoliza generated by hbm2java
 */
public class DetallePoliza  implements java.io.Serializable {


     private String referencia;
     private Poliza poliza;
     private int digitoControl;
     private Date fechaAlta;
     private Date fechaVencimiento;

    public DetallePoliza() {
    }

	
    public DetallePoliza(Poliza poliza, int digitoControl) {
        this.poliza = poliza;
        this.digitoControl = digitoControl;
    }
    public DetallePoliza(Poliza poliza, int digitoControl, Date fechaAlta, Date fechaVencimiento) {
       this.poliza = poliza;
       this.digitoControl = digitoControl;
       this.fechaAlta = fechaAlta;
       this.fechaVencimiento = fechaVencimiento;
    }
    
    public DetallePoliza(String referencia, Poliza poliza, int digitoControl, Date fechaAlta, Date fechaVencimiento) {
       this.referencia = referencia;
       this.poliza = poliza;
       this.digitoControl = digitoControl;
       this.fechaAlta = fechaAlta;
       this.fechaVencimiento = fechaVencimiento;
    }
   
    public String getReferencia() {
        return this.referencia;
    }
    
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    public Poliza getPoliza() {
        return this.poliza;
    }
    
    public void setPoliza(Poliza poliza) {
        this.poliza = poliza;
    }
    public int getDigitoControl() {
        return this.digitoControl;
    }
    
    public void setDigitoControl(int digitoControl) {
        this.digitoControl = digitoControl;
    }
    public Date getFechaAlta() {
        return this.fechaAlta;
    }
    
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    public Date getFechaVencimiento() {
        return this.fechaVencimiento;
    }
    
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }




}


