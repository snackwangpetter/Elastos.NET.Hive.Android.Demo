package org.elastos.hive.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.elastos.hive.demo.utils.ToastUtils;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private NavigationView navigationView ;
    private DrawerLayout drawer;

    private boolean backPressedToExitOnce = false;

    private FABsMenu fabsMenu;

    Fragment fragment;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int REQUEST_STOREAGE_PERMISSION = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                startRequestPermission();
            }
        }
        setContentView(R.layout.activity_main);

        initView();
    }

    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_STOREAGE_PERMISSION);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Local storage");

        toolbar.inflateMenu(R.menu.main);

        drawer = findViewById(R.id.drawer_layout);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.internalstorage);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.action_more:
                        ToastUtils.showShortToastSafe("TODO more");
                        break;
                    case R.id.action_back_home:
                        MainPresenter presenter = ((MainFragment)getFragmentAtFrame()).presenter;
                        ClientType clientType = presenter.getCurrentClientType();
                        String defaultPath = Config.getDefaultPath(clientType) ;
                        presenter.setCurrentPath(defaultPath);
                        presenter.refreshData();
                        ToastUtils.showShortToastSafe("home...");
                        break;
                }
                return true;
            }
        });

        fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        initialiseFab();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id){
            case R.id.internalstorage:
                toolbar.setTitle(R.string.internalstorage);
                ((MainFragment)fragment).presenter.changeClientType(ClientType.INTERNAL_STORAGE_TYPE);
                break;
            case R.id.onedrive:
                toolbar.setTitle(R.string.onedrive);
                ((MainFragment)fragment).presenter.changeClientType(ClientType.ONEDRIVE_TYPE);
                break;
            case R.id.ipfs:
                toolbar.setTitle(R.string.ipfs);
                ((MainFragment)fragment).presenter.changeClientType(ClientType.IPFS_TYPE);
                break;
        }
        drawer.closeDrawers();
        return true;
    }

    public Fragment getFragmentAtFrame() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    void initialiseFab() {
        fabsMenu = findViewById(R.id.fabs_menu);
        fabsMenu.setAnimationDuration(500);

        initFabTitle(findViewById(R.id.menu_new_folder), 0);
        initFabTitle(findViewById(R.id.menu_new_file), 1);
    }

    private void initFabTitle(TitleFAB fabTitle, int type) {
        fabTitle.setOnClickListener(view -> {
            String currentPath = ((MainFragment)getFragmentAtFrame()).presenter.getCurrentPath();
            switch (type){
                case 0:
                    ToastUtils.showShortToastSafe(currentPath+" ; type = 0");

                    ((MainFragment)getFragmentAtFrame()).presenter.createDirectory(currentPath+"/test"+System.currentTimeMillis());
                    break;
                case 1:
                    ToastUtils.showShortToastSafe(currentPath+" ; type = 1");
                    ((MainFragment)getFragmentAtFrame()).presenter.createFile(currentPath+"/test"+System.currentTimeMillis());
                    break;
            }
            fabsMenu.collapse();
        });
    }

    private void exit(){
        if (backPressedToExitOnce) {
            finish();
        } else {
            this.backPressedToExitOnce = true;
            ToastUtils.showShortToastSafe(R.string.pressagain);
            new Handler().postDelayed(() -> {
                backPressedToExitOnce = false;
            }, 2000);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        exit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STOREAGE_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showShortToastSafe("Access failed, open manually!");
                }
            }
        }
    }

}
