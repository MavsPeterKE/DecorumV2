package com.rapid.furnitureaugmentreal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapid.furnitureaugmentreal.R;
import com.rapid.furnitureaugmentreal.appConstants.AppConstants;
import com.rapid.furnitureaugmentreal.domain.UserAddress;
import com.rapid.furnitureaugmentreal.preferencehelper.PreferenceHelper;
import com.rapid.furnitureaugmentreal.progress.ProgressFragment;

import java.util.HashMap;
import java.util.Map;

import io.opencensus.internal.Utils;

public class AddAddressActivity extends AppCompatActivity {


    ImageView imgbtnBack;

    EditText edtName, edtFlatno, edtHousename, edtLandMark, edtPincode, edtMobileNumber,edtDistrict,edtState,edtCountry;

    Button btnAdd;


    ProgressFragment progressFragment;

    UserAddress userAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        getSupportActionBar().hide();
        userAddress=(UserAddress) getIntent().getSerializableExtra("SelectedAddress");

        imgbtnBack = findViewById(R.id.imgbtnBack);
        // txtTime=findViewById(R.id.txtTime);
        edtName = findViewById(R.id.edtName);
        edtFlatno = findViewById(R.id.edtFlatno);
        edtHousename = findViewById(R.id.edtHousename);
        edtLandMark = findViewById(R.id.edtLandMark);
        edtPincode = findViewById(R.id.edtPincode);
        edtMobileNumber = findViewById(R.id.edtMobileNumber);

        edtDistrict= findViewById(R.id.edtDistrict);
        edtState= findViewById(R.id.edtState);
        edtCountry= findViewById(R.id.edtCountry);

        btnAdd = findViewById(R.id.btnAdd);

        setData();


        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtName.getText().toString().equals("")) {

                    if (!edtFlatno.getText().toString().equals("")) {

                        if (!edtHousename.getText().toString().equals("")) {

                            if (!edtLandMark.getText().toString().equals("")) {

                                if (!edtDistrict.getText().toString().equals("")) {

                                    if (!edtState.getText().toString().equals("")) {

                                        if (!edtCountry.getText().toString().equals("")) {


                                            if (!edtPincode.getText().toString().equals("")) {
                                                if (!edtMobileNumber.getText().toString().equals("")) {

                                                    //if(!time.equals("")){



                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("Name", edtName.getText().toString());
                                                    map.put("Flatno",edtFlatno.getText().toString());
                                                    map.put("Housename", edtHousename.getText().toString());
                                                    map.put("LandMark", edtLandMark.getText().toString());
                                                    map.put("District", edtDistrict.getText().toString());
                                                    map.put("State", edtState.getText().toString());
                                                    map.put("Country", edtCountry.getText().toString());
                                                    map.put("Pincode", edtPincode.getText().toString());
                                                    map.put("Mobilenumber", edtMobileNumber.getText().toString());
                                                    //  map.put("Deliverytime",time);
                                                    map.put("userid", new PreferenceHelper(AddAddressActivity.this).getData(AppConstants.Userid));



                                                    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();



                                                    if(userAddress!=null)
                                                    {

                                                        progressFragment = new ProgressFragment();
                                                        progressFragment.show(getSupportFragmentManager(), "dkdskksd");

                                                        mFirestore.collection("Address").document(userAddress.getId())
                                                                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                progressFragment.dismiss();

                                                                onBackPressed();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

progressFragment.dismiss();
                                                            }
                                                        });

                                                    }
                                                    else {

                                                        progressFragment = new ProgressFragment();
                                                        progressFragment.show(getSupportFragmentManager(), "dkdskksd");

                                                        mFirestore.collection("Address")
                                                                .add(map)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

progressFragment.dismiss();

                                                                        onBackPressed();


                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        //  Log.w(TAG, "Error adding document", e);

progressFragment.dismiss();
                                                                    }
                                                                });


                                                    }


//                                        } else {
//                                            Toast.makeText(AddAddressActivity.this, "Select delivery time", Toast.LENGTH_SHORT).show();
//                                        }


                                                } else {
                                                    Toast.makeText(AddAddressActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                                                }


                                            } else {
                                                Toast.makeText(AddAddressActivity.this, "Enter pincode", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(AddAddressActivity.this, "Enter country", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(AddAddressActivity.this, "Enter State", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(AddAddressActivity.this, "Enter District", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(AddAddressActivity.this, "Enter Land mark", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(AddAddressActivity.this, "Enter House name", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(AddAddressActivity.this, "Enter flat number or house number", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(AddAddressActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void setData()
    {
        if(userAddress!=null)
        {
            edtName.setText(userAddress.getName());
            edtFlatno.setText(userAddress.getFlatno());
            edtHousename.setText(userAddress.getHousename());
            edtLandMark.setText(userAddress.getLandMark());
            edtPincode.setText(userAddress.getPincode());
            edtMobileNumber.setText(userAddress.getMobilenumber());
            edtCountry.setText(userAddress.getCountry());
            edtState.setText(userAddress.getState());
            edtDistrict.setText(userAddress.getDistrict());



        }
    }
}
