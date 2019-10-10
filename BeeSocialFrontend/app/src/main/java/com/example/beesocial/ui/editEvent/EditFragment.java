package com.example.beesocial.ui.editEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.beesocial.R;

public class EditFragment extends Fragment {

    private EditViewModel editViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editViewModel =
                ViewModelProviders.of(this).get(EditViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit, container, false);
        final TextView textView = root.findViewById(R.id.text_edit);
        editViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}