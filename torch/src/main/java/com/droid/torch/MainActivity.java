package com.droid.torch;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends ActionBarActivity  {

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    SurfaceHolder.Callback callback;
    final String LOG_TAG = "MainActivity";
    private Boolean FLASH_ON = false;
    private String FLASH_STATE = "FLASH_STATE";
    private ImageView switch_torch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        */

        //keep screen on
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(savedInstanceState != null){
            FLASH_ON = savedInstanceState.getBoolean(FLASH_STATE);
            Log.i(LOG_TAG,"got saved stuff:: "+FLASH_STATE+"-> "+FLASH_ON);

            switchBoard(null);
        }

        callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        };

        surfaceView = (SurfaceView)findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(callback);
        switch_torch = (ImageView)findViewById(R.id.switch_btn);
    }

    @Override
    public void onResume(){
        try{
            camera = Camera.open();
        }catch(Exception e){
            Log.e(LOG_TAG, e.toString());
        }
        super.onResume();
    }

    @Override
    public void onPause(){
        TurnOff(null);
        camera.release();
        super.onPause();
        switch_torch.setImageResource(R.drawable.btn_switch_off);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putBoolean(FLASH_STATE, FLASH_ON);
        Log.i(LOG_TAG,"saving "+FLASH_STATE+" -> "+FLASH_ON);
        super.onSaveInstanceState(outState);
    }

    public void TurnOn(View view){
        if(FlashAvailable() && camera != null){
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            try{
                camera.setPreviewDisplay(surfaceHolder);
            }catch(IOException e){
                Log.e(LOG_TAG, e.toString());
            }
        }
        redneckery();
        FLASH_ON = true;
    }

    public void redneckery(){
        if(FlashAvailable() && camera != null){
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            try{
                camera.setPreviewDisplay(surfaceHolder);
            }catch(IOException e){
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    public void TurnOff(View view){
        if(FlashAvailable() && camera != null){
            Camera.Parameters params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            FLASH_ON = false;
        }
    }

    public void switchBoard(View view){
        if(FLASH_ON){
            TurnOff(null);
            switch_torch.setImageResource(R.drawable.btn_switch_off);
        }else{
            TurnOn(null);
            switch_torch.setImageResource(R.drawable.btn_switch_on);
        }
    }

    private Boolean FlashAvailable(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
