package fina.com.jxlmvn;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    private DBhelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        importSheet();
        dbHelper = new DBhelper(this, "TEST.db", null, 1);
        contentValues = new ContentValues();
    }

    private void importSheet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getResources().getAssets().open("vip.xls");
                    Workbook workbook = Workbook.getWorkbook(is);
//                    getsheet in workbook(excel)
                    Sheet sheet = workbook.getSheet(0);
                    for (int j = 0; j < sheet.getRows(); j++) {
//                       method getCell(col,row);
                        init(sheet.getCell(0, j).getContents(), sheet.getCell(1, j).getContents());
                    }
                    sqLiteDatabase.close();
                    dbHelper.close();
                    workbook.close();
                } catch (IOException | BiffException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void init(String author, String price) {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        contentValues.put("author", author);
        contentValues.put("price", price);
        sqLiteDatabase.insert("Book", null, contentValues);
        contentValues.clear();
    }
}
