package com.example.arknights;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 1000;
    private WindowManager windowManager;
    private View overlayIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.canDrawOverlays(MainActivity.this)) {
                    createOverlayIcon(); // 오버레이 아이콘 생성
                } else {
                    // 오버레이 권한 요청
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                createOverlayIcon(); // 오버레이 아이콘 생성
            } else {
                Toast.makeText(this, "오버레이 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createOverlayIcon() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        // 오버레이 아이콘의 레이아웃 설정
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.TRANSLUCENT
        );

        layoutParams.gravity = android.view.Gravity.TOP | android.view.Gravity.START;
        layoutParams.x = 0;
        layoutParams.y = 100;

        // 오버레이 아이콘 레이아웃을 설정합니다 (custom_icon_layout.xml을 사용)
        LayoutInflater inflater = LayoutInflater.from(this);
        overlayIcon = inflater.inflate(R.layout.custom_icon_layout, null);

        // 오버레이 아이콘 클릭 리스너
        ImageView iconView = overlayIcon.findViewById(R.id.overlay_icon);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 특정 화면을 띄우기
                Intent intent = new Intent(MainActivity.this, PopUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // 오버레이 아이콘을 화면에 추가
        windowManager.addView(overlayIcon, layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (overlayIcon != null) {
            windowManager.removeView(overlayIcon);
        }
    }
}