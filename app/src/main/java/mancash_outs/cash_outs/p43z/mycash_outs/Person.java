package mancash_outs.cash_outs.p43z.mycash_outs;

import java.io.Serializable;
import java.util.List;

/**
 * Created by p43z on 18/05/16.
 */
public class Person implements Serializable {


    private String ID;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private int icono;
    private String telefono;
    private String direccion;

    Person(String id,int icono, String nombre, String apellido1,String apellido2,String telefono,String direccion) {
        this.nombre = nombre;
        this.ID = id;
        this.icono=icono;
        this.apellido1 = apellido1;
        this.apellido2=apellido2;

        this.telefono=telefono;
        this.direccion=direccion;


    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
