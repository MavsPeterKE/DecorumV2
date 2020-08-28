package com.rapid.furnitureaugmentreal.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;

import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.FixedHeightViewSizer;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.otaliastudios.zoom.ZoomLayout;
import com.rapid.furnitureaugmentreal.CustomArFragment;
import com.rapid.furnitureaugmentreal.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.rapid.furnitureaugmentreal.TermsandConditionsFragment;
import com.rapid.furnitureaugmentreal.adapter.FurnitureAdapter;
import com.rapid.furnitureaugmentreal.appConstants.AppConstants;
import com.rapid.furnitureaugmentreal.domain.Furniture;
import com.rapid.furnitureaugmentreal.preferencehelper.PreferenceHelper;
import com.rapid.furnitureaugmentreal.progress.ProgressFragment;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SurfaceHolder.Callback {

    //ArFragment arFragment;

    ModelRenderable andyRenderable;

    ImageView imglogout,imgUser,imginfo;

    NavigationView nav_view;

    ImageView imgUserProfile;

    List<Furniture>foodmenus;
    RecyclerView recycler_view;

    TextView txtDetails;
    ViewRenderable viewRenderable=null;

    List<ViewRenderable>viewRenderables;
    ScaleGestureDetector mScaleGestureDetector=null;


    FirebaseStorage storage;

    FirebaseFirestore firebaseFirestore;
    StorageReference storageRef;
    ProgressFragment progressFragment;

    TextView txtName,txtEmail;
    int selected=0;
    ViewRenderable chairrenderable=null,tablerenderable=null,cupboardrenderable=null,tvstandrenderable=null,cotrenderable=null,benchrenderable=null,deskrenderable=null;
    ImageView imgcart,imgchair,imgtable,imgcupboard,imgtvstand,imgcot,imgbench,imgdesk,imgcup,imgmenu;

    DrawerLayout drawerLayout;
    Furniture selected_furniture=null;

    Button btn_viewdetails;

    int selectedposition=0;
    ImageView imageView;

    TextView txtCart;

    int i=0;

    SurfaceView surfaceview;
    Camera camera;

    SurfaceHolder surfaceHolder;

    ImageView commonimg;

    ZoomLayout zoomlayout;


    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        imgchair=findViewById(R.id.imgchair);
        viewRenderables=new ArrayList<>();


        imgcart=findViewById(R.id.imgcart);
        txtCart=findViewById(R.id.txtCart);
        zoomlayout=findViewById(R.id.zoomlayout);


        recycler_view=findViewById(R.id.recycler_view);
        btn_viewdetails=findViewById(R.id.btn_viewdetails);
        txtDetails=findViewById(R.id.txtDetails);
        imgtable=findViewById(R.id.imgTable);
        imgcupboard=findViewById(R.id.imgcupboard);
        imgtvstand=findViewById(R.id.imgtvstand);
        imgcot=findViewById(R.id.imgcot);
        imgbench=findViewById(R.id.imgbench);
        imgdesk=findViewById(R.id.imgdesk);
        imgcup=findViewById(R.id.imgcup);

        imginfo=findViewById(R.id.imginfo);
        imglogout=findViewById(R.id.imglogout);
        imgUser=findViewById(R.id.imgUser);
        imgmenu=findViewById(R.id.imgmenu);
        drawerLayout=findViewById(R.id.drawer_layout);

        commonimg=findViewById(R.id.commonimg);

        scaleGestureDetector = new ScaleGestureDetector(MainActivity.this, new ScaleListener());

        storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");
        storageRef = storage.getReference();

        nav_view=findViewById(R.id.nav_view);

        View view=nav_view.getHeaderView(0);

        txtEmail=view.findViewById(R.id.txtEmail);

        txtName=view.findViewById(R.id.txtName);
        imgUserProfile=view.findViewById(R.id.imgUserProfile);
        getProfile();
        showBottomDialog();
        getFoodCount();
            surfaceview=this.findViewById(R.id.surfaceview);

        surfaceHolder=surfaceview.getHolder();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {

            surfaceHolder.addCallback(MainActivity.this);
        }
        else

            {

                Toast.makeText(MainActivity.this,"Please restart and  allow camera permission",Toast.LENGTH_SHORT).show();

            //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},11);
        }



        nav_view.setNavigationItemSelectedListener(this);

        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //onBackPressed();

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this,UserProfileActivity.class));
            }
        });

        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this,UserProfileActivity.class));
            }
        });


        imgUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this,UserProfileActivity.class));
            }
        });



        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialogFragment(selected);


            }
        });


        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,UserProfileActivity.class));
            }
        });
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Tour management");
                builder.setMessage("Do you want to logout now ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        new PreferenceHelper(MainActivity.this).clearData();
                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);

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


            }
        });


       // setRenderables();
        //set3dRenderables();

        btn_viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                Intent intent=new Intent(MainActivity.this, FurnituredetailsActivity.class);
                //intent.putExtra("furniture",selected_furniture);
                intent.putExtra("id",selected_furniture.getId());
                intent.putExtra("image",selected_furniture.getImage());
                intent.putExtra("description",selected_furniture.getDescription());
                intent.putExtra("name",selected_furniture.getName());
                intent.putExtra("price",selected_furniture.getPrice());

                startActivity(intent);
            }
        });



        zoomlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                txtDetails.setVisibility(View.VISIBLE);
                btn_viewdetails.setVisibility(View.VISIBLE);


                int w=commonimg.getWidth();
                int h=commonimg.getHeight();

                double wi=w+motionEvent.getRawX();

                double he=h+motionEvent.getRawY();

                txtDetails.setText("Width : "+wi+" cm\nHeight : "+he);





                return false;
            }
        });




    }




    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            commonimg.setScaleX(mScaleFactor);
            commonimg.setScaleY(mScaleFactor);
            return true;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


                camera = Camera.open();
                camera.setDisplayOrientation(90);
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getProfile();
        getFoodCount();
    }



    public void showBottomDialog() {

        firebaseFirestore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");
        storageRef = storage.getReference();


        progressFragment = new ProgressFragment();
        progressFragment.show(getSupportFragmentManager(), "dkdskksd");

        firebaseFirestore.collection("Furniture").get().addOnSuccessListener(MainActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressFragment.dismiss();

                foodmenus = new ArrayList<>();


                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                    Map<String, Object> logindata = document.getData();

                    Furniture foodmenu = new Furniture();
                    foodmenu.setId(document.getId());
                    foodmenu.setImage(logindata.get("image").toString());
                    foodmenu.setDescription(logindata.get("description").toString());
                    foodmenu.setName(logindata.get("name").toString());
                    foodmenu.setPrice(logindata.get("price").toString());
                    foodmenus.add(foodmenu);



                }


                if (foodmenus.size() > 0) {
                    foodmenus.get(0).setSelected(1);
                    selectedposition=0;

                   // selected_furniture = foodmenus.get(selectedposition);
                    FurnitureAdapter hotelListAdapter = new FurnitureAdapter(MainActivity.this, foodmenus);
                    recycler_view.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                    recycler_view.setAdapter(hotelListAdapter);
                } else {

                    Toast.makeText(MainActivity.this, "No food items", Toast.LENGTH_SHORT).show();
                }


            }
        }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressFragment.dismiss();

            }
        });

    }




    public void getFurniture_details(Furniture furniture,int pos)
    {
//        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
//        transformableNode.setParent(anchorNode);
//        transformableNode.setRenderable(cotrenderable);
//        transformableNode.select();


            selectedposition = pos;

            selected_furniture = furniture;

        storageRef.child(furniture.getImage()).getDownloadUrl().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                String url = uri.toString();



                Glide.with(MainActivity.this).asBitmap().load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder)).into(commonimg);

                // updateUserImage();

                //Toast.makeText(UserProfileActivity.this,url,Toast.LENGTH_SHORT).show();
            }
        });






    }


    public void getFoodCount()
    {

        i=0;
        firebaseFirestore.collection("Cart").get().addOnSuccessListener(MainActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<Furniture> foodmenus=new ArrayList<>();




                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                    Map<String, Object> logindata = document.getData();
                    if(logindata.get("Userid").toString().equalsIgnoreCase(new PreferenceHelper(MainActivity.this).getData(AppConstants.Userid)))
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
        }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //progressFragment.dismiss();

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);


        switch (menuItem.getItemId())
        {



            case R.id.nav_shopping:
              //  startActivity(new Intent(MainActivity.this, ShoppingActivity.class));

                break;

            case R.id.nav_details:
                startActivity(new Intent(MainActivity.this, OrdersActivity.class));
               // showDialogFragment(selected);


                break;


            case R.id.nav_logout:

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Tour management");
                builder.setMessage("Do you want to logoutnow ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        new PreferenceHelper(MainActivity.this).clearData();
                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);

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

    public void getProfile()
    {



        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("User").document(new PreferenceHelper(MainActivity.this).getData(AppConstants.Userid)).addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
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

        storageRef.child(new PreferenceHelper(MainActivity.this).getData(AppConstants.Userid)).getDownloadUrl().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                String url = uri.toString();


                Glide.with(MainActivity.this).load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder).circleCrop()).into(imgUserProfile);

                //updateUserImage();

                //Toast.makeText(UserProfileActivity.this,url,Toast.LENGTH_SHORT).show();
            }
        });
    }











    @Override
    protected void onResume() {
        super.onResume();


//    }
    }


    public void showDialogFragment(int selected)
    {
        TermsandConditionsFragment termsandConditionsFragment=new TermsandConditionsFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("Key",selected);
        termsandConditionsFragment.setArguments(bundle);
        termsandConditionsFragment.show(getSupportFragmentManager(),"jkfkj");

    }
}
