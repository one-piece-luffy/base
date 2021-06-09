package com.baofu.base;


import android.content.pm.PackageManager;
import android.os.Build;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BaseCoreActivity extends AppCompatActivity {
    public String TAG=getClass().getName();

    public void requestPermission(String permision, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permisionStatus = ContextCompat.checkSelfPermission(this, permision);
            if (permisionStatus != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {
                        permision
                }, requestCode);

            } else {
                //todo
                onPermissionsResult(requestCode, true);
            }
        } else {
            onPermissionsResult(requestCode, true);
        }
    }

    public void requestPermission(int requestCode,String... permision) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SparseArray<String> denied = new SparseArray();
            for (int i = 0; i < permision.length; i++) {
                int permisionStatus = ContextCompat.checkSelfPermission(this, permision[i]);
                if (permisionStatus != PackageManager.PERMISSION_GRANTED) {
                    denied.append(i, permision[i]);
                }
            }
            if (denied.size() > 0) {

                String request[] = new String[denied.size()];
                for (int i = 0; i < denied.size(); i++) {
                    int key = denied.keyAt(i);
                    request[i] = denied.get(key);
                }
                ActivityCompat.requestPermissions(this, request, requestCode);
            } else {
                // todo
                onPermissionsResult(requestCode, true);
            }

        } else {
            onPermissionsResult(requestCode, true);
        }
    }

    public void onPermissionsResult(int requestCode, boolean permissionGranted) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean granted = true;
        if (grantResults != null && grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
        } else {
            granted = false;
        }

        if (granted) {
            onPermissionsResult(requestCode, true);

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            onPermissionsResult(requestCode, false);
        }

    }
}
