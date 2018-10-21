package blurb.documenta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signUp extends AppCompatActivity {
    private EditText efullname, eemail, epassword, ephone;
    private FirebaseAuth mAuth;
    ProgressDialog progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        efullname =findViewById(R.id.fname);
        eemail = findViewById(R.id.email);
        epassword =findViewById(R.id.password);
        ephone =findViewById(R.id.pno);
        mAuth = FirebaseAuth.getInstance();
        Button submit=findViewById(R.id.submit);
        progressBar =new ProgressDialog(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });
        }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    private void registerUser() {
        final String name =efullname.getText().toString().trim();
        final String email =eemail.getText().toString().trim();
        final String password =epassword.getText().toString().trim();
        final String phno =ephone.getText().toString().trim();

        if(name.isEmpty()){
            efullname.setError("Please Fill name");
            efullname.requestFocus();
        }
        if(email.isEmpty()){
            eemail.setError("Please Fill email");
            eemail.requestFocus();
        }
        if(password.isEmpty()){
            epassword.setError("Please Fill password");
            epassword.requestFocus();
        }
        if(phno.isEmpty()){
            ephone.setError("Please Fill phone number");
            ephone.requestFocus();
        }
        if(password.length()<6){
            epassword.setError("Password is too short");
            epassword.requestFocus();
        }
        if(phno.length()!=10){
            ephone.setError("Please enter valid phone number");
            ephone.requestFocus();
        }
        progressBar.setTitle("Creating account");
        progressBar.setMessage("Please wait while we create your account");
        progressBar.show();
        progressBar.setCanceledOnTouchOutside(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.dismiss();
                            Users user = new Users(
                                    name,
                                    email,
                                    phno

                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(signUp.this, "Registration successful! Kindly signin", Toast.LENGTH_LONG).show();
                                        Intent in=new Intent(signUp.this,registerUser.class);
                                        startActivity(in);
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(signUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    }
