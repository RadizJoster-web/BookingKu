package com.example.myapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragment yang menampilkan booking SEBELUMNYA yang tersimpan di SharedPreferences.
 * Data dikirim dari ResultActivity lewat arguments.
 */
public class HistoryFragment extends Fragment {

    private static final String ARG_RIWAYAT = "arg_riwayat";

    public static HistoryFragment newInstance(String riwayat) {
        HistoryFragment f = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RIWAYAT, riwayat);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txt = view.findViewById(R.id.txtRiwayat);
        String data = getArguments() != null ? getArguments().getString(ARG_RIWAYAT) : "";

        if (TextUtils.isEmpty(data)) {
            txt.setText(R.string.riwayat_kosong);
        } else {
            txt.setText(data);
        }
    }
}
