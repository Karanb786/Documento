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

public class registerUser extends AppCompatActivity {
    EditText username,passwordd;
    Button login,register;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        username =findViewById(R.id.username);
        passwordd=findViewById(R.id.password);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        }


        public void login(View view){
        final String email=username.getText().toString().trim();
        final String pwd=passwordd.getText().toString().trim();
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(registerUser.this,"You are Logged in",Toast.LENGTH_SHORT).show();
                                home();


                            }else
                            {
                                String messege = task.getException().getMessage();
                                Toast.makeText(registerUser.this,"Login Failed"+messege,Toast.LENGTH_SHORT).show();

                            }

                        }
                    });




        }

    private void home() {
        Intent registerUser =new Intent(blurb.documenta.registerUser.this,home.class);

    }

    public void register(View view){
Intent signUp= new Intent(registerUser.this, signUp.class);
        }

}
