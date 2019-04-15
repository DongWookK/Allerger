package com.example.allerger;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

// 공지사항 출력에 대한 NoticeActivity
public class NoticeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.notice);
        // ActionBar의 타이틀 변경
        getSupportActionBar().setTitle("Notice");
        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
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
                finish();
                break;
            case R.id.menu_notice:
                Toast.makeText(this,"(Notice)",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_help:
                Toast.makeText(this,"(HELP)",Toast.LENGTH_SHORT).show();
                finish();
                Intent intent_help = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent_help);
                break;
            case R.id.menu_settings:
                Toast.makeText(this,"(SETTINGS)",Toast.LENGTH_SHORT).show();
                finish();
                Intent intent_settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent_settings);
        }
        return super.onOptionsItemSelected(item);
    }
}