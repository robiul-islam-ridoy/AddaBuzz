package com.example.addabuzz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText email, pass;
    Button button;
    FirebaseAuth auth;
    String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.loginButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email= email.getText().toString();
                String password = pass.getText().toString();

                if((Email.isEmpty())){
                    Toast.makeText(login.this, "Please Enater Email.", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(login.this, "Please Enter Password.", Toast.LENGTH_LONG).show();
                } else if (Email.matches(emailPattern)) {
                    email.setError("Please input proper email type.");
                }else{
                    auth.signInWithEmailAndPassword(Email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                try {
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });


        
    }
}