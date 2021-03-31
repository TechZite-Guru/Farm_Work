package com.example.farmwork;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    ImageView change_profile, change_username, phone_edit_icon;
    CircleImageView profile_pic;
    TextView profile_fullName, profile_email, profile_phone, full_address, role_title;
    EditText username_change_field, add_phone_field;
    Button profile_update, logout_btn;
    FirebaseAuth fAuth;
    String currentUserID, download_url;
    FirebaseFirestore fStore;
    int i = 1;
    private static final int Gallery_pick = 1;
    private StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        profile_pic = root.findViewById(R.id.profile_pic);
        change_profile = root.findViewById(R.id.profile_pic_edit);
        change_username = root.findViewById(R.id.user_name_edit);
        username_change_field = root.findViewById(R.id.edit_username_field);
        //profile_email = root.findViewById(R.id.user_email);
        role_title = root.findViewById(R.id.role_title);
        profile_fullName = root.findViewById(R.id.user_name);
        profile_phone = root.findViewById(R.id.user_phone);
        phone_edit_icon = root.findViewById(R.id.phone_edit);
        add_phone_field = root.findViewById(R.id.add_phone_number);
        profile_update = root.findViewById(R.id.profile_update_button);
        logout_btn = root.findViewById(R.id.logout_button);
        full_address = root.findViewById(R.id.full_address);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUserID = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Image").child(currentUserID + ".jpg");

        profileData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                profileData();
            }
        });

        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_pick);
            }
        });

        change_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_fullName.setVisibility(View.GONE);
                username_change_field.setVisibility(View.VISIBLE);
            }
        });

        phone_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_phone.setVisibility(View.GONE);
                add_phone_field.setVisibility(View.VISIBLE);
            }
        });

        profile_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_username = username_change_field.getText().toString().trim();
                String new_search_name = username_change_field.getText().toString().toLowerCase().trim();
                String new_Phone_number = add_phone_field.getText().toString().trim();
                if (!TextUtils.isEmpty(new_username)){
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", new_username);
                    user.put("search_name", new_search_name);
                    fStore.collection("users").document(currentUserID).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Username", "Username Updated Succesfully");
                            Log.d("Search_Name", "Search_Name Updated Succesfully");
                        }
                    });
                    username_change_field.setVisibility(View.GONE);
                    profile_fullName.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(new_Phone_number)){
                    Map<String, Object> user = new HashMap<>();
                    user.put("phone", new_Phone_number);
                    fStore.collection("users").document(currentUserID).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Phone Number", "Phone Number Updated Succesfully");
                        }
                    });
                    add_phone_field.setVisibility(View.GONE);
                    profile_phone.setVisibility(View.VISIBLE);
                }
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getContext(),LoginActivity.class));
            }
        });

        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_pick && resultCode==RESULT_OK && data!=null){
            Uri image_uri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity(), ProfileFragment.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri result_uri = result.getUri();
                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Profile Image").child(currentUserID + ".jpg");
                filePath.putFile(result_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {
                        if (uploadTask.isSuccessful()){
                            Log.d("Photo", "Upload success");
                            uploadTask.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    download_url = String.valueOf(task.getResult());
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("profile_image", download_url);
                                    fStore.collection("users").document(currentUserID).update(user);
                                    Log.d("URL","Download URL :" +download_url);
                                }
                            });
                        }
                        else {
                            Log.d("Photo", "Upload Failed ");
                        }
                    }
                });
            }
        }
    }

    public void profileData() {
        DocumentReference documentReference = fStore.collection("users").document(currentUserID);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (documentSnapshot != null) {
                    //profile_email.setText(documentSnapshot.getString("email"));
                    role_title.setText(documentSnapshot.getString("roles"));
                    profile_fullName.setText(documentSnapshot.getString("name"));
                    profile_phone.setText(documentSnapshot.getString("phone"));
                    full_address.setText(documentSnapshot.getString("location"));

                    String profile_pic_string = documentSnapshot.getString("profile_image");
                    Picasso.get().load(profile_pic_string).placeholder(R.drawable.profile_image_placeholder).into(profile_pic);
                    Log.d("URL", "Download URL :" + profile_pic_string);
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_service);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.search_service){
            return true;
        }

        if (id == R.id.lang_support){
            PrefManager prefManager = new PrefManager(getContext());

            // make first time launch TRUE
            prefManager.setFirstTimeLaunch(true);

            startActivity(new Intent(getContext(), SelectLanguage.class));
        }

        if (id == R.id.help) {
            contactAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    private void contactAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        String phnumber = "+919398274873";
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Call Us");
        alertDialog.setMessage(phnumber);
        alertDialog.setPositiveButton("   CALL   ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callintent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phnumber, null));
                getContext().startActivity(callintent);
            }
        });
        alertDialog.setNegativeButton("   Cancel  ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(getResources().getColor(R.color.orange_500));
        pbutton.setTextColor(Color.WHITE);
    }
}