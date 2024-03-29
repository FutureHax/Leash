package com.t3hh4xx0r.leash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.me;
import com.google.android.gms.wearable.Wearable;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

    GoogleApiClient mGoogleApiClient;
    Ringtone ringTone;
    RingtoneManager ringManager;
    Intent ringIntent;

    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            if (preference.getKey().equals("enable_leash_wear")) {
                boolean booleanValue = Boolean.parseBoolean(value.toString());
                preference.setSummary(booleanValue ? "Notifying on Wear when disconnected" : "Leash disabled on Wear");
            } else if (preference.getKey().equals("enable_leash_phone")) {
                boolean booleanValue = Boolean.parseBoolean(value.toString());
                preference.setSummary(booleanValue ? "Notifying on Phone when disconnected" : "Leash disabled on Phone");
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (preference.getKey().equals("enable_leash_phone")) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), true)
            );
        }

        if (preference.getKey().equals("enable_leash_wear")) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), true)
            );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        if (ringTone != null && ringTone.isPlaying()) {
            ringTone.stop();
        }
    }

    public void handleRingtoneUri() {
        String uriString = PreferenceManager.getDefaultSharedPreferences(this).getString("notification_tone", "null");
        if (!uriString.equals("null")) {
            ringTone = ringManager.getRingtone(this, Uri.parse(uriString));
            findPreference("notification").setSummary(ringTone.getTitle(this));
        } else {
            findPreference("notification").setSummary("");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int id, MenuItem item) {
        int ID = item.getItemId();

        if (ID == R.id.menu_contact) {
            showFeedbackChooser();
        }
        return super.onMenuItemSelected(id, item);
    }

    public void showFeedbackChooser() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String sAux = "Check out this amazing application! ";
                sAux = sAux
                        + "https://play.google.com/store/apps/details?id=com.t3hh4xx0r.leash \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Share via..."));
            }
        });
        b.setNegativeButton("Send Feedback", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent emailIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { "r2doesinc@futurehax.com" });
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Leash Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send via..."));
            }
        });
        b.create().show();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ringManager = new RingtoneManager(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        findPreference("enable_leash_wear").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                new PhoneSendMessageTask(mGoogleApiClient, preference.getContext(), Boolean.parseBoolean(newValue.toString()) ? PhoneSendMessageTask.SendMessageType.ENABLE_WEAR_LEASH : PhoneSendMessageTask.SendMessageType.DISABLE_WEAR_LEASH).execute();
                                sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, newValue);
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        mGoogleApiClient.connect();
        setupSimplePreferencesScreen();

        findPreference("find_wear").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(preference.getContext(), PhoneSendMessageService.class);
                i.setAction(PhoneSendMessageService.ACTION_REMOTE_ALARM_ON);
                startService(i);
                return false;
            }
        });

        findPreference("notification").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ringIntent = new Intent(ringManager.ACTION_RINGTONE_PICKER);
                ringIntent.putExtra(ringManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                ringIntent.putExtra(ringManager.EXTRA_RINGTONE_TITLE, "Select a notification tone");

                String uri = null;
                if (uri != null) {
                    ringIntent.putExtra(ringManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(uri));
                } else {
                    ringIntent.putExtra(ringManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                }

                startActivityForResult(ringIntent, 0);
                return false;
            }
        });

        handleRingtoneUri();

    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("enable_leash_phone"));
        bindPreferenceSummaryToValue(findPreference("enable_leash_wear"));
    }

    private void play(Uri uri) {
        if (uri != null) {
            ringTone = ringManager.getRingtone(this, uri);
            ringTone.play();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (resultCode) {
            case RESULT_OK:
                Uri uri = intent.getParcelableExtra(ringManager.EXTRA_RINGTONE_PICKED_URI);

                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("notification_tone", uri == null ? "null" : uri.toString()).commit();
                handleRingtoneUri();
                //play(uri);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }
}
