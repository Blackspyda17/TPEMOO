package mancash_outs.cash_outs.p43z.mycash_outs;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Random;
import java.util.regex.Pattern;

public class Agregarcliente extends AppCompatActivity {

    private TextInputLayout tilNombre;
    private TextInputLayout tilTelefono;
    private TextInputLayout tilID;
    private TextInputLayout tilapellido;
    private TextInputLayout tilapellido2;
    private TextInputLayout tildireccion;
    private RadioButton masculino;


    //private TextInputLayout tilCorreo;

    private EditText campoNombre;
    private EditText campoTelefono;
    private EditText ID;
    private EditText apellido1;
    private EditText apellido2;
    private EditText direccion;
    public static int sex;
    Boolean y;
    Boolean d=false;
    private BD_tool herramienta=new BD_tool();



private int iconosm[]={R.mipmap.boy1,R.mipmap.boy2,R.mipmap.boy3,R.mipmap.boy4,R.mipmap.boy5,R.mipmap.boy6,
        R.mipmap.boy7,R.mipmap.boy8,R.mipmap.boy9,R.mipmap.boy10,R.mipmap.boy11,R.mipmap.boy12,R.mipmap.boy13,
        R.mipmap.boy14,R.mipmap.boy15,R.mipmap.boy16,R.mipmap.boy17,R.mipmap.boy18,R.mipmap.boy19,R.mipmap.boy20,
        R.mipmap.boy21,R.mipmap.boy22,R.mipmap.boy23};
private int iconosf[]={R.mipmap.girl1,R.mipmap.girl2,R.mipmap.girl3,R.mipmap.girl4,R.mipmap.girl5,R.mipmap.girl6,
        R.mipmap.girl7,R.mipmap.girl8,R.mipmap.girl9,R.mipmap.girl10,R.mipmap.girl11,R.mipmap.girl12,R.mipmap.girl13,
        R.mipmap.girl14,R.mipmap.girl15,R.mipmap.girl16,R.mipmap.girl17,R.mipmap.girl18,R.mipmap.girl19,R.mipmap.girl20,
        R.mipmap.girl21,R.mipmap.girl22,R.mipmap.girl23};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarcliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Referencias TILs
        masculino= (RadioButton) findViewById(R.id.radio_masculino);

        tilID = (TextInputLayout) findViewById(R.id.til_ID);
        tilNombre = (TextInputLayout) findViewById(R.id.til_nombre);
        tilapellido = (TextInputLayout) findViewById(R.id.til_apellido);
        tilapellido2 = (TextInputLayout) findViewById(R.id.til_apellido2);
        tilTelefono = (TextInputLayout) findViewById(R.id.til_telefono);
        tildireccion = (TextInputLayout) findViewById(R.id.til_direccion);

      //  tilCorreo = (TextInputLayout) findViewById(R.id.til_correo);

        // Referencias ETs
        campoNombre = (EditText) findViewById(R.id.campo_nombre);
        campoTelefono = (EditText) findViewById(R.id.campo_telefono);
        apellido1 = (EditText) findViewById(R.id.campo_apellido);
        apellido2 = (EditText) findViewById(R.id.campo_apellido2);
        direccion = (EditText) findViewById(R.id.campo_direccion);
        ID = (EditText) findViewById(R.id.campo_ID);

       // campoCorreo = (EditText) findViewById(R.id.campo_correo);

      campos(campoNombre,tilNombre);

  campos(campoTelefono,tilTelefono);
        campos(apellido1,tilapellido);
        campos(apellido2,tilapellido2);
       campos(ID,tilID);
        campos(direccion,tildireccion);
        try {
            Bundle x= this.getIntent().getExtras();
            y=x.getBoolean("ban");
            if(y == true) {
                String id = x.getString("id");
                System.out.println("id"+ id);
                herramienta.Modi_Client_txt(this, id, campoNombre, apellido1, apellido2, direccion, campoTelefono);
                y = false;
                ID.setText(id);
                Button botonAceptar = (Button) findViewById(R.id.boton_aceptar);
                botonAceptar.setText("Actualizar");
                d=true;
            }
        }catch (Exception d){

        }




