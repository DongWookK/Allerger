package com.example.allerger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    private final int REQUEST_PERMISSION_CODE = 2222;
    private final int CAMERA_CODE = 1111;
    private final int GALLERY_CODE = 1112;
    private Uri photoUri;
    private String currentPhotoPath;  // 실제 사진 파일 경로
    String mImageCaptureName;  // 이미지 이름

    // 메인 레이아웃 생성 및 실행
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);

        // 권한 요청
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CODE);
            }
        }

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
                selectPhoto();
            }
        });

        // 갤러리 버튼 클릭 후 기능 실행
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGallery();
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

    // 사용자 권한 요청 응답 처리
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast toast=Toast.makeText(this,
                            "동의해주셔서 감사합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast=Toast.makeText(this,
                            "기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
                return;
        }
    }

    // 카메라 기능
    private void selectPhoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_CODE);
                }
            }

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

    // 카메라로 찍은 사진을 결과 창으로 전달
    private void getPictureForPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        setContentView(R.layout.result);
        ImageView imageView = findViewById(R.id.imgview);
        imageView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
        TextView textView = findViewById(R.id.pathId);
        textView.setText(currentPhotoPath);  // 텍스트를 경로로 변경
    }

    // 갤러리 기능 실행
    private void selectGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    // 사진의 회전값 가져오기
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    // 사진을 정방향대로 회전하기
    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    // 결과 창으로 사진 전달
    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        setContentView(R.layout.result);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);  //  경로를 통해 비트맵으로 전환
        ImageView imageView = findViewById(R.id.imgview);
        imageView.setImageBitmap(rotate(bitmap, exifDegree));  //  이미지 뷰에 비트맵 넣기
        TextView textView = findViewById(R.id.pathId);
        textView.setText(imagePath);  // 텍스트를 경로로 변경
    }

    // 각 Intent 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    sendPicture(data.getData());  // 갤러리에서 가져오기
                    break;
                case CAMERA_CODE:
                     getPictureForPhoto();  // 카메라에서 가져오기
                    break;
                default:
                    break;
            }

        }
    }

    // 사진의 절대경로 구하기
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] pic = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, pic, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
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