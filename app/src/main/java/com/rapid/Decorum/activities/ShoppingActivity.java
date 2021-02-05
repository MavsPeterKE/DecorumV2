package com.rapid.Decorum.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapid.Decorum.R;
import com.rapid.Decorum.adapter.FurnitureAdapter;
import com.rapid.Decorum.appConstants.AppConstants;
import com.rapid.Decorum.domain.Furniture;
import com.rapid.Decorum.preferencehelper.PreferenceHelper;
import com.rapid.Decorum.progress.ProgressFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShoppingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recycler_view;
    EditText edtsearch;

    ImageView imgmenu;

    ProgressFragment progressFragment;
    FirebaseFirestore firebaseFirestore;

    List<Furniture> foodmenus;

    ImageView imgcart;
    TextView txtCart;
    TextView txtName,txtEmail;
    ImageView imgUserProfile;

    NavigationView nav_view;

    int i=0;

    FirebaseStorage storage;
    StorageReference storageRef;

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        getSupportActionBar().hide();
        foodmenus=new ArrayList<>();

        firebaseFirestore=FirebaseFirestore.getInstance();
        edtsearch=findViewById(R.id.edtsearch);
        recycler_view=findViewById(R.id.recycler_view);
        storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");
        storageRef = storage.getReference();

        imgmenu=findViewById(R.id.imgmenu);
        imgcart=findViewById(R.id.imgcart);
        txtCart=findViewById(R.id.txtCart);

        drawerLayout=findViewById(R.id.drawer_layout);

        nav_view=findViewById(R.id.nav_view);

        View view=nav_view.getHeaderView(0);

        txtEmail=view.findViewById(R.id.txtEmail);

        txtName=view.findViewById(R.id.txtName);
        imgUserProfile=view.findViewById(R.id.imgUserProfile);

        getProfile();
        getFoodCount();

        nav_view.setNavigationItemSelectedListener(this);

        imgcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoppingActivity.this,CartActivity.class));

            }
        });

        txtCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoppingActivity.this,CartActivity.class));


            }
        });

        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equalsIgnoreCase(""))
                {
                    List<Furniture> foodmenusearch=new ArrayList<>();


                    for (Furniture furniture:foodmenus)
                    {
                        if(furniture.getName().toUpperCase(Locale.getDefault()).contains(s.toString().toUpperCase(Locale.getDefault()))||furniture.getName().toLowerCase(Locale.getDefault()).contains(s.toString().toLowerCase(Locale.getDefault())))
                        {

                            foodmenusearch.add(furniture);

                        }
                    }

                    if(foodmenusearch.size()>0)
                    {
                        FurnitureAdapter hotelListAdapter = new FurnitureAdapter(ShoppingActivity.this, foodmenusearch);
                        recycler_view.setLayoutManager(new LinearLayoutManager(ShoppingActivity.this));
                        recycler_view.setAdapter(hotelListAdapter);
                    }

                }
                else {

                    if(foodmenus.size()>0)
                    {
                        FurnitureAdapter hotelListAdapter = new FurnitureAdapter(ShoppingActivity.this, foodmenus);
                        recycler_view.setLayoutManager(new LinearLayoutManager(ShoppingActivity.this));
                        recycler_view.setAdapter(hotelListAdapter);
                    }



                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setFurnitureMenu();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFoodCount();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);


        switch (menuItem.getItemId())
        {
            case R.id.nav_logout:

                AlertDialog.Builder builder=new AlertDialog.Builder(ShoppingActivity.this);
                builder.setTitle("Tour management");
                builder.setMessage("Do you want to logoutnow ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        new PreferenceHelper(ShoppingActivity.this).clearData();
                        Intent intent=new Intent(ShoppingActivity.this, LoginActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.show();

                break;



        }



        return true;
    }

    public void setFurnitureMenu()
    {

        progressFragment = new ProgressFragment();
        progressFragment.show(getSupportFragmentManager(), "dkdskksd");

        firebaseFirestore.collection("Furniture").get().addOnSuccessListener(ShoppingActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressFragment.dismiss();

                 foodmenus=new ArrayList<>();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                    Map<String, Object> logindata = document.getData();

                        Furniture foodmenu=new Furniture();

                        foodmenu.setId(document.getId());
                        foodmenu.setImage(logindata.get("image").toString());
                        foodmenu.setDescription(logindata.get("description").toString());
                        foodmenu.setName(logindata.get("name").toString());
                        foodmenu.setPrice(logindata.get("price").toString());
                        foodmenus.add(foodmenu);






                }


                if(foodmenus.size()>0)
                {
                    FurnitureAdapter hotelListAdapter = new FurnitureAdapter(ShoppingActivity.this, foodmenus);
                    recycler_view.setLayoutManager(new LinearLayoutManager(ShoppingActivity.this));
                    recycler_view.setAdapter(hotelListAdapter);
                }
                else {

                    Toast.makeText(ShoppingActivity.this,"No food items",Toast.LENGTH_SHORT).show();
                }






            }
        }).addOnFailureListener(ShoppingActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressFragment.dismiss();

            }
        });

    }

    public void getProfile()
    {



        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("User").document(new PreferenceHelper(ShoppingActivity.this).getData(AppConstants.Userid)).addSnapshotListener(ShoppingActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {



                Map<String, Object> logindata = documentSnapshot.getData();


                txtEmail.setText(logindata.get("Email").toString());
                txtName.setText(logindata.get("Name").toString());

                getDownloadUrl();






            }
        });

    }

    public void getDownloadUrl() {

        storageRef.child(new PreferenceHelper(ShoppingActivity.this).getData(AppConstants.Userid)).getDownloadUrl().addOnSuccessListener(ShoppingActivity.this, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                String url = uri.toString();


                Glide.with(ShoppingActivity.this).load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder).circleCrop()).into(imgUserProfile);

                //updateUserImage();

                //Toast.makeText(UserProfileActivity.this,url,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getFoodCount()
    {

        i=0;
        firebaseFirestore.collection("Cart").get().addOnSuccessListener(ShoppingActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<Furniture> foodmenus=new ArrayList<>();




                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                    Map<String, Object> logindata = document.getData();
                    if(logindata.get("Userid").toString().equalsIgnoreCase(new PreferenceHelper(ShoppingActivity.this).getData(AppConstants.Userid)))
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
        }).addOnFailureListener(ShoppingActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //progressFragment.dismiss();

            }
        });
    }
}
