package mancash_outs.cash_outs.p43z.mycash_outs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class DetalleFactura extends AppCompatActivity {


    private static final String PAGO="Pago";
    private static final String INFORME="informe";
    private static final String OPC="Opcion";


    public int  sMonth, sYear, sDay, sHour, sMin, sAM;
    FloatingActionsMenu opciones;
   FloatingActionButton menuPrincipal, nuevoRegistro, compartir;
    Toolbar toolbar;
    TextView edt_fecha;
    TextView edt_hora;
    TextView edt_nombre_cli;
    TextView edt_tarifa;
    TextView edt_cant;
    TextView edt_estado;
    TextView edt_us;
    TextView edt_numfac;
    TextView edt_concept;
    TextView edt_monto;
    TextView txt_titulo;
    TextView txt_cant;
    TextView txt_estado;
    TextView txt_tarifa;
    TextView txt_numfac;
    TextView txt_concept;
    TextView txt_monto;



    public static Pago recibo;
    public static   Person cliente;
    private BD_tool bd_tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_factura);
         toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        Bundle x = this.getIntent().getExtras();
        if (x != null) {
            switch (x.getInt(OPC)){

                case 1:
                    recibo= (Pago) x.getSerializable(PAGO);
                    cliente=null;
                    break;
                case 2:
                    cliente=(Person) x.getSerializable(INFORME);
                    recibo=null;
                    break;
            }
        }
        txt_titulo=(TextView) findViewById(R.id.info_titu);
        txt_numfac=(TextView) findViewById(R.id.txt_num_fac);
        txt_tarifa=(TextView) findViewById(R.id.txt_tarifa);
        txt_cant=(TextView) findViewById(R.id.txt_cantidad);
        txt_concept=(TextView) findViewById(R.id.txt_concepto);
        txt_monto=(TextView) findViewById(R.id.txt_monto);
        txt_estado=(TextView) findViewById(R.id.txt_cond_pago);
        edt_us = (TextView) findViewById(R.id.txt_cliente);
        edt_numfac = (TextView) findViewById(R.id.txt_fac);
        edt_concept = (TextView) findViewById(R.id.txt_va_concep);
        edt_monto = (TextView) findViewById(R.id.txt_pago);
        edt_fecha = (TextView) findViewById(R.id.id_fecha_sis);
        edt_hora = (TextView) findViewById(R.id.id_hora);
        edt_nombre_cli = (TextView) findViewById(R.id.edt_Nombre);
        edt_tarifa = (TextView) findViewById(R.id.edt_tarifa);
        edt_cant = (TextView) findViewById(R.id.edt_cantidad);
        edt_estado = (TextView) findViewById(R.id.edt_condicion);



        cCalendario();

        opciones = (FloatingActionsMenu) findViewById(R.id.float_menu_opciones);
        nuevoRegistro = (FloatingActionButton) findViewById(R.id.float_nuevo_registro);
        compartir = (FloatingActionButton) findViewById(R.id.float_compartir);

        if(recibo!=null){
            datos();
        }else{
            informe_pagos();
        }



        nuevoRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
