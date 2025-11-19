package com.example.proto4.qr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.example.proto4.MainActivity;
import com.example.proto4.MapActivity;
import com.example.proto4.R;
import com.example.proto4.building;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;

import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.Executors;

public class Main extends AppCompatActivity {
    private PreviewView previewView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.qr_scanner);
        ImageButton bck=findViewById(R.id.back2);
        bck.setOnClickListener(v->{
            Intent intent=new Intent(Main.this, MainActivity.class);
            startActivity(intent);
        });
        previewView = findViewById(R.id.previewView);

        startCamera();

    }
    private void startCamera() {
        //This line asks th system give me the camera when it's ready
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        //when camera is ready this block will execute
        cameraProviderFuture.addListener(() -> {
            try {
                //gets the actual camera provider we can access phone camera by this statement
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                //creates a camera preview
                Preview preview = new Preview.Builder().build();

                //connects preview to the ui
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                //creates image analysis for qr detection ImageAnalysis class gets frames from camera
                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)//KEEP_ONLY_Latest means no lag,only latest frames are processed
                        .build();

                //this detects qr codes and runs in background thread Image class captures frames coming from camera
                analysis.setAnalyzer(Executors.newSingleThreadExecutor(), image -> {
                    @SuppressLint("UnsafeOptInUsageError")

                     //it converts Camera Frame to ML kit Input i can only scan using InputImage
                    Image mediaImage = image.getImage();
                    if (mediaImage != null) {
                        InputImage inputImage = InputImage.fromMediaImage(
                                mediaImage, image.getImageInfo().getRotationDegrees()
                        );

                        //it creates barcode scanner
                        BarcodeScanner scanner = BarcodeScanning.getClient();

                        //it proccess the frames
                        scanner.process(inputImage)
                                //it loops through detected qr codes and hetRawValue() returns text from qr
                                .addOnSuccessListener(barcodes -> {
                                    for (Barcode barcode : barcodes) {
                                        String value = barcode.getRawValue();
                                        if (value != null) {
                                            //returns QR result to previous Activity
                                            Intent result = new Intent();
                                            result.putExtra("qrResult", value);
                                            setResult(RESULT_OK, result);
                                            finish();
                                        }
                                    }
                                })
                                //it frees the frame and closes the image
                                .addOnCompleteListener(task -> image.close());
                    }
                });
                //selects back camera
                CameraSelector cameraSelector =
                        new CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build();
                //unbind old camera seesion if any
                cameraProvider.unbindAll();
                //starts camera,shows preview and starts scanning
                cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, analysis
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }
}
