package com.rapid.Decorum.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapid.Decorum.R;
import com.rapid.Decorum.activities.CartActivity;
import com.rapid.Decorum.domain.Cart;
import com.rapid.Decorum.progress.ProgressFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    Context context;
    List<Cart>carts;

    FirebaseStorage storage;

    StorageReference storageRef;

    ProgressFragment progressFragment;

    int cartqty=0;

    double price=0;

    public CartAdapter(Context context, List<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    public class CartHolder extends RecyclerView.ViewHolder{

        TextView txtFoodName,txtHotelname,txtQty,txtPrice;

        Button btnIncreaseQty,btnDecreaseQty,btnDelete;
        ImageView imgCart;

        public CartHolder(@NonNull View itemView) {
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
            btnDecreaseQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    progressFragment = new ProgressFragment();
                    progressFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "dkdskksd");

                    cartqty=Integer.parseInt(carts.get(getAdapterPosition()).getQty());

                    cartqty--;

                    FirebaseFirestore  mFirestore = FirebaseFirestore.getInstance();
                    Map<String, Object> map = new HashMap<>();
                    map.put("qty", String.valueOf(cartqty));
                    mFirestore.collection("Cart").document(carts.get(getAdapterPosition()).getId()).update(map).addOnCompleteListener((AppCompatActivity)context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            progressFragment.dismiss();

                            if (task.isSuccessful()) {

                                //  Toast.makeText(SearchActivity.this,"updated",Toast.LENGTH_SHORT).show();

                                carts.get(getAdapterPosition()).setQty(String.valueOf(cartqty));
                                notifyItemChanged(getAdapterPosition());

                                ((CartActivity)context).setCartTotal();


                            } else {
                                // Log.w(TAG, "Error getting documents.", task.getException());

                                Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

            btnIncreaseQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressFragment = new ProgressFragment();
                    progressFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "dkdskksd");

                    cartqty=Integer.parseInt(carts.get(getAdapterPosition()).getQty());

                    cartqty++;

                    FirebaseFirestore  mFirestore = FirebaseFirestore.getInstance();
                    Map<String, Object> map = new HashMap<>();
                    map.put("qty", String.valueOf(cartqty));
                    mFirestore.collection("Cart").document(carts.get(getAdapterPosition()).getId()).update(map).addOnCompleteListener((AppCompatActivity)context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            progressFragment.dismiss();

                            if (task.isSuccessful()) {

                                //  Toast.makeText(SearchActivity.this,"updated",Toast.LENGTH_SHORT).show();

                                carts.get(getAdapterPosition()).setQty(String.valueOf(cartqty));
                                notifyItemChanged(getAdapterPosition());

                                ((CartActivity)context).setCartTotal();


                            } else {
                                // Log.w(TAG, "Error getting documents.", task.getException());

                                Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });


            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressFragment = new ProgressFragment();
                    progressFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "dkdskksd");
                    FirebaseFirestore  mFirestore = FirebaseFirestore.getInstance();

                    mFirestore.collection("Cart").document(carts.get(getAdapterPosition()).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressFragment.dismiss();

                            int p=getAdapterPosition();
                            carts.remove(p);
                            notifyItemRemoved(p);
                            ((CartActivity)context).setCartTotal();
                        }
                    });

                }
            });
        }
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutcart,parent,false);



        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {

        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        holder.txtQty.setText(carts.get(position).getQty());



        firebaseFirestore.collection("Furniture").document(carts.get(position).getFid()).addSnapshotListener((AppCompatActivity) context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                try {

                    Map<String, Object> logindata = documentSnapshot.getData();


                    int qty = Integer.parseInt(carts.get(position).getQty());
                    //price = Double.parseDouble(logindata.get("price").toString()) *qty;
                    price = 650;

                    // ((FoodCartActivity)context).setCartTotal(price);

                    holder.txtPrice.setText(" Ksh."+ logindata.get("price").toString());




                    holder.txtFoodName.setText(logindata.get("name").toString());
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
        return carts.size();
    }
}
