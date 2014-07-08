package com.t3hh4xx0r.leash;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by r2doesinc on 6/30/14.
 */
public class PhoneSendMessageTask extends AsyncTask<Void, Void, Void> {
    public enum SendMessageType {
        OPEN(0, "open"), FIND_ON(1, "find_wear_on"), FIND_OFF(2, "find_wear_off"), ENABLE_WEAR_LEASH(3, "enable_leash"), DISABLE_WEAR_LEASH(4, "disable_leash"), DISMISS_WEAR_NOTIFICATION(5, "dismiss") ;
        int type;
        String message;

        SendMessageType(int type, String message) {
            this.message = message;
            this.type = type;
        }
    }

    GoogleApiClient mGoogleApiClient;
    Context ctx;
    SendMessageType type;

    public PhoneSendMessageTask(GoogleApiClient mGoogleApiClient, Context ctx, SendMessageType type) {
        this.mGoogleApiClient = mGoogleApiClient;
        this.ctx = ctx;
        this.type = type;
    }

    @Override
    protected Void doInBackground(Void... params) {
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
            Log.d("SENDING MESSAGE TO NODE FROM PHONE", node.getId() + " : " + type.message);
            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                    type.message, null);
        }
        return null;
    }
}
