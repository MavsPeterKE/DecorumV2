package com.rapid.Decorum.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapid.Decorum.R;
import com.rapid.Decorum.appConstants.AppConstants;
import com.rapid.Decorum.domain.Furniture;
import com.rapid.Decorum.preferencehelper.PreferenceHelper;
import com.rapid.Decorum.progress.ProgressFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FurnituredetailsActivity extends AppCompatActivity {

    Furniture furniture;

    ImageView imgFurniture,imgback;

    FirebaseStorage storage;

    StorageReference storageRef;
    TextView txtDetails,txtCart;
    ImageView imgcart;
    Button btn_addtocart;
    int   i=0;
    boolean a=false;
    ProgressFragment progressFragment;
    FirebaseFirestore firebaseFirestore;

    String image="";
    String id="";
    String price="";
    String name="";

    String description="";

//     foodmenu.setId(document.getId());
//                    foodmenu.setImage(logindata.get("image").toString());
//                    foodmenu.setDescription(logindata.get("description").toString());
//                    foodmenu.setName(logindata.get("name").toString());
//                    foodmenu.setPrice(logindata.get("price").toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furnituredetails);
        getSupportActionBar().hide();
       // furniture=(Furniture) getIntent().getSerializableExtra("furniture");


       id= getIntent().getStringExtra("id");
       image= getIntent().getStringExtra("image");
        description=  getIntent().getStringExtra("description");
       name= getIntent().getStringExtra("name");
        price=getIntent().getStringExtra("price");
        furniture=new Furniture();

        furniture.setId(id);
        furniture.setImage(image);
        furniture.setDescription(description);
        furniture.setName(name);
        furniture.setPrice(price);



        imgFurniture=findViewById(R.id.imgFurniture);
        imgcart=findViewById(R.id.imgcart);
        txtDetails=findViewById(R.id.txtDetails);
        btn_addtocart=findViewById(R.id.btn_addtocart);
        imgback=findViewById(R.id.imgback);
        txtCart=findViewById(R.id.txtCart);

        storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");
        firebaseFirestore=FirebaseFirestore.getInstance();

        storageRef = storage.getReference();
        getFoodCount();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        imgcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(FurnituredetailsActivity.this,CartActivity.class));

            }
        });

        txtCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(FurnituredetailsActivity.this,CartActivity.class));


            }
        });

        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CheckAlreadyAdded();


            }
        });

        if(!furniture.getImage().equalsIgnoreCase(""))
        {

            String image="";

            if(furniture.getImage().contains(","))
            {
                String arr[]=furniture.getImage().split(",");
                image=arr[0];
            }
            else {
                image=furniture.getImage();
            }



            storageRef.child(image).getDownloadUrl().addOnSuccessListener(FurnituredetailsActivity.this, new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    String url = uri.toString();

                    Glide.with(FurnituredetailsActivity.this).load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder)).into(imgFurniture);

                    // updateUserImage();

                    //Toast.makeText(UserProfileActivity.this,url,Toast.LENGTH_SHORT).show();
                }
            });

        }

    txtDetails.setText("Name : "+furniture.getName()+"\nPrice : "+furniture.getPrice()+"\nDescription: "+furniture.getDescription());




    }




public void addtocart()
{
    ProgressFragment progressFragment=new ProgressFragment();
    progressFragment.show(getSupportFragmentManager(),"cvmn");
    Map<String,String> stringObjectMap=new HashMap<>();
    stringObjectMap.put("Userid",new PreferenceHelper(FurnituredetailsActivity.this).getData(AppConstants.Userid));
    stringObjectMap.put("fid",furniture.getId());
    stringObjectMap.put("qty","1");


    firebaseFirestore.collection("Cart").add(stringObjectMap).addOnSuccessListener(FurnituredetailsActivity.this, new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Toast.makeText(FurnituredetailsActivity.this,"Added to cart",Toast.LENGTH_SHORT).show();

            getFoodCount();

            progressFragment.dismiss();





        }
    }).addOnFailureListener(FurnituredetailsActivity.this, new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

            Toast.makeText(FurnituredetailsActivity.this,"Failure",Toast.LENGTH_SHORT).show();

            progressFragment.dismiss();

        }
    });

}


    public boolean CheckAlreadyAdded()
    {

        a=false;

        progressFragment=new ProgressFragment();
        progressFragment.show(getSupportFragmentManager(),"cvmn");



        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        progressFragment.dismiss();

                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.e(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> logindata = document.getData();

                                if(logindata.containsKey("Userid")) {

                                    if (logindata.get("Userid").toString().equalsIgnoreCase(new PreferenceHelper(FurnituredetailsActivity.this).getData(AppConstants.Userid))) {

                                        if (logindata.containsKey("fid")) {


                                            if (logindata.get("fid").toString().equals(furniture.getId())) {

                                                a = true;

                                                break;
                                            }
                                        }
                                    }
                                }

                            }

                            if(!a)
                            {
                                addtocart();
                            }
                            else {

                                Toast.makeText(FurnituredetailsActivity.this, "Already added", Toast.LENGTH_SHORT).show();

                            }












                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());

                            Toast.makeText(FurnituredetailsActivity.this, " failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(FurnituredetailsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressFragment.dismiss();

                Toast.makeText(FurnituredetailsActivity.this, "failed", Toast.LENGTH_SHORT).show();

            }
        });


        return a;

    }

    public void getFoodCount()
    {

        i=0;
        firebaseFirestore.collection("Cart").get().addOnSuccessListener(FurnituredetailsActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<Furniture> foodmenus=new ArrayList<>();




                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                    Map<String, Object> logindata = document.getData();
                    if(logindata.get("Userid").toString().equalsIgnoreCase(new PreferenceHelper(FurnituredetailsActivity.this).getData(AppConstants.Userid)))
                    {
                        i++;
                    }





                }

                if(i>0)
                {
                    txtCart.setVisibility(View.VISIBLE);

                    txtCart.setText(i+"");

                }
                else {

                    txtCart.setVisibility(View.GONE);
                }









            }
        }).addOnFailureListener(FurnituredetailsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //progressFragment.dismiss();

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getFoodCount();
    }
}
