package com.pritam.httprestclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pritam.httprestclient.fragment.Collection_Fragment;
import com.pritam.httprestclient.fragment.History_Fragment;
import com.pritam.httprestclient.fragment.Home_Fragment;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    public static HashMap<String,Object> Response_hm = new HashMap();
    public static HashMap<String,Object> Request_hm = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Home_Fragment fragment = new Home_Fragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Toast.makeText(this, getSupportFragmentManager().findFragmentById(R.id.content_frame).getClass().getName(), Toast.LENGTH_LONG).show();

            String s = getSupportFragmentManager().findFragmentById(R.id.content_frame).getClass().getName();
            if (s != null && s.equalsIgnoreCase("com.pritam.httprestclient.fragment.Response_Fragment")) {
                Home_Fragment fragment1 = new Home_Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment1).commit();
            } else {
                //super.onBackPressed();
                exitPopup();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            exitApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_new:
                getSupportActionBar().setTitle("Home");
                fragment = new Home_Fragment();
                break;
            case R.id.nav_collection:
                getSupportActionBar().setTitle("Collection");
                fragment = new Collection_Fragment();
                break;
            case R.id.nav_history:
                getSupportActionBar().setTitle("History");
                fragment = new History_Fragment();
                break;
           /* case R.id.nav_import:
                getSupportActionBar().setTitle("Import");
                fragment = new Home_Fragment();
                break;
            case R.id.nav_export:
                getSupportActionBar().setTitle("Export");
                fragment = new Home_Fragment();
                break;
*/
            case R.id.nav_exit:
                exitApp();
                break;
            case R.id.nav_share:
                openPlayStore();
                break;
        }

        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
            return true;
    }

    private void openPlayStore() {
        String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private  void exitPopup() {
        //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        //AlertDialog alertDialog = builder.create();
        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.setMessage("Do you want exit application?");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, HomeActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, HomeActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                exitApp();
            }
        });

        alertDialog.show();
    }

    public  void exitApp() {
        super.onBackPressed();
    }


}
