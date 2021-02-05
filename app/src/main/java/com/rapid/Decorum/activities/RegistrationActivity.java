package com.rapid.Decorum.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rapid.Decorum.R;
import com.rapid.Decorum.domain.Child;

public class RegistrationActivity extends AppCompatActivity {


    ImageView imgback;

    EditText input_name,input_email,input_mobile,input_password,input_reEnterPassword;
    RadioButton radiomale,radiofemale;

    RelativeLayout btn_signup;

    Button link_login;



    String gender="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        getSupportActionBar().hide();
        imgback=findViewById(R.id.imgback);
        input_name=findViewById(R.id.input_name);
        input_email=findViewById(R.id.input_email);
        input_mobile=findViewById(R.id.input_mobile);
        input_password=findViewById(R.id.input_password);
        input_reEnterPassword=findViewById(R.id.input_reEnterPassword);
        radiomale=findViewById(R.id.rdMale);
        radiofemale=findViewById(R.id.radiofemale);
        link_login = findViewById(R.id.bt_ready_login);
        btn_signup = findViewById(R.id.link_login);

     //   btn_signup=findViewById(R.id.btLogin);


        btn_signup.setOnClickListener(v -> {


            if(!input_name.getText().toString().equals(""))
            {
                if(!input_email.getText().toString().equals(""))
                {

                    if(!input_mobile.getText().toString().equals(""))
                    {
                        if(!input_password.getText().toString().equals(""))
                        {

                            if(input_password.getText().toString().length()>=6)
                            {

                                if(input_password.getText().toString().equals(input_reEnterPassword.getText().toString()))
                                {

                                   // showTermsDialog();

                                    if(radiomale.isChecked())
                                    {
                                        gender="male";
                                    }

                                    if(radiofemale.isChecked())
                                    {
                                        gender="female";
                                    }

                                    Child child =new Child();
                                    child.setName(input_name.getText().toString());
                                    child.setEmail(input_email.getText().toString());
                                    child.setGender(gender);
                                    child.setMobile(input_mobile.getText().toString());
                                    child.setPassword(input_password.getText().toString());

                                    Intent intent=new Intent(RegistrationActivity.this,OTPActivity.class);
                                    intent.putExtra("Child", child);

                                    startActivity(intent);
                                }
                                else {

                                    Toast.makeText(RegistrationActivity.this,"Password confirmation failed",Toast.LENGTH_SHORT).show();
                                }


                            }
                            else {

                                Toast.makeText(RegistrationActivity.this,"Enter password with atleast 6 characters",Toast.LENGTH_SHORT).show();
                            }



                        }
                        else {

                            Toast.makeText(RegistrationActivity.this,"Enter password",Toast.LENGTH_SHORT).show();
                        }


                    }
                    else {

                        Toast.makeText(RegistrationActivity.this,"Enter mobile",Toast.LENGTH_SHORT).show();
                    }



                }
                else {

                    Toast.makeText(RegistrationActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
                }


            }
            else {

                Toast.makeText(RegistrationActivity.this,"Enter name",Toast.LENGTH_SHORT).show();
            }
        });




        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }
}
