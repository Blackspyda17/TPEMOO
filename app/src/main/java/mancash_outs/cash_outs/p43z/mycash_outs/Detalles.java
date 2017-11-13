package mancash_outs.cash_outs.p43z.mycash_outs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import layout.detalles_frag;
import layout.listapagos;

import static android.widget.Toast.LENGTH_LONG;


public class Detalles extends AppCompatActivity implements View.OnLongClickListener {

    public boolean is_in_action_mode=false;
/*        private static final String EXTRA_NAME = "mancash_outs.cash_outs.p43z.mycash_outs.name";
        private static final String EXTRA_DRAWABLE = "mancash_outs.cash_outs.p43z.mycash_outs.drawable";
    public static  String id_perso;
    public  static String nom_per;*/
    public  static String name;
    public  static int idDrawable;


    private static final String PERSONA="Persona";
    public TextView contador_text;
    public listapagos Pagos;
    public ArrayList<Pago> Pagos_seleccionadas=new ArrayList<>();
    public Toolbar toolbar;
    private TabLayout tabLayout;
    ViewPagerAdapter adapter;
    public ViewPager viewPager;
    private FloatingActionsMenu fab_menu;
    private BD_tool tools=new BD_tool();
    public static Person Cliente;
    int contador=0;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detalles);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
       fab_menu=(FloatingActionsMenu) findViewById(R.id.menu_fab);

       contador_text= (TextView) findViewById(R.id.contador_toolbar_user) ;
       contador_text.setText(name);


            CollapsingToolbarLayout collapser =
                    (CollapsingToolbarLayout) findViewById(R.id.collapser);
          //collapser.setTitle(name); // Cambiar título

            loadImageParallax(idDrawable);// Cargar Imagen

            // Setear escucha al FAB
/*            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent registroN= new Intent(Detalles.this,RegistroPago.class);
                            registroN.putExtra("persona",Cliente);
                            startActivity(registroN);
                        }
                    }
            );*/





            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);


            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton agrega = (FloatingActionButton) findViewById(R.id.accion_agrega);
        agrega.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fab_menu.collapse();
                RegistroPago.createInstance(Detalles.this,Cliente);
            }
        });


        FloatingActionButton inform = (FloatingActionButton) findViewById(R.id.accion_informe);
        inform.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fab_menu.collapse();
                if(tools.cant_pagos(Detalles.this,Cliente.getID())>0){
                    DetalleFactura.generaInforme(Detalles.this,Cliente);
                }else {
                    Toast.makeText(Detalles.this, "Debes registrar pagos para generar un informe", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    @Override
    public void onBackPressed() {


        if(is_in_action_mode){
            quitar_actionmode();
            notificar_cambio_view();
        }

        else {
            super.onBackPressed();
        }
    }

        public static void createInstance(Activity activity, Person persona) {
            Intent intent = getLaunchIntent(activity, persona);
            activity.startActivity(intent);
        }

        public static Intent getLaunchIntent(Context context, Person persona) {
            Intent intent = new Intent(context, Detalles.class);
            Cliente=persona;
            name = Cliente.getNombre();
            idDrawable = Cliente.getIcono();
            return intent;
        }



    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Se carga una imagen aleatoria para el detalle
     */
    private void loadImageParallax(int id) {
        ImageView image = (ImageView) findViewById(R.id.image_paralax);
        // Usando Glide para la carga asíncrona
        Glide.with(this)
                .load(id)
                .centerCrop()
                .into(image);
    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
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
                Confirmacion();
                return true;
            case R.id.action_modify:
                Boolean modi= true;
                Intent x = new Intent(this,Agregarcliente.class);
                x.putExtra("ban",modi);
                x.putExtra("id",Cliente.getID());
                startActivity(x);
                return true;
            case android.R.id.home:
                if(is_in_action_mode){
                quitar_actionmode();
                notificar_cambio_view();
                }else{
                    onBackPressed();
                }
                return true;
            case R.id.borrar:
                is_in_action_mode = false;
                try {
                    PagoAdapter pagoAdapter = Pagos.adapter;
                    pagoAdapter.updateAdapter(Pagos_seleccionadas);
                    quitar_actionmode();
                    notificar_cambio_view();
                }catch (Exception e){

                }

                return true;





            default:
                return super.onOptionsItemSelected(item);
        }


    }
    public void Confirmacion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Usted esta a punto de Eliminar a "+ Cliente.getNombre())
                .setTitle("Seguro que desea eliminarlo?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        elimiPer();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    public void elimiPer(){
        BD_tool ne = new BD_tool();
        ne.eliminar(this,1,Cliente.getID());
        super.onBackPressed();
    }
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        detalles_frag fragmentDemo = detalles_frag.newInstance(Cliente);
        Pagos=listapagos.newInstance(Cliente.getID());
        ft.commit();
        adapter.addFragment(fragmentDemo, "Informacion");
        adapter.addFragment(Pagos, "Pagos");
        viewPager.setAdapter(adapter);


    }

    @Override
    public boolean onLongClick(View v) {

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action_mode);

        contador_text.setText(name);
        is_in_action_mode=true;
        notificar_cambio_view();
        return true;
    }

    public void preparar_select(View view, int position) {

                try {
                    if (((CheckBox) view).isChecked()) {


                        Pagos_seleccionadas.add(Pagos.LPagos.get(position));
                        contador++;
                        actualizar_conta(contador);


                    } else {
                        Pagos_seleccionadas.remove(Pagos.LPagos.get(position));
                        contador--;
                        actualizar_conta(contador);
                    }
                } catch (Exception e) {

                }

    }

    public void actualizar_conta(int contad){

                if(contad==0){

                    contador_text.setText("0 Registros Seleccionados");
                }else {
                    contador_text.setText(contad+" Registros Seleccionados");
                }




    }

    public void quitar_actionmode(){
        is_in_action_mode=false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.detalles);
        contador_text.setText(name);
        contador=0;
        Pagos_seleccionadas.clear();
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void notificar_cambio_view(){

                try{
                    Pagos.adapter.notifyDataSetChanged();
                }catch (Exception e){

                }


        }

}