        // Referencia Botón
        Button botonAceptar = (Button) findViewById(R.id.boton_aceptar);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(d==true){
                    ModifcarDatos();

                }else{
                    validarDatos();
                }
            }
        });


        Button botonCancelar = (Button) findViewById(R.id.boton_cancelar);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Agregarcliente.super.onBackPressed();

            }
        });



    }

    private boolean esNomapellidoValido(String texto,String advertencia,TextInputLayout camptex) {
        Pattern patron = Pattern.compile("^[a-z ñáéíóú A-Z]*$");
        if (!patron.matcher(texto).matches() || texto.length() > 15) {
            camptex.setError(advertencia);
            return false;
        } else {
            camptex.setError(null);
        }

        return true;
    }



    private boolean esDiryIDva(String texto,String advertencia,TextInputLayout camptex,int cantMax) {


        if (texto.length() > cantMax && texto.length() < 0) {
            camptex.setError(advertencia);
            return false;
        } else {
            camptex.setError(null);
        }

        return true;
    }



    private boolean esTelefonoValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches()) {
            tilTelefono.setError("Teléfono inválido");
            return false;
        } else {
            tilTelefono.setError(null);
        }

        return true;
    }
/**
    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.setError("Correo electrónico inválido");
            return false;
        } else {
            tilCorreo.setError(null);
        }

        return true;
    }
*/
    private void validarDatos() {
        String id=tilID.getEditText().getText().toString();
        String nombre = tilNombre.getEditText().getText().toString();
        String apellido1=tilapellido.getEditText().getText().toString();
        String apellido2=tilapellido2.getEditText().getText().toString();

        String telefono = tilTelefono.getEditText().getText().toString();
        String direccion=tildireccion.getEditText().getText().toString();
        //String correo = tilCorreo.getEditText().getText().toString();
        boolean a=esDiryIDva(id,"Codigo Invalido",tilID,10);
        boolean b = esNomapellidoValido(apellido1,"Nombre Invalido",tilNombre);
        boolean c = esNomapellidoValido(apellido2,"Apellido Invalido",tilapellido);
        boolean d = esNomapellidoValido(nombre,"Apellido Invalido",tilapellido2);
        boolean e= esDiryIDva(direccion,"Direccion Invalida",tildireccion,80);
        boolean f = esTelefonoValido(telefono);

        //boolean c = esCorreoValido(correo);

        if (a && b && c && d && e && f) {
            // OK, se pasa a la siguiente acción
         sex=sexo();

           if(herramienta.Guardarbd(this,id,nombre,apellido1,apellido2,telefono,direccion)){
              super.onBackPressed();
           }else {

               AlertDialog.Builder builder =
                       new AlertDialog.Builder(this);

               builder.setMessage("Error")
                       .setTitle("El Identificador (ID) ya se encuentra registrado, Ingrese un ID diferente")
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               dialog.cancel();
                           }
                       });

           }



        }

    }





    public int sexo(){
        if (masculino.isChecked()){
            return iconosm[randInt(0,23)];
        }else{
            return iconosf[randInt(0,23)];
        }

    }

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void campos(EditText campo, final TextInputLayout til){

        campo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                til.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void ModifcarDatos() {
        String id=tilID.getEditText().getText().toString();
        String nombre = tilNombre.getEditText().getText().toString();
        String apellido1=tilapellido.getEditText().getText().toString();
        String apellido2=tilapellido2.getEditText().getText().toString();

        String telefono = tilTelefono.getEditText().getText().toString();
        String direccion=tildireccion.getEditText().getText().toString();
        //String correo = tilCorreo.getEditText().getText().toString();
        boolean a=esDiryIDva(id,"Codigo Invalido",tilID,10);
        boolean b = esNomapellidoValido(apellido1,"Nombre Invalido",tilNombre);
        boolean c = esNomapellidoValido(apellido2,"Apellido Invalido",tilapellido);
        boolean d = esNomapellidoValido(nombre,"Apellido Invalido",tilapellido2);
        boolean e= esDiryIDva(direccion,"Direccion Invalida",tildireccion,80);
        boolean f = esTelefonoValido(telefono);

        //boolean c = esCorreoValido(correo);

        if (a && b && c && d && e && f) {
            // OK, se pasa a la siguiente acción
            sex=sexo();

          if(herramienta.modificar_Cliente(this,id,nombre,apellido1,apellido2,telefono,direccion)){
              super.onBackPressed();
          }else {

              AlertDialog.Builder builder =
                      new AlertDialog.Builder(this);

              builder.setMessage("Error")
                      .setTitle("El Identificador (ID) ya se encuentra registrado, No puede modificar el ID por uno ya registrado")
                      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int id) {
                              dialog.cancel();
                          }
                      });

          }



        }

    }


}
