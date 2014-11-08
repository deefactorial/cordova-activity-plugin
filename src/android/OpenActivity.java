package technology.deefactorial.cordova.activityopen;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.cblite.phonegap.CBLite;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.replicator.Replication.ChangeEvent;
import com.couchbase.lite.replicator.Replication.ChangeListener;
import com.couchbase.lite.replicator.Replication.ReplicationStatus;
import com.couchbase.lite.replicator.ReplicationState;

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
        	MyRunnable obj = new MyRunnable(context, callbackContext);
        	ExecutorService ex = this.cordova.getThreadPool();
        	ex.execute(obj);
        	
        } else if(action.equals("setReplicationChangeListener")) {

        	
        	try {
	        	
        		ChangeListener listner = new ChangeListener() {
	  				
	  				@Override
	  				public void changed(ChangeEvent event) {
	  					if (event.getTransition() != null ) {
	  						System.out.println("setReplicationChangeListener Transition Source:" + event.getTransition().getSource() + ", Destination:" + event.getTransition().getDestination() );
	  					}
	  				}
	  			};
	  			
	    		Manager server = CBLite.getManager();
	
				List<String> names = server.getAllDatabaseNames();
				
				Iterator<String> namesIt = names.iterator();
				while(namesIt.hasNext()){
					String name = namesIt.next();
					System.out.println("setReplicationChangeListener database name:" + name);
					Database db = server.getExistingDatabase(name);
					
					List<Replication> ReplicationList = db.getAllReplications();
					System.out.println("setReplicationChangeListener Replication size:" + ReplicationList.size());
					Iterator<Replication> ReplcationIt = ReplicationList.iterator();
					while(ReplcationIt.hasNext()) {
						Replication rp = ReplcationIt.next();
						
						rp.addChangeListener(listner);
					}
				}
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
    
    private class MyRunnable implements Runnable {
  	  private CallbackContext callbackContext;
  	  private Context context;
  	  public MyRunnable(Context _context, CallbackContext _callbackContext) {
  		this.context = _context;
  	    this.callbackContext = _callbackContext;
  	  }

  	  public void run() {
  		try {
  			  			
  			
    		Manager server = CBLite.getManager();
			
			JSONArray ResonseArray = new JSONArray(); 

			List<String> names = server.getAllDatabaseNames();
			
			Iterator<String> namesIt = names.iterator();
			while(namesIt.hasNext()){
				String name = namesIt.next();
				System.out.println("getReplicationStatus database name:" + name);
				Database db = server.getExistingDatabase(name);
				
				List<Replication> ReplicationList = db.getAllReplications();
				System.out.println("getReplicationStatus Replication size:" + ReplicationList.size());
				Iterator<Replication> ReplcationIt = ReplicationList.iterator();
				while(ReplcationIt.hasNext()) {
					Replication rp = ReplcationIt.next();
					ReplicationStatus status = rp.getStatus();

					JSONObject cbResponse = new JSONObject();
					cbResponse.put("Database", db.getName());
					cbResponse.put("Replication", rp.getSessionID());
					cbResponse.put("Status",status.name());
					cbResponse.put("changeCount", rp.getChangesCount());
					cbResponse.put("completedChangeCount",rp.getCompletedChangesCount());
					
					//String response = String.format("{ \"Database\": %s, \"Replication\": %s, \"Status\": %s }", db.getName(), rp.getSessionID(), status.name());
					ResonseArray.put(cbResponse);
				}
			}

			callbackContext.success(ResonseArray);
			

		} catch (final Exception e) {
			e.printStackTrace();
			callbackContext.error(e.getMessage());
		}
  	  }
  }
}

