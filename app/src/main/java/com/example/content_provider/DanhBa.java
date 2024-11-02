package com.example.content_provider;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import android.Manifest;


public class DanhBa extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSIONS =1001;
    ListView lvDanhBa;
    ArrayList <Contact> dsDanhBa;
    ArrayAdapter<Contact> adapterDanhBa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_ba);
        addControl();
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            showAllContactFromDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAllContactFromDevice();
            } else {
                Log.d("DanhBa", "Quyền bị từ chối.");
                // Hiện thông báo hoặc xử lý khi quyền bị từ chối
            }
        }
    }

    private void showAllContactFromDevice() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            dsDanhBa.clear();
            while (cursor.moveToNext()) {
                String tenCotname = ContactsContract.Contacts.DISPLAY_NAME;
                String tenCotPhone = ContactsContract.CommonDataKinds.Phone.NUMBER;
                int vt = cursor.getColumnIndex(tenCotname);
                int vt2 = cursor.getColumnIndex(tenCotPhone);

                if (vt != -1 && vt2 != -1) {
                    String name = cursor.getString(vt);
                    String phone = cursor.getString(vt2);
                    Contact contact = new Contact(name, phone);
                    dsDanhBa.add(contact);
                }
            }
            cursor.close();
            Log.d("DanhBa", "Số lượng danh bạ: " + dsDanhBa.size());
            adapterDanhBa.notifyDataSetChanged();
        } else {
            Log.d("DanhBa", "Không thể truy vấn danh bạ.");
        }
    }

    private void addControl() {
        lvDanhBa = findViewById(R.id.lvDanhBa);
        dsDanhBa = new ArrayList<>();
        adapterDanhBa = new ArrayAdapter<>(
                DanhBa.this, android.R.layout.simple_list_item_1, dsDanhBa
        );
        lvDanhBa.setAdapter(adapterDanhBa);
    }
}