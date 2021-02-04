package com.rapid.furnitureaugmentreal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapid.furnitureaugmentreal.R;
import com.rapid.furnitureaugmentreal.adapter.UserAddressAdapter;
import com.rapid.furnitureaugmentreal.appConstants.AppConstants;
import com.rapid.furnitureaugmentreal.domain.UserAddress;
import com.rapid.furnitureaugmentreal.preferencehelper.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    FloatingActionButton fabAddAddress;
    ImageView imgbtnBack;
    TextView txtNetTotal,txtNodata;
    Button btnCheckOut;
    RecyclerView recyclerCart;
    List<UserAddress> userAddresses;
    double totalpriceCart=0.0;



    String userAddressSelected="";

    String selectedTime="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getSupportActionBar().hide();

        totalpriceCart=getIntent().getDoubleExtra("PayableAmount",0);

        userAddresses=new ArrayList<>();
        fabAddAddress=findViewById(R.id.fabAddAddress);
        recyclerCart=findViewById(R.id.recyclerCart);
        imgbtnBack=findViewById(R.id.imgbtnBack);
        txtNodata=findViewById(R.id.txtNodata);
        txtNetTotal=findViewById(R.id.txtNetTotal);
        btnCheckOut=findViewById(R.id.btnCheckOut);

        txtNetTotal.setText("Payable amount : "+" "+String.valueOf(totalpriceCart)+" Rs");

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this, PaymentSummaryActivity.class).putExtra("Payableamount", totalpriceCart).putExtra("Addressid", userAddressSelected));

            }
        });


        fabAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
            }
        });

        getAllAddress();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getAllAddress();
    }

    public void getAllAddress()
    {

        // showProgressDialog();

        userAddresses=new ArrayList<>();

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Address")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // dismissDialog();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Map<String, Object> logindata = document.getData();

                                if (logindata != null) {

                                    if (logindata.containsKey("userid")) {

                                        if (logindata.get("userid").toString().equals(new PreferenceHelper(AddressActivity.this).getData(AppConstants.Userid))) {


                                            UserAddress userAddress = new UserAddress();
                                            userAddress.setId(document.getId());
                                            userAddress.setFlatno(logindata.get("Flatno").toString());
                                            userAddress.setName(logindata.get("Name").toString());
                                            userAddress.setHousename(logindata.get("Housename").toString());
                                            userAddress.setLandMark(logindata.get("LandMark").toString());
                                            userAddress.setPincode(logindata.get("Pincode").toString());
                                            userAddress.setDistrict(logindata.get("District").toString());
                                            userAddress.setState(logindata.get("State").toString());
                                            userAddress.setCountry(logindata.get("Country").toString());
                                            userAddress.setMobilenumber(logindata.get("Mobilenumber").toString());

                                            userAddresses.add(userAddress);


                                        }
                                    }
                                    else {

//                                        recyclerCart.setVisibility(View.GONE);
//                                        txtNodata.setVisibility(View.VISIBLE);
//
//                                        btnCheckOut.setVisibility(View.INVISIBLE);
//                                        txtNetTotal.setVisibility(View.INVISIBLE);
                                    }


                                }
                                else {

                                    recyclerCart.setVisibility(View.GONE);
                                    txtNodata.setVisibility(View.VISIBLE);

                                    btnCheckOut.setVisibility(View.INVISIBLE);
                                    txtNetTotal.setVisibility(View.INVISIBLE);
                                }



                            }

                            if(userAddresses.size()>0)
                            {
                                userAddresses.get(0).setSelected(1);

                                userAddressSelected= userAddresses.get(0).getId();

                                UserAddressAdapter userAddressAdapter=new UserAddressAdapter(AddressActivity.this,userAddresses);
                                recyclerCart.setLayoutManager(new LinearLayoutManager(AddressActivity.this));
                                recyclerCart.setAdapter(userAddressAdapter);



                            }
                            else {

                                recyclerCart.setVisibility(View.GONE);
                                txtNodata.setVisibility(View.VISIBLE);

                                btnCheckOut.setVisibility(View.INVISIBLE);
                                txtNetTotal.setVisibility(View.INVISIBLE);
                            }








                        } else {
                            //  Log.w(TAG, "Error getting documents.", task.getException());

                            recyclerCart.setVisibility(View.GONE);
                            txtNodata.setVisibility(View.VISIBLE);

                            btnCheckOut.setVisibility(View.INVISIBLE);
                            txtNetTotal.setVisibility(View.INVISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //dismissDialog();
                recyclerCart.setVisibility(View.GONE);
                txtNodata.setVisibility(View.VISIBLE);

                btnCheckOut.setVisibility(View.INVISIBLE);
                txtNetTotal.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setSelectedAddress(String addressId)
    {
        this.userAddressSelected=addressId;
    }
}
