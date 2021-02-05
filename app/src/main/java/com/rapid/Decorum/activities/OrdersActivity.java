package com.rapid.Decorum.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapid.Decorum.R;
import com.rapid.Decorum.adapter.OrdersAdapter;
import com.rapid.Decorum.appConstants.AppConstants;
import com.rapid.Decorum.domain.Furniture;
import com.rapid.Decorum.domain.Orders;
import com.rapid.Decorum.preferencehelper.PreferenceHelper;
import com.rapid.Decorum.progress.ProgressFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {

    ImageView imgbtnBack;
    RecyclerView recyclerCart;

    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    StorageReference storageRef;

    ProgressFragment progressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getSupportActionBar().hide();
        recyclerCart=findViewById(R.id.recyclerCart);
        imgbtnBack=findViewById(R.id.imgbtnBack);

        storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");
        firebaseFirestore=FirebaseFirestore.getInstance();

        storageRef = storage.getReference();

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        getOrders();
    }


    public void getOrders()
    {

        progressFragment=new ProgressFragment();
        progressFragment.show(getSupportFragmentManager(),"dk");
        firebaseFirestore.collection("Orders").get().addOnSuccessListener(OrdersActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                progressFragment.dismiss();

                List<Furniture> foodmenus=new ArrayList<>();



                List<Orders>carts=new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                    Map<String, Object> logindata = document.getData();

                    if(logindata.get("uid").toString().equalsIgnoreCase(new PreferenceHelper(OrdersActivity.this).getData(AppConstants.Userid)))
                    {
                        Orders cart=new Orders();
                        cart.setFid(logindata.get("fid").toString());
                        cart.setOrdered_date(logindata.get("ordered_date").toString());
                        cart.setUid(logindata.get("uid").toString());
                        cart.setQty(logindata.get("qty").toString());
                        cart.setAddressid(logindata.get("Addressid").toString());
                        carts.add(cart);




                    }





                }

                if(carts.size()>0)
                {

                    OrdersAdapter cartAdapter=new OrdersAdapter(OrdersActivity.this,carts);
                    recyclerCart.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
                    recyclerCart.setAdapter(cartAdapter);

                }
                else {


                 //   btnCheckOut.setVisibility(View.GONE);

                    Toast.makeText(OrdersActivity.this,"Cart empty",Toast.LENGTH_SHORT).show();
                }









            }
        }).addOnFailureListener(OrdersActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressFragment.dismiss();

            }
        });

    }
}
