package com.example.allerger;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

// 초기화면 및 기본설정에 대한 MainActivity
public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    protected static final int REQUEST_PICK = 0;

    // 메인 레이아웃 생성 및 실행
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);

        // ActionBar의 타이틀 변경
        getSupportActionBar().setTitle("Allerger test version");
        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));

        // 메인 로고 폰트 적용
        TextView textView = findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font_b.ttf");
        textView.setTypeface(typeface);

        // 각 버튼마다 객체 생성
        ImageButton cameraButton = findViewById(R.id.cameraBtn);
        ImageButton galleryButton = findViewById(R.id.galleryBtn);
        Button profileButton = findViewById(R.id.profileBtn);

        // 카메라 버튼 클릭 후 기능 실행
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    // 카메라 실행
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("return-data", true);
                startActivity(intent);
            }
        });

        // 갤러리 버튼 클릭 후 기능 실행
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_PICK);
            }
        });

        // 프로필 생성 버튼 클릭 후 기능 실행
        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "(Profile) 준비중입니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 갤러리에서 고른 사진 photoFile로 저장
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Uri uri = data.getData();
        Cursor cursor = null;
        try{
            cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            File photoFile = new File(cursor.getString(index));
        }catch (Exception e){
            Log.e("_devc",e.getMessage(), e);
        }finally{
            if(cursor != null) cursor.close();
        }
        Toast.makeText(this,"Photo selected!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        startActivity(intent);
    }

    // Menu Inflater 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // menu_id로 구분된 메뉴 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int menu_id = item.getItemId();

        switch(menu_id){
            case R.id.menu_home:
                Toast.makeText(this,"(HOME)",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_notice:
                Toast.makeText(this,"(Notice)",Toast.LENGTH_SHORT).show();
                Intent intent_notice = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivity(intent_notice);
                break;
            case R.id.menu_help:
                Toast.makeText(this,"(HELP)",Toast.LENGTH_SHORT).show();
                Intent intent_help = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent_help);
                break;
            case R.id.menu_settings:
                Toast.makeText(this,"(SETTINGS)",Toast.LENGTH_SHORT).show();
                Intent intent_settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    // 뒤로가기 버튼 눌렀을 경우 Event 실행
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }
}
