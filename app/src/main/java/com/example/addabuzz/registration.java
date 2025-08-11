package com.example.addabuzz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.addabuzz.util.CloudinaryConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {

    TextView rgLogin;
    EditText rgFullName, rgEmail, rgPassword, rgConfirmPassword;
    Button rgButton;
    CircleImageView rgProfile;
    Uri imageUri;
    FirebaseAuth auth;
    FirebaseDatabase database;

    final int IMAGE_REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        rgLogin = findViewById(R.id.rgLogin);
        rgFullName = findViewById(R.id.rgFullName);
        rgEmail = findViewById(R.id.rgEmail);
        rgPassword = findViewById(R.id.rgPassword);
        rgConfirmPassword = findViewById(R.id.rgConfirmPassword);
        rgButton = findViewById(R.id.btnRegister);
        rgProfile = findViewById(R.id.rgProfile);
        CircularProgressIndicator progressIndicator = findViewById(R.id.regiCircularProgressIndicator);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        rgLogin.setOnClickListener(v -> {
            startActivity(new Intent(registration.this, login.class));
            finish();
        });

        rgProfile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), IMAGE_REQUEST_CODE);
        });

        rgButton.setOnClickListener(v -> {
            String fullName = rgFullName.getText().toString().trim();
            String userEmail = rgEmail.getText().toString().trim();
            String userPassword = rgPassword.getText().toString();
            String userCPassword = rgConfirmPassword.getText().toString();

            if (fullName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userCPassword.isEmpty()) {
                Toast.makeText(registration.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!userPassword.equals(userCPassword)) {
                rgConfirmPassword.setError("Passwords do not match");
                return;
            }

            if (imageUri == null) {
                Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(registration.this);
            progressDialog.setMessage("Uploading Image...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            try {
                // Compress image
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] imageData = baos.toByteArray();
                InputStream imageStream = new ByteArrayInputStream(imageData);

                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", CloudinaryConfig.getCloudName(),
                        "api_key", CloudinaryConfig.getApiKey(),
                        "api_secret", CloudinaryConfig.getApiSecret()
                ));

                new Thread(() -> {
                    try {
                        Map uploadResult = cloudinary.uploader().upload(imageStream, ObjectUtils.emptyMap());
                        String imageUrl = (String) uploadResult.get("secure_url");

                        runOnUiThread(() -> {
                            progressIndicator.setVisibility(View.VISIBLE);
                            progressDialog.setMessage("Registering user...");

                            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                                    .addOnCompleteListener(task -> {
                                        progressDialog.dismiss();

                                        if (task.isSuccessful()) {
                                            String userId = task.getResult().getUser().getUid();
                                            DatabaseReference ref = database.getReference().child("user").child(userId);

                                            ref.child("fullName").setValue(fullName);
                                            ref.child("email").setValue(userEmail);
                                            ref.child("profileImage").setValue(imageUrl);
                                            ref.child("status").setValue("I'm using AddaBuzz.");

                                            Toast.makeText(registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(registration.this, login.class));
                                            finish();
                                        } else {
                                            Toast.makeText(registration.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(registration.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                }).start();

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(registration.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(rgProfile);
        }
    }
}
