package com.Allergerapp.allerger;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

  private final String dbName = "webnautes";// DBNAME
  //private String tableName = "Allerger_table";
  public String tableName;

  Bitmap imageBitmap;
  ImageView imageView;

  private String Food_E[];

  {
    Food_E = new String[]{"bean", "egg", "shrimp", "peach", "kiwi", "flour", "peanut", "fish", "tomato",
        "almond", "melon", "walnut", "hamburger", "cheese", "salmon", "crap", "wheat", "chocolate", "butter", "cocoa", "canola", "soy", "albumin", "mayonnaise", "globulin",
        "ovalbumin", "ovomucin", "ovomucoid", "livetin", "baked", "lecithin", "macaroni", "nougat", "pasta",
        "cream", "casein", "curds", "custard", "ghee", "lacto", "pudding", "yogurt", "margarine", "miso", "sprouts", "tofu", "nuts", "cashews", "hazelnuts", "ginko", "chestnut",
        "lichi", "lichee", "lychee", "pecans", "pine", "pistachio", "langoustine", "langouste", "scampo", "tomalley", "abalone"
        , "clams", "mussel", "octopus", "oyster", "snail", "escargot", "surimi", "abocado", "banana"};
  }

  private final String Ingredient_E[];// COMPARE WITH NAMES THEN SHOW TO USER THE KOREAN

  {
    Ingredient_E = new String[]{"견과류알러지(땅콩)", "계란 알러지(달걀)", "갑각류 알러지(새우)", "과일 알러지(복숭아)", "과일 알러지(키위)", "밀가루 알러지(밀)", "견과류 알러지(땅콩)", "어패류 알러지(물고기)", "과일 알러지(토마토)", "견과류 알러지(아몬드)", "과일 알러지(멜론)", "견과류 알러지(호두)",
        "유제품 알러지(햄버거)", "유제품 알러지(치즈)", "어패류 알러지(연어)", "갑각류 알러지(게)", "밀가루 알러지(밀)", "유제품 알러지(초콜렛)", "유제품 알러지(버터)", "코코아 알러지", " 콩 알러지(카놀라)", "콩 알러지(된장)", "계란 알러지", "계란 알러지(마요네즈 함유)", "계란 알러지",
        "계란관련 알러지", "계란관련 알러지", "계란관련 알러지", "계란관련 알러지", "계란관련 알러지(빵)", "계란관련 알러지", "계란관련 알러지", "계란관련 알러지(누가초콜릿)",
        "계란관련 알러지(파스타면)", "유제품 알러지(크림)", "유제품 알러지(카제인)", "유제품 알러지", "유제품 알러지(커스타드)", "유제품 알러지(버터오일)", "유제품 알러지",
        "유제품 알러지(푸딩)", "유제품 알러지(요거트)", "유제품 알러지(마가린)", "콩 알러지(미소)", "콩 알러지(콩나물)", "콩 알러지(두부)", "견과류 알러지", "견과류 알러지(캐슈넛)", "견과류 알러지(헤이즐넛)"
        , "견과류 알러지(은행)", "견과류 알러지(밤)", "견과류 알러지(리찌넛)", "견과류 알러지(리찌넛)", "견과류 알러지(리찌넛)", "견과류 알러지(피칸)", "견과류 알러지(잣)", "견과류 알러지(피스타치오)",
        "갑각류 알러지(랍스터)", "갑각류 알러지(랍스터)", "갑각류 알러지(랍스터)", "갑각류 알러지(랍스터)", "갑각류 알러지(전복)", "갑각류 알러지(조개)", "갑각류 알러지(홍합)", "해산물 알러지(문어)",
        "갑각류 알러지(굴)", "달팽이", "달팽이", "맛살", "아보카도", "바나나"};
  }

  private String Food_F[];

  {
    Food_F = new String[]{"fèves", "eggufs", "crevettes", "pêche", "kiwi", "farine", "arachide", "poisson", "tomate",
        "al", "melon", "walnut", "hamburger", "fromage", "saumon", "crap", "blé", "chocolat", "beurre", "cacao", "canola", "soja", "albumine", "mayonnaise", "globuline",
        "ovalbumine", "ovomucine", "ovomucoïde", "livetin", "aufour", "lécithine", "macaroni", "nougat", "pâtes",
        "crème", "caséine", "cur", "custard", "ghee", "lacto", "pudding", "yogourt", "margarine", "miso", "lesgermes", "tofu", "noix", "cajou", "noisettes", "ginko", "marron",
        "lichi", "lichee", "lychee", "pacanes", "pin", "pistachio", "pistaches", "langouste", "scampo", "tomalley", "oral"
        , "myes", "moules", "pieuvre", "huître", "snail", "escargot", "ron", "abocado", "banane"};
  }


  private final String Ingredient_F[];// COMPARE WITH NAMES THEN SHOW TO USER THE KOREAN

  {
    Ingredient_F = new String[]{"견과류알러지(땅콩)", "계란 알러지(달걀)", "갑각류 알러지(새우)", "과일 알러지(복숭아)", "과일 알러지(키위)", "밀가루 알러지(밀)", "견과류 알러지(땅콩)", "어패류 알러지(물고기)", "과일 알러지(토마토)", "견과류 알러지(아몬드)", "과일 알러지(멜론)", "견과류 알러지(호두)",
        "유제품 알러지(햄버거)", "유제품 알러지(치즈)", "어패류 알러지(연어)", "갑각류 알러지(게)", "밀가루 알러지(밀)", "유제품 알러지(초콜렛)", "유제품 알러지(버터)", "코코아 알러지", " 콩 알러지(카놀라)", "콩 알러지(된장)", "계란 알러지", "계란 알러지(마요네즈 함유)", "계란 알러지",
        "계란관련 알러지", "계란관련 알러지", "계란관련 알러지", "계란관련 알러지", "계란관련 알러지(빵)", "계란관련 알러지", "계란관련 알러지", "계란관련 알러지(누가초콜릿)",
        "계란관련 알러지(파스타면)", "유제품 알러지(크림)", "유제품 알러지(카제인)", "유제품 알러지", "유제품 알러지(커스타드)", "유제품 알러지(버터오일)", "유제품 알러지",
        "유제품 알러지(푸딩)", "유제품 알러지(요거트)", "유제품 알러지(마가린)", "콩 알러지(미소)", "콩 알러지(콩나물)", "콩 알러지(두부)", "견과류 알러지", "견과류 알러지(캐슈넛)", "견과류 알러지(헤이즐넛)"
        , "견과류 알러지(은행)", "견과류 알러지(밤)", "견과류 알러지(리찌넛)", "견과류 알러지(리찌넛)", "견과류 알러지(리찌넛)", "견과류 알러지(피칸)", "견과류 알러지(잣)", "견과류 알러지(피스타치오)",
        "갑각류 알러지(랍스터)", "갑각류 알러지(랍스터)", "갑각류 알러지(랍스터)", "갑각류 알러지(랍스터)", "갑각류 알러지(전복)", "갑각류 알러지(조개)", "갑각류 알러지(홍합)", "해산물 알러지(문어)",
        "갑각류 알러지(굴)", "달팽이", "달팽이", "맛살", "아보카도", "바나나"};
  }


  ArrayList<HashMap<String, String>> AllergyList; //DB CODE
  ListView list;
  private static final String TAG_FOOD = "Food";
  private static final String TAG_INGREDIENT = "Allergy";

  SQLiteDatabase sampleDB = null;
  ListViewAdapter adapter;

  Bitmap image; // 사용되는 이미지
  private TessBaseAPI mTess; // Tess API reference
  String datapath = ""; // 언어데이터가 있는 경로

 
  // 오류시 show list 지우기
  protected void showList(String Clearing) {
    try {

      SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
      // SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
      Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);
      if (c != null) {
        if (c.moveToFirst()) {
          do {

            String Name = c.getString(c.getColumnIndex("Food"));
            String Phone = c.getString(c.getColumnIndex("Ingredient"));
            HashMap<String, String> Allergy = new HashMap<String, String>();
            int pic_id = 0;
            if (Clearing.contains(Name)) {

              if (Phone.contains("계란")) {
                pic_id = R.drawable.eggs;
              } else if (Phone.contains("유제품")) {
                pic_id = R.drawable.milk;
              } else if (Phone.contains("콩")) {
                pic_id = R.drawable.soya;
              } else if (Phone.contains("견과류")) {
                pic_id = R.drawable.peanut_red_109453;
              } else if (Phone.contains("어패류")) {
                pic_id = R.drawable.fishpic;
              } else if (Phone.contains("갑각류")) {
                pic_id = R.drawable.jogaepic;
              } else {
                pic_id = R.drawable.sesame;
              }

              adapter.addItem(ContextCompat.getDrawable(this, pic_id), Name, Phone);


            }


            // HashMap에 넣습니다.

          } while (c.moveToNext());
        }
      }
      ReadDB.close();

      // 새로운 apapter를 생성하여 데이터를 넣은 후..
           /* adapter = new SimpleAdapter(
                    this, personList, R.layout.list_item,
                    new String[]{TAG_NAME,TAG_PHONE},
                    new int[]{ R.id.name, R.id.phone}
            );*/

      // 화면에 보여주기 위해 Listview에 연결합니다.
      list.setAdapter(adapter);

    } catch (SQLiteException se) {
      Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("", se.getMessage());
    }

  }

  public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter() {

    }

    public int getCount() {
      return listViewItemList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      final int pos = position;
      final Context context = parent.getContext();

      if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item, parent, false);
      }


      TextView nameTextView = (TextView) convertView.findViewById(R.id.name);

      TextView descTextView = (TextView) convertView.findViewById(R.id.phone);

      ImageView iconImageView = (ImageView) convertView.findViewById(R.id.image_pic);

      ListViewItem listViewItem = listViewItemList.get(position);


      nameTextView.setText(listViewItem.getTitle());
      descTextView.setText(listViewItem.getDesc());
      iconImageView.setImageDrawable(listViewItem.getIcon());

      return convertView;

    }

    public long getItemId(int position) {
      return position;
    }

    public Object getItem(int position) {
      return listViewItemList.get(position);
    }

    public void addItem(Drawable icon, String title, String desc) {
      ListViewItem item = new ListViewItem();

      item.setIcon(icon);
      item.setTitle(title);
      item.setDesc(desc);

      listViewItemList.add(item);
    }
  }

  public class ListViewItem {
    private Drawable iconDrawable;
    private String titleStr;
    private String descStr;

    public void setIcon(Drawable icon) {
      iconDrawable = icon;
    }

    public void setTitle(String title) {
      titleStr = title;
    }

    public void setDesc(String desc) {
      descStr = desc;
    }

    public Drawable getIcon() {
      return this.iconDrawable;
    }

    public String getTitle() {
      return this.titleStr;
    }

    public String getDesc() {
      return this.descStr;
    }
  }
}