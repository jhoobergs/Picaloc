package be.tomberndjesse.picaloc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by jesse on 17/09/2016.
 */
public class ReceiverCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Service Stops", "Ohhhhhhh");
        context.startService(new Intent(context, LocationService.class));;
    }

}

