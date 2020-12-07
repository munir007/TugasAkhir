package com.example.ta_1818007;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxNomor, TxNama, TxJaringan, TxTanggal, TxWarna, TxRam, TxRom, TxBattery, TxHarga;
    Spinner SpMK;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    CircularImageView imageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxNomor = (EditText)findViewById(R.id.txNomor_Edit);
        TxNama = (EditText)findViewById(R.id.txNama_Edit);
        TxJaringan = (EditText)findViewById(R.id.txJaringan_Edit);
        TxTanggal = (EditText)findViewById(R.id.txTglRilis_Edit);
        TxWarna = (EditText)findViewById(R.id.txWarna_Edit);
        TxRam = (EditText)findViewById(R.id.txRam_Edit);
        TxRom = (EditText)findViewById(R.id.txRom_Edit);
        TxBattery = (EditText)findViewById(R.id.txBattery_Edit);
        TxHarga = (EditText)findViewById(R.id.txHarga_Edit);
        SpMK = (Spinner)findViewById(R.id.spMK_Edit);
        imageView = (CircularImageView)findViewById(R.id.image_profile);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(EditActivity.this);
            }
        });

        getData();
    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();

        datePickerDialog =  new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String nomor = cursor.getString(cursor.getColumnIndex(DBHelper.row_nomor));
            String nama = cursor.getString(cursor.getColumnIndex(DBHelper.row_nama));
            String jaringan = cursor.getString(cursor.getColumnIndex(DBHelper.row_jaringan));
            String mk = cursor.getString(cursor.getColumnIndex(DBHelper.row_mk));
            String tanggal = cursor.getString(cursor.getColumnIndex(DBHelper.row_tglRilis));
            String warna = cursor.getString(cursor.getColumnIndex(DBHelper.row_warna));
            String ram = cursor.getString(cursor.getColumnIndex(DBHelper.row_ram));
            String rom = cursor.getString(cursor.getColumnIndex(DBHelper.row_rom));
            String battery = cursor.getString(cursor.getColumnIndex(DBHelper.row_battery));
            String harga = cursor.getString(cursor.getColumnIndex(DBHelper.row_harga));
            String foto = cursor.getString(cursor.getColumnIndex(DBHelper.row_foto));

            TxNomor.setText(nomor);
            TxNama.setText(nama);

            if (mk.equals("Apple")){
                SpMK.setSelection(0);
            }else if(mk.equals("Asus")){
                SpMK.setSelection(1);
            }else if(mk.equals("Oppo")){
                SpMK.setSelection(2);
            }else if(mk.equals("Realme")){
                SpMK.setSelection(3);
            }else if(mk.equals("Xiaomi")){
                SpMK.setSelection(3);
            }

            TxJaringan.setText(jaringan);
            TxTanggal.setText(tanggal);
            TxWarna.setText(warna);
            TxRam.setText(ram);
            TxRom.setText(rom);
            TxBattery.setText(battery);
            TxHarga.setText(harga);

            if (foto.equals("null")){
                imageView.setImageResource(R.drawable.ic_baseline_smartphone_24);
            }else{
                imageView.setImageURI(Uri.parse(foto));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_edit:
                String nomor = TxNomor.getText().toString().trim();
                String nama = TxNama.getText().toString().trim();
                String tanggal = TxTanggal.getText().toString().trim();
                String jaringan = TxJaringan.getText().toString().trim();
                String warna = TxWarna.getText().toString().trim();
                String ram = TxRam.getText().toString().trim();
                String rom = TxRom.getText().toString().trim();
                String battery = TxBattery.getText().toString().trim();
                String harga = TxHarga.getText().toString().trim();
                String mk = SpMK.getSelectedItem().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_nomor, nomor);
                values.put(DBHelper.row_nama, nama);
                values.put(DBHelper.row_jaringan, jaringan);
                values.put(DBHelper.row_tglRilis, tanggal);
                values.put(DBHelper.row_warna, warna);
                values.put(DBHelper.row_ram, ram);
                values.put(DBHelper.row_rom, rom);
                values.put(DBHelper.row_battery, battery);
                values.put(DBHelper.row_harga, harga);
                values.put(DBHelper.row_mk, mk);
                values.put(DBHelper.row_foto, String.valueOf(uri));

                if (nomor.equals("") || nama.equals("") || jaringan.equals("") ||tanggal.equals("") || warna.equals("") || ram.equals("") || rom.equals("") || battery.equals("") || harga.equals("")){
                    Toast.makeText(EditActivity.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()){
            case R.id.delete_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("Data ini akan dihapus.");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(EditActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)){
                uri = imageuri;
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}
                        , 0);
            }else{
                startCrop(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                imageView.setImageURI(result.getUri());
                uri = result.getUri();
            }
        }
    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
        uri = imageuri;
    }
}
