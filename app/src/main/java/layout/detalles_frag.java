package layout;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import mancash_outs.cash_outs.p43z.mycash_outs.Person;
import mancash_outs.cash_outs.p43z.mycash_outs.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link detalles_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link detalles_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detalles_frag extends Fragment {

    private TextView ID;
    private TextView DATOS;
    private TextView TELEFONO;
    private TextView DIRECCION;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLIENTE = "clave";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Person cliente;


    private OnFragmentInteractionListener mListener;

    public detalles_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param prueba Parameter 1.

     * @return A new instance of fragment detalles_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static detalles_frag newInstance(Person cliente) {
        detalles_frag fragment = new detalles_frag();
        Bundle args = new Bundle();
        args.putSerializable(CLIENTE,cliente);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cliente = (Person) getArguments().getSerializable(CLIENTE);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_detalles_frag, container, false);
        ID=(TextView)v.findViewById(R.id.datos);
        DATOS=(TextView)v.findViewById(R.id.datos1);
        TELEFONO=(TextView)v.findViewById(R.id.telefono);
        DIRECCION=(TextView)v.findViewById(R.id.direccion);

        ID.setText("ID: "+cliente.getID());
        DATOS.setText("Nombre Completo: "+cliente.getNombre()+" "+cliente.getApellido1()+" "+cliente.getApellido2());
        TELEFONO.setText("Telefono: "+cliente.getTelefono());
        DIRECCION.setText("Direcion: "+cliente.getDireccion());






        return v;



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
