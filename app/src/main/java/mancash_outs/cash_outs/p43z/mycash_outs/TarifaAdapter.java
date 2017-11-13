package mancash_outs.cash_outs.p43z.mycash_outs;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p43z on 04/08/16.
 */
public class TarifaAdapter extends RecyclerView.Adapter <TarifaAdapter.TarifaViewHolder> implements ItemClickListener {

    private List<Tarifa> items=new ArrayList<>();

    private final Context context;
    private Principal activity_principal;





    public static class TarifaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView titulo;
        public TextView precio;
        public CheckBox check;
        public Principal activity_principal;
        CardView cardView;
        public ItemClickListener listener;


        public TarifaViewHolder(View v, ItemClickListener listener,Principal activity_principal) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.icono_pago);
            titulo= (TextView) v.findViewById(R.id.num_factura);
            precio = (TextView) v.findViewById(R.id.precio_tarifa);
            check=(CheckBox) v.findViewById(R.id.checkbox_tarifa);
            this.activity_principal=activity_principal;
            this.listener=listener;
            cardView=(CardView) itemView.findViewById(R.id.cardview_tarifa);
            cardView.setOnLongClickListener(activity_principal);
            check.setOnClickListener(this);
            v.setOnClickListener(this);
        }


        public void onClick(View v) {


            if(activity_principal.is_in_action_mode && (activity_principal.tab_principal==2)){
                activity_principal.preparar_select(v,getAdapterPosition());
            }else{
                listener.onItemClick(v, getAdapterPosition());
            }

        }
    }

    public TarifaAdapter(Context context,List<Tarifa> items) {

        this.context = context;
        this.items = items;
        activity_principal=(Principal) context;
    }

    @Override
    public int getItemCount() {

     try {
         return items.size();
     }catch (Exception e){
         return 0;
     }

    }


    public TarifaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_tarifa, viewGroup, false);
        return new TarifaViewHolder(v,this,activity_principal);


    }



    public void onBindViewHolder(TarifaViewHolder viewHolder, int i) {
        Tarifa currentItem = items.get(i);
        viewHolder.titulo.setText(""+currentItem.getTitulo());
        Glide.with(viewHolder.imagen.getContext())
                .load(R.mipmap.ic_ticket)
                .centerCrop()
                .into(viewHolder.imagen);
        viewHolder.precio.setText("₵"+currentItem.getMonto());

        if(activity_principal.is_in_action_mode && (activity_principal.tab_principal==2)){
            viewHolder.check.setVisibility(View.VISIBLE);
            viewHolder.check.setChecked(false);
            activity_principal.viewPager.getAdapter().notifyDataSetChanged();
        }else {
            viewHolder.check.setVisibility(View.GONE);
        }
    }




    /**
     * Sobrescritura del método de la interfaz {@link ItemClickListener}
     *
     * @param view     item actual
     * @param position posición del item actual
     */
    @Override
    public void onItemClick(View view, int position) {
        Detalle_tarifa.createInstance(
                (Activity) context, items.get(position));
    }

    public void updateAdapter(ArrayList<Tarifa> List){
        for (Tarifa tarifa:List){
            BD_tool DB_tool=new BD_tool();
            DB_tool.eliminar(this.context,3,tarifa.getTitulo());
            items.remove(tarifa);
        }
       try {
           activity_principal.Tarifas.adapter.notifyDataSetChanged();
       }catch (Exception e){

       }
        //activity_principal.viewPager.setAdapter(activity_principal.adapter);
    }

}
