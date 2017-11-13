package mancash_outs.cash_outs.p43z.mycash_outs;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by p43z on 15/06/16.
 */
public class PagoAdapter extends RecyclerView.Adapter <PagoAdapter.PagoViewHolder> implements ItemClickListener {
    SimpleDateFormat fechaEx = new SimpleDateFormat("dd/MM/yyyy");

    private List<Pago> items;
    private Detalles activity_detalles;
    private final Context context;



    public static class PagoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView num_factura;
        public TextView fecha;
        public TextView monto;
        public TextView condicion;
        public CheckBox check;
        private Detalles activity_detalles;
        public ItemClickListener listener;
        CardView cardView;

        public PagoViewHolder(View v, ItemClickListener listener,Detalles activity_detalles) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.icono_pago);
            num_factura = (TextView) v.findViewById(R.id.num_factura);
            fecha = (TextView) v.findViewById(R.id.id_perso);
            monto=(TextView) v.findViewById(R.id.monto_pago);
            condicion=(TextView) v.findViewById(R.id.txt_condicion);
            check=(CheckBox) v.findViewById(R.id.checkbox_pago);
            this.activity_detalles=activity_detalles;
            this.listener=listener;
            cardView=(CardView) itemView.findViewById(R.id.cardview_pago);
            cardView.setOnLongClickListener(activity_detalles);
            check.setOnClickListener(this);
            v.setOnClickListener(this);

        }


        public void onClick(View v) {
            if(activity_detalles.is_in_action_mode){
                activity_detalles.preparar_select(v,getAdapterPosition());
            }else{
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public PagoAdapter(Context context,List<Pago> items) {

        this.context = context;
        this.items = items;
        activity_detalles = (Detalles) context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public PagoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_pago, viewGroup, false);
        return new PagoViewHolder(v,this,activity_detalles);


    }



    public void onBindViewHolder(PagoViewHolder viewHolder, int i) {
        Pago currentItem = items.get(i);
        viewHolder.num_factura.setText("Doc #"+currentItem.getN_comprob());
        Glide.with(viewHolder.imagen.getContext())
                .load(R.mipmap.ic_doc)
                .centerCrop()
                .into(viewHolder.imagen);
        viewHolder.fecha.setText(currentItem.getFecha());
        viewHolder.monto.setText("₵"+currentItem.getMonto());
        if(currentItem.isEstado_pago()){
            viewHolder.condicion.setTextColor(Color.parseColor(("#4CAF50")));
            viewHolder.condicion.setText("CANCELADO");
        }else {
            viewHolder.condicion.setTextColor(Color.parseColor(("#F44336")));
            viewHolder.condicion.setText("PENDIENTE");
        }
        if(activity_detalles.is_in_action_mode ){
            viewHolder.check.setVisibility(View.VISIBLE);
            viewHolder.check.setChecked(false);
            activity_detalles.viewPager.getAdapter().notifyDataSetChanged();
    }else {
        viewHolder.check.setVisibility(View.GONE);
    }
    }

    private String fech(Date fecha){
        try {
            return fecha.toString();
        }catch (Exception e){

        }
        return "no hay";
    }


    public void updateAdapter(ArrayList<Pago> List){
        for (Pago pago:List){
            items.remove(pago);
            BD_tool DB_tool=new BD_tool();
            DB_tool.eliminar(this.context,2,Integer.toString(pago.getN_comprob()));
        }
        activity_detalles.viewPager.getAdapter().notifyDataSetChanged();
    }


    /**
     * Sobrescritura del método de la interfaz {@link ItemClickListener}
     *
     * @param view     item actual
     * @param position posición del item actual
     */
    @Override
    public void onItemClick(View view, int position) {
        DetalleFactura.createInstance(
                (Activity) context, items.get(position));
    }


}



