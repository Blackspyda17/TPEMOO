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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

public class Detalle_tarifa extends AppCompatActivity {

    private static final String TARIFA="Tarifa";



    FloatingActionsMenu opciones;
    FloatingActionButton menuPrincipal, nuevoRegistro, compartir;

    private Tarifa tarifa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarifa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        tarifa= (Tarifa) i.getSerializableExtra(TARIFA);
        datos();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        opciones = (FloatingActionsMenu) findViewById(R.id.float_menu_opciones);
        nuevoRegistro = (FloatingActionButton) findViewById(R.id.float_nuevo_registro);
        compartir = (FloatingActionButton) findViewById(R.id.float_compartir);


        nuevoRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent x=new Intent(Detalle_tarifa.this,agregartarifa.class);
                startActivity(x);

            }
        });
        compartir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                saveBitmap(takeScreenshot());


            }
        });


    }
    public static void createInstance(Activity activity, Tarifa registro) {
        Intent intent = getLaunchIntent(activity, registro);
        activity.startActivity(intent);
    }

    public static Intent getLaunchIntent(Context context, Tarifa registro) {
        Intent intent = new Intent(context, Detalle_tarifa.class);
        intent.putExtra(TARIFA,registro);
        return intent;
    }




    public void datos(){
        TextView titulo= (TextView) findViewById(R.id.txt_titulo);
        TextView descripcion= (TextView) findViewById(R.id.desc_tarifa);
        TextView precio = (TextView) findViewById(R.id.txt_precio);


        try {
            titulo.setText(" "+tarifa.getTitulo());
            descripcion.setText(" "+tarifa.getDescripcion());
            precio.setText(""+tarifa.getMonto());
        }catch (Exception e){

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tarifa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_delete:
                BD_tool tool=new BD_tool();
                tool.eliminar(this,3,tarifa.getTitulo());
                super.onBackPressed();
                //      Confirmacion();
                return true;
            case R.id.action_add:
                Intent x=new Intent(Detalle_tarifa.this,agregartarifa.class);
                startActivity(x);
                return true;






            default:
                return super.onOptionsItemSelected(item);
        }


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




    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/Tarifa.png");
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
}
