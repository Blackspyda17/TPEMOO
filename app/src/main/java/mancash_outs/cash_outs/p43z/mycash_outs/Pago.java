package mancash_outs.cash_outs.p43z.mycash_outs;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by p43z on 14/06/16.
 */
public class Pago implements Serializable{

    private int n_comprob;
    private String id_persona;
    private String tarifa;
    private String fecha;
    private String modalidad;
    private int cantidad;
    private int monto;
    private String tipo_serv;
    private String detalle;
    private boolean estado_pago;

    public Pago(int n_comprob, String id_persona,String tarifa,String fecha,String modalidad,int cantidad, int monto, String tipo_serv, String detalle,boolean estado) {
        this.n_comprob = n_comprob;
        this.id_persona = id_persona;
        this.tarifa=tarifa;
        this.fecha = fecha;
        this.modalidad=modalidad;
        this.cantidad=cantidad;
        this.monto = monto;
        this.tipo_serv = tipo_serv;
        this.detalle = detalle;
        this.estado_pago=estado;

    }



    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getN_comprob() {
        return n_comprob;
    }

    public void setN_comprob(int n_comprob) {
        this.n_comprob = n_comprob;
    }

    public String getId_persona() {
        return id_persona;
    }

    public void setId_persona(String id_persona) {
        this.id_persona = id_persona;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getTipo_serv() {
        return tipo_serv;
    }

    public void setTipo_serv(String tipo_serv) {
        this.tipo_serv = tipo_serv;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }


    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public boolean isEstado_pago() {
        return estado_pago;
    }

    public void setEstado_pago(boolean estado_pago) {
        this.estado_pago = estado_pago;
    }
}
