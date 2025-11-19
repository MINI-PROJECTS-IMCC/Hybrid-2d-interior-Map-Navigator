package com.example.proto4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.proto4.qr.Verify;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.proto4.login.ui.Login;
import com.example.proto4.qr.Main;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button login=findViewById(R.id.log);
        login.setOnClickListener(v->{
            Intent intent=new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });
        Button scan=findViewById(R.id.scan);
        scan.setOnClickListener(v->{
            Intent intent=new Intent(MainActivity.this, Main.class);
            qrScannerLauncher.launch(intent);
        });

    }
    private ActivityResultLauncher<Intent> qrScannerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String qrValue = data.getStringExtra("qrResult");
                        // TODO: Use QR result here

                        Verify v=new Verify(MainActivity.this);
                        v.checkQr(qrValue);

                    }
                }
            });
}