package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    public static final String PREF_NAME = "BookingKuPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.result_root), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        Intent in = getIntent();
        String nama    = in.getStringExtra("nama");
        String email   = in.getStringExtra("email");
        String hp      = in.getStringExtra("hp");
        String film    = in.getStringExtra("film");
        String studio  = in.getStringExtra("studio");
        String snack   = in.getStringExtra("snack");
        String tanggal = in.getStringExtra("tanggal");
        String jam     = in.getStringExtra("jam");
        int jumlah     = in.getIntExtra("jumlah", 0);
        int total      = in.getIntExtra("total", 0);

        String totalRp = NumberFormat.getNumberInstance(new Locale("in", "ID")).format(total);

        ((TextView) findViewById(R.id.txtNama)).setText(getString(R.string.result_nama, nama));
        ((TextView) findViewById(R.id.txtEmail)).setText(getString(R.string.result_email, email));
        ((TextView) findViewById(R.id.txtHp)).setText(getString(R.string.result_hp, hp));
        ((TextView) findViewById(R.id.txtFilm)).setText(getString(R.string.result_film, film));
        ((TextView) findViewById(R.id.txtStudio)).setText(getString(R.string.result_studio, studio));
        ((TextView) findViewById(R.id.txtSnack)).setText(getString(R.string.result_snack, snack));
        ((TextView) findViewById(R.id.txtTanggal)).setText(getString(R.string.result_tanggal, tanggal));
        ((TextView) findViewById(R.id.txtJam)).setText(getString(R.string.result_jam, jam));
        ((TextView) findViewById(R.id.txtJumlah)).setText(getString(R.string.result_jumlah, String.valueOf(jumlah)));
        ((TextView) findViewById(R.id.txtTotal)).setText(getString(R.string.result_total, totalRp));

        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String riwayatLama = pref.getString("riwayat", "");

        String ringkasan =
                "Nama   : " + nama + "\n" +
                "Film   : " + film + "\n" +
                "Studio : " + studio + "\n" +
                "Tanggal: " + tanggal + " " + jam + "\n" +
                "Jumlah : " + jumlah + " tiket\n" +
                "Total  : Rp " + totalRp;

        SharedPreferences.Editor edit = pref.edit();
        edit.putString("riwayat", ringkasan);
        edit.apply();

        if (savedInstanceState == null) {
            HistoryFragment fragment = HistoryFragment.newInstance(riwayatLama);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

        Button btnHubungi = findViewById(R.id.btnHubungi);
        btnHubungi.setOnClickListener(v -> {
            Intent dial = new Intent(Intent.ACTION_DIAL);
            dial.setData(Uri.parse("tel:" + getString(R.string.phone_bioskop)));
            startActivity(dial);
        });

        Button btnEmail = findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(v -> {
            Intent mail = new Intent(Intent.ACTION_SENDTO);
            mail.setData(Uri.parse("mailto:"));
            mail.putExtra(Intent.EXTRA_EMAIL, new String[]{ getString(R.string.email_bioskop) });
            mail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            mail.putExtra(Intent.EXTRA_TEXT, ringkasan);
            if (mail.resolveActivity(getPackageManager()) != null) {
                startActivity(mail);
            } else {
                Toast.makeText(this, "Tidak ada aplikasi email terpasang", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnWebsite = findViewById(R.id.btnWebsite);
        btnWebsite.setOnClickListener(v -> {
            Intent web = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.website_bioskop)));
            startActivity(web);
        });

        Button btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(v -> finish());

        if (!TextUtils.isEmpty(nama)) {
            Toast.makeText(this, "Pemesanan berhasil dibuat!", Toast.LENGTH_SHORT).show();
        }
    }
}
