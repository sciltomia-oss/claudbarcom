package com.example.claudbarcom.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.claudbarcom.R;
import com.example.claudbarcom.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupViews();
        setupClickListeners();

        return root;
    }

    private void setupViews() {
        // Aquí irán las configuraciones de vistas (animaciones, etc)
    }

    private void setupClickListeners() {
        // Bar Live
        binding.btnBarLive.setOnClickListener(v ->
            showToast("Bar Live iniciando...")
        );

        // Send button
        binding.btnSend.setOnClickListener(v -> {
            String message = binding.inputMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                showToast("Pregunta: " + message);
                binding.inputMessage.setText("");
            }
        });

        // Mic button
        binding.btnMicInput.setOnClickListener(v ->
            showToast("Escuchando...")
        );

        // Other controls
        binding.btnAuto.setOnClickListener(v -> showToast("Modo Auto"));
        binding.btnAgent.setOnClickListener(v -> showToast("Agente activado"));
        binding.btnClock.setOnClickListener(v -> showToast("Reloj"));
        binding.btnEdit.setOnClickListener(v -> showToast("Editar"));
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
