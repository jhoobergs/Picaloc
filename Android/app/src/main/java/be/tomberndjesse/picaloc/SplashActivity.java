package be.tomberndjesse.picaloc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Calendar;

import be.tomberndjesse.picaloc.utils.SettingsUtil;
import be.tomberndjesse.picaloc.utils.SharedPreferencesKeys;

/**
 * Created by jesse on 17/09/2016.
 */
public class SplashActivity extends AppCompatActivity{
    private final int mAccesFineLocationId=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startService(new Intent(this, LocationService.class));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.getToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    new SettingsUtil(getApplicationContext()).setString(SharedPreferencesKeys.TokenString, task.getResult().getToken());
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            mAccesFineLocationId);

                }
                else{
                    goFurther();
                };
            }
        }, 3000);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case mAccesFineLocationId: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goFurther();
                } else {
                        for (int i = 0, len = permissions.length; i < len; i++) {
                            String permission = permissions[i];
                            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                                boolean showRationale = shouldShowRequestPermissionRationale( permission );
                                if (! showRationale) {
                                    AlertDialog.Builder d = new AlertDialog.Builder(this);
                                    d.setTitle("The app needs the location permission");
                                    d.setMessage("Please enable the location permission.");
                                    d.setCancelable(false);
                                    d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivityForResult(intent, mAccesFineLocationId);
                                        }
                                    });
                                    d.show();
                                } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            mAccesFineLocationId);
                                }
                            }
                        }
                    }
                }
                return;
            }

    }

    private void goFurther(){
        startActivity(new Intent(this, SignInActivity.class));
    }
}
