package com.potional.myapplication.activities;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.potional.myapplication.R;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private Button startChronometerButton, stopChronometerButton, resetChronometerButton;
    private boolean isChronometerRunning = false;
    private long chronometerPauseOffset = 0;

    private TextView countdownTimerTextView;
    private EditText timerMinutesEditText;
    private EditText timerSecondsEditText;
    private Button startTimerButton, stopTimerButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initChronometer();
        initCountdownTimer();
    }

    private void initChronometer() {
        chronometer = findViewById(R.id.chronometer);
        startChronometerButton = findViewById(R.id.start_chronometer_button);
        stopChronometerButton = findViewById(R.id.stop_chronometer_button);
        resetChronometerButton = findViewById(R.id.reset_chronometer_button);

        startChronometerButton.setOnClickListener(v -> startChronometer());
        stopChronometerButton.setOnClickListener(v -> stopChronometer());
        resetChronometerButton.setOnClickListener(v -> resetChronometer());
    }

    private void startChronometer() {
        if (!isChronometerRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime() - chronometerPauseOffset);
            chronometer.start();
            isChronometerRunning = true;
        }
    }

    private void stopChronometer() {
        if (isChronometerRunning) {
            chronometer.stop();
            chronometerPauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            isChronometerRunning = false;
        }
    }

    private void resetChronometer() {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometerPauseOffset = 0;
        isChronometerRunning = false;
    }

    private void initCountdownTimer() {
        countdownTimerTextView = findViewById(R.id.countdown_timer_text_view);
        timerMinutesEditText = findViewById(R.id.timer_minutes_edit_text);
        timerSecondsEditText = findViewById(R.id.timer_seconds_edit_text);
        startTimerButton = findViewById(R.id.start_timer_button);
        stopTimerButton = findViewById(R.id.stop_timer_button);

        startTimerButton.setOnClickListener(v -> startTimer());
        stopTimerButton.setOnClickListener(v -> stopTimer());
    }

    private void startTimer() {
        if (!isTimerRunning) {
            String minutesStr = timerMinutesEditText.getText().toString();
            String secondsStr = timerSecondsEditText.getText().toString();

            if (TextUtils.isEmpty(minutesStr) && TextUtils.isEmpty(secondsStr)) {
                Toast.makeText(this, "Please enter a duration", Toast.LENGTH_SHORT).show();
                return;
            }

            long minutes = TextUtils.isEmpty(minutesStr) ? 0 : Long.parseLong(minutesStr);
            long seconds = TextUtils.isEmpty(secondsStr) ? 0 : Long.parseLong(secondsStr);

            long durationInMillis = (minutes * 60 + seconds) * 1000;
            if (durationInMillis == 0) {
                Toast.makeText(this, "Duration must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            timeLeftInMillis = durationInMillis;

            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateCountdownText();
                }

                @Override
                public void onFinish() {
                    isTimerRunning = false;
                    updateCountdownText();
                    playSound();
                }
            }.start();

            isTimerRunning = true;
        }
    }

    private void stopTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }

    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countdownTimerTextView.setText(timeLeftFormatted);
    }

    private void playSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}