package com.rapid.furnitureaugmentreal.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapid.furnitureaugmentreal.R;
import com.rapid.furnitureaugmentreal.adapter.CartAdapter;
import com.rapid.furnitureaugmentreal.appConstants.AppConstants;
import com.rapid.furnitureaugmentreal.domain.Cart;
import com.rapid.furnitureaugmentreal.domain.Furniture;
import com.rapid.furnitureaugmentreal.preferencehelper.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    ImageView imgback;
    RecyclerView recycler_view;
    Button btnCheckOut;

    FirebaseStorage storage;

    StorageReference storageRef;
    FirebaseFirestore firebaseFirestore;
    double totalprice=0;

    TextView txtNetTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();
        imgback=findViewById(R.id.imgback);
        recycler_view=findViewById(R.id.recycler_view);
        txtNetTotal=findViewById(R.id.txtNetTotal);
        btnCheckOut=findViewById(R.id.btnCheckOut);

        storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");
        firebaseFirestore=FirebaseFirestore.getInstance();

        storageRef = storage.getReference();


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CartActivity.this,AddressActivity.class).putExtra("PayableAmount",totalprice));
                
            }
        });

        getFoodCart();
        setCartTotal();
    }


    public void setCartTotal()
    {


        totalprice=0;
//        progressFragment = new ProgressFragment();
//        progressFragment.show(getSupportFragmentManager(), "dkdskksd");
        firebaseFirestore.collection("Cart").get().addOnSuccessListener(CartActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {




                // progressFragment.dismiss();


                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                    final Map<String, Object> logindata = document.getData();
                    if(logindata.get("Userid").toString().equalsIgnoreCase(new PreferenceHelper(CartActivity.this).getData(AppConstants.Userid)))
                    {

                        final Cart foodCart=new Cart();
                        foodCart.setUid(logindata.get("Userid").toString());
                        foodCart.setQty(logindata.get("qty").toString());
                        foodCart.setFid(logindata.get("fid").toString());
                        foodCart.setId(document.getId());
                        // foodmenus.add(foodCart);

                        firebaseFirestore.collection("Furniture").document(foodCart.getFid()).addSnapshotListener(CartActivity.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                try {

                                    Map<String, Object> logindata = documentSnapshot.getData();


                                    int qty = Integer.parseInt(foodCart.getQty());
                                    double price = Double.parseDouble(logindata.get("price").toString()) * qty;

                                    totalprice=totalprice+price;
                                    txtNetTotal.setText("Total Price:"+totalprice+" Rs");

//                holder.txtHotelname.setText(logindata.get("name").toString());


                                } catch (Exception e1) {

                                }


                            }
                        });


                    }





                }













            }
        }).addOnFailureListener(CartActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //  progressFragment.dismiss();

            }
        });



    }


    public void getFoodCart()
    {


        firebaseFirestore.collection("Cart").get().addOnSuccessListener(CartActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<Furniture> foodmenus=new ArrayList<>();



                List<Cart>carts=new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                    Map<String, Object> logindata = document.getData();

                    if(logindata.get("Userid").toString().equalsIgnoreCase(new PreferenceHelper(CartActivity.this).getData(AppConstants.Userid)))
                    {
                        Cart cart=new Cart();
                        cart.setFid(logindata.get("fid").toString());
                        cart.setId(document.getId());
                        cart.setUid(logindata.get("Userid").toString());
                        cart.setQty(logindata.get("qty").toString());
                        carts.add(cart);

                    }





                }

                if(carts.size()>0)
                {

                    CartAdapter cartAdapter=new CartAdapter(CartActivity.this,carts);
                    recycler_view.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    recycler_view.setAdapter(cartAdapter);

                }
                else {


                    btnCheckOut.setVisibility(View.GONE);

                    Toast.makeText(CartActivity.this,"Cart empty",Toast.LENGTH_SHORT).show();
                }









            }
        }).addOnFailureListener(CartActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //progressFragment.dismiss();

            }
        });
    }

}
