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
import com.openmoney.mobile.reporting;


public class OpenActivity extends CordovaPlugin {
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d("openActivity", "EXECUTING");
        Context context = this.cordova.getActivity().getApplicationContext();
        //Intent i = new Intent(Intent.ACTION_MAIN);
        Intent i = null;
        if (action.equals("SendErrorReport")) {
        	reporting application = this.cordova.getActivity().getApplication();
        	application.sendErrorReport(null);
        } else if(action.equals("NFCSettings")) {
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                i = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        } else {
	            i = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        }
            context.startActivity(i);
        } else {
        	i = new Intent(action);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(i);
        }
        
        //PackageManager manager = context.getPackageManager();
        //i = manager.getLaunchIntentForPackage(action);
        //i.addCategory(Intent.CATEGORY_LAUNCHER);
        
        return true;
    }

}