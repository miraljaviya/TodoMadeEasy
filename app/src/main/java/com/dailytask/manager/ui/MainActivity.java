package com.dailytask.manager.ui;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dailytask.manager.R;
import com.dailytask.manager.fragments.CompletedTaskFragment;
import com.dailytask.manager.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById (R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //this checks to see if there's any savedInstance,if null, then it replaces the fragment container
        // with home fragment.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,
                    new HomeFragment ()).commit();
        }

    }


    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener=
            menuItem -> {
                Fragment selectedfragment=null;
                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        selectedfragment=new HomeFragment();
                        break;

                    case R.id.completed:
                        selectedfragment=new CompletedTaskFragment ();
                        break;
                }
                if (selectedfragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,
                            selectedfragment).commit();
                }

                return true;
            };
    @Override
    public void onBackPressed() {
        FragmentManager fm=getFragmentManager ();
        if (fm.getBackStackEntryCount ()>0) {
            fm.popBackStack ();
        }
           else {
               super.onBackPressed ();
           }
        }
        // handles on hard ware backpressed..



}


