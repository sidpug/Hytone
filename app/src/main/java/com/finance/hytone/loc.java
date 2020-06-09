package com.finance.hytone;

public class loc {

}
/*
private FusedLocationProviderClient fusedLocationClient;
private boolean globalpermission = false;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //to proceed start location update
        findViewById(R.id.someButton).setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        globalpermission = Helper.checkPermissions(HomeActivity.this);
        if (globalpermission) {
        if (fusedLocationClient == null || !Helper.isLocationEnabled(getApplicationContext())){
        Helper.showDialog(HomeActivity.this,true,"" ,"Please enable location!");
        createLocationRequest();
        }
        else {

        callStartTripApi();
        }
        }
        else{
        Toast.makeText(HomeActivity.this, "Please provide location permission to proceed!", Toast.LENGTH_LONG).show();
        }

        }
        });

        }

public void onRequestPermissionsResult (int requestCode,
        String[] permissions,
        int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        boolean permissionAccessCoarseLocationApproved =
        ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED;

        globalpermission = permissionAccessCoarseLocationApproved;
        if (permissionAccessCoarseLocationApproved) {
        Log.e("has permission 2","starting");
        activateHandlerAfter(5);
        // App has permission to access location in the foreground. Start your
        // foreground service that has a foreground service type of "location".
        //startService();
        if (!Helper.isLocationEnabled(getApplicationContext()))
        createLocationRequest();
        } else {
        Toast.makeText(this, "Location permission is required to proceed", Toast.LENGTH_LONG).show();
        }

        }
public void startService() {
        Log.e("starting0","srvc");
        Intent serviceIntent = new Intent(this, MyLocationService.class);
        serviceIntent.putExtra("inputExtra", "App is running in foreground");
        ContextCompat.startForegroundService(this, serviceIntent);
        }
public void stopService() {
        Intent serviceIntent = new Intent(this, MyLocationService.class);
        stopService(serviceIntent);
        }
protected void createLocationRequest() {
final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(Constants.LOC_UPDATE_INTERVAL * 1000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
@Override
public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        // All location settings are satisfied. The client can initialize
        // location requests here.
        // ...
        globalLocationRequestStatus = 1;
        Log.e("hi","onsuccess");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
        Task<Location> fgl = fusedLocationClient.getLastLocation();

        fgl.addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
@Override
public void onSuccess(Location location) {
        // Got last known location. In some rare situations this can be null.
        globalLocationStatus = 1;
        globalLocationData = ""+location;

        if (location != null) {
        // Logic to handle location object
        Log.e("loca",location.getLatitude()+","+location.getLongitude()+","+location.getAccuracy());
        }
        }
        });
        fgl.addOnFailureListener(new OnFailureListener() {
@Override
public void onFailure(@NonNull Exception e) {
        globalLocationStatus = 0;
        globalLocationError = e.toString();
        }
        });

        if (pendingServiceStart)
        {
        pendingServiceStart = false;
        startService();
        }
        //fusedLocationClient.removeLocationUpdates(locationCallback);

        }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
@Override
public void onFailure(@NonNull Exception e) {
        globalLocationRequestStatus = 1;
        globalLocationRequestError = ""+ e;
        if (e instanceof ResolvableApiException) {
        // Location settings are not satisfied, but this can be fixed
        // by showing the user a dialog.
        Log.e("hi2","onFail");

        try {
        if (pendingServiceStart)
        Toast.makeText(HomeActivity.this, "Please enable location to continue2!", Toast.LENGTH_LONG).show();

        // Show the dialog by calling startResolutionForResult(),
        // and check the result in onActivityResult().
        ResolvableApiException resolvable = (ResolvableApiException) e;
        resolvable.startResolutionForResult(HomeActivity.this,
        Constants.REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException sendEx) {
        // Ignore the error.
        globalLocationRequestError = "2ndError "+ sendEx;
        }
        }
        }
        });
        }*/