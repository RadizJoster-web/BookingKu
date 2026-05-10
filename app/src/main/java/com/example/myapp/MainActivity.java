package com.example.myapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Komponen form
    private EditText edtNama, edtEmail, edtHp, edtJumlah;
    private Spinner spnFilm;
    private RadioGroup rgStudio;
    private CheckBox cbPopcorn, cbMinuman, cbNachos;
    private Button btnTanggal, btnJam, btnPesan, btnReset;

    // Daftar film untuk Spinner
    private final String[] daftarFilm = {
            "Avengers: Secret Wars",
            "Spider-Man: Beyond",
            "Dune: Part Three",
            "KKN di Desa Penari 2",
            "Pengabdi Setan: Komuni"
    };

    // Menyimpan tanggal & jam yang dipilih
    private String tanggalDipilih = "";
    private String jamDipilih = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        edtNama   = findViewById(R.id.edtNama);
        edtEmail  = findViewById(R.id.edtEmail);
        edtHp     = findViewById(R.id.edtHp);
        edtJumlah = findViewById(R.id.edtJumlah);
        spnFilm   = findViewById(R.id.spnFilm);
        rgStudio  = findViewById(R.id.rgStudio);
        cbPopcorn = findViewById(R.id.cbPopcorn);
        cbMinuman = findViewById(R.id.cbMinuman);
        cbNachos  = findViewById(R.id.cbNachos);
        btnTanggal = findViewById(R.id.btnTanggal);
        btnJam     = findViewById(R.id.btnJam);
        btnPesan   = findViewById(R.id.btnPesan);
        btnReset   = findViewById(R.id.btnReset);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, daftarFilm);
        spnFilm.setAdapter(adapter);

        btnTanggal.setOnClickListener(v -> tampilkanDatePicker());

        btnJam.setOnClickListener(v -> tampilkanTimePicker());

        btnPesan.setOnClickListener(v -> prosesPesan());

        btnReset.setOnClickListener(v -> resetForm());
    }

    private void tampilkanDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    tanggalDipilih = String.format(Locale.getDefault(),
                            "%02d/%02d/%d", day, month + 1, year);
                    btnTanggal.setText(tanggalDipilih);
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        dlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dlg.show();
    }

    private void tampilkanTimePicker() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog dlg = new TimePickerDialog(this,
                (view, hour, minute) -> {
                    jamDipilih = String.format(Locale.getDefault(),
                            "%02d:%02d", hour, minute);
                    btnJam.setText(jamDipilih);
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true);
        dlg.show();
    }

    private void prosesPesan() {
        String nama   = edtNama.getText().toString().trim();
        String email  = edtEmail.getText().toString().trim();
        String hp     = edtHp.getText().toString().trim();
        String jumlah = edtJumlah.getText().toString().trim();

        // ====== VALIDASI FORM ======
        // Nama tidak boleh kosong
        if (nama.isEmpty()) {
            edtNama.setError(getString(R.string.err_nama));
            edtNama.requestFocus();
            return;
        }
        // Email tidak boleh kosong & harus mengandung "@"
        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.err_email_kosong));
            edtEmail.requestFocus();
            return;
        }
        if (!email.contains("@")) {
            edtEmail.setError(getString(R.string.err_email_format));
            edtEmail.requestFocus();
            return;
        }
        // No HP tidak boleh kosong & harus angka
        if (hp.isEmpty()) {
            edtHp.setError(getString(R.string.err_hp_kosong));
            edtHp.requestFocus();
            return;
        }
        if (!hp.matches("\\d+")) {
            edtHp.setError(getString(R.string.err_hp_angka));
            edtHp.requestFocus();
            return;
        }
        // Jumlah tiket tidak boleh kosong & harus angka > 0
        if (jumlah.isEmpty()) {
            edtJumlah.setError(getString(R.string.err_jumlah_kosong));
            edtJumlah.requestFocus();
            return;
        }
        int jumlahTiket;
        try {
            jumlahTiket = Integer.parseInt(jumlah);
            if (jumlahTiket <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            edtJumlah.setError(getString(R.string.err_jumlah_angka));
            edtJumlah.requestFocus();
            return;
        }
        // Tanggal & jam wajib dipilih
        if (tanggalDipilih.isEmpty()) {
            Toast.makeText(this, R.string.err_tanggal, Toast.LENGTH_SHORT).show();
            return;
        }
        if (jamDipilih.isEmpty()) {
            Toast.makeText(this, R.string.err_jam, Toast.LENGTH_SHORT).show();
            return;
        }

        // ====== AMBIL DATA INPUT ======
        String film = spnFilm.getSelectedItem().toString();

        int idStudio = rgStudio.getCheckedRadioButtonId();
        RadioButton rbStudio = findViewById(idStudio);
        String studio = rbStudio.getText().toString();

        // Kumpulkan snack yang dipilih
        StringBuilder snack = new StringBuilder();
        if (cbPopcorn.isChecked()) snack.append("Popcorn, ");
        if (cbMinuman.isChecked()) snack.append("Minuman, ");
        if (cbNachos.isChecked())  snack.append("Nachos, ");
        String snackText = snack.length() == 0
                ? getString(R.string.snack_kosong)
                : snack.substring(0, snack.length() - 2);

        // Hitung total harga
        int hargaStudio = 50000;
        if (rbStudio.getId() == R.id.rbVip)  hargaStudio = 75000;
        if (rbStudio.getId() == R.id.rbImax) hargaStudio = 100000;

        int hargaSnack = 0;
        if (cbPopcorn.isChecked()) hargaSnack += 25000;
        if (cbMinuman.isChecked()) hargaSnack += 15000;
        if (cbNachos.isChecked())  hargaSnack += 30000;

        int total = (hargaStudio * jumlahTiket) + hargaSnack;

        // ====== EXPLICIT INTENT ke ResultActivity ======
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("nama",     nama);
        intent.putExtra("email",    email);
        intent.putExtra("hp",       hp);
        intent.putExtra("film",     film);
        intent.putExtra("studio",   studio);
        intent.putExtra("snack",    snackText);
        intent.putExtra("tanggal",  tanggalDipilih);
        intent.putExtra("jam",      jamDipilih);
        intent.putExtra("jumlah",   jumlahTiket);
        intent.putExtra("total",    total);
        startActivity(intent);
    }

    private void resetForm() {
        edtNama.setText("");
        edtEmail.setText("");
        edtHp.setText("");
        edtJumlah.setText("");
        spnFilm.setSelection(0);
        rgStudio.check(R.id.rbRegular);
        cbPopcorn.setChecked(false);
        cbMinuman.setChecked(false);
        cbNachos.setChecked(false);
        tanggalDipilih = "";
        jamDipilih = "";
        btnTanggal.setText(R.string.hint_pilih_tanggal);
        btnJam.setText(R.string.hint_pilih_jam);
        Toast.makeText(this, "Form berhasil direset", Toast.LENGTH_SHORT).show();
    }
}
