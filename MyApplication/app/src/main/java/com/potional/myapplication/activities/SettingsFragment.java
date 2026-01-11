package com.potional.myapplication.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.potional.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityResultLauncher<String> createDocumentLauncher;
    private ActivityResultLauncher<String[]> openDocumentLauncher;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        setupBackupAndRestore();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void setupBackupAndRestore() {
        Preference backupPref = findPreference("backup");
        Preference restorePref = findPreference("restore");

        createDocumentLauncher = registerForActivityResult(new ActivityResultContracts.CreateDocument("application/octet-stream"), uri -> {
            if (uri != null) {
                backupDatabase(uri);
            }
        });

        openDocumentLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) {
                restoreDatabase(uri);
            }
        });

        if (backupPref != null) {
            backupPref.setOnPreferenceClickListener(preference -> {
                createDocumentLauncher.launch(getString(R.string.backup_file_name));
                return true;
            });
        }

        if (restorePref != null) {
            restorePref.setOnPreferenceClickListener(preference -> {
                openDocumentLauncher.launch(new String[]{"application/octet-stream"});
                return true;
            });
        }
    }

    private void backupDatabase(Uri uri) {
        try {
            File dbFile = getContext().getDatabasePath(getString(R.string.database_name));
            try (InputStream in = new FileInputStream(dbFile);
                 OutputStream out = getContext().getContentResolver().openOutputStream(uri)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                Toast.makeText(getContext(), R.string.backup_successful, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), R.string.backup_failed, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void restoreDatabase(Uri uri) {
        try {
            File dbFile = getContext().getDatabasePath(getString(R.string.database_name));
            try (InputStream in = getContext().getContentResolver().openInputStream(uri);
                 OutputStream out = new FileOutputStream(dbFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                Toast.makeText(getContext(), R.string.restore_successful, Toast.LENGTH_LONG).show();
                // Close the app to allow the database to be reloaded
                requireActivity().finishAffinity();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), R.string.restore_failed, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("language")) {
            String language = sharedPreferences.getString(key, "en");
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(language);
            AppCompatDelegate.setApplicationLocales(appLocale);
        } else if (key.equals("theme")) {
            String theme = sharedPreferences.getString(key, "system");
            switch (theme) {
                case "light":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case "dark":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
            }
        }
    }
}
