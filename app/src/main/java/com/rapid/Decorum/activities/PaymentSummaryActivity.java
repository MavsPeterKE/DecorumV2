package com.rapid.Decorum.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapid.Decorum.R;
import com.rapid.Decorum.appConstants.AppConstants;
import com.rapid.Decorum.domain.Cart;
import com.rapid.Decorum.preferencehelper.PreferenceHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentSummaryActivity extends AppCompatActivity {

    double totalAmount = 0.0;
    TextView txtPayaableamount;
    ImageView imgbtnBack;

    Button btnPay;
    EditText edtCardNumber, edtCVV, edtValidity;
    ArrayList<String> listOfPattern = new ArrayList<String>();



    AppCompatImageView imgCard;

    String Addressid="",time="";
Thread thread=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_summary);
        getSupportActionBar().hide();
        totalAmount = getIntent().getDoubleExtra("Payableamount", 0);
        time=getIntent().getStringExtra("time");
        Addressid=getIntent().getStringExtra("Addressid");
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtCVV = findViewById(R.id.edtCVV);
        edtValidity = findViewById(R.id.edtValidity);
        imgCard=findViewById(R.id.imgCard);

        txtPayaableamount = findViewById(R.id.txtPayaableamount);
        imgbtnBack = findViewById(R.id.imgbtnBack);
        btnPay = findViewById(R.id.btnPay);

        txtPayaableamount.setText( String.valueOf(totalAmount)+" Rs");

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtCardNumber.getText().toString().equals("")) {

                    if (!edtCVV.getText().toString().equals("")) {

                        if (edtCVV.getText().toString().length() == 3) {
                            if (!edtValidity.getText().toString().equals("")) {
                                gotoPayment();

                               thread= new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try{

                                           thread.sleep(8000);

                                                                                    Intent intent=new Intent(PaymentSummaryActivity.this,MainActivity.class);

                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        }catch (Exception e)
                                        {

                                        }


//                                        AlertDialog.Builder builder=new AlertDialog.Builder(PaymentSummaryActivity.this);
//                                        builder.setTitle("Tour management");
//                                        builder.setMessage("Payment completed successfully");
//                                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                dialog.dismiss();
//
//                                                Intent intent=new Intent(PaymentSummaryActivity.this, ShoppingActivity.class);
//
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                startActivity(intent);
//
//                                            }
//                                        });
//
//
//
//                                        builder.show();





                                    }
                                });

                               thread.start();








                            } else {

                                Toast.makeText(PaymentSummaryActivity.this, "Enter card validity", Toast.LENGTH_SHORT).show();
                            }


                        } else {

                            Toast.makeText(PaymentSummaryActivity.this, "Enter cvv properly", Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        Toast.makeText(PaymentSummaryActivity.this, "Enter cvv", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(PaymentSummaryActivity.this, "Enter card number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
    }

    private void gotoPayment() {

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Cart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

//                dismissDialog();

                if (task.isSuccessful()) {

                    int count = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Log.e(TAG, document.getId() + " => " + document.getData());

                        Map<String, Object> logindata = document.getData();

                        if (logindata != null) {


                            if(logindata.size()>0) {

                               // cartSize=logindata.size();

                                if (logindata.containsKey("Userid")) {

                                    if (logindata.get("Userid").toString().equals(new PreferenceHelper(PaymentSummaryActivity.this).getData(AppConstants.Userid))) {

                                        Cart cart = new Cart();
                                        cart.setId(document.getId());
                                        cart.setFid(logindata.get("fid").toString());
                                        cart.setUid(logindata.get("Userid").toString());
                                        cart.setQty(logindata.get("qty").toString());

                                        addToMyOrders(cart);

//                                        selectedCart.add(cart);
//                                        hotelid.add(logindata.get("hotelid").toString());


                                    }

                                }
                            }

                        }

                    }






                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // dismissDialog();

                Toast.makeText(PaymentSummaryActivity.this,"Payment failed",Toast.LENGTH_SHORT).show();



            }
        });
    }



    public void addToMyOrders(final Cart cart)
    {


        //  showProgressDialog();

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("fid", cart.getFid());
           // map.put("id", cart.getId());
            map.put("qty", cart.getQty());
            map.put("uid", cart.getUid());

            Calendar calendar = Calendar.getInstance();
            String date = "";
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            date = formatter.format(new Date());


            map.put("ordered_date", date);
            map.put("Addressid", Addressid);
            map.put("Paymentstatus", "1");


            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();


            mFirestore.collection("Orders")
                    .add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // dismissDialog();

                            Toast.makeText(PaymentSummaryActivity.this,"Payment completed successfully",Toast.LENGTH_SHORT).show();

                            deleteFromCart(cart);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  dismissDialog();
                            Toast.makeText(PaymentSummaryActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();

                        }
                    });

        }catch (Exception e)
        {

        }
    }


    public void deleteFromCart(Cart cart)
    {

        // showProgressDialog();

        FirebaseFirestore  mFirestore = FirebaseFirestore.getInstance();




        mFirestore.collection("Cart").document(cart.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // dismissDialog();




            }
        });

    }
}
