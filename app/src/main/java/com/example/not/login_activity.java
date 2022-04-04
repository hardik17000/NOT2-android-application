package com.example.not;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.not.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_activity extends AppCompatActivity {
    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog LoadingBar;
    private String parentDbName="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton= (Button) findViewById(R.id.login_button);
        InputNumber=(EditText) findViewById(R.id.login_phone_no_input);
        InputPassword=(EditText)findViewById(R.id.login_password_input);
        LoadingBar= new ProgressDialog(this);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser();
            }
        });
    }
    private void loginuser()
    {
        String phone =InputNumber.getText().toString();
        String password= InputPassword.getText().toString();
        if(TextUtils.isEmpty(phone))
    {
        Toast.makeText(this,"Please enter the phone number... ", Toast.LENGTH_SHORT).show();
    }
       else if(TextUtils.isEmpty(password))
    {
        Toast.makeText(this,"Please enter the password...", Toast.LENGTH_SHORT).show();
    }
       else
        {
            LoadingBar.setTitle("Login Account");
            LoadingBar.setMessage("Please wait,while we are checking the credentials");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();


            AllowAccessToAccount(phone,password);
        }
    }
    private void AllowAccessToAccount(String phone, String password)
    {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.child(parentDbName).child(phone).exists())
                    {
                        Users userData= snapshot.child(parentDbName).child(phone).getValue(Users.class);
                        if(userData.getPhone().equals(phone))
                        {
                            if(userData.getPassword().equals(password))
                            {
                                Toast.makeText(login_activity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent= new Intent(login_activity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(login_activity.this, "Wrong Password! Please try again", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                            }
                        }
                    }
                else
                     {
                        Toast.makeText(login_activity.this, "Account with this number"+phone+"already exists!", Toast.LENGTH_SHORT).show();
                        LoadingBar.dismiss();
                         Toast.makeText(login_activity.this, "Please create ann account with this number", Toast.LENGTH_SHORT).show();
                     }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
                {

                }
        });
    }
}