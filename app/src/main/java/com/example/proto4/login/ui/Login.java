package com.example.proto4.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proto4.R;
import com.example.proto4.api.ApiClient;
import com.example.proto4.api.ApiService;
import com.example.proto4.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_register);

        // redirect to create-account page
        TextView createAccount = findViewById(R.id.newAccount);
        createAccount.setOnClickListener(v1 -> {
            Intent intent = new Intent(Login.this, CreateAccount.class);
            startActivity(intent);
        });

        // Login handling
        Button loginButton = findViewById(R.id.loginButton);
        EditText userName = findViewById(R.id.userName);
        EditText password = findViewById(R.id.password);

        loginButton.setOnClickListener(v -> {
            String u = userName.getText().toString();
            String p = password.getText().toString();

            User user = new User(u, p);

            ApiService api = ApiClient.getClient().create(ApiService.class);

            api.loginUser(user).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Login.this, response.body(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(Login.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginui), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    } // onCreate ends
}
