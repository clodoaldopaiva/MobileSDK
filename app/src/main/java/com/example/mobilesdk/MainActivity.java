package com.example.mobilesdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.util.Log;

import android.os.Bundle;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import android.widget.Toast;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Campaign;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.UserProfile;
import com.adobe.marketing.mobile.CampaignClassic;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileCore.setApplication(this.getApplication());

        MobileCore.setLogLevel(LoggingMode.DEBUG);

        setContentView(R.layout.activity_main);


        Log.d(TAG, " === > Starting My Activity... ");

        String campaignClassicExtensionVersion = CampaignClassic.extensionVersion();

        Log.d(TAG, " === > campaignClassicExtensionVersion  "+campaignClassicExtensionVersion);

        try {
            CampaignClassic.registerExtension();
            UserProfile.registerExtension();
            Identity.registerExtension();
            Lifecycle.registerExtension();
            Signal.registerExtension();
            MobileCore.start(new AdobeCallback() {
                @Override
                public void call(Object o) {
                    MobileCore.configureWithAppID("6ac3e976c146/7f57240019a9/launch-a62f46126499-staging");
                    UserProfile.updateUserAttribute("",null);
                }
            });

            Log.d(TAG, " === > Initialized Successfully!!!  ");
        }catch(InvalidInitException e){
            Log.d(TAG, " === > Failed to Initialize  "+e);
        }

        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, " ===> Key: " + key + " ===> Value: " + value);
            }
        }
        // [END handle_data_extras]

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        registerToken();

    }


    void registerToken() {
        // [START get_installation_token]
        FirebaseInstallations.getInstance().getToken(/* forceRefresh */true)
                .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            //String token = task.getResult().getToken();
                            String token = "cU2J9RZeQC2LihAiMma2G-:APA91bHhXxmQlX1SijjIwNjk7KYXv4XO2bNiUb4ZMD8lVueMLotcNn7EGsJfA4tTxHrzmgajlxYijXW5_A1yGTx_HHlBNkFoQ04CnCZtHzDia-7lRaLH51RlpdIausMt0xBJ462ASNxX";

                            MobileCore.setPushIdentifier(token);

                            //Log.d(" ===> Installations", "Installation auth token: " + task.getResult().getToken());

                            Log.d(" ===> TestApp", "FCM SDK registration token received : " + token);
                            // Create a map of additional parameters
                            Map<String, Object> additionalParams = new HashMap<String, Object>();
                            additionalParams.put("name", "Clodo Paiva");
                            //additionalParams.put("serial", 1234);
                            additionalParams.put("platinum", true);
                            // Send the registration info



                            CampaignClassic.registerDevice(token, "cpaiva@adobe.com", additionalParams,new AdobeCallback<Boolean>() {
                                @Override
                                public void call(final Boolean status) {
                                    Log.d(" ===> TestApp", "Registration Status: " + status);
                                }
                            });

                        } else {
                            Log.e(" ===> Installations", "Unable to get Installation auth token");
                        }
                    }
                });
        // [END get_installation_token]
    }

    @Override
    public void onResume() {
        super.onResume();
        MobileCore.setApplication(getApplication());
        MobileCore.lifecycleStart(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobileCore.lifecyclePause();
    }

}