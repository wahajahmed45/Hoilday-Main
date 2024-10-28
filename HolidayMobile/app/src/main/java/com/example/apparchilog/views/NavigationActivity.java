package com.example.apparchilog.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.apparchilog.R;
import com.example.apparchilog.fragments.DetailsActivityFragment;
import com.example.apparchilog.fragments.DetailsVacationFragment;
import com.example.apparchilog.fragments.DocumentFragment;
import com.example.apparchilog.fragments.WeatherFragment;
import com.example.apparchilog.models.holiday.Vacation;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.viewModels.DetailsVacationViewModel;
import com.example.apparchilog.viewModels.DetailsActivityViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.google.maps.errors.ApiException;
import com.google.maps.android.PolyUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int FINE_PERMISSION = 1;
    private static final long UPDATE_INTERVAL = 10000;
    private Location currentLocation;
    private BottomNavigationView bottomNavigationView;
    private Marker startingPointMarker;
    private Marker destinationMarker;
    private final Handler handler = new Handler();
    private DetailsVacationViewModel mDViewModel;
    private DetailsActivityViewModel mDAViewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap googleMap;
    private MarkerOptions startingMarker;
    private MarkerOptions destMarker;
    private ExecutorService executorService;
    private GeoApiContext geoApiContext;
    private SupportMapFragment mapFragment;
    private Long vacationId;
    private Long activityId;
    private boolean isVacation;
    private int selectedItemId = -1;
    private LatLng latLng;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mDViewModel = new ViewModelProvider(this).get(DetailsVacationViewModel.class);
        mDAViewModel = new ViewModelProvider(this).get(DetailsActivityViewModel.class);

        if (getIntent() != null) {
            vacationId = getIntent().getLongExtra("vacation_id", 0L);
            activityId = getIntent().getLongExtra("activity_id", 0L);
            isVacation = getIntent().getBooleanExtra("is_vacation", false);
        }

        if (vacationId != 0L && isVacation) {
            mDViewModel.loadVacation(vacationId);
        }
        if (activityId != 0L && !isVacation) {
            mDAViewModel.loadActivity(activityId);
        }

        observerViewModel();
        bottomNavigationView = findViewById(R.id.bottomVacationNavigationView);
        ImageButton buttonResetCamera = findViewById(R.id.resetCamera);
        setupBottomNavigationView();
        buttonResetCamera.setOnClickListener(v -> {
            if (currentLocation != null) {
                LatLng newStartingPoint = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(newStartingPoint)
                        .zoom(15)
                        .build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateStartingPoint();
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        }, UPDATE_INTERVAL);
        // Initialize the executor service
        executorService = Executors.newSingleThreadExecutor();
    }

    private void observerViewModel() {
        mDAViewModel.getActivityLiveData().observe(this, new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                latLng = new LatLng(activity.getPlaceRequest().getLatitude(), activity.getPlaceRequest().getLongitude());
                getLastLocationAndUpdateMap();
            }
        });
        mDViewModel.getVacationLiveData().observe(this, new Observer<Vacation>() {
            @Override
            public void onChanged(Vacation vacation) {
                latLng = new LatLng(vacation.getPlaceRequest().getLatitude(), vacation.getPlaceRequest().getLongitude());
                getLastLocationAndUpdateMap();
            }
        });
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMaps) {
        googleMap = googleMaps;
        startingMarker = new MarkerOptions();
        destMarker = new MarkerOptions();

        // Activer la boussole
        googleMap.getUiSettings().setCompassEnabled(true);

        // Activer les boutons de zoom
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Activer le bouton Ma position
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION);
        }
        googleMap.getUiSettings();
        // Activer la gestion du trafic en temps réel
        googleMap.setTrafficEnabled(true);

        // Obtenir la dernière position et mettre à jour la carte
        getLastLocationAndUpdateMap();
    }

    private void getLastLocationAndUpdateMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION);
            return;
        }

        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    LatLng startingPoint = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    startingMarker.position(startingPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.nav)).title("startingPoint");

                    if (latLng != null) {
                        destMarker.position(latLng).title("destination");
                        startingPointMarker = googleMap.addMarker(startingMarker);
                        destinationMarker = googleMap.addMarker(destMarker);
                        googleMap.animateCamera(updateMapAccordingToPoints(startingMarker, destMarker));
                        drawPolyline(googleMap, startingPoint, latLng);


                    }
                }
            }
        });
    }

    private void updateStartingPoint() {
        if (currentLocation != null) {
            googleMap.clear();
            LatLng newStartingPoint = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            startingMarker.position(newStartingPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.nav)).title("startingPoint");
            startingPointMarker = googleMap.addMarker(startingMarker);
            destinationMarker = googleMap.addMarker(destMarker);

            drawPolyline(googleMap, newStartingPoint, latLng);
        }
    }

    private CameraUpdate updateMapAccordingToPoints(MarkerOptions starting, MarkerOptions destination) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(starting.getPosition());
        builder.include(destination.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        return CameraUpdateFactory.newLatLngBounds(bounds, padding);
    }

    private void drawPolyline(GoogleMap map, LatLng start, LatLng end) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_map_apiKey))
                .build();

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                DirectionsResult directions = DirectionsApi.newRequest(context)
                        .mode(TravelMode.TRANSIT)
                        .origin(new com.google.maps.model.LatLng(start.latitude, start.longitude))
                        .destination(new com.google.maps.model.LatLng(end.latitude, end.longitude))
                        .units(Unit.METRIC)
                        .await();

                if (directions != null && directions.routes != null && directions.routes.length > 0) {
                    DirectionsRoute route = directions.routes[0];
                    List<LatLng> points = PolyUtil.decode(route.overviewPolyline.getEncodedPath());
                    runOnUiThread(() -> {
                        PolylineOptions lineOptions = new PolylineOptions()
                                .addAll(points)
                                .width(12)
                                .color(ContextCompat.getColor(this, R.color.rouge)) // Changez la couleur ici
                                .geodesic(true);

                        map.addPolyline(lineOptions);
                    });
                    for (int i = 1; i < directions.routes.length; i++) {
                        List<LatLng> alternativePoints = PolyUtil.decode(directions.routes[i].overviewPolyline.getEncodedPath());
                        PolylineOptions alternativeLineOptions = new PolylineOptions();
                        alternativeLineOptions.color(Color.BLUE); // Set a different color for alternative routes
                        alternativeLineOptions.width(5); // Set the width of the alternative polyline
                        for (LatLng point : alternativePoints) {
                            alternativeLineOptions.add(point);
                        }
                        map.addPolyline(alternativeLineOptions);
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(NavigationActivity.this, "Route non trouver", Toast.LENGTH_SHORT).show());
                }
            } catch (ApiException | InterruptedException | IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(NavigationActivity.this, "Error getting directions: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapFragment != null) {
            mapFragment.onDestroy();
        }
        stopBackgroundTasks();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapFragment != null) {
            mapFragment.onStop();
        }
        stopBackgroundTasks();
    }

    private void stopBackgroundTasks() {
        // Disable location updates and clear the map
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(false);
            googleMap.clear();
        }

        // Shutdown the executor service
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }

        // Remove any pending posts from the handler
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapFragment != null) {
            mapFragment.onPause();
        }
        stopBackgroundTasks();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapFragment != null) {
            mapFragment.onLowMemory();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapFragment != null) {
            mapFragment.onResume();
        }
    }

    private void goToHome() {

        if (isVacation) {
            startActivity(HomeActivity.FRAGMENT_DETAIL_VACATION);
        } else {
            startActivity(HomeActivity.FRAGMENT_DETAIL_ACTIVITY);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            boolean isItemSelected = false;
            switch (item.getItemId()) {
                case R.id.home:
                    if (selectedItemId != R.id.home) {
                        goToHome();
                        selectedItemId = R.id.home;
                        isItemSelected = true;
                    }
                    break;
                case R.id.weather:
                    if (selectedItemId != R.id.weather) {
                        startActivity(HomeActivity.FRAGMENT_WEATHER);
                        selectedItemId = R.id.weather;
                        isItemSelected = true;
                    }
                    break;
                case R.id.navigation:
                    if (selectedItemId != R.id.navigation) {
                        selectedItemId = R.id.navigation;
                        isItemSelected = true;
                    }
                    break;
                case R.id.files:
                    if (selectedItemId != R.id.files) {
                        startActivity(HomeActivity.FRAGMENT_DOCUMENT);
                        selectedItemId = R.id.files;
                        isItemSelected = true;
                    }
                    break;
                default:
                    break;
            }
            return isItemSelected;
        });
        // Initialiser le premier élément sélectionné
        bottomNavigationView.setSelectedItemId(R.id.navigation);
        selectedItemId = R.id.navigation;
    }

    private void startActivity(int fragment) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.EXTRA_FRAGMENT, fragment); // Pour ActivityFragment
        intent.putExtra("vacation_id", vacationId);
        intent.putExtra("activity_id", activityId);
        intent.putExtra("is_vacation", isVacation);
        startActivity(intent);

    }
  /*  @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapFragment != null) {
            mapFragment.onSaveInstanceState(outState);
        }
    }*/
}
