package com.example.ta_1818007;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    DBHelper helper;
    LayoutInflater inflater;
    View dialogView;
    TextView Tv_Nomor, Tv_Nama, Tv_MK, Tv_Tanggal, Tv_Jaringan, Tv_Warna, Tv_Ram, Tv_Rom, Tv_Battery, Tv_Harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        helper = new DBHelper(this);
        listView = (ListView)findViewById(R.id.list_data);
        listView.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListView(){
        Cursor cursor = helper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        listView.setAdapter(customCursorAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long x) {
        TextView getId = (TextView)view.findViewById(R.id.listID);
        final long id = Long.parseLong(getId.getText().toString());
        final Cursor cur = helper.oneData(id);
        cur.moveToFirst();

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pilih Opsi");

        //Add a list
        String[] options = {"Lihat Data", "Edit Data", "Hapus Data"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        final AlertDialog.Builder viewData = new AlertDialog.Builder(MainActivity.this);
                        inflater = getLayoutInflater();
                        dialogView = inflater.inflate(R.layout.view_data, null);
                        viewData.setView(dialogView);
                        viewData.setTitle("Lihat Data");

                        Tv_Nomor = (TextView)dialogView.findViewById(R.id.tv_No);
                        Tv_Nama = (TextView)dialogView.findViewById(R.id.tv_Nama);
                        Tv_Jaringan = (TextView)dialogView.findViewById(R.id.tv_Jaringan);
                        Tv_Tanggal = (TextView)dialogView.findViewById(R.id.tv_Tanggal);
                        Tv_MK = (TextView)dialogView.findViewById(R.id.tv_MK);
                        Tv_Warna = (TextView)dialogView.findViewById(R.id.tv_Warna);
                        Tv_Ram = (TextView)dialogView.findViewById(R.id.tv_Ram);
                        Tv_Rom = (TextView)dialogView.findViewById(R.id.tv_Rom);
                        Tv_Battery = (TextView)dialogView.findViewById(R.id.tv_Battery);
                        Tv_Harga = (TextView)dialogView.findViewById(R.id.tv_Harga);

                        Tv_Nomor.setText("Nomor: " + cur.getString(cur.getColumnIndex(DBHelper.row_nomor)));
                        Tv_Nama.setText("Nama: " + cur.getString(cur.getColumnIndex(DBHelper.row_nama)));
                        Tv_MK.setText("Merk: " + cur.getString(cur.getColumnIndex(DBHelper.row_mk)));
                        Tv_Tanggal.setText("Tanggal Rilis: " + cur.getString(cur.getColumnIndex(DBHelper.row_tglRilis)));
                        Tv_Jaringan.setText("Jaringan: " + cur.getString(cur.getColumnIndex(DBHelper.row_jaringan)));
                        Tv_Warna.setText("Warna: " + cur.getString(cur.getColumnIndex(DBHelper.row_warna)));
                        Tv_Ram.setText("Ram: " + cur.getString(cur.getColumnIndex(DBHelper.row_ram)));
                        Tv_Rom.setText("Rom: " + cur.getString(cur.getColumnIndex(DBHelper.row_rom)));
                        Tv_Battery.setText("Battery: " + cur.getString(cur.getColumnIndex(DBHelper.row_battery)));
                        Tv_Harga.setText("Harga: " + cur.getString(cur.getColumnIndex(DBHelper.row_harga)));

                        viewData.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        viewData.show();
                }
                switch (which){
                    case 1:
                        Intent iddata = new Intent(MainActivity.this, EditActivity.class);
                        iddata.putExtra(DBHelper.row_id, id);
                        startActivity(iddata);
                }
                switch (which){
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("Data ini akan dihapus.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteData(id);
                                Toast.makeText(MainActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        });
                        builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }
}