package com.mundiabrian.uber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginRegisterActivity extends AppCompatActivity
{
    private Button DriverLoginButton;
    private Button DriverRegisterButton;
    private TextView DriverRegisterLink;
    private TextView DriverStatus;
    private EditText EmailDriver;
    private EditText PasswordDriver;
    private ProgressDialog LoadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference DriverDatabaseRef;
    private String onlineDriverID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login_register);

        mAuth = FirebaseAuth.getInstance();

        DriverLoginButton = (Button) findViewById(R.id. driver_login_btn);
        DriverRegisterButton = (Button) findViewById(R.id. driver_register_btn);
        DriverRegisterLink = (TextView) findViewById(R.id. register_driver_link);
        DriverStatus = (TextView) findViewById(R.id. driver_status);
        EmailDriver = (EditText) findViewById(R.id. email_driver);
        PasswordDriver = (EditText) findViewById(R.id. password_driver);
        LoadingBar = new ProgressDialog(this);

        DriverRegisterButton.setVisibility(View.INVISIBLE);
        DriverRegisterButton.setEnabled(false);

        DriverRegisterLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View V)
            {
                DriverLoginButton.setVisibility(View.INVISIBLE);
                DriverRegisterLink.setVisibility(View.INVISIBLE);
                DriverStatus .setText("Register Driver");

                DriverRegisterButton.setVisibility(View.VISIBLE);
                DriverRegisterButton.setEnabled(true);

            }
        });
        DriverRegisterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email =EmailDriver.getText().toString();
                String password = PasswordDriver.getText().toString();


                RegisterDriver(email,password);

            }
        });

        DriverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email =EmailDriver.getText().toString();
                String password = PasswordDriver.getText().toString();

                SigninDriver(email, password);
            }
        });
    }

    private void SigninDriver(String email, String password)
    {
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(DriverLoginRegisterActivity.this,"Please write Email...",Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(DriverLoginRegisterActivity.this,"Please write Password...",Toast.LENGTH_SHORT).show();
        }

        else
        {
            LoadingBar.setTitle("Driver Login");
            LoadingBar.setMessage("Please wait, while we  are checking your credentials....");
            LoadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                Intent driverIntent = new Intent(DriverLoginRegisterActivity.this,DriversMapActivity.class);
                                startActivity(driverIntent);

                                Toast.makeText(DriverLoginRegisterActivity.this, "Driver Logged in Successful...", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();


                            }
                            else
                            {
                                Toast.makeText(DriverLoginRegisterActivity.this, "Driver Login UnSuccessful, Please Try Again...", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();

                            }
                        }
                    });

        }
    }

    private void RegisterDriver(String email, String password)
        {
                        if (TextUtils.isEmpty(email))
                        {
                            Toast.makeText(DriverLoginRegisterActivity.this,"Please write Email...",Toast.LENGTH_SHORT).show();
                        }


                        if (TextUtils.isEmpty(password))
                        {
                            Toast.makeText(DriverLoginRegisterActivity.this,"Please write Password...",Toast.LENGTH_SHORT).show();
                        }

            else
            {
            LoadingBar.setTitle("Driver Registration");
            LoadingBar.setMessage("Please wait, while we  register your data....");
            LoadingBar.show();
            
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                onlineDriverID = mAuth.getCurrentUser().getUid();
                                DriverDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child("onlineDriverID");
                                DriverDatabaseRef.setValue(true);

                                Toast.makeText(DriverLoginRegisterActivity.this, "Driver Register Successful...", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();

                                Intent driverIntent = new Intent(DriverLoginRegisterActivity.this,DriversMapActivity.class);
                                startActivity(driverIntent);
                            }
                            else
                            {
                                Toast.makeText(DriverLoginRegisterActivity.this, "Driver Registration UnSuccessful, Please Try Again...", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                            }
                        }
                    });

        }
    }
}