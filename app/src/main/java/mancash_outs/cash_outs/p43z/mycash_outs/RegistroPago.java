package mancash_outs.cash_outs.p43z.mycash_outs;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegistroPago extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String MODIFICAR_PAGO = "Pago";
    private static final String OPCION = "Opc";

 private static final String PERSONA="Persona";

    protected void onResume() {
        super.onResume();
        cargar_tarifas();
    }

    private int mMonth, mYear, mDay, año, sMonth, sYear, sDay;
    static final int DATE_ID = 0;

    boolean cambio_fecha_ini = false;
    boolean cambio_fecha_fin = false;
    boolean cambio_fecha_emi = false;
    private int precio_tarifa = 0;
    SimpleDateFormat fechaIn = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fechaEx = new SimpleDateFormat("dd/MM/yyyy");
    private List<Tarifa> lista_tarifas;

    private ArrayAdapter<String> dataAdapter2;
    private Person Cliente;
    private Pago pago_modi;

    private Date fechaini = null;
    private Date fechafinal = null;
    private Date fechaEmision = null;
    private Switch swt_estado;
    private TextView descripcion_tarifa;
    private TextView estado;
    private TextView txt_habilita;


    private TextInputLayout tilcantidad;
    private TextInputLayout tilpagar;
    private TextInputLayout tilconcepto;
    private EditText campo_concepto;
    private EditText campo_cantidad;

    private EditText campo_pagar;


    private Button fecha_ini;
    private Button fecha_fin;
    private Button botonfechaem;
    private Button Aceptar;
    TextView fecha_emis;
    TextView informpago;
    Spinner spinner;
    Spinner spinner2;
    BD_tool tools_varios = new BD_tool();
    List<String> tarifas = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pago);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        fecha_emis = (TextView) findViewById(R.id.txt_fechaemi);
        informpago = (TextView) findViewById(R.id.txt_infpag);
        estado = (TextView) findViewById(R.id.txt_condi_pago);
        tilconcepto = (TextInputLayout) findViewById(R.id.til_concepto);
        tilcantidad = (TextInputLayout) findViewById(R.id.til_cantidad);
        tilpagar = (TextInputLayout) findViewById(R.id.til_pagar);
        campo_concepto = (EditText) findViewById(R.id.campo_conceptopago);
        campo_cantidad = (EditText) findViewById(R.id.campo_cantidad);
        campo_pagar = (EditText) findViewById(R.id.campo_pagar);
        descripcion_tarifa = (TextView) findViewById(R.id.txt_descripcion);

        swt_estado = (Switch) findViewById(R.id.swt_condicion);

        Calendar C = Calendar.getInstance();
        sYear = C.get(Calendar.YEAR);
        sMonth = C.get(Calendar.MONTH);
        sDay = C.get(Calendar.DAY_OF_MONTH);
        fecha_emis.setText(FechaAct());


     Bundle x = this.getIntent().getExtras();
        if (x != null) {
            switch (x.getInt(OPCION)){

                case 1:
                    Cliente=(Person) x.getSerializable(PERSONA);
                    pago_modi=null;
                    break;
                case 2:
                    pago_modi=(Pago) x.getSerializable(MODIFICAR_PAGO);
                    Cliente=null;
                    break;
            }
        }

        fechaini = ObtenerFecha(FechaAct());
        fechafinal = ObtenerFecha(FechaAct());
        fechaEmision = ObtenerFecha(FechaAct());


        fecha_ini = (Button) findViewById(R.id.btn_fechaini);
        fecha_fin = (Button) findViewById(R.id.btn_fechafin);


        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> modalidadesPago = new ArrayList<String>();
        modalidadesPago.add("Dia");
        modalidadesPago.add("Semana");
        modalidadesPago.add("Quincena");
        modalidadesPago.add("Mes");
        modalidadesPago.add("Personalizado");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modalidadesPago);


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        botonfechaem = (Button) findViewById(R.id.btn_fechaemi);


        botonfechaem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
                cambio_fecha_emi = true;

            }

        });


        fecha_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cambio_fecha_ini = true;
                cambio_fecha_fin = false;

                switch (spinner.getSelectedItem().toString()) {
                    case "Mes":
                        showDialog(1);
                        break;
                    default:
                        showDialog(DATE_ID);
                        break;
                }


            }

        });


        fecha_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);

                cambio_fecha_ini = false;
                cambio_fecha_fin = true;


            }

        });

        Aceptar = (Button) findViewById(R.id.boton_aceptar);


        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pago_modi != null) {
                    validarPago(2);
                }else {
                    validarPago(1);
                }

            }

        });

        Button botonCancelar = (Button) findViewById(R.id.boton_cancelar);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             RegistroPago.super.onBackPressed();
            }
        });


        campo_cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (String.valueOf(s).length() > 0) {
                    montoPagar(Integer.parseInt(String.valueOf(s)));
                } else {
                    campo_pagar.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });








        swt_estado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    estado.setText("Pago Cancelado");
                } else {
                    estado.setText("Pago Pendiente");
                }
            }
        });

        if (pago_modi != null) {
            modifica_pago(fecha_emis, spinner, informpago, spinner2, tilcantidad, tilpagar, tilconcepto);
            Aceptar.setText("Actualizar");
        }
    }



    private void modifica_pago(TextView fecha_emis, Spinner spinner, TextView informpago, Spinner spinner2, TextInputLayout tilcantidad, TextInputLayout tilpagar, TextInputLayout tilconcepto) {
        fecha_emis.setText(pago_modi.getFecha());
        cambiar_spin(spinner, pago_modi.getModalidad());
        cambiar_spin(spinner2, pago_modi.getTarifa());
        tilcantidad.getEditText().setText("" + pago_modi.getCantidad());
        tilpagar.getEditText().setText("" + pago_modi.getMonto());
        tilconcepto.getEditText().setText(pago_modi.getDetalle());
        informpago.setText(pago_modi.getTipo_serv());
        swt_estado.setChecked(pago_modi.isEstado_pago());
    }

    private void cambiar_spin(Spinner spinner, String modalidad) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(modalidad)) {
                index = i;
                break;
            }
        }

        if (index == 0) {
            try {
                dataAdapter2.add(modalidad);
                dataAdapter2.notifyDataSetChanged();
                cambiar_spin(spinner, modalidad);
            } catch (Exception e) {

            }
        } else {
            spinner.setSelection(index);
        }


    }

    private void montoPagar(int i) {
        if (i > 0 && precio_tarifa > 0) {
            campo_pagar.setText("" + i * precio_tarifa);
        }

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        switch (item) {
            case "Dia":
                fecha_fin.setVisibility(View.GONE);
                fecha_ini.setText("Cambiar Fecha del dia de Pago");
                informpago.setText("Pago Correspondiente Al Dia " + FechaAct());
                break;
            case "Semana":
                fecha_fin.setVisibility(View.GONE);
                fecha_ini.setText("Cambiar Fecha de Inicio de semana");
                informpago.setText("Pago Del Dia " + FechaAct() + " Al Dia " + fechaEx.format(sumarRestarDiasFecha(ObtenerFecha(FechaAct()), 7)));
                break;

            case "Quincena":
                fecha_fin.setVisibility(View.GONE);
                fecha_ini.setText("Cambiar Fecha de Inicio de Quincena");
                informpago.setText("Pago Del Dia " + FechaAct() + " Al Dia " + fechaEx.format(sumarRestarDiasFecha(ObtenerFecha(FechaAct()), 15)));
                break;

            case "Mes":
                fecha_fin.setVisibility(View.GONE);
                fecha_ini.setText("Cambiar Mes del Pago");
                informpago.setText("Pago Correspondiente Al Mes " + sMonth);
                break;

            case "Personalizado":
                fecha_fin.setVisibility(View.VISIBLE);
                fecha_ini.setText("Cambiar Fecha de Inicio");
                informpago.setText("Pago Del Dia " + FechaAct() + " Al Dia " + FechaAct());

                break;
            case "Ninguna":
                precio_tarifa = 0;
                descripcion_tarifa.setText("Descripcion: ");
                campo_pagar.setEnabled(true);

                break;

            case "Agregar Tarifa":
                Intent agregatarifa = new Intent(RegistroPago.this, agregartarifa.class);
                startActivity(agregatarifa);

                break;

            default:
                campo_pagar.setEnabled(true);
                colocar_tarifa(posTarifa(item));

                break;


        }
    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            if (cambio_fecha_emi) {
                fecha_emis.setText(Fechadia());
                fechaEmision = ObtenerFecha(Fechadia());
                cambio_fecha_emi = false;
            } else {


                switch (spinner.getSelectedItem().toString()) {

                    case "Dia":
                        fechaini = ObtenerFecha(Fechadia());
                        informpago.setText("Pago Del Dia " + fechaEx.format(fechaini).toString());
                        break;

                    case "Semana":
                        fechaini = ObtenerFecha(Fechadia());
                        informpago.setText("Pago Del Dia " + fechaEx.format(fechaini).toString() + " Al Dia " + fechaEx.format(sumarRestarDiasFecha(fechaini, 7)));
                        break;

                    case "Quincena":
                        fechaini = ObtenerFecha(Fechadia());
                        informpago.setText("Pago Del Dia " + fechaEx.format(fechaini).toString() + " Al Dia " + fechaEx.format(sumarRestarDiasFecha(fechaini, 15)));
                        break;

                    case "Mes":
                        informpago.setText("Pago Correspondiente Al Mes " + mMonth);
                        break;

                    case "Personalizado":

                        if (cambio_fecha_ini) {

                            fechaini = ObtenerFecha(Fechadia());
                            informpago.setText("Pago Del Dia " + fechaEx.format(fechaini).toString() + " Al Dia " + fechaEx.format(fechafinal).toString());
                        } else {
                            fechafinal = ObtenerFecha(Fechadia());
                            informpago.setText("Pago Del Dia " + fechaEx.format(fechaini).toString() + " Al Dia " + fechaEx.format(fechafinal).toString());
                        }
                        break;
                }


            }
        }
    };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYear, sMonth, sDay);
            case 1:
                return createDialogWithoutDateField();

        }
        return null;
    }

    public String Fechadia() {


        String dia = "";
        String mes = "";


        if (mDay < 10) {
            dia = "0" + mDay;
        } else {
            dia = "" + mDay;
        }

        if (mMonth < 10) {
            mes = "0" + mMonth;
        } else {
            mes = "" + mMonth;
        }


        return dia + "/" + mes + "/" + mYear;


    }

    public String FechaAct() {


        String dia = "";
        String mes = "";


        if (sDay < 10) {
            dia = "0" + sDay;
        } else {
            dia = "" + sDay;
        }

        if (sMonth < 10) {
            mes = "0" + sMonth;
        } else {
            mes = "" + sMonth;
        }


        return dia + "/" + mes + "/" + sYear;


    }

    public Date ObtenerFecha(String fecha) {
        Date nueva = null;
        try {
            nueva = fechaEx.parse(fecha);


        } catch (Exception e) {
            Toast.makeText(this, "no se convirtio la fecha", Toast.LENGTH_SHORT);

        }
        return nueva;
    }

    public Date fecha_BD(String fecha) {

        Date nueva = null;
        Date antigua = null;
        try {
            System.out.println(fecha);
            antigua = fechaEx.parse(fecha);
            System.out.println("La fecha es para: " + antigua);
            nueva = fechaIn.parse(fechaIn.format(antigua));
            System.out.println("la nueva es: " + nueva);


        } catch (Exception e) {
            Toast.makeText(this, "no se convirtio la fecha", Toast.LENGTH_SHORT);

        }
        return nueva;
    }


    public Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

    public MyDatePickerDialog createDialogWithoutDateField() {

        MyDatePickerDialog dpd = new MyDatePickerDialog(this, mDateSetListener, sYear, sMonth, sDay) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                int day = getContext().getResources()
                        .getIdentifier("android:id/day", null, null);
                if (day != 0) {
                    View dayPicker = findViewById(day);
                    if (dayPicker != null) {
                        dayPicker.setVisibility(View.GONE);
                    }
                }
            }
        };
        dpd.setPermanentTitle("Ingrese un Mes y el Año");
        return dpd;
    }


    public void cargar_tarifas() {
        tarifas = new ArrayList<>();
        lista_tarifas = tools_varios.Lista_Tarifas(this);
        for (Tarifa tarifa : lista_tarifas) {
            tarifas.add(tarifa.getTitulo());
        }
        tarifas.add("Ninguna");
        tarifas.add("Agregar Tarifa");


        dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tarifas);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);
    }

    public int posTarifa(String titulo) {

        for (Tarifa elemento : lista_tarifas) {
            if (elemento.getTitulo().equals(titulo)) {
                return lista_tarifas.indexOf(elemento);
            }
        }
        return -1;

    }

    public void colocar_tarifa(int posicion) {

        Tarifa nueva = lista_tarifas.get(posicion);
        descripcion_tarifa.setText("Descripcion: " + nueva.getDescripcion());
        precio_tarifa = nueva.getMonto();

    }


    private void validarPago(int cond) {
        int estado = 0;
        String id_persona = "";
        if (swt_estado.isChecked()) {
            estado = 1;
        }
        if(pago_modi !=null) {
            id_persona=pago_modi.getId_persona();
        } else {
            id_persona=Cliente.getID();
        }

        String emision = fecha_emis.getText().toString().trim();
        String modalidad = spinner.getSelectedItem().toString();
        String periodo = informpago.getText().toString();
        String cantidad = tilcantidad.getEditText().getText().toString();
        String monto = tilpagar.getEditText().getText().toString();
        String tarifa = spinner2.getSelectedItem().toString();
        String concepto = tilconcepto.getEditText().getText().toString();


        boolean a = tools_varios.escampovalido(cantidad, 2, tilcantidad);
        boolean c = tools_varios.escampovalido(monto, 2, tilpagar);

        if (a && c) {

            switch (cond){
                case 1:
                    if (tools_varios.GuardarPago(this, id_persona, tarifa, emision, modalidad, Integer.parseInt(cantidad), Integer.parseInt(monto), periodo, concepto, estado)) {
                        Cliente=null;
                        super.onBackPressed();
                    }
                    break;
                case 2:
                    if (tools_varios.moificar_Pago(this,pago_modi.getN_comprob(),id_persona, tarifa, emision, modalidad, Integer.parseInt(cantidad), Integer.parseInt(monto), periodo, concepto, estado)) {
                        pago_modi=null;
                        Detalles.createInstance(this,tools_varios.persona(this,id_persona));
                    }
                    break;
            }


        }


    }


    public static void modificarInstancia(Activity activity, Pago registro) {
        Intent intent = lanzar_modificar(activity, registro);
        activity.startActivity(intent);

    }

    public static Intent lanzar_modificar(Context context, Pago registro) {
        Intent intent = new Intent(context, RegistroPago.class);
        intent.putExtra(OPCION,2);
        intent.putExtra(MODIFICAR_PAGO, registro);
    /*    pago_modi = registro;
        Cliente=null;*/
        return intent;
    }

    public static void createInstance(Activity activity,Person persona) {
        Intent intent = getLaunchIntent(activity, persona);
        activity.startActivity(intent);
    }

    public static Intent getLaunchIntent(Context context, Person persona) {
        Intent intent = new Intent(context, RegistroPago.class);
        intent.putExtra(OPCION,1);
        intent.putExtra(PERSONA,persona);
//        pago_modi=null;
        return intent;
    }



}
