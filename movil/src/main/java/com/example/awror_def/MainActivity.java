package com.example.awror_def;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new fragment_inicio())
                .commit();

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            if(item.getItemId() == R.id.nav_inicio){
                fragment = new fragment_inicio();
            }

            if(item.getItemId() == R.id.nav_vacunas){
                fragment = new fragment_vacunas();
            }

            if(item.getItemId() == R.id.nav_perfil){
                fragment = new fragment_perfil();
            }

            if(item.getItemId() == R.id.nav_mascotas){
                fragment = new fragment_pet();
            }

            if(fragment != null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
            }
            return true;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable logo = getResources().getDrawable(R.drawable.awrorlogo);
        Bitmap bitmap = ((BitmapDrawable) logo).getBitmap();
        Drawable smallLogo = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
        toolbar.setLogo(smallLogo);

    }
}