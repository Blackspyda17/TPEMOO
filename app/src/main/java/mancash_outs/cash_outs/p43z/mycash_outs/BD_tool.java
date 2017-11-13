package mancash_outs.cash_outs.p43z.mycash_outs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by p43z on 22/05/16.
 */
public class BD_tool {


    private int mMonth,mYear,mDay,fecha,año,sMonth,sYear,sDay;
    static final int DATE_ID=0;

   public boolean Comp2(Context cont, String nombre, String apellido,String apellido2) {
        boolean res=true;
        SQLiteDatabase db = Ins_BD(cont);
        Cursor c = db.rawQuery("select Id from Persona where (Nombre like " + "'%" + nombre.trim() + "%'" + ") AND (Apellido1 like %"  + apellido + "% AND Apellido2 like %"+apellido2.trim()+"%)", null);
        if(db!=null){
            try {
                if (c.moveToFirst()) {
                    c.close();
                    db.close();
                    res= true;
                }else{
                    res=false;
                }
            }catch (Exception e){
                Toast.makeText(cont, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        return res;
    }


    public Boolean Guardarbd(Context cont,String id,String nombre, String apellido1,String apellido2, String telefono,String direccion) {
        SQLiteDatabase db=Ins_BD(cont);
        if(db!=null){
            ContentValues registronuevo=new ContentValues();
            registronuevo.put("Id",id.trim());
            registronuevo.put("Icon",Agregarcliente.sex);
            registronuevo.put("Nombre",nombre.trim());
            registronuevo.put("Apellido1",apellido1.trim());
            registronuevo.put("Apellido2",apellido2.trim());
            registronuevo.put("Telefono",telefono.trim());
            registronuevo.put("Direccion",direccion);
            long i=db.insert("Persona",null,registronuevo);
            if(i>0){
                Toast.makeText(cont, "Registro Insertado", Toast.LENGTH_SHORT).show();
                db.close();
                return true;

            }
        }
        return false;
    }

    public Boolean GuardarTarifa(Context cont,String titulo,String descripcion, int monto) {
        SQLiteDatabase db=Ins_BD(cont);
        if(db!=null){
            ContentValues registronuevo=new ContentValues();

            registronuevo.put("Id_tarifa",titulo);
            registronuevo.put("Descripcion",descripcion);
            registronuevo.put("Monto",monto);
            long i=db.insert("Tarifa",null,registronuevo);

            if(i>0){
                Toast.makeText(cont, "Tarifa Guardada", Toast.LENGTH_SHORT).show();
                db.close();
                return true;

            }
        }


        return false;
    }

    public Boolean GuardarPago (Context cont,String idcliente,String idtarifa, String emision,String modalidad,int cantidad,int monto_pagar,String periodo,String concepto,int estado) {
        SQLiteDatabase db=Ins_BD(cont);
        if(db!=null){
            ContentValues registronuevo=new ContentValues();
            registronuevo.put("IdPersona",idcliente);
            registronuevo.put("IdTarifa",idtarifa);
            registronuevo.put("Fecha",emision);
            registronuevo.put("Modalidad",modalidad);
            registronuevo.put("Cantidad",cantidad);
            registronuevo.put("Monto",monto_pagar);
            registronuevo.put("Tipo_modalidad",periodo);
            registronuevo.put("Detalle",concepto);
            registronuevo.put("Estado",estado);
            long i=db.insert("Registro",null,registronuevo);

            if(i>0){
                Toast.makeText(cont, "Pago Guardado", Toast.LENGTH_SHORT).show();
                db.close();
                return true;

            }
        }


        return false;
    }








    public SQLiteDatabase Ins_BD(Context cont){
        BD_cashouts baseHelper=new BD_cashouts(cont,"STBD",null,1);
        SQLiteDatabase db=baseHelper.getWritableDatabase();
        return db;

    }

    public List<Person> Lista_client(Context cont) {
        List<Person> nueva_l = new ArrayList<>();
        SQLiteDatabase db = Ins_BD(cont);
        if (db != null) {
            Cursor c = db.rawQuery("select * from Persona", null);
            if (c.moveToFirst()) {
                do {
                    Log.i("TAG", "Id" + c.getString(0) + "icono: " + c.getString(1));
                    Person NuevaP = new Person(c.getString(0), Integer.parseInt(c.getString(1)), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6));
                    nueva_l.add(NuevaP);
                } while (c.moveToNext());
            }


        }
        return nueva_l;
    }

        public List<Pago> Lista_pagosC(Context cont, String id){
          boolean estado=false;

            List<Pago> lista= new ArrayList<>();

            SQLiteDatabase db=Ins_BD(cont);
            if(db!=null){
                Cursor c=db.rawQuery("select * from Registro where IdPersona = '"+id+"'",null);
                if(c.moveToFirst()){
                    do{

                       // Log.i("TAG", "Id"+c.getString(0)+"y si entro: "+c.getString(3));
                        if(c.getInt(9)>0){
                            estado=true;
                        }else {
                            estado=false;
                        }
                        Pago NuevoP=new Pago(Integer.parseInt(c.getString(0)),c.getString(1),c.getString(2),c.getString(3),c.getString(4),Integer.parseInt(c.getString(5)),Integer.parseInt(c.getString(6)),c.getString(7),c.getString(8),estado);
                        lista.add(NuevoP);
                    }while(c.moveToNext());
                }


            }
return lista;

    }

    public Date Fecha(String fecha) throws ParseException {
       Date nueva=null;
        SimpleDateFormat fecha_formato = new SimpleDateFormat("dd/MM/yyyy");
        nueva= fecha_formato.parse(fecha);
        return nueva;


    }


    public List<Tarifa> Lista_Tarifas(Context cont){
        List<Tarifa> nueva_l=new ArrayList<>();
        SQLiteDatabase db=Ins_BD(cont);
        if(db!=null){
            Cursor c=db.rawQuery("select * from Tarifa",null);
            if(c.moveToFirst()){
                do{
                    Tarifa NuevaT=new Tarifa(c.getString(0),c.getString(1),Integer.parseInt(c.getString(2)));
                    nueva_l.add(NuevaT);
                }while(c.moveToNext());
            }


        }
        return nueva_l;

    }





    public String fechaAct(){

        String dia;
        String mes;
        String ano;

        Calendar C=Calendar.getInstance();
        sYear=C.get(Calendar.YEAR);
        sMonth=C.get(Calendar.MONTH);
        sDay=C.get(Calendar.DAY_OF_MONTH);


        SpannableString Dia = new SpannableString(new StringBuilder().append(sDay));
        Dia.setSpan(new UnderlineSpan(), 0, Dia.length(), 0);
        SpannableString Mes = new SpannableString(new StringBuilder().append(sMonth+1));
        Mes.setSpan(new UnderlineSpan(), 0, Mes.length(), 0);
        SpannableString Año = new SpannableString(new StringBuilder().append(sYear));
        Año.setSpan(new UnderlineSpan(), 0, Año.length(), 0);

        dia=Dia.toString();
        mes=Mes.toString();
        ano=Año.toString();



        if(Dia.length()==1){
            dia="0"+Dia;
        }
        if(Mes.length()==1){
            mes="0"+Mes;
        }

        return dia+"/"+mes+"/"+ano;
    }


    public boolean escampovalido(String texto,int condicion,TextInputLayout camptex) {




        if( texto.length() < 1) {
            camptex.setError("Campo Vacio");
            return false;
        } else {
            switch (condicion){
                case 1:
                    break;
                case 2:
                    if(Integer.parseInt(texto)<1){
                        camptex.setError("Monto Invalido");
                        return false;
                    }
                    break;

            }



            camptex.setError(null);
        }

        return true;
    }

    public void Modi_Client_txt(Context cont, String id , EditText tilNombre, EditText  tilApellido, EditText  tilApellido2, EditText  tilDireccion, EditText telefono){
        SQLiteDatabase db=Ins_BD(cont);
        if(db!=null){
            Cursor c=db.rawQuery("SELECT * FROM Persona WHERE Id = '"+id+"'" ,null);
            System.out.println("hice"+id);
            if(c.moveToFirst()){
                do{
                    tilNombre.setText(""+c.getString(2));
                    tilApellido.setText(c.getString(3));
                    tilApellido2.setText(c.getString(4));
                    telefono.setText(c.getString(5));
                    tilDireccion.setText(c.getString(6));

                }while(c.moveToNext());
            }

        }
    }


    public boolean modificar_Cliente(Context cont, String id, String nombre, String apellido1, String apellido2, String telefono, String direccion){
        SQLiteDatabase db=Ins_BD(cont);
        if(db!=null) {
            ContentValues valores = new ContentValues();
            valores.put("Id", id);
            valores.put("Nombre", nombre);
            valores.put("Apellido1", apellido1);
            valores.put("Apellido2", apellido2);
            valores.put("Telefono", telefono);
            valores.put("Direccion", direccion);
            long i = db.update("Persona", valores, "Id='" + id + "'", null);

            if (i > 0) {
                Toast.makeText(cont, "Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                db.close();
                return true;
            }
        }
        return false;
    }


    public boolean moificar_Pago(Context cont,int Id,String IdPersona,String IdTarifa,String fecha,String modalidad,int cantidad,int monto,String tipo_modalidad,String detalle,int estado){
        SQLiteDatabase db=Ins_BD(cont);
        if(db!=null){
            ContentValues registro_mod=new ContentValues();
            registro_mod.put("IdPersona",IdPersona);
            registro_mod.put("IdTarifa",IdTarifa);
            registro_mod.put("Fecha",fecha);
            registro_mod.put("Modalidad",modalidad);
            registro_mod.put("Cantidad",cantidad);
            registro_mod.put("Monto",monto);
            registro_mod.put("Tipo_modalidad",tipo_modalidad);
            registro_mod.put("Detalle",detalle);
            registro_mod.put("Estado",estado);
            long i=db.update("Registro",registro_mod,"Id='"+Id+"'",null);

            if(i>0){
                Toast.makeText(cont, "Modificado Correctamente", Toast.LENGTH_SHORT).show();
                db.close();
                return true;

            }
        }


        return false;



    }



    public void eliminar(Context cont,int opc,String eliminar){
        SQLiteDatabase db=Ins_BD(cont);
        if(db!=null) {
            try {

                switch (opc) {
                    case 1:
                        db.execSQL("delete from Registro where IdPersona='" + eliminar + "'");
                        db.execSQL("delete from Persona where Id='" + eliminar + "'");
                       Toast.makeText(cont, "Se Eliminaron los Estudiantes Seleccionados", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        db.execSQL("delete from Registro where Id='" + eliminar + "'");
                      //  Toast.makeText(cont, "Se Eliminaron los Pagos Seleccionados del Estudiante ", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        db.execSQL("delete from Tarifa where Id_tarifa='" + eliminar + "'");
                       // Toast.makeText(cont, "Se Eliminaron las Tarifas Seleccionadas ", Toast.LENGTH_SHORT).show();
                        break;
                }


            }catch (Exception e) {
                Toast.makeText(cont, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }

public String nombre_cli(Context cont,String id){
    SQLiteDatabase db=Ins_BD(cont);
    String nombre="";
    if(db!=null){
        Cursor c=db.rawQuery("SELECT Nombre,Apellido1,Apellido2 FROM Persona WHERE Id = '"+id+"'" ,null);
        if(c.moveToFirst()){
            do{
              nombre= c.getString(0)+" "+c.getString(1)+" "+c.getString(2);
            }while(c.moveToNext());
        }
    }
   return nombre;
}



public Person persona(Context cont,String id){

Person person=null;

    SQLiteDatabase db = Ins_BD(cont);
    if (db != null) {
        Cursor c = db.rawQuery("select * from Persona where Id = '"+id+"'", null);
        if (c.moveToFirst()) {
            do {
                Log.i("TAG", "Id" + c.getString(0) + "icono: " + c.getString(1));
                person = new Person(c.getString(0), Integer.parseInt(c.getString(1)), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6));
            } while (c.moveToNext());
        }
    }
    return person;
}


public int cant_pagos(Context cont,String id){

    int cant=0;
    SQLiteDatabase db = Ins_BD(cont);
    Cursor c = db.rawQuery("select count(*) from Registro where IdPersona = '"+id+"'", null);
    if (c.moveToFirst()) {
        do {

           cant=c.getInt(0);
        } while (c.moveToNext());
    }
return cant;

}


    public int cant_cancelados(Context cont,String id){

        int cant=0;
        SQLiteDatabase db = Ins_BD(cont);
        Cursor c = db.rawQuery("select count(*) from Registro where IdPersona = '"+id+"' and Estado > 0", null);
        if (c.moveToFirst()) {
            do {
                cant=c.getInt(0);
            } while (c.moveToNext());
        }
        return cant;
    }


    public int cant_pendientes(Context cont,String id){

        int cant=0;
        SQLiteDatabase db = Ins_BD(cont);
        Cursor c = db.rawQuery("select count(*) from Registro where IdPersona = '"+id+"' and Estado = 0", null);
        if (c.moveToFirst()) {
            do {
                cant=c.getInt(0);
            } while (c.moveToNext());
        }
        return cant;
    }


    public String ult_fecha(Context cont,String id) throws ParseException {
        SimpleDateFormat fecha_formato = new SimpleDateFormat("dd/MM/yyyy");
        List<Date> pagos=new ArrayList<Date>();
        SQLiteDatabase db = Ins_BD(cont);
        Cursor c = db.rawQuery("select Fecha from Registro where IdPersona = '"+id+"'", null);
        if (c.moveToFirst()) {
            do {

               pagos.add(Fecha(c.getString(0)));
            } while (c.moveToNext());
        }

return fecha_formato.format(Collections.max(pagos));
    }
}
