package com.example.allerger;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.media.ExifInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.support.design.widget.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab1, fab2, fab3;

    // REQUEST CODE
    private final int REQUEST_PERMISSION_CODE = 1111;
    private final int CAMERA_CODE = 2222;
    private final int GALLERY_CODE = 3333;
    private Uri photoUri;

    // PHOTO PATH
    private String currentPhotoPath;
    private String imagePath;
    String mImageCaptureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 최초 레이아웃 실행
        setContentView(R.layout.activity_main);

        // 권한 체크
        checkPermission();

        // Toolbar 실행
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FloatingActionButton 이벤트 처리
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim();
            }
        });
        fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
        fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGallery();
            }
        });

        // DrawerLayout 실행 및 관리
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation 이벤트 리스너
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TabLayout 실행
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tab_main));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tab_list));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tab_search));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // 스와이프에 대한 ViewPage, tabLayout 간 동기화
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // 수동 선택에 대한 ViewPage, tabLayout 간 동기화
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // 뒤로가기 버튼 처리
    @Override
    public void onBackPressed() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        WebView webView = findViewById(R.id.webView);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(isFabOpen){
            anim();
        } else if(tabLayout.getSelectedTabPosition() == 2){
            if(webView.canGoBack()) {
                try {
                    webView.goBack();
                } catch (Exception e) {
                }
            } else{
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    // Navigation 이벤트 처리
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            selectPhoto();
        } else if (id == R.id.nav_gallery) {
            selectGallery();
        } else if (id == R.id.nav_settings) {
            Intent intent_settings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent_settings);
        } else if(id == R.id.nav_help){
            Intent intent_help = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent_help);
        } else if(id == R.id.nav_policy){
            Intent intent_policy = new Intent(getApplicationContext(), PolicyActivity.class);
            startActivity(intent_policy);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 사용자 권한 요청
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE);
            }
        }
    }

    // 사용자 권한 요청 응답 처리
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(MainActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용 되었을 경우
                Toast.makeText(MainActivity.this, "권한이 허용 되었습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // 카메라 기능
    private void selectPhoto() {
        String state = Environment.getExternalStorageState();
        // 외장메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("selectPhoto Error", ex.toString());
                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, "com.example.allerger", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_CODE);
                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // 카메라로 찍은 사진 파일 생성
    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/"
                + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;
    }

    // 갤러리 기능 실행
    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);

    }

    // 각 Intent 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_CODE:
                    if (data != null) {
                        Uri imgUri = data.getData();
                        imagePath = getRealPathFromURI(imgUri); // path 경로
                        Intent intent_result_gallery = new Intent(MainActivity.this, ResultActivity.class);
                        intent_result_gallery.putExtra("path", imagePath);
                        startActivity(intent_result_gallery);
                    }
                    break;

                case CAMERA_CODE:
                    if (data != null) {
                        Intent intent_result_camera = new Intent(MainActivity.this, ResultActivity.class);
                        intent_result_camera.putExtra("path", currentPhotoPath);
                        startActivity(intent_result_camera);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    // 사진의 절대경로 구하기
    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] pic = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, pic, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    // 플로팅 버튼 애니메이션
    public void anim(){
        if(isFabOpen){
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
            fab1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_o)));
            fab1.setImageDrawable(getDrawable(R.drawable.ic_tab_add));
        }else{
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
            fab1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_c)));
            fab1.setImageDrawable(getDrawable(R.drawable.ic_fab_close));
        }
    }
}