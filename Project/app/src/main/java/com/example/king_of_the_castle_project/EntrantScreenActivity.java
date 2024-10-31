package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.common.moduleinstall.ModuleInstall;
import com.google.android.gms.common.moduleinstall.ModuleInstallClient;
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
import com.google.android.gms.common.moduleinstall.ModuleInstallResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

/**
 * Activity that shows toolbar, buttons and navbar on Entrant Screen
 */
public class EntrantScreenActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavView;
    private AppCompatButton qrCodeBut;
    private AppCompatButton viewInvitationsBut;
    private AppCompatButton waitingListBut;
    private Button changeRolesBut;
    
    private boolean isScannedInstalled = false;
    private GmsBarcodeScanner scanner;
    private String scannnedCode;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_screen);

        bottomNavView = findViewById(R.id.bottom_nav);
        bottomNavView.setSelectedItemId(R.id.home);

        qrCodeBut = findViewById(R.id.qr_button);
        viewInvitationsBut = findViewById(R.id.invitations_button);
        waitingListBut = findViewById(R.id.waitinglist_button);
        changeRolesBut = findViewById(R.id.change_roles_button);

        /*bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileScreenActivity.class));
                    finish();
                    return true;
                case R.id.settings:
                    startActivity(new Intent(getApplicationContext(), SettingsScreenActivity.class));
                    finish();
                    return true;
            }
            return false;
        });*/

        // Installs, initializes and sets on click listener for Google QR code scanner
        // When the code is scanned a Toast is displayed on the screen showing the raw value it returns
        //The raw value is stored in scannnedCode
        installGoogleScanner();
        initVars();
        qrCodeBut.setOnClickListener(v -> {
            if (isScannedInstalled) {
                startScanning();
                Intent intent = new Intent(getApplicationContext(), EventDetailsScreen.class);
                intent.putExtra("qr_result", scannnedCode);
                startActivity(intent);
            } else {
                Toast.makeText(EntrantScreenActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
            }
        });

        viewInvitationsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        waitingListBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntrantScreenActivity.this, MyWaitlistsActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * Installs GmsBarcodeScanning Module onto the device
     */
    private void installGoogleScanner() {
        ModuleInstallClient moduleInstall = ModuleInstall.getClient(this);
        ModuleInstallRequest moduleInstallRequest = ModuleInstallRequest
                .newBuilder()
                .addApi(GmsBarcodeScanning.getClient(this))
                .build();
        
        moduleInstall.installModules(moduleInstallRequest)
                .addOnSuccessListener(new OnSuccessListener<ModuleInstallResponse>() {
                    @Override
                    public void onSuccess(ModuleInstallResponse moduleInstallResponse) {
                        isScannedInstalled = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isScannedInstalled = false;
                        Toast.makeText(EntrantScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Initializes the Google scanner
     */
    private void initVars() {
        GmsBarcodeScannerOptions options = initializeGoogleScanner();
        scanner = GmsBarcodeScanning.getClient(EntrantScreenActivity.this, options);
    }

    /**
     * Builds and returns a GmsBarcodeScannerOptions object
     * @return
     *      returns a GmsBarcodeScannerOptions object that auto zooms to the qr code and
     *      specifically searches for QR codes
     */
    private GmsBarcodeScannerOptions initializeGoogleScanner() {
        return new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();
    }

    /**
     * Requests a code scanning. If it's successful print the result in a toast, if it's cancelled
     *      print cancelled in a toast and if it failed print the error message in a toast
     */
    private void startScanning() {
        scanner.startScan()
                .addOnSuccessListener(new OnSuccessListener<Barcode>() {
                    @Override
                    public void onSuccess(Barcode barcode) {
                        String result = barcode.getRawValue();
                        if (result != null) {
                            scannnedCode = result;
                            Toast.makeText(EntrantScreenActivity.this, scannnedCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnCanceledListener(() -> Toast.makeText(EntrantScreenActivity.this, "Cancelled", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(EntrantScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}