package mancash_outs.cash_outs.p43z.mycash_outs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnLongClickListener {
    public boolean is_in_action_mode=false;

    ActionBarDrawerToggle toggle;
    ViewPagerAdapter adapter;
    public TextView contador_text;
    public ImageButton bnt_back;
    public Toolbar toolbar;
    private TabLayout tabLayout;
    public ViewPager viewPager;
    public listaclientes Clientes;
    public listatarifas Tarifas;
    public ArrayList<Tarifa> Tarifas_seleccionadas=new ArrayList<>();
    public ArrayList<Person> Clientes_seleccionados=new ArrayList<>();
    public int tab_principal =1;
    int contador=0;
    FloatingActionButton fab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

      if (toolbar != null) {
          setSupportActionBar(toolbar);

        }


checkWritePermission();




        bnt_back=(ImageButton) findViewById(R.id.btn_back);
        bnt_back.setVisibility(View.GONE);
            // listen to the backstack of the fragment manager

        contador_text= (TextView) findViewById(R.id.contador_toolbar) ;
        contador_text.setText("TPEMOO");


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tab_principal ==1){
                    Intent agregacli= new Intent(Principal.this,Agregarcliente.class);
                    startActivity(agregacli);
                }else {
                    Intent agregatari=new Intent(Principal.this,agregartarifa.class);
                    startActivity(agregatari);
                }



            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(viewPager.getAdapter().getPageTitle(position).equals("Tarifas")){
                    fab.setImageResource(R.drawable.ic_agrega_tarifa);
                    tab_principal =2;


                }else {
                    fab.setImageResource(R.drawable.ic_person_add_black_24dp);
                    tab_principal =1;
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if(is_in_action_mode){
            quitar_actionmode();
        notificar_cambio_view(tab_principal);
        }

        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item) && is_in_action_mode) {
            quitar_actionmode();
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id== R.id.borrar){
            is_in_action_mode=false;
            switch (tab_principal){
                case 1:
                    PersonAdapter personAdapter=Clientes.adapter;
                    personAdapter.updateAdapter(Clientes_seleccionados);
                    quitar_actionmode();
                    break;
                case 2:
                    TarifaAdapter tarifaAdapter=Tarifas.adapter;
                    tarifaAdapter.updateAdapter(Tarifas_seleccionadas);
                    quitar_actionmode();
                    break;
            }

        }



        //noinspection SimplifiableIfStatement
       else if (id == R.id.action_add) {
            switch (tab_principal){
                case 1:
                    Intent agregacli= new Intent(Principal.this,Agregarcliente.class);
                    startActivity(agregacli);
                    break;
                case 2:
                    Intent agregatari=new Intent(Principal.this,agregartarifa.class);
                    startActivity(agregatari);
                    break;
            }
            return true;
        }

        else if (id == R.id.action_delete) {
            activar_actionmode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            Intent nuevo=new Intent(this,Agregarcliente.class);
            startActivity(nuevo);
        } else if (id == R.id.nav_send) {
Intent nuevo=new Intent(this,agregartarifa.class);
            startActivity(nuevo);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean comprobar() {
        BD_cashouts baseHelper = new BD_cashouts(this, "STBD", null, 1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("select * from Persona", null);
            int cantidad = c.getCount();
            if(cantidad<1){
                return true;
            }



        }


        return false;
    }






    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Clientes=new listaclientes();
        Tarifas=new listatarifas();
        adapter.addFragment(Clientes, "Clientes");
        adapter.addFragment(Tarifas, "Tarifas");

        viewPager.setAdapter(adapter);


    }

    @Override
    public boolean onLongClick(View v) {
        activar_actionmode();
        return true;
    }


    public void activar_actionmode(){

        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action_mode);
        bnt_back.setVisibility(View.VISIBLE);
        contador_text.setText("0 items seleccionados");
        is_in_action_mode=true;
        notificar_cambio_view(tab_principal);
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

    public void preparar_select(View view, int position) {
        switch (tab_principal){
            case 1:

                try {
                    if (((CheckBox) view).isChecked()) {


                        Clientes_seleccionados.add(Clientes.LPersonas.get(position));
                        contador++;
                        actualizar_conta(contador);



                    } else {
                        Clientes_seleccionados.remove(Clientes.LPersonas.get(position));
                        contador--;
                        actualizar_conta(contador);
                    }
                } catch (Exception e) {

                }
                break;
            case 2:
                try {
                    if (((CheckBox) view).isChecked()) {


                        Tarifas_seleccionadas.add(Tarifas.LTarifas.get(position));
                        contador++;
                        actualizar_conta(contador);



                    } else {
                        Tarifas_seleccionadas.remove(Tarifas.LTarifas.get(position));
                        contador--;
                        actualizar_conta(contador);

                    }
                } catch (Exception e) {

                }
                break;

        }

    }


    public void actualizar_conta(int contad){
switch (tab_principal){
    case 1:
        if(contad==0){

            contador_text.setText("0 Clientes Seleccionados");
        }else {
            contador_text.setText(contad+" Clientes Seleccionados");
        }
        break;

    case 2:
        if(contad==0){
            contador_text.setText("0 Tarifas Seleccionadas");
        }else {
            contador_text.setText(contad+" Tarifas Seleccionadas");
        }
        break;
}

    }

    public void quitar_actionmode(){
        is_in_action_mode=false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.principal);
        contador_text.setText("TPEMOO");
        bnt_back.setVisibility(View.GONE);
        contador=0;
        Clientes_seleccionados.clear();
        Tarifas_seleccionadas.clear();
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }


public void backButtonHandler(View v){
   quitar_actionmode();
    notificar_cambio_view(tab_principal);
}


    public void notificar_cambio_view(int posicion_tab){
        switch (posicion_tab){
            case 1:
                try{
                    Clientes.adapter.notifyDataSetChanged();
                }catch (Exception e){

                }
                break;
            case 2:
                try{
                    Tarifas.adapter.notifyDataSetChanged();

                }catch (Exception e){

                }
        }
    }

    private void checkWritePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar escribir.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para enviar Escribir!");
        }
    }


}





