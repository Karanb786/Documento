package blurb.documenta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class home extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton came;
    private StorageReference mStorageRef;
    String mCurrentPhotoPath;
    FirebaseAuth mAuth;
    private static final int gly_intent=2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mStorageRef = FirebaseStorage.getInstance().getReference();
        came=findViewById(R.id.cam);
        came.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(Intent.ACTION_PICK);
                takePictureIntent.setType("image/*");
                startActivityForResult(takePictureIntent,gly_intent);

                }

        });




    }


    }


