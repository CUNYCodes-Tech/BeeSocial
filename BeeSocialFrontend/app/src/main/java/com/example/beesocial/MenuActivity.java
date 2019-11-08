package com.example.beesocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //drawer menu settings
        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFrag()).commit();
        }

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //switches between the different pages on the map

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFrag()).commit();
                break;
            case R.id.createevent:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateEventFrag()).commit();
                break;
            case R.id.editevent:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditEventFrag()).commit();
                break;
            case R.id.chats:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFrag()).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFrag()).commit();
                break;
            case R.id.logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogoutFrag()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



  /*  @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      //  Fragment fragmentClass=null;
        switch(menuItem.getItemId()){
            case R.id.home:
                Toast.makeText(this,"Home Btn Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.createevent:
                Toast.makeText(this,"Create Event Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editevent:
                Toast.makeText(this,"Edit Event Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chats:
                Toast.makeText(this,"Chats Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"Settings Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(this,"Logout Clicked", Toast.LENGTH_SHORT).show();
                break;

        }
        return false;
    }
*/


}
