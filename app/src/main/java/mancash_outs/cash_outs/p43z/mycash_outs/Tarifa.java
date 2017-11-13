package mancash_outs.cash_outs.p43z.mycash_outs;

import java.io.Serializable;

/**
 * Created by p43z on 05/07/16.
 */
public class Tarifa implements Serializable {

    private String titulo;
    private String descripcion;
    private int monto;

    public Tarifa(String titulo, String descripcion, int monto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.monto = monto;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }
}
