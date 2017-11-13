package mancash_outs.cash_outs.p43z.mycash_outs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class agregartarifa extends AppCompatActivity {

    private TextInputLayout tiltarifa;
    private TextInputLayout tildescripcion;
    private TextInputLayout tilmonto;

    private EditText campotarifa;
    private EditText campodescripcion;
    private EditText campomonto;
    private BD_tool herramienta=new BD_tool();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregartarifa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tiltarifa=(TextInputLayout) findViewById(R.id.til_tarifa);
        tildescripcion=(TextInputLayout)findViewById(R.id.til_descscripcion);
        tilmonto=(TextInputLayout)findViewById(R.id.til_monto);
        campotarifa=(EditText)findViewById(R.id.campo_titulo_ruta_ID);
        campodescripcion=(EditText)findViewById(R.id.campo_descripcion);
        campomonto=(EditText)findViewById(R.id.campo_titulo_monto);


        campos(campotarifa,tiltarifa);
        campos(campodescripcion,tildescripcion);
        campos(campomonto,tilmonto);

        Button botonAceptar = (Button) findViewById(R.id.boton_aceptar);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }

        });


        Button botonCancelar = (Button) findViewById(R.id.boton_cancelar);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        agregartarifa.super.onBackPressed();

            }
        });



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


    private void validarDatos() {

        String tituloTarif=tiltarifa.getEditText().getText().toString();
        String descripcion=tildescripcion.getEditText().getText().toString();
        String monto=tilmonto.getEditText().getText().toString();
        boolean a=herramienta.escampovalido(tituloTarif,1,tiltarifa);
        boolean c=herramienta.escampovalido(monto,2,tilmonto);
        if (a && c){
            if(herramienta.GuardarTarifa(this,tituloTarif,descripcion,Integer.parseInt(monto))){
                super.onBackPressed();
            }


        }



    }





}
