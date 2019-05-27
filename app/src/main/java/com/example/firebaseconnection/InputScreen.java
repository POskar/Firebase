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

public class InputScreen extends AppCompatActivity {

    Button buttonConfirm;
    EditText editTextImie, editTextNazwisko, editTextWaga, editTextWzrost;
    String userNameString, userSurnameString, userHeightString, userWeightString;

    DatabaseReference mDatabaseRef;

    //FIREBASE AUTHENTICATION
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_screen);

        //ASSIGN ID'S
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        editTextImie = (EditText) findViewById(R.id.editTextImie);
        editTextNazwisko = (EditText) findViewById(R.id.editTextNazwisko);
        editTextWzrost = (EditText) findViewById(R.id.editTextWzrost);
        editTextWaga = (EditText) findViewById(R.id.editTextWaga);

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

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Data input operation
                userNameString = editTextImie.getText().toString().trim();
                userSurnameString = editTextNazwisko.getText().toString().trim();
                userHeightString = editTextWzrost.getText().toString().trim();
                userWeightString = editTextWaga.getText().toString().trim();

                if(!TextUtils.isEmpty(userNameString) && !TextUtils.isEmpty(userSurnameString) && !TextUtils.isEmpty(userHeightString) && !TextUtils.isEmpty(userWeightString))
                {
                    //WRITE TO DATABASE
                    DatabaseReference mChildDatabase = mDatabaseRef.child("Users").child(mAuth.getCurrentUser().getUid());

                    mChildDatabase.child("UserImie").setValue(userNameString);
                    mChildDatabase.child("UserNazwisko").setValue(userSurnameString);
                    mChildDatabase.child("UserWzrost").setValue(userHeightString);
                    mChildDatabase.child("UserWaga").setValue(userWeightString);
                    mChildDatabase.child("isVerified").setValue("verified");

                    startActivity(new Intent(InputScreen.this, DisplayScreen.class));
                }
                else
                {
                    Toast.makeText(InputScreen.this, "Uzupełnij pola", Toast.LENGTH_LONG).show();
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
