package com.rapid.furnitureaugmentreal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapid.furnitureaugmentreal.R;
import com.rapid.furnitureaugmentreal.activities.OrderdetailsActivity;
import com.rapid.furnitureaugmentreal.activities.OrdersActivity;
import com.rapid.furnitureaugmentreal.domain.Orders;

import java.util.List;
import java.util.Map;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersHolder> {

    Context context;
    List<Orders>orders;

    FirebaseStorage storage;

    StorageReference storageRef;

    public OrdersAdapter(Context context, List<Orders> orders) {
        this.context = context;
        this.orders = orders;
    }

    public class OrdersHolder extends RecyclerView.ViewHolder{

        TextView txtFoodName,txtHotelname,txtQty,txtPrice;

        Button btnIncreaseQty,btnDecreaseQty,btnDelete;
        ImageView imgCart;

        public OrdersHolder(@NonNull View itemView) {
            super(itemView);

            txtFoodName=itemView.findViewById(R.id.txtFoodName);
            //txtFoodCategory=itemView.findViewById(R.id.txtFoodCategory);
            txtHotelname=itemView.findViewById(R.id.txtHotelname);
            txtQty=itemView.findViewById(R.id.txtQty);
            txtPrice=itemView.findViewById(R.id.txtPrice);
            imgCart=itemView.findViewById(R.id.imgCart);

            btnIncreaseQty=itemView.findViewById(R.id.btnIncreaseQty);
            btnDecreaseQty=itemView.findViewById(R.id.btnDecreaseQty);
            btnDelete=itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, OrderdetailsActivity.class);
                    intent.putExtra("Order",orders.get(getAdapterPosition()));
                    context.startActivity(intent);

                }
            });
        }
    }

    @NonNull
    @Override
    public OrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_ordersadapter,parent,false);
        return new OrdersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersHolder holder, int position) {


        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        holder.txtQty.setText("Qty : "+orders.get(position).getQty());



        firebaseFirestore.collection("Furniture").document(orders.get(position).getFid()).addSnapshotListener((AppCompatActivity) context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                try {

                    Map<String, Object> logindata = documentSnapshot.getData();


                    int qty = Integer.parseInt(orders.get(position).getQty());
                  double  price = Double.parseDouble(logindata.get("price").toString()) *qty;

                    // ((FoodCartActivity)context).setCartTotal(price);

                    holder.txtPrice.setText(logindata.get("price").toString() + " Rs");




                    holder.txtFoodName.setText(logindata.get("name").toString()+"\n\n Date : "+orders.get(position).getOrdered_date());
//                holder.txtHotelname.setText(logindata.get("name").toString());

                    String image="";

                    if(logindata.get("image").toString().contains(","))
                    {
                        String arr[]=logindata.get("image").toString().split(",");
                        image=arr[0];
                    }
                    else {
                        image=logindata.get("image").toString();
                    }




                    storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");

                    storageRef = storage.getReference();

                    storageRef.child(image).getDownloadUrl().addOnSuccessListener((Activity) context, new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            String url = uri.toString();

                            Glide.with(context).load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder)).into(holder.imgCart);

                            // updateUserImage();

                            //Toast.makeText(UserProfileActivity.this,url,Toast.LENGTH_SHORT).show();
                        }
                    });


                }catch (Exception e1)
                {

                }







            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
