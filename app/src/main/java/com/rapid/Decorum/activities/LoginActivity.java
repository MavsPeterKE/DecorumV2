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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapid.Decorum.R;
import com.rapid.Decorum.appConstants.AppConstants;
import com.rapid.Decorum.preferencehelper.PreferenceHelper;
import com.rapid.Decorum.progress.ProgressFragment;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView link_signup,link_password;
    EditText input_email,input_password;

    Button btn_login;

    ProgressFragment progressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        link_signup=findViewById(R.id.tvSignUp);
        btn_login=findViewById(R.id.btLogin);
        input_email=findViewById(R.id.edUserName);
        input_password=findViewById(R.id.edPassword);
        link_password=findViewById(R.id.tvForgotPass);


        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));





            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!input_email.getText().toString().equals(""))
                {
                    if(!input_password.getText().toString().equals(""))
                    {
                        login();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"enter password",Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(LoginActivity.this,"enter email",Toast.LENGTH_SHORT).show();

                }
            }


        });
    }


    public void login()
    {
        progressFragment=new ProgressFragment();
        progressFragment.show(getSupportFragmentManager(),"cvmn");

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        progressFragment.dismiss();

                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.e(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> logindata = document.getData();

                                if (logindata.containsKey("Email")) {

                                    if (logindata.containsKey("password")) {

                                        if (logindata.get("Email").toString().equals(input_email.getText().toString())) {
                                            if (logindata.get("password").toString().equals(input_password.getText().toString())) {


                                                new PreferenceHelper(LoginActivity.this).putData(AppConstants.Userid, document.getId());

                                                //   new PreferenceHelper(LoginActivity.this).putData(AppConstants.Currentuserkey, Type);
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();



                                            }
                                        }
                                    }
                                }

                            }
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());

                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressFragment.dismiss();

                Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
