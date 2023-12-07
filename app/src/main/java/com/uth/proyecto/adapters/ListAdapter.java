package com.uth.proyecto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uth.proyecto.R;
import com.uth.proyecto.classes.ListElement;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<ListElement> itemList, Context context){
        this.mInflater= LayoutInflater.from(context);
        this.context= context;
        this.mData= itemList;
    }
    @Override
    public int getItemCount(){
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_list_element, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Imagen;
        TextView Nombre;
        TextView Apellido;
        TextView Pais;

        ViewHolder(View itemView){
            super(itemView);
            Imagen= itemView.findViewById(R.id.imageFoto);
            Nombre= itemView.findViewById(R.id.txtNombre);
            Apellido= itemView.findViewById(R.id.txtApellido);
            Pais= itemView.findViewById(R.id.txtPais);
        }

        void bindData(final ListElement item) {
            Nombre.setText(item.getFirstName());
            Apellido.setText(item.getLastName());
            Pais.setText(item.getCountry());
        }
    }
}
