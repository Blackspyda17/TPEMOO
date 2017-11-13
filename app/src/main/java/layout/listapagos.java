package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mancash_outs.cash_outs.p43z.mycash_outs.BD_tool;
import mancash_outs.cash_outs.p43z.mycash_outs.Pago;
import mancash_outs.cash_outs.p43z.mycash_outs.PagoAdapter;
import mancash_outs.cash_outs.p43z.mycash_outs.R;


public class listapagos extends Fragment {


    public void onResume(){
        super.onResume();
        cargar();
    }

    public View rootView;
public RecyclerView rv;
   public PagoAdapter adapter;
    LinearLayoutManager llm;
    private BD_tool DB_tool=new BD_tool();

    private static final String PAGOS = "ListaPagos";
    //private static final String ARG_PARAM2 = "param2";
    private String pagos;
    // TODO: Rename and change types of parameters
    public List<Pago> LPagos;

    public listapagos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_listapagos, container, false);

         rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);


        adapter = new PagoAdapter(this.getContext(),LPagos);
            rv.setAdapter(adapter);


       llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    public static listapagos newInstance(String LPagos) {
        listapagos fragment = new listapagos();
        Bundle args = new Bundle();
        args.putSerializable(PAGOS, LPagos);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pagos = getArguments().getString(PAGOS);

        }


    }

    public void cargar(){
        LPagos=DB_tool.Lista_pagosC(this.getContext(),pagos);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        // Usar un administrador para LinearLayout

        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        // Crear un nuevo adaptador
        adapter = new PagoAdapter(this.getContext(),LPagos);
        rv.setAdapter(adapter);

    }


}