RegistroPago.createInstance(DetalleFactura.this,bd_tool.persona(DetalleFactura.this,recibo.getId_persona()));
            }
        });
        compartir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                saveBitmap(takeScreenshot());


            }
        });
    }

    private void informe_pagos() {
        bd_tool=new BD_tool();

            txt_titulo.setText("Informe De Cliente");
            edt_us.setText(""+cliente.getID());
            edt_nombre_cli.setText(cliente.getNombre()+" "+cliente.getApellido1()+" "+cliente.getApellido2());
             edt_hora.setVisibility(View.GONE);
            cargaFecha2(sMin,sAM,sHour, edt_fecha);
            txt_numfac.setVisibility(View.GONE);
            edt_numfac.setVisibility(View.GONE);
            txt_tarifa.setVisibility(View.GONE);
            edt_tarifa.setVisibility(View.GONE);
            txt_cant.setVisibility(View.GONE);
            edt_cant.setVisibility(View.GONE);
            txt_concept.setText("Registros: ");
            edt_concept.setText(text_pagos());
            txt_monto.setText("Monto Pendiente: ");
            int mont_total=mont_pendiente();

            edt_monto.setText(""+mont_total);
            txt_estado.setText("Condicion: ");
            if(mont_total>0){
                edt_estado.setTextColor(Color.parseColor(("#F44336")));
                edt_estado.setText("CON MOROSIDAD");
            }else{
                edt_estado.setTextColor(Color.parseColor(("#4CAF50")));
                edt_estado.setText("SIN PENDIENTES");
            }






    }

    public SpannableString text_condi_pago(boolean opc){
        SpannableString text=new SpannableString("");

        if(opc){
            text= new SpannableString("Cancelado");
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), 0,6,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else{
            text= new SpannableString("Pendiente");
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#F44336")), 0,6,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return text;
    }

    public SpannableString text_pagos(){
        SpannableString text=new SpannableString("\n");
        bd_tool=new BD_tool();
        for(Pago pago :bd_tool.Lista_pagosC(this,cliente.getID())){
            SpannableString deta=new SpannableString(pago.getTipo_serv()+" ₵"+pago.getMonto()+" - "+text_condi_pago(pago.isEstado_pago()));
            text = new SpannableString(TextUtils.concat(text,deta,"\n"));

        }

        return text;

    }
    public int mont_pendiente(){
    int cant_tot=0;
        bd_tool=new BD_tool();
        for(Pago pago :bd_tool.Lista_pagosC(this,cliente.getID())){
            if(pago.isEstado_pago()==false)
           cant_tot=cant_tot+pago.getMonto();
        }
        return cant_tot;
    }



    public void cargaFecha(int  min, int am_pm, int horas, TextView hora){
        String y;
        if (min <= 9 && am_pm == 0) {
            y = horas + ":" + "0" + min + " " + "AM";
            hora.setText(y);
            //fecha.setText(x);
        } else if (min <= 9 && am_pm == 1) {
            y = horas + ":" + "0" + min + " " + "PM";
            hora.setText(y);
            //fecha.setText(x);
        }else{
            //fecha.setText(x);
            if(am_pm==1){
                y = horas+ ":" + min + " " + "PM";
                hora.setText(y);
            }else{
                y = horas + ":" + min + " " + "AM";
                hora.setText(y);
            }

        }

    }

    public void cargaFecha2(int  min, int am_pm, int horas, TextView fecha){
        String y;
        if (min <= 9 && am_pm == 0) {
            y = horas + ":" + "0" + min + " " + "AM";
            fecha.setText(sDay+"/"+sMonth+"/"+sYear+" "+y);
            //fecha.setText(x);
        } else if (min <= 9 && am_pm == 1) {
            y = horas + ":" + "0" + min + " " + "PM";
            fecha.setText(sDay+"/"+sMonth+"/"+sYear+" "+y);
        }else{
            //fecha.setText(x);
            if(am_pm==1){
                y = horas+ ":" + min + " " + "PM";
                fecha.setText(sDay+"/"+sMonth+"/"+sYear+" "+y);
            }else{
                y = horas + ":" + min + " " + "AM";
                fecha.setText(sDay+"/"+sMonth+"/"+sYear+" "+y);
            }

        }

    }





    public void datos(){

    bd_tool=new BD_tool();
        cargaFecha(sMin,sAM,sHour, edt_hora);
        try {

            edt_us.setText(""+recibo.getId_persona());
            edt_concept.setText("Cobro "+recibo.getTipo_serv());
            edt_numfac.setText(""+recibo.getN_comprob());
            edt_monto.setText(""+recibo.getMonto());
            edt_fecha.setText(recibo.getFecha());
            edt_nombre_cli.setText(bd_tool.nombre_cli(this,recibo.getId_persona()));
            edt_tarifa.setText(recibo.getTarifa());
            edt_cant.setText(""+recibo.getCantidad());
          if(recibo.isEstado_pago()){
              edt_estado.setTextColor(Color.parseColor(("#4CAF50")));
              edt_estado.setText("CANCELADO");
          }else {
              edt_estado.setTextColor(Color.parseColor(("#F44336")));
              edt_estado.setText("PENDIENTE");
          }
        }catch (Exception e){

        }

    }
    public void cCalendario(){
        Calendar C = Calendar.getInstance();
        sYear = C.get(Calendar.YEAR);
        sMonth = C.get(Calendar.MONTH);
        sDay = C.get(Calendar.DAY_OF_MONTH);
        sHour = C.get(Calendar.HOUR);
        sMin = C.get(Calendar.MINUTE);
        sAM = C.get(Calendar.AM_PM);
    }
    public Bitmap takeScreenshot() {

      View x = findViewById(R.id.linea);
        Bitmap nuevo=Bitmap.createBitmap(x.getWidth(), x.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(nuevo);
        //Get the view's background
        Drawable bgDrawable =x.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        x.draw(canvas);
        //return the bitmap
        return nuevo;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete:
          //      Confirmacion();
                return true;
            case R.id.action_add:
         RegistroPago.modificarInstancia(this,recibo);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            imagePath.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imagePath));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }


    public static void createInstance(Activity activity, Pago registro) {
        Intent intent = getLaunchIntent(activity, registro);
        activity.startActivity(intent);
    }

    public static Intent getLaunchIntent(Context context, Pago registro) {
        Intent intent = new Intent(context, DetalleFactura.class);
        intent.putExtra(OPC,1);
        intent.putExtra(PAGO,registro);
        return intent;
    }


    public static void generaInforme(Activity activity, Person client) {
        Intent intent = lanzarInforme(activity, client);
        activity.startActivity(intent);
    }

    public static Intent lanzarInforme(Context context, Person client) {
        Intent intent = new Intent(context, DetalleFactura.class);
        intent.putExtra(OPC,2);
        intent.putExtra(INFORME,client);
        return intent;
    }
}

