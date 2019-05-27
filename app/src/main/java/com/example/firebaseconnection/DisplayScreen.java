package com.example.firebaseconnection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayScreen extends AppCompatActivity {

    Button buttonLogout;
    TextView tvEmail, tvPassword, tvImie, tvNazwisko, tvWzrost, tvWaga;

    DatabaseReference mDatabaseRef;

    //FIREBASE AUTHENTICATION
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_screen);

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        tvEmail = (TextView) findViewById(R.id.textViewEmail);
        tvPassword = (TextView) findViewById(R.id.textViewPassword);
        tvImie = (TextView) findViewById(R.id.textViewImie);
        tvNazwisko = (TextView) findViewById(R.id.textViewNazwisko);
        tvWzrost = (TextView) findViewById(R.id.textViewWzrost);
        tvWaga = (TextView) findViewById(R.id.textViewWaga);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvEmail.setText(dataSnapshot.child("Login").getValue().toString());
                tvPassword.setText(dataSnapshot.child("Password").getValue().toString());
                tvImie.setText(dataSnapshot.child("UserImie").getValue().toString());
                tvNazwisko.setText(dataSnapshot.child("UserNazwisko").getValue().toString());
                tvWzrost.setText(dataSnapshot.child("UserWzrost").getValue().toString());
                tvWaga.setText(dataSnapshot.child("UserWaga").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DisplayScreen.this, "WYLOGOWANO", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DisplayScreen.this, MainActivity.class));
            }
        });
    }
}
