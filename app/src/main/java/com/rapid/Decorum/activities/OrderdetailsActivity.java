package com.rapid.Decorum.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapid.Decorum.R;
import com.rapid.Decorum.domain.Orders;

import java.util.Map;

public class OrderdetailsActivity extends AppCompatActivity {

    Orders orders;

    FirebaseStorage storage;

    StorageReference storageRef;

    TextView txtFoodName,txtHotelname,txtQty,txtPrice;

    Button btnIncreaseQty,btnDecreaseQty,btnDelete;
    ImageView imgCart;

    TextView txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        getSupportActionBar().hide();
        orders=(Orders) getIntent().getSerializableExtra("Order");
        txtFoodName=findViewById(R.id.txtFoodName);
        txtAddress=findViewById(R.id.txtAddress);
        //txtFoodCategory=itemView.findViewById(R.id.txtFoodCategory);
        txtHotelname=findViewById(R.id.txtHotelname);
        txtQty=findViewById(R.id.txtQty);
        txtPrice=findViewById(R.id.txtPrice);
        imgCart=findViewById(R.id.imgCart);

        btnIncreaseQty=findViewById(R.id.btnIncreaseQty);
        btnDecreaseQty=findViewById(R.id.btnDecreaseQty);
        btnDelete=findViewById(R.id.btnDelete);



        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
     txtQty.setText("Qty : "+orders.getQty());



        firebaseFirestore.collection("Furniture").document(orders.getFid()).addSnapshotListener(OrderdetailsActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                try {

                    Map<String, Object> logindata = documentSnapshot.getData();


                    int qty = Integer.parseInt(orders.getQty());
                    double  price = Double.parseDouble(logindata.get("price").toString()) *qty;

                    // ((FoodCartActivity)context).setCartTotal(price);

                 txtPrice.setText(logindata.get("price").toString() + " Rs");




                 txtFoodName.setText(logindata.get("name").toString()+"\n\n Date : "+orders.getOrdered_date());
//             txtHotelname.setText(logindata.get("name").toString());

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

                    storageRef.child(image).getDownloadUrl().addOnSuccessListener(OrderdetailsActivity.this, new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            String url = uri.toString();

                            Glide.with(OrderdetailsActivity.this).load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder)).into(imgCart);

                            // updateUserImage();

                            //Toast.makeText(UserProfileActivity.this,url,Toast.LENGTH_SHORT).show();
                        }
                    });


                }catch (Exception e1)
                {

                }







            }
        });


        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Address").document(orders.getAddressid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {




                txtAddress.setText("Address \n\n Flat no :"+documentSnapshot.get("Flatno").toString()+"\n"+
                        "Housename :"+documentSnapshot.get("Housename").toString()+"\n"+
                        "Landmark :"+documentSnapshot.get("LandMark").toString()+"\n"+
                        "pincode :"+documentSnapshot.get("Pincode").toString()+"\n"+
                        "District :"+documentSnapshot.get("District")+"\n"+
                        "State :"+documentSnapshot.get("State")+"\n"+
                        "Country :"+documentSnapshot.get("Country")+"\n"+
                        "Mobile :"+documentSnapshot.get("Mobilenumber"));

            }
        });

    }
}
