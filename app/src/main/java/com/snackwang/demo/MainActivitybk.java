//package com.snackwang.demo;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//import android.view.View;
//
//import org.elastos.hive.demo.ClientType;
//import org.elastos.hive.demo.MainFragment;
//import org.elastos.hive.demo.R;
//import org.elastos.hive.demo.utils.ToastUtils;
//
//import jahirfiquitiva.libs.fabsmenu.FABsMenu;
//import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
//import jahirfiquitiva.libs.fabsmenu.TitleFAB;
//
//public class MainActivitybk extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
//    private Toolbar toolbar;
//    private NavigationView navigationView ;
//    private DrawerLayout drawer;
//
//    private boolean backPressedToExitOnce = false;
//
//    private FABsMenu fabsMenu;
//
//    Fragment fragment;
//
//    public ClientType currentClientType ;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initView();
//    }
//
//    private void initView() {
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Local storage");
//
//        toolbar.inflateMenu(R.menu.main);
//
//
//        drawer = findViewById(R.id.drawer_layout);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
////                this, drawer, toolbar, R.string.open, R.string.close);
////        drawer.addDrawerListener(toggle);
////        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.internalstorage);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                drawer.showContextMenu();
////                navigationView.showContextMenu();
//
//                drawer.openDrawer(GravityCompat.START);
//            }
//        });
//
//
//        fragment = new MainFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
//        initialiseFab();
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        int id = menuItem.getItemId();
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        switch (id){
//            case R.id.internalstorage:
//                toolbar.setTitle(R.string.internalstorage);
//
////                Fragment fragment = new Fragment();
////                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
//
////                Fragment internalStorageFragment = new InternalStorageFragment();
//
//
////
////                transaction.replace(R.id.content_frame, internalStorageFragment);
////                transaction.commit();
//
//                ((MainFragment)fragment).presenter.changeClientType(ClientType.INTERNAL_STORAGE_TYPE);
//
//
//
//                break;
//            case R.id.onedrive:
//                toolbar.setTitle(R.string.onedrive);
//                ((MainFragment)fragment).presenter.changeClientType(ClientType.ONEDRIVE_TYPE);
//                break;
//            case R.id.ipfs:
////                Fragment ipfsFragment = new IPFSFragment();
////                transaction.replace(R.id.content_frame, ipfsFragment);
//                ((MainFragment)fragment).presenter.changeClientType(ClientType.IPFS_TYPE);
//
//
//                //transaction.addToBackStack(null);
////                drawer.setSomethingSelected(true);
////                openProcesses = false;
//                //title.setText(utils.getString(con, R.string.process_viewer));
//                //Commit the transaction
////                transaction.commit();
//
//
//                toolbar.setTitle(R.string.ipfs);
//                break;
//        }
////        drawer.closeDrawer(GravityCompat.START);
//        drawer.closeDrawers();
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//
//
//        switch (id){
//
//
//            case R.id.action_newfile:
//                toolbar.setTitle("action_newfile");
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public Fragment getFragmentAtFrame() {
//        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
//    }
//
//    void initialiseFab() {
////        int colorAccent = getAccent();
//
//        fabsMenu = findViewById(R.id.fabs_menu);
////        fabsMenu.getMenuButton().setBackgroundColor(colorAccent);
////        fabsMenu.getMenuButton().setRippleColor(Utils.getColor(this, R.color.white_translucent));
//        fabsMenu.setAnimationDuration(500);
//        fabsMenu.setMenuListener(new FABsMenuListener() {
//            @Override
//            public void onMenuExpanded(FABsMenu fabsMenu) {
////                showSmokeScreen();
//            }
//
//            @Override
//            public void onMenuCollapsed(FABsMenu fabsMenu) {
////                hideSmokeScreen();
//            }
//        });
//
////        floatingActionButton.setMenuListener(new FABsMenuListener() {
////            @Override
////            public void onMenuExpanded(FABsMenu fabsMenu) {
////                FileUtils.revealShow(fabBgView, true);
////            }
////
////            @Override
////            public void onMenuCollapsed(FABsMenu fabsMenu) {
////                FileUtils.revealShow(fabBgView, false);
////            }
////        });
////
////        initFabTitle(findViewById(R.id.menu_new_folder), MainActivityHelper.NEW_FOLDER);
////        initFabTitle(findViewById(R.id.menu_new_file), MainActivityHelper.NEW_FILE);
////        initFabTitle(findViewById(R.id.menu_new_cloud), MainActivityHelper.NEW_CLOUD);
//
//
//        initFabTitle(findViewById(R.id.menu_new_folder), 0);
//        initFabTitle(findViewById(R.id.menu_new_file), 1);
//    }
//
//
//    private void initFabTitle(TitleFAB fabTitle, int type) {
////        int iconSkin = getCurrentColorPreference().iconSkin;
//
////        fabTitle.setBackgroundColor(iconSkin);
////        fabTitle.setRippleColor(Utils.getColor(this, R.color.white_translucent));
//        fabTitle.setOnClickListener(view -> {
////            mainActivityHelper.add(type);
//
//            switch (type){
//                case 0:
//                    ToastUtils.showShortToastSafe(((MainFragment)getFragmentAtFrame()).presenter.getCurrentPath()+" ; type = 0");
//                    break;
//                case 1:
//                    ToastUtils.showShortToastSafe(((MainFragment)getFragmentAtFrame()).presenter.getCurrentPath()+" ; type = 1");
//                    break;
//            }
//            fabsMenu.collapse();
//        });
//
////        switch (getAppTheme().getSimpleTheme()) {
////            case DARK:
////                fabTitle.setTitleBackgroundColor(Utils.getColor(this, R.color.holo_dark_background));
////                fabTitle.setTitleTextColor(Utils.getColor(this, R.color.text_dark));
////                break;
////            case BLACK:
////                fabTitle.setTitleBackgroundColor(Color.BLACK);
////                fabTitle.setTitleTextColor(Utils.getColor(this, R.color.text_dark));
////                break;
////        }
//    }
//
//    private void exit(){
//        if (backPressedToExitOnce) {
//            finish();
//        } else {
//            this.backPressedToExitOnce = true;
//            ToastUtils.showShortToastSafe(R.string.pressagain);
//            new Handler().postDelayed(() -> {
//                backPressedToExitOnce = false;
//            }, 2000);
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawers();
//            return;
//        }
//
//        exit();
//    }
//
//
//}
