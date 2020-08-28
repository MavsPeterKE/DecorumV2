package com.rapid.furnitureaugmentreal.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rapid.furnitureaugmentreal.R;
import com.rapid.furnitureaugmentreal.appConstants.AppConstants;
import com.rapid.furnitureaugmentreal.preferencehelper.PreferenceHelper;
import com.rapid.furnitureaugmentreal.progress.ProgressFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    ImageView imgback,imgProfile;

    Button btnUpdate;
    EditText input_name,input_email,input_mobile,input_password,input_reEnterPassword;
    AppCompatRadioButton radiomale,radiofemale;

    ProgressFragment progressFragment;

    FirebaseStorage storage;

    FirebaseFirestore firebaseFirestore;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();
        firebaseFirestore=FirebaseFirestore.getInstance();
        imgback=findViewById(R.id.imgback);
        imgProfile=findViewById(R.id.imgProfile);

        storage = FirebaseStorage.getInstance("gs://furnitureapp-e2c19.appspot.com/");
        storageRef = storage.getReference();

        input_name=findViewById(R.id.input_name);
        input_email=findViewById(R.id.input_email);
        input_mobile=findViewById(R.id.input_mobile);
        btnUpdate=findViewById(R.id.btnUpdate);

        radiomale=findViewById(R.id.radiomale);
        radiofemale=findViewById(R.id.radiofemale);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!input_name.getText().toString().equals(""))
                {
                    if(!input_email.getText().toString().equals(""))
                    {

                        if(!input_mobile.getText().toString().equals(""))
                        {

                            String gender="";

                            progressFragment=new ProgressFragment();
                            progressFragment.show(getSupportFragmentManager(),"cvmn");


                            Map<String,Object> stringObjectMap=new HashMap<>();
                            stringObjectMap.put("Name",input_name.getText().toString());
                            stringObjectMap.put("Email",input_email.getText().toString());
                            stringObjectMap.put("Mobile",input_mobile.getText().toString());
                            if(radiofemale.isChecked())
                            {
                                gender="Female";
                            }

                            else if(radiomale.isChecked())
                            {
                                gender="male";
                            }


                            stringObjectMap.put("gender",gender);



                            firebaseFirestore.collection("User").document(new PreferenceHelper(UserProfileActivity.this).getData(AppConstants.Userid)).update(stringObjectMap).addOnSuccessListener(UserProfileActivity.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    progressFragment.dismiss();

                                    Toast.makeText(UserProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(UserProfileActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressFragment.dismiss();
                                    Toast.makeText(UserProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();

                                }
                            });



                        }
                        else {

                            Toast.makeText(UserProfileActivity.this,"Enter mobile",Toast.LENGTH_SHORT).show();
                        }



                    }
                    else {

                        Toast.makeText(UserProfileActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
                    }


                }
                else {

                    Toast.makeText(UserProfileActivity.this,"Enter name",Toast.LENGTH_SHORT).show();
                }
            }
        });





        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();
            }
        });

        getProfile();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadPhotos();
            }
        });

        getDownloadUrl();
    }

    public void uploadPhotos()
    {
        if (ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UserProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

        } else {

            pickImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickImage();

        }
    }


    public void pickImage() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);


            uploadFile(new File(picturePath));

            Glide.with(UserProfileActivity.this)
                    .load(new File(picturePath))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgProfile);

            cursor.close();
        }
    }

    public void uploadFile(File f) {


        progressFragment=new ProgressFragment();
        progressFragment.show(getSupportFragmentManager(),"xx");

        InputStream stream = null;
        try {
            stream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StorageReference mountainsRef = storageRef.child(new PreferenceHelper(UserProfileActivity.this).getData(AppConstants.Userid));

        UploadTask uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressFragment.dismiss();
                Toast.makeText(UserProfileActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                progressFragment.dismiss();
                getDownloadUrl();

            }
        });
    }

    public void getDownloadUrl() {

        storageRef.child(new PreferenceHelper(UserProfileActivity.this).getData(AppConstants.Userid)).getDownloadUrl().addOnSuccessListener(UserProfileActivity.this, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                String url = uri.toString();


                Glide.with(UserProfileActivity.this).load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder).circleCrop()).into(imgProfile);

                //updateUserImage();

                //Toast.makeText(UserProfileActivity.this,url,Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getProfile()
    {

        progressFragment=new ProgressFragment();
        progressFragment.show(getSupportFragmentManager(),"cvmn");

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("User").document(new PreferenceHelper(UserProfileActivity.this).getData(AppConstants.Userid)).addSnapshotListener(UserProfileActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                progressFragment.dismiss();

                Map<String, Object> logindata = documentSnapshot.getData();



                input_email.setText(logindata.get("Email").toString());
                input_mobile.setText(logindata.get("Mobile").toString());
                input_name.setText(logindata.get("Name").toString());
                if (logindata.get("gender").toString().equalsIgnoreCase("Male"))
                {

                    radiomale.setChecked(true);
                }
                else {

                    radiofemale.setChecked(true);

                }




            }
        });

    }
}
