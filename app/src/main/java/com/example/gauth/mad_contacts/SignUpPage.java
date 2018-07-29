package com.example.gauth.mad_contacts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String FirstName;
    String LastName;
    String email;
    String password;
    String Uid;
    EditText username;
    EditText Password;
    EditText firstName;
    EditText lastName;
    public DatabaseReference databaseReference;
    public DatabaseReference conditionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        conditionReference=databaseReference.child("TestContacts1");
        mAuth = FirebaseAuth.getInstance();
firstName=(EditText)findViewById(R.id.firstName);
lastName=(EditText)findViewById(R.id.lastName);
        username=(EditText)findViewById(R.id.username);
        Password=(EditText)findViewById(R.id.password2);


     //   Toast.makeText(this,"Username "+email+"Password "+password,Toast.LENGTH_SHORT).show();
           }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("Current","User");
    }

public void SignUp(View view)
{
    FirstName=firstName.getText().toString();
    LastName=lastName.getText().toString();
    email=username.getText().toString();
    password=Password.getText().toString();
    Toast.makeText(this,"FirstName "+FirstName+"LastName "+LastName,Toast.LENGTH_SHORT).show();
    Toast.makeText(this,"Username "+email+"Password "+password,Toast.LENGTH_SHORT).show();
    mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Context context=SignUpPage.this;
                        Toast.makeText(context,"Successful sign up!",Toast.LENGTH_SHORT).show();
                        Uid = mAuth.getCurrentUser().getUid();
                        UserSignUpDetails userSignUpDetails=
                                new UserSignUpDetails(FirstName,LastName,email,password,Uid);
                        conditionReference.push().setValue(userSignUpDetails);

                        Intent intent=new Intent(SignUpPage.this,LoginPage.class);
                        startActivity(intent);
                    }else {
                        Context context=SignUpPage.this;
                        Toast.makeText(context,"Unsuccessful in signing up!",Toast.LENGTH_SHORT).show();
                        Log.i("Exception", task.getException().getMessage());
}
                }
            });
}
}
