package com.example.diuride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diuride.databinding.ActivityEditProfileBinding;
import com.example.diuride.models.UserModel;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore mdb;
    Uri newimageUri = null;
    FirebaseStorage storage;
    boolean isProPicChanged = false;
    boolean isEditTextChanged = true;
    UserModel mUser;
    String uName,diuid,userType,uEmail,propic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));
        mAuth = FirebaseAuth.getInstance();
        mdb = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mUser = new UserModel();


//        // Retrieving user data in another activity
//        SharedPreferences prefs = getSharedPreferences("userInfo", MODE_PRIVATE);
//          userType = prefs.getString("usertype", "");
//          diuid = prefs.getString("udiuid", "");
//          uEmail = prefs.getString("uemail", "");
//          propic = prefs.getString("userpropic", "");
//          uName = prefs.getString("uname", "");
//         binding.userTYpe.setText(userType);
//         binding.shwodiuid.setText(diuid);
//         binding.showusername.setText(uName);
//         binding.showEmail.setText(uEmail);
//         Glide.with(EditProfileActivity.this)
//                 .load(propic)
//                 .centerCrop()
//                 .into(binding.proPic);
//
//         Retrieve other user information as needed


        DocumentReference userInfo = mdb.collection("Users").document(mAuth.getCurrentUser().getUid());
        userInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                mUser = documentSnapshot.toObject(UserModel.class);
                if (mUser != null) {
                    uName = mUser.getName();
                    diuid = mUser.getDiuid();
                    uEmail = mUser.getEmail();
                    userType = mUser.getUserType();
                    binding.userTYpe.setText(userType);
                    binding.shwodiuid.setText(diuid);
                    binding.showusername.setText(uName);
                    binding.showEmail.setText(uEmail);
                    Glide.with(EditProfileActivity.this)
                            .load(mUser.getProimage())
                            .centerCrop()
                            .placeholder(R.drawable.profilesvg)
                            .into(binding.proPic);

                }

            }
        });


       binding.saveChangesbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               if(!binding.shwodiuid.getText().toString().equals(diuid))
               {
                   mdb.collection("Users").document(mAuth.getCurrentUser().getUid())
                           .update("diuid", binding.shwodiuid.getText().toString())
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {

                                       Toast.makeText(EditProfileActivity.this, "DIU ID Updated!", Toast.LENGTH_SHORT).show();
                                       diuid=binding.shwodiuid.getText().toString();
                                       // Saving user data in SharedPreferences
                                       SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                                       editor.putString("udiuid", diuid);
                                       // Add other user information as needed
                                       editor.apply();
                                   }
                                   else
                                   {
                                       Toast.makeText(EditProfileActivity.this, "failed.Try again", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(EditProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                               }
                           });


               }
               if(!binding.showusername.getText().toString().equals(uName))
               {
                   mdb.collection("Users").document(mAuth.getCurrentUser().getUid())
                           .update("name", binding.showusername.getText().toString())
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {

                                       Toast.makeText(EditProfileActivity.this, "Name Updated!", Toast.LENGTH_SHORT).show();
                                       uName=binding.showusername.getText().toString();
                                       // Saving user data in SharedPreferences
                                       SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                                       editor.putString("uname", uName);
                                       // Add other user information as needed
                                       editor.apply();
                                   }
                                   else
                                   {
                                       Toast.makeText(EditProfileActivity.this, "failed.Try again", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(EditProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                               }
                           });

               }



               if(isProPicChanged)
               {
                   final ProgressDialog mProgressDialog = new ProgressDialog(EditProfileActivity.this);
                   mProgressDialog.setTitle("Profile Image Uploading");
                   if (newimageUri != null) {

                       mProgressDialog.show();

                       StorageReference newImgref = storage.getReference().child("Profiles").child(mAuth.getCurrentUser().getUid());
                       newImgref.putFile(newimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               newImgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       mdb.collection("Users").document(mAuth.getCurrentUser().getUid())
                                               .update("proimage", uri);
                                       // Saving user data in SharedPreferences
                                       SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                                       editor.putString("userpropic", uri.toString());
                                       // Add other user information as needed
                                       editor.apply();
                                       mProgressDialog.dismiss();
                                       isProPicChanged = false;

                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {

                                   }
                               });

                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                           }
                       }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                               float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                               mProgressDialog.setMessage("Uploading..." + (int) percent + "%");

                           }
                       });

                   }

               }
           }
       });
//        binding.showusername.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Toast.makeText(EditProfileActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                isEditTextChanged = true;
//            }
//        });
//        binding.shwodiuid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(EditProfileActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                isEditTextChanged = true;
//
//            }
//        });

        binding.showEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.showEmail.setError("You Can't Edit Your Email");
                Toast.makeText(EditProfileActivity.this, "You Can't Edit Your Email", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void chaneproPic(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please select a image"), 55);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 55 && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getData() != null) {
                    isProPicChanged = true;
                    newimageUri = data.getData();
                    binding.proPic.setImageURI(newimageUri);

                    Toast.makeText(EditProfileActivity.this, "success", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }
}