package com.example.aadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class UserProfile extends AppCompatActivity {
    private TextView editTextFullName, editTextemail;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        editTextFullName = findViewById(R.id.profile_full_name);
        editTextemail = findViewById(R.id.profile_email);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                editTextFullName.setText(documentSnapshot.getString("fullName"));
                editTextemail.setText("Email address: " + documentSnapshot.getString("email"));
            }
        });


        //Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.user_bottom_navigation);

        // Select Map
        bottomNavigationView.setSelectedItemId(R.id.user_profile);

        // Switch between activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_profile:
                        return true;
                    case R.id.map:
                        Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        overridePendingTransition(0, 0);
                        startActivity(mapIntent);
                        return true;
                    case R.id.restaurant:
                        Intent restaurantIntent = new Intent(getApplicationContext(), RestaurantRecyclerView.class);
                        restaurantIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        overridePendingTransition(0, 0);
                        startActivity(restaurantIntent);
                        return true;
                }
                return false;
            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}
