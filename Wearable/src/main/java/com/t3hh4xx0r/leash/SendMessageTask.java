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
public class SendMessageTask extends AsyncTask<Void, Void, Void> {
    public enum SendMessageType {
        OPEN(0, "open"), FIND_ON(1, "find_phone_on"), FIND_OFF(2, "find_phone_off");
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

    public SendMessageTask(GoogleApiClient mGoogleApiClient, Context ctx, SendMessageType type) {
        this.mGoogleApiClient = mGoogleApiClient;
        this.ctx = ctx;
        this.type = type;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        if (nodes != null && nodes.getNodes() != null) {
            Log.d("THE NODES LIST SIZE", nodes.getNodes().size() + ":");
            for (Node node : nodes.getNodes()) {
                Log.d("SENDING MESSAGE TO NODE FROM WEAR", node.getId() + " : " + type.message);
                Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                        type.message, null);
            }

        } else {
            Log.d("CLIENT NULL", "UNABLE TO SEND HERE");

        }
        return null;
    }
}
