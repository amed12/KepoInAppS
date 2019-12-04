package com.example.sun3toline.kepoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sun3toline.kepoin.R;

public class Province extends AppCompatActivity {
    Context context;
    //    //Membuat data
    String[] arrayProvince = {"Aceh", "Bali", "Bangka Belitung", "Banten", "Bengkulu", "Gorontalo", "Jakarta", "Jambi", "Jawa Barat", "Jawa Tengah", "Jawa Timur", "Kalimantan Barat", "Kalimantan Selatan", "Kalimantan Tengah", "kalimantan Timur", "Kepuluan Riau", "Lampung", "Maluku", "Maluku Utara", "Nusa Tenggara Barat", "Nusa Tenggara Timur", "Papua", "Papua Barat", "Riau", "Sulawesi Barat", "Sulawesi Selatan", "Sulawesi Tengah", "Sulawesi Tenggara", "Sumatera Utara", "Sumatera Barat", "Sumatera Selatan", "Yogyakarta"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        context = Province.this;

        //Inisialisasi ListView
        ListView listView = findViewById(R.id.listView);
        final TextView jupuk1 = findViewById(R.id.jupuk);
        jupuk1.setText(getIntent().getStringExtra("nama"));
        //Membuat Adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
                R.layout.item_province, arrayProvince);

        //Mengatur adapter
        listView.setAdapter(arrayAdapter);

        //Memberi aksi ketika di klik masing2 item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

//                Toast.makeText(context,"Click="+arrayBuah[position],
//                        Toast.LENGTH_SHORT).show();

                //Pindah activity dan mengirim string 'data' di extra
                Intent intent = new Intent(context, Post.class);
                intent.putExtra("data1", arrayProvince[position]);
                intent.putExtra("data2",jupuk1.getText().toString());

                startActivity(intent);
            }
        });

    }
}
