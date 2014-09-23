package technology.deefactorial.cordova.activityopen;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.os.Bundle;



public class OpenActivity extends CordovaPlugin {
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d("openActivity", "EXECUTING");
        Context context = this.cordova.getActivity().getApplicationContext();
        //Intent i = new Intent(Intent.ACTION_MAIN);
        Intent i = null;
        if(action == "NfcSettings") {
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                i = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
	        } else {
	            i = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	        }
        } else {
        	i = new Intent(action);
        }
        
        //PackageManager manager = context.getPackageManager();
        //i = manager.getLaunchIntentForPackage(action);
        //i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;
    }

}