package blurb.documenta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class uploadimg extends AppCompatActivity {

    private static final int pick_img_req=1;
    private Button mchooseimg, muploadimg;
    private TextView mShowuploads;
    private EditText mFilename;
    private ImageView viewImg;
    ProgressDialog progressbar;
    private Uri mImageUri;
    private StorageReference PostsImagesRefrence;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;
    private String savecurrentDate,saveCurrentTime,postRandomName,downloadUrl, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimg);
        mchooseimg =findViewById(R.id.choseimg);
        muploadimg = findViewById(R.id.uploadimg);
        mShowuploads =findViewById(R.id.showuploads);
        mFilename =findViewById(R.id.entername);
        viewImg =findViewById(R.id.viewimg);
        progressbar =new ProgressDialog(this);
      mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        PostsImagesRefrence = FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        mchooseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        muploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });








    }

    private void ValidatePostInfo() {
        String filename=mFilename.getText().toString().trim();
        if(mImageUri == null){
            Toast.makeText(this, "Please select post image...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(filename))
        {
            Toast.makeText(this, "Please say something about your image...", Toast.LENGTH_SHORT).show();
        }else{
            progressbar.setTitle("Add new Post");
            progressbar.setMessage("Please wait, while we are updating your new post");
            progressbar.show();
            StoringImageToFirebaseStorage();
        }
    }

    private void StoringImageToFirebaseStorage() {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        savecurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = savecurrentDate + saveCurrentTime;


        StorageReference filePath = PostsImagesRefrence.child("Post Images").child(mImageUri.getLastPathSegment() + postRandomName + ".jpg");

       filePath.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
               if (task.isSuccessful()) {
                   downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                   Toast.makeText(uploadimg.this, "image uploaded successfully to Storage...", Toast.LENGTH_SHORT).show();
                   SavingPostInformationToDatabase();
               } else {

                   Toast.makeText(uploadimg.this, "Error occured ", Toast.LENGTH_SHORT).show();
               }
           }
       });

        }

    private void SavingPostInformationToDatabase() {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String userFullName = dataSnapshot.child("fullname").getValue().toString();
                    String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", savecurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", mFilename);
                    postsMap.put("postimage", downloadUrl);
                    postsMap.put("profileimage", userProfileImage);
                    PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        SendUserToMainActivity();
                                        Toast.makeText(uploadimg.this, "New Post is updated successfully.", Toast.LENGTH_SHORT).show();
                                        progressbar.dismiss();
                                    } else {
                                        Toast.makeText(uploadimg.this, "Error Occured while updating your post.", Toast.LENGTH_SHORT).show();
                                        progressbar.dismiss();
                                    }
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(uploadimg.this, home.class);
        startActivity(mainIntent);
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, pick_img_req);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==pick_img_req && resultCode==RESULT_OK && data!=null)
        {
            mImageUri = data.getData();

        }
    }



}
