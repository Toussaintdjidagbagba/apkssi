package mdp.dste.lassi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeListener extends BroadcastReceiver {
    Common common = new Common();
    @Override
    public void onReceive(Context context, Intent intent) {

        if (!common.isConnectedToInternet(context)) {
            Intent n = new Intent(context,MainActivity2.class);
            n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(n);
        }

    }
}
