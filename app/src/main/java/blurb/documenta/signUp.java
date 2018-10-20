package blurb.documenta;

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
import com.google.firebase.auth.FirebaseUser;

public class signUp extends AppCompatActivity {
    EditText fullname,emailadd,pwd,phoneno;
    Button submit;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullname =findViewById(R.id.fname);
        emailadd=findViewById(R.id.email);
        pwd=findViewById(R.id.password);
        phoneno=findViewById(R.id.pno);
        }
        public void submit(View view){
        final String name = fullname.getText().toString().trim();
        final String email=emailadd.getText().toString().trim();
        final String password =pwd.getText().toString().trim();
        final String number = phoneno.getText().toString().trim();
        mAuth =FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            Toast.makeText(signUp.this,"You are Register",Toast.LENGTH_SHORT).show();
                            loin();


                            }else{
                            Toast.makeText(signUp.this,"Some thing is not correct ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        }

    private void loin() {
        Intent login =new Intent(this,registerUser.class);

    }
}
