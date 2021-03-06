package com.example.firebaseconnection;

import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    //FIELDS
    Button buttonLogin;
    EditText editTextLogin, editTextPassword;

    String userLoginString, userPasswordString;

    DatabaseReference mDatabaseRef;

    //FIREBASE AUTHENTICATION
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ASSIGN ID'S
          buttonLogin = (Button)findViewById(R.id.buttonLogin);
          editTextLogin = (EditText)findViewById(R.id.editTextLogin);
          editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        //ASSIGN INSTANCES
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {

                }else{

                }

            }
        };

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Login operation
                userLoginString = editTextLogin.getText().toString().trim();
                userPasswordString = editTextPassword.getText().toString().trim();

                if(!TextUtils.isEmpty(userLoginString) && !TextUtils.isEmpty(userPasswordString))
                {
                    mAuth.signInWithEmailAndPassword(userLoginString, userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                final DatabaseReference mChildDatabase = mDatabaseRef.child("Users").child(mAuth.getCurrentUser().getUid());

                                mChildDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.child("isVerified").getValue().toString().equals("unverified")){
                                            startActivity(new Intent(LoginActivity.this, InputScreen.class));
                                        }
                                        else
                                        {
                                            startActivity(new Intent(LoginActivity.this, DisplayScreen.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Nie udało się zalogować", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }
}