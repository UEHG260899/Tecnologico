package com.example.tecnologico.ui.listara;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tecnologico.R;

public class ListarActivosFragment extends Fragment {

    private ListarActivosViewModel listarActivosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listarActivosViewModel =
                new ViewModelProvider(this).get(ListarActivosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listaa, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        listarActivosViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}