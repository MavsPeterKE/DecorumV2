package com.rapid.furnitureaugmentreal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapid.furnitureaugmentreal.R;
import com.rapid.furnitureaugmentreal.activities.FurnituredetailsActivity;
import com.rapid.furnitureaugmentreal.activities.MainActivity;
import com.rapid.furnitureaugmentreal.domain.Furniture;

import java.util.List;

public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.FurnitureHolder> {

    Context context;
    List<Furniture>furnitures;

    FirebaseStorage storage;

    StorageReference storageRef;


    public FurnitureAdapter(Context context, List<Furniture> furnitures) {
        this.context = context;
        this.furnitures = furnitures;
    }

    public class FurnitureHolder extends RecyclerView.ViewHolder{

        ImageView imgplace;
        TextView txtPlacename,txtAddress,txtPlace;
        LinearLayout layoutmain;

        public FurnitureHolder(@NonNull View itemView) {
            super(itemView);
            txtAddress=itemView.findViewById(R.id.txtAddress);
            imgplace=itemView.findViewById(R.id.imgplace);
            txtPlacename=itemView.findViewById(R.id.txtPlacename);
            txtPlace=itemView.findViewById(R.id.txtPlace);
            layoutmain=itemView.findViewById(R.id.layoutmain);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (Furniture f:furnitures
                         ) {
                        f.setSelected(0);
                    }

                    furnitures.get(getAdapterPosition()).setSelected(1);



                    ((MainActivity)context).getFurniture_details(furnitures.get(getAdapterPosition()),getAdapterPosition());

                    notifyDataSetChanged();
                }
            });
        }
    }


    @NonNull
    @Override
    public FurnitureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_furniture,parent,false);




        return new FurnitureHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FurnitureHolder holder, int position) {


        holder.txtAddress.setText("price : "+furnitures.get(position).getPrice()+" Rs");



           // holder.txtPlace.setText("Dinner ");

        if(furnitures.get(position).getSelected()==1)
        {
            holder.layoutmain.setBackgroundColor(Color.parseColor("#909396"));
        }
        else {
            holder.layoutmain.setBackgroundColor(Color.TRANSPARENT);
        }




        holder.txtPlacename.setText(furnitures.get(position).getName());

        storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");

        storageRef = storage.getReference();

        if(!furnitures.get(position).getImage().equalsIgnoreCase(""))
        {

            String image="";

            if(furnitures.get(position).getImage().contains(","))
            {
                String arr[]=furnitures.get(position).getImage().split(",");
                image=arr[0];
            }
            else {
                image=furnitures.get(position).getImage();
            }



            storageRef.child(image).getDownloadUrl().addOnSuccessListener((Activity) context, new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    String url = uri.toString();

                    Glide.with(context).load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder)).into(holder.imgplace);

                    // updateUserImage();

                    //Toast.makeText(UserProfileActivity.this,url,Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return furnitures.size();
    }
}
