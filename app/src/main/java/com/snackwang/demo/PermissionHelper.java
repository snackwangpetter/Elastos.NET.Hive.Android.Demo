//package org.elastos.hive.demo.Utils;
//
//import android.app.Activity;
//import android.app.DownloadManager;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.view.Gravity;
//import android.widget.Toast;
//
//public class PermissionHelper{
//    public static boolean isShowToast = true ;
//
//    public boolean checkPermission(Activity activity , PermissionCallback callback , String permission){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            String[] permissions = new String[1];
//            permissions[0]=permission;
//            int checkSelfPermission = ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission);
//            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
//                startRequestPermission(activity,permissions,callback);
//                return false ;
//            }
//            return true;
//        }
//        return true ;
//    }
//
//    private void startRequestPermission(Activity activity ,String[] permissions , PermissionCallback callback){
//        ActivityCompat.requestPermissions(activity, permissions, 654);
//        new PermissionCallback();
//    }
//
//    class PermissionResult implements ActivityCompat.OnRequestPermissionsResultCallback{
//        private int permissionRequestCode ;
//        private PermissionCallback permissionCallback ;
//        public PermissionResult(int requestCode , PermissionCallback callback) {
//            this.permissionRequestCode = requestCode;
//            this.permissionCallback = callback ;
//        }
//
//        @Override
//        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//            if (requestCode == permissionRequestCode) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                        showFailToast();
//                        permissionCallback.onFail(permissions,grantResults);
//                    } else {
//                        permissionCallback.onSuccess(permissions,grantResults);
//                    }
//                }
//            }
//        }
//    }
//
//    private void showFailToast(){
//        if (isShowToast){
//            Toast toast = Toast.makeText(this, "Access failed, open manually!", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//    }
//    interface PermissionCallback{
//        void onSuccess(@NonNull String[] permissions, @NonNull int[] grantResults);
//        void onFail(@NonNull String[] permissions, @NonNull int[] grantResults);
//    }
//}
