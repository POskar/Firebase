package com.example.firebaseconnection;

import android.content.Intent;
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

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    //FIELDS
    Button buttonRegister, moveToLogin;
    EditText userLogin, userPassword;

    DatabaseReference mDatabaseRef, mUserCheckData;

    //FIREBASE AUTHENTIACTION
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ASSIGN ID
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        moveToLogin = (Button) findViewById(R.id.moveToLogin);
        userLogin = (EditText) findViewById(R.id.editTextLoginOnRegister);
        userPassword = (EditText) findViewById(R.id.editTextPasswordOnRegister);

        //ASSIGN INSTANCES
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserCheckData = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {

                }
                else
                {

                }
            }
        };

        //BUTTON LISTENERS
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userLoginString, userPasswordString;

                userLoginString = userLogin.getText().toString().trim();
                userPasswordString = userPassword.getText().toString().trim();

                if(!TextUtils.isEmpty(userLoginString) && !TextUtils.isEmpty(userPasswordString))
                {
                    mAuth.createUserWithEmailAndPassword(userLoginString, userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference mChildDatabase = mDatabaseRef.child("Users").child(mAuth.getCurrentUser().getUid());

                                String key_user = mChildDatabase.getKey();

                                mChildDatabase.child("isVerified").setValue("unverified");
                                mChildDatabase.child("userKey").setValue(key_user);
                                mChildDatabase.child("Login").setValue(userLoginString);
                                mChildDatabase.child("Password").setValue(userPasswordString);

                                Toast.makeText(MainActivity.this, "Stworzono użytkownika", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, InputScreen.class));
                            }else
                            {
                                Toast.makeText(MainActivity.this, "Nie udało się stworzyć użytkownika", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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