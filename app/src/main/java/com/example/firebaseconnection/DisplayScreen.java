package com.example.firebaseconnection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayScreen extends AppCompatActivity {

    Button buttonLogout, buttonLosuj;
    TextView tvEmail, tvPassword, tvImie, tvNazwisko, tvWzrost, tvWaga, tvSzamka;
    RadioButton radioButton;
    String previousLogin;

    DatabaseReference mDatabaseRef, mFoodDatabaseRef;

    //FIREBASE AUTHENTICATION
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_screen);

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLosuj = (Button) findViewById(R.id.buttonLosuj);
        tvEmail = (TextView) findViewById(R.id.textViewEmail);
        tvPassword = (TextView) findViewById(R.id.textViewPassword);
        tvImie = (TextView) findViewById(R.id.textViewImie);
        tvNazwisko = (TextView) findViewById(R.id.textViewNazwisko);
        tvWzrost = (TextView) findViewById(R.id.textViewWzrost);
        tvWaga = (TextView) findViewById(R.id.textViewWaga);
        tvSzamka = (TextView) findViewById(R.id.textViewDinner);
        radioButton = (RadioButton) findViewById(R.id.radioButtonBreakfast);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mFoodDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Food");

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                previousLogin = dataSnapshot.child("LastLogin").getValue().toString();
                mDatabaseRef.child("PreviousLogin").setValue(previousLogin);

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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Date date = new Date(mAuth.getCurrentUser().getMetadata().getLastSignInTimestamp());
                mDatabaseRef.child("LastLogin").setValue(DateFormat.getDateInstance(DateFormat.SHORT).format(date));
            }
        }, 200);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DisplayScreen.this, MainActivity.class));
            }
        });



        buttonLosuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int random = new Random().nextInt(13) + 1;

                mFoodDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {
                            tvSzamka.setText(dataSnapshot.child(String.valueOf(random)).child("Nazwa").getValue().toString() + "        " + dataSnapshot.child(String.valueOf(random)).child("Kalorie").getValue().toString());
                        } catch(Exception e)
                        {
                            Toast.makeText(DisplayScreen.this, "Jeszcze raz", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.child("checked").setValue("true");
            }
        });

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("PreviousLogin").getValue().toString().equals(dataSnapshot.child("LastLogin").getValue().toString())) {
                    mDatabaseRef.child("checked").setValue("false");
                    radioButton.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("checked").getValue().toString().equals("true")){

                            radioButton.setChecked(true);
                        }
                        else
                        {
                            radioButton.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 200);
    }
}
