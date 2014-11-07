package technology.deefactorial.cordova.activityopen;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.replicator.Replication.ReplicationStatus;

import android.util.Log;
import android.os.Bundle;
import technology.deefactorial.cordova.activityopen.reporting;


public class OpenActivity extends CordovaPlugin {
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d("openActivity", "EXECUTING");
        Context context = this.cordova.getActivity();
        //Intent i = new Intent(Intent.ACTION_MAIN);
        Intent i = null;
        if (action.equals("SendErrorReport")) {
        	reporting application = (reporting)context.getApplicationContext();
        	application.sendErrorReport(new Exception(args.toString()));
        } else if(action.equals("NFCSettings")) {
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                i = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        } else {
	            i = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        }
            context.startActivity(i);
        } else if(action.equals("getReplicationStatus")) {
        	try {
        		Manager server = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
				
				JSONArray ResonseArray = new JSONArray(); 
				Collection<Database> databases = server.allOpenDatabases();
				Iterator<Database> it = databases.iterator();
				while(it.hasNext()) {
					Database db = it.next();
					List<Replication> ReplicationList = db.getAllReplications();
					Iterator<Replication> ReplcationIt = ReplicationList.iterator();
					while(ReplcationIt.hasNext()) {
						Replication rp = ReplcationIt.next();
						ReplicationStatus status = rp.getStatus();
						JSONObject cbResponse = new JSONObject();
						cbResponse.put("Database", db.getName());
						cbResponse.put("Replication", rp.getSessionID());
						cbResponse.put("Status",status.name());
						//String response = String.format("{ \"Database\": %s, \"Replication\": %s, \"Status\": %s }", db.getName(), rp.getSessionID(), status.name());
						ResonseArray.put(cbResponse);
					}
				}

				callbackContext.success(ResonseArray.toString());

				return true;
				

			} catch (final Exception e) {
				e.printStackTrace();
				callbackContext.error(e.getMessage());
			}
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