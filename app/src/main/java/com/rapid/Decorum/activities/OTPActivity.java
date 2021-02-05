package com.rapid.Decorum.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapid.Decorum.R;
import com.rapid.Decorum.appConstants.AppConstants;
import com.rapid.Decorum.domain.Child;
import com.rapid.Decorum.preferencehelper.PreferenceHelper;
import com.rapid.Decorum.progress.ProgressFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText edtCode;
    TextView txtCode;

    FirebaseFirestore firebaseFirestore;

    Child child;
    String id="";

    ProgressFragment progressFragment;

    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().hide();
        firebaseFirestore=FirebaseFirestore.getInstance();

        child=(Child) getIntent().getSerializableExtra("Child");
        type=getIntent().getStringExtra("type");

        btnSubmit=findViewById(R.id.btnSubmit);
        edtCode=findViewById(R.id.edtCode);
        txtCode=findViewById(R.id.txtCode);

        Random random=new Random();


        id = String.format("%04d",random.nextInt(10000));

        txtCode.setText("Code : "+id);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(id.equals(edtCode.getText().toString()))
                {


                    progressFragment=new ProgressFragment();
                    progressFragment.show(getSupportFragmentManager(),"cvmn");


                    Map<String,String> stringObjectMap=new HashMap<>();
                    stringObjectMap.put("Name",child.getName());
                    stringObjectMap.put("Email",child.getEmail());
                    stringObjectMap.put("Mobile",child.getMobile());
                    stringObjectMap.put("password",child.getPassword());
                    stringObjectMap.put("gender",child.getGender());



                    firebaseFirestore.collection("User").add(stringObjectMap).addOnSuccessListener(OTPActivity.this, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(OTPActivity.this,"Success",Toast.LENGTH_SHORT).show();

                            progressFragment.dismiss();

                            new PreferenceHelper(OTPActivity.this).putData(AppConstants.Userid,documentReference.getId());

                           // new PreferenceHelper(OTPActivity.this).putData(AppConstants.Currentuserkey, AppConstants.userchild);
                            Intent intent=new Intent(OTPActivity.this,MainActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);



                        }
                    }).addOnFailureListener(OTPActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(OTPActivity.this,"Failure",Toast.LENGTH_SHORT).show();

                            progressFragment.dismiss();

                        }
                    });



                }
                else {
                    Toast.makeText(OTPActivity.this,"Enter code",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
