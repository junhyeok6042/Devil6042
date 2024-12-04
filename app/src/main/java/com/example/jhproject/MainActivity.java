package com.example.jhproject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    LinearLayout timeCountSettingLV, timeCountLV;
    EditText hourET, minuteET, secondET;
    TextView hourTV, minuteTV, secondTV, finishTV;
    Button startBtn, resetBtn;
    int hour, minute, second;
    Timer timer;
    TimerTask timerTask;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View 연결
        timeCountSettingLV = findViewById(R.id.timeCountSettingLV);
        timeCountLV = findViewById(R.id.timeCountLV);

        hourET = findViewById(R.id.hourET);
        minuteET = findViewById(R.id.minuteET);
        secondET = findViewById(R.id.secondET);

        hourTV = findViewById(R.id.hourTV);
        minuteTV = findViewById(R.id.minuteTV);
        secondTV = findViewById(R.id.secondTV);
        finishTV = findViewById(R.id.finishTV);

        startBtn = findViewById(R.id.startBtn);
        resetBtn = findViewById(R.id.resetBtn);

        handler = new Handler();

        // 시작 버튼 이벤트
        startBtn.setOnClickListener(view -> {
            // 초기 설정 숨기기
            timeCountSettingLV.setVisibility(View.GONE);
            timeCountLV.setVisibility(View.VISIBLE);

            // 입력값 가져오기 (비어 있으면 0으로 처리)
            String hourInput = hourET.getText().toString().trim();
            String minuteInput = minuteET.getText().toString().trim();
            String secondInput = secondET.getText().toString().trim();

            hour = hourInput.isEmpty() ? 0 : Integer.parseInt(hourInput);
            minute = minuteInput.isEmpty() ? 0 : Integer.parseInt(minuteInput);
            second = secondInput.isEmpty() ? 0 : Integer.parseInt(secondInput);

            // 모든 값이 0인 경우 타이머 실행하지 않음
            if (hour == 0 && minute == 0 && second == 0) {
                finishTV.setText("시간을 입력하세요.");
                return;
            }

            // 텍스트뷰에 입력값 반영
            hourTV.setText(hour <= 9 ? "0" + hour : String.valueOf(hour));
            minuteTV.setText(minute <= 9 ? "0" + minute : String.valueOf(minute));
            secondTV.setText(second <= 9 ? "0" + second : String.valueOf(second));

            // 기존 타이머가 있으면 취소
            if (timer != null) {
                timer.cancel();
            }

            // 새로운 타이머 시작
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (second != 0) {
                        second--;
                    } else if (minute != 0) {
                        second = 59;
                        minute--;
                    } else if (hour != 0) {
                        second = 59;
                        minute = 59;
                        hour--;
                    }

                    // UI 업데이트
                    handler.post(() -> {
                        // 시간, 분, 초 표시
                        hourTV.setText(hour <= 9 ? "0" + hour : String.valueOf(hour));
                        minuteTV.setText(minute <= 9 ? "0" + minute : String.valueOf(minute));
                        secondTV.setText(second <= 9 ? "0" + second : String.valueOf(second));

                        // 타이머 종료 처리
                        if (hour == 0 && minute == 0 && second == 0) {
                            timer.cancel();
                            finishTV.setText("물 마실 시간입니다.");

                            // 3초 후 초기 화면으로 돌아가기
                            handler.postDelayed(() -> {
                                finishTV.setText("");
                                resetToInitialState();
                            }, 3000);
                        }
                    });
                }
            };

            timer.schedule(timerTask, 0, 1000);
        });

        // 재설정 버튼 이벤트
        resetBtn.setOnClickListener(view -> resetToInitialState());
    }

    // 초기 화면으로 복원하는 메서드
    private void resetToInitialState() {
        if (timer != null) {
            timer.cancel();
        }

        timeCountSettingLV.setVisibility(View.VISIBLE);
        timeCountLV.setVisibility(View.GONE);
        finishTV.setText("");

        hourET.setText("");
        minuteET.setText("");
        secondET.setText("");

        hour = 0;
        minute = 0;
        second = 0;
    }
}