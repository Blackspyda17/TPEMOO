package mancash_outs.cash_outs.p43z.mycash_outs;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;



public class listaclientes extends Fragment {


    public void onResume(){
        super.onResume();
        cargar();
    }


    public View rootView;
    public RecyclerView rv;
    PersonAdapter adapter;
    LinearLayoutManager llm;
    private BD_tool DB_tool=new BD_tool();

    public List<Person> LPersonas;


    public listaclientes() {
        // Required empty public constructor
    }


    public static listaclientes newInstance() {
        listaclientes fragment = new listaclientes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listapagos, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);


        adapter = new PersonAdapter(this.getContext(),LPersonas);
        rv.setAdapter(adapter);


        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
        // Inflate the layout for this fragment

    }





    public void cargar(){
       LPersonas= DB_tool.Lista_client(this.getContext());

        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        // Usar un administrador para LinearLayout



        adapter = new PersonAdapter(this.getContext(),LPersonas);
        rv.setAdapter(adapter);


        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        // Crear un nuevo adaptador
        adapter = new PersonAdapter(this.getContext(),LPersonas);
        rv.setAdapter(adapter);

    }
}
