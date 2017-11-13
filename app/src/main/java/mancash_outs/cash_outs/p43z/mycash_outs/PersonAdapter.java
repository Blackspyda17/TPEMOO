package mancash_outs.cash_outs.p43z.mycash_outs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by p43z on 18/05/16.
 */
public class PersonAdapter extends RecyclerView.Adapter <PersonAdapter.PersonViewHolder> implements ItemClickListener {


    private List<Person> items=new ArrayList<>();
    private final Context context;
    private Principal activity_principal;
    private  BD_tool bd_tool=new BD_tool();






    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView perso_id;
        public TextView pagos;
        public TextView ult_fecha;
        public TextView estado;
        public CheckBox check;
       public Principal activity_principal;
        CardView cardView;

        public ItemClickListener listener;


        public PersonViewHolder(View v, ItemClickListener listener,Principal activity_principal) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.person_photo);
            nombre = (TextView) v.findViewById(R.id.person_name);
            perso_id = (TextView) v.findViewById(R.id.id_perso);
            pagos= (TextView) v.findViewById(R.id.monto_pago);
            ult_fecha = (TextView) v.findViewById(R.id.fecha_ult_pag);
            estado = (TextView) v.findViewById(R.id.txt_condi_cli);
            check=(CheckBox) v.findViewById(R.id.checkbox);
            this.activity_principal=activity_principal;
            this.listener=listener;
            cardView=(CardView) itemView.findViewById(R.id.cardview_persona);
            cardView.setOnLongClickListener(activity_principal);
            check.setOnClickListener(this);
            v.setOnClickListener(this);
        }


        public void onClick(View v) {

if(activity_principal.is_in_action_mode && (activity_principal.tab_principal==1)){
activity_principal.preparar_select(v,getAdapterPosition());
}else{
    listener.onItemClick(v, getAdapterPosition());
}

        }
    }

    public PersonAdapter( Context context,List<Person> items) {

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


    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista, viewGroup, false);
        return new PersonViewHolder(v,this,activity_principal);


    }



    public void onBindViewHolder(PersonViewHolder viewHolder, int i) {
        Person currentItem = items.get(i);
        viewHolder.nombre.setText(currentItem.getNombre());
        Glide.with(viewHolder.imagen.getContext())
                .load(currentItem.getIcono())
                .centerCrop()
                .into(viewHolder.imagen);
        viewHolder.perso_id.setText("ID: "+currentItem.getID());

        if(bd_tool.cant_pagos(this.context,currentItem.getID())>0){
            int cancelados=bd_tool.cant_cancelados(this.context,currentItem.getID());
            int pendientes=bd_tool.cant_pendientes(this.context,currentItem.getID());
            viewHolder.pagos.setText(TextUtils.concat("Pagos: ",Cancelados_text(cancelados)," y " ,Pendientes_text(pendientes)));
            try {
                viewHolder.ult_fecha.setText("Fecha Del Ultimo Registro: "+bd_tool.ult_fecha(this.context,currentItem.getID()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(pendientes==0){
                viewHolder.estado.setText(TextUtils.concat("Estado: ",text_estado(1)));
            }else {
                viewHolder.estado.setText(TextUtils.concat("Estado: ",text_estado(2)));
            }

        }else{
            viewHolder.pagos.setText("Pagos: Sin Registros");
            viewHolder.ult_fecha.setText("Fecha Del Ultimo Registro: Aun No Ingresados");
            viewHolder.estado.setText(TextUtils.concat("Estado: ",text_estado(1)));
        }




        if(activity_principal.is_in_action_mode && (activity_principal.tab_principal==1)){
            viewHolder.check.setVisibility(View.VISIBLE);
            viewHolder.check.setChecked(false);
try{
    activity_principal.Clientes.adapter.notifyDataSetChanged();
}catch (Exception e){

}

            //activity_principal.viewPager.getAdapter().notifyDataSetChanged();
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
        Detalles.createInstance(
                (Activity) context, items.get(position));


    }

    public void updateAdapter(ArrayList<Person> List){
        for (Person persona:List){
            BD_tool DB_tool=new BD_tool();
            DB_tool.eliminar(this.context,1,persona.getID());
            items.remove(persona);
        }
        activity_principal.Clientes.adapter.notifyDataSetChanged();
    }

    public SpannableString Cancelados_text(int cant){
        SpannableString text =new SpannableString(cant+" Cancelados");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), 2, 12,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    public SpannableString Pendientes_text(int cant){
        SpannableString text = new SpannableString(cant+" Pendientes");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#F44336")), 2, 12,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    public SpannableString text_estado(int opc){
        SpannableString text=new SpannableString("");
        switch (opc){
            case 1:
                text= new SpannableString("Al Dia");
                text.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), 0,6,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 2:
                text= new SpannableString("Moroso");
                text.setSpan(new ForegroundColorSpan(Color.parseColor("#F44336")), 0,6,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;

        }
        return text;

    }

}


interface ItemClickListener {

    void onItemClick(View view, int position);
}