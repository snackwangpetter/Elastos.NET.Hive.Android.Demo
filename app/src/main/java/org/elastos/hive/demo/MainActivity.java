package org.elastos.hive.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.leon.lfilepickerlibrary.LFilePicker;

import org.elastos.hive.demo.utils.FileUtils;
import org.elastos.hive.demo.utils.OpenFileHelper;
import org.elastos.hive.demo.utils.ToastUtils;

import java.util.List;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
//    private NavigationView navigationView ;
    private DrawerLayout drawer;
    private boolean backPressedToExitOnce = false;
    private FABsMenu fabsMenu;
    Fragment fragment;
    private TitleFAB newFile , newDirectory , uploadFile = null;
    private FileItem downloadRealFileItem ;

    private final int MENU_TYPE_NEW_FILE = 0;
    private final int MENU_TYPE_NEW_DIRECTORY = 1;
    private final int MENU_TYPE_UPLOAD_FILE = 2 ;
    private final int REQUESTCODE_UPLOAD_FILE = 1000;
    private final int REQUESTCODE_DOWNLOAD_FILE = 1001;


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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        fabsMenu.collapse();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id){
            case R.id.internalstorage:
                removeUploadFileTitleFAB();
                toolbar.setTitle(R.string.internalstorage);
                ((MainFragment)fragment).presenter.changeClientType(ClientType.INTERNAL_STORAGE_TYPE);
                break;
            case R.id.onedrive:
                createUploadFileTitleFAB();
                toolbar.setTitle(R.string.onedrive);
                ((MainFragment)fragment).presenter.changeClientType(ClientType.ONEDRIVE_TYPE);
                break;
            case R.id.ipfs:
                createUploadFileTitleFAB();
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

        newFile = findViewById(R.id.menu_new_file);
        newDirectory = findViewById(R.id.menu_new_folder);

        initFabTitle(newDirectory, MENU_TYPE_NEW_DIRECTORY);
        initFabTitle(newFile, MENU_TYPE_NEW_FILE);
    }

    private void initFabTitle(TitleFAB fabTitle, int type) {
        fabTitle.setOnClickListener(view -> {
            String currentPath = ((MainFragment)getFragmentAtFrame()).presenter.getCurrentPath();
            switch (type){
                case MENU_TYPE_NEW_DIRECTORY:
                    showInputDialog(R.string.create_new_dir,R.string.create_new_dir_content,
                            R.string.directory , currentPath , MENU_TYPE_NEW_DIRECTORY);
                    break;
                case MENU_TYPE_NEW_FILE:
                    showInputDialog(R.string.create_new_file,R.string.create_new_file_content,
                            R.string.file , currentPath , MENU_TYPE_NEW_FILE);
                    break;
                case MENU_TYPE_UPLOAD_FILE:
                    uploadFilePick();
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

    private void showInputDialog(int title , int content , int hint , String currentPath , int type){
        new MaterialDialog.Builder(MainActivity.this)
                .title(title)
                .content(content)
                .negativeText("cancel")
                .positiveText("ok")
                .input(hint, hint, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String name = dialog.getInputEditText().getText().toString();
                        switch (type){
                            case MENU_TYPE_NEW_FILE:
                                ((MainFragment)getFragmentAtFrame()).presenter.
                                        createFile(FileUtils.appendParentPath(currentPath,name));
                                break;
                            case MENU_TYPE_NEW_DIRECTORY:
                                ((MainFragment)getFragmentAtFrame()).presenter.
                                        createDirectory(FileUtils.appendParentPath(currentPath,name));
                                break;
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .show();
    }

    private void uploadFilePick(){
        new LFilePicker()
                .withActivity(MainActivity.this)
                .withRequestCode(REQUESTCODE_UPLOAD_FILE)
                .withStartPath(Config.DEFAULT_UPLOAD_PICK_FILE_PATH)//指定初始显示路径
                .withMutilyMode(false)
                .start();
    }

    public void downloadFilePick(FileItem realFileItem){
        downloadRealFileItem = realFileItem ;
        new LFilePicker()
                .withActivity(MainActivity.this)
                .withRequestCode(REQUESTCODE_DOWNLOAD_FILE)
                .withChooseMode(false)
                .withStartPath(Config.DEFAULT_DOWNLOAD_PICK_FILE_PATH)//指定初始显示路径
                .withMutilyMode(false)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_UPLOAD_FILE) {
                List<String> list = data.getStringArrayListExtra("paths");
                String fileAbsPath = list.get(0);
                ToastUtils.showShortToastSafe(fileAbsPath);
                uploadFile(fileAbsPath);
            }

            if (requestCode == REQUESTCODE_DOWNLOAD_FILE) {
                String path = data.getStringExtra("path");
                ToastUtils.showShortToastSafe("path="+path);
                ((MainFragment)getFragmentAtFrame()).presenter.excuteFile(downloadRealFileItem , path);
            }
        }
    }

    private void uploadFile(String saveFileAbsPath){
        ((MainFragment)getFragmentAtFrame()).presenter.uploadFile(saveFileAbsPath);
    }

    private void removeUploadFileTitleFAB(){
        if (uploadFile !=null){
            fabsMenu.removeButton(uploadFile);
            uploadFile = null ;
        }

    }
    private void createUploadFileTitleFAB(){
        if (uploadFile == null){
            uploadFile = new TitleFAB(MainActivity.this);
            uploadFile.setTitle("uploadFile");
            uploadFile.setSize(TitleFAB.SIZE_MINI);
            uploadFile.setImageResource(R.mipmap.file_fab);
            fabsMenu.addButton(uploadFile);
            initFabTitle(uploadFile, MENU_TYPE_UPLOAD_FILE);
        }
    }

    public void openFile(String filepath){
        startActivity(OpenFileHelper.openFile(this,filepath));
    }
}
