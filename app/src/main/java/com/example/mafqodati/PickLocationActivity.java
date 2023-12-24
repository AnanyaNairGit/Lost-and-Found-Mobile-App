package com.example.mafqodati;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mafqodati.models.Post;
import com.example.mafqodati.util.FireStorage;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.ImageHolder;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import java.util.ArrayList;
import java.util.List;


public class PickLocationActivity extends AppCompatActivity {

    MapView mapView;
    FloatingActionButton btnFocusLocation;
    Button btnContinue;
    private ProgressDialog progressDialog;

    private double selectedLongitude;
    private double selectedLatitude;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {

            } else {

            }
        }
    });

    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };

    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(20.0).build());
            getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
            updateSelectedLocation(point);
        }
    };

    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            getGestures(mapView).removeOnMoveListener(onMoveListener);
            btnFocusLocation.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        mapView = findViewById(R.id.mapView);
        btnFocusLocation = findViewById(R.id.btnFocusLocation);
        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            progressDialog = new ProgressDialog(PickLocationActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            Post.getInstance().setLongitude(selectedLongitude);
            Post.getInstance().setLatitude(selectedLatitude);
            List<Uri> imagesUriList = new ArrayList<>();
            for( String uri : Post.getInstance().getImagesUri()){
                imagesUriList.add(Uri.parse(uri));
            }
            // Upload images to Firebase Storage
            FireStorage.uploadImages(imagesUriList).addOnSuccessListener(downloadUrls -> {
                // downloadUrls is a List<String> containing the download URLs for all uploaded images

                // Save the post to Firestore with the download URLs
                FireStore.addPost(downloadUrls).addOnSuccessListener(documentReference -> {
                    progressDialog.dismiss();
                    Post.destruct();
                    finishAndGoBackToManin();
                }).addOnFailureListener(e -> {
                    // Handle the failure to save post to Firestore
                });
            }).addOnFailureListener(e -> {
                // Handle the failure to upload images
            });
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        mapView.getMapboxMap().loadStyleUri(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());
                LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
                locationComponentPlugin.setEnabled(true);
                LocationPuck2D locationPuck2D = new LocationPuck2D();
                locationPuck2D.setBearingImage(ImageHolder.from(R.drawable.baseline_location_on_24));
                locationComponentPlugin.setLocationPuck(locationPuck2D);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);
                btnFocusLocation.setOnClickListener(v -> {
                    locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                    locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                    getGestures(mapView).addOnMoveListener(onMoveListener);
                    btnFocusLocation.hide();
                });
            }
        });
    }

    private void updateSelectedLocation(Point point) {
        selectedLatitude = point.latitude();
        selectedLongitude = point.longitude();
    }

    public void finishAndGoBackToManin() {
        // Create an Intent to navigate back to FirstActivity
        Intent intent = new Intent(this, MainActivity.class);

        // Set flags to clear the back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Start the FirstActivity and finish the current activity
        startActivity(intent);
        finish();
    }
}