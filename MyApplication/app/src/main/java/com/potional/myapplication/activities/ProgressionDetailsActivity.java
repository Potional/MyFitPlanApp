package com.potional.myapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.potional.myapplication.R;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.Progression;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgressionDetailsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;

    private TextView dateTextView;
    private EditText weightEditText, musclePercentEditText, fatPercentEditText;
    private Button selectPhotoButton, saveProgressionButton;
    private ImageView photoImageView;

    private AppDatabaseDao database;
    private ExecutorService executor;
    private String currentDate;
    private Uri photoUri;
    private boolean isToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progression_details);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();

        long dateInMillis = getIntent().getLongExtra("date", 0);
        isToday = getIntent().getBooleanExtra("is_today", false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = sdf.format(new Date(dateInMillis));

        initViews();
        loadProgressionData();
    }

    private void initViews() {
        dateTextView = findViewById(R.id.progression_date_text_view);
        weightEditText = findViewById(R.id.weight_edit_text);
        musclePercentEditText = findViewById(R.id.muscle_percent_edit_text);
        fatPercentEditText = findViewById(R.id.fat_percent_edit_text);
        selectPhotoButton = findViewById(R.id.select_photo_button);
        saveProgressionButton = findViewById(R.id.save_progression_button);
        photoImageView = findViewById(R.id.photo_image_view);

        dateTextView.setText(currentDate);

        if (!isToday) {
            setReadOnly(weightEditText);
            setReadOnly(musclePercentEditText);
            setReadOnly(fatPercentEditText);
            selectPhotoButton.setVisibility(View.GONE);
            saveProgressionButton.setVisibility(View.GONE);
        } else {
            selectPhotoButton.setOnClickListener(v -> showPhotoDialog());
            saveProgressionButton.setOnClickListener(v -> saveProgression());
        }
    }

    private void setReadOnly(EditText editText) {
        editText.setFocusable(false);
        editText.setClickable(false);
        editText.setFocusableInTouchMode(false);
        editText.setEnabled(false);
    }

    private void showPhotoDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Choose Photo")
                .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        dispatchTakePictureIntent();
                    } else {
                        openFileChooser();
                    }
                })
                .show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle error
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.potional.myapplication.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                photoUri = data.getData();
            } else if (requestCode == TAKE_PHOTO_REQUEST) {
                // Photo already saved to photoUri
            }
            photoImageView.setImageURI(photoUri);
        }
    }

    private void loadProgressionData() {
        executor.execute(() -> {
            Progression progression = database.progressionDao().getProgressionByDate(currentDate);
            runOnUiThread(() -> {
                if (progression != null) {
                    weightEditText.setText(String.valueOf(progression.getWeight()));
                    musclePercentEditText.setText(String.valueOf(progression.getMusclePercent()));
                    fatPercentEditText.setText(String.valueOf(progression.getFatPercent()));
                    if (progression.getPhotoUri() != null) {
                        photoUri = Uri.parse(progression.getPhotoUri());
                        photoImageView.setImageURI(photoUri);
                    }
                }
            });
        });
    }

    private void saveProgression() {
        String weightStr = weightEditText.getText().toString();
        String musclePercentStr = musclePercentEditText.getText().toString();
        String fatPercentStr = fatPercentEditText.getText().toString();

        if (TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(musclePercentStr) || TextUtils.isEmpty(fatPercentStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double musclePercent = Double.parseDouble(musclePercentStr);
        double fatPercent = Double.parseDouble(fatPercentStr);

        Progression newProgression = new Progression();
        newProgression.setDate(currentDate);
        newProgression.setWeight(weight);
        newProgression.setMusclePercent(musclePercent);
        newProgression.setFatPercent(fatPercent);
        if (photoUri != null) {
            newProgression.setPhotoUri(photoUri.toString());
        }

        executor.execute(() -> {
            database.progressionDao().insert(newProgression);
            runOnUiThread(() -> {
                Toast.makeText(this, "Progression saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}