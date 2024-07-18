package com.example.chatjp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registeration extends AppCompatActivity {
    TextView loginbut;
    EditText rg_username, rg_email, rg_password, rg_repassword;
    Button rg_signup;
    CircleImageView rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        loginbut = findViewById(R.id.Loginbut); // Changed the id to 'loginbutton'
        rg_username = findViewById(R.id.rgusername);
        rg_email = findViewById(R.id.rgemail);
        rg_password = findViewById(R.id.rgpassword);
        rg_repassword = findViewById(R.id.rgrepassword);
        rg_profileImg = findViewById(R.id.profilerg0);
        rg_signup = findViewById(R.id.signupbutton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Establishing the Account..");
        progressDialog.setCancelable(false);

        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registeration.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namee = rg_username.getText().toString();
                String emaill = rg_email.getText().toString();
                String Password = rg_password.getText().toString();
                String cPassword = rg_repassword.getText().toString();
                String status = "Hey I'm using CHATJP";

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) ||
                        TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword))
                {
                    Toast.makeText(registeration.this, "Please enter valid information",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!emaill.matches(emailPattern))
                {
                    rg_email.setError("Type a valid email here");
                }
                else if (Password.length() < 6)
                {
                    rg_password.setError("Password must be SIX characters or more");
                }
                else if (!Password.equals(cPassword))
                {
                    rg_password.setError("The passwords don't match");
                }
                else
                {
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(emaill, Password).addOnCompleteListener(registeration.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("User").child(id);
                                Users users = new Users(id, namee, emaill, Password, imageuri, status);

                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(registeration.this, "Register successfully on database", Toast.LENGTH_SHORT).show();
                                            uploadImageAndSaveUser(reference, users, id);
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(registeration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(registeration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            rg_profileImg.setImageURI(imageURI);
        }
    }

    private void uploadImageAndSaveUser(DatabaseReference reference, Users users, String id) {
        if (imageURI != null) {
            StorageReference storageReference = storage.getReference().child("Upload").child(id);
            storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageuri = uri.toString();
                                users.setImageuri(imageuri);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(registeration.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(registeration.this, "Image successfully uploaded", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(registeration.this, "Error in uploading image", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(registeration.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Intent intent = new Intent(registeration.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
