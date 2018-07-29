package com.example.gauth.mad_contacts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//MAKE CHANGES IN LOGINPAGE FOR FIREBASE FROM CONTACTDETAILS TO CONTACTDETAILS2
public class MainActivity extends AppCompatActivity {

    public DatabaseReference databaseReference;
    public DatabaseReference conditionReference;
    private FirebaseAuth mAuth;

EditText email;
EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button login=(Button)findViewById(R.id.login);

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        conditionReference=databaseReference.child("TestContacts1");
        final TextView signUp=(TextView)findViewById(R.id.signUp);
        mAuth = FirebaseAuth.getInstance();

        //LOGIN CLICKED

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Login ","Pressed");
                login(email.getText().toString(),password.getText().toString());
            }
        });


        //SIGNUP CLICKED

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SignUp","Pressed");
                //conditionReference.push().setValue("SIGNUP PRESSED");
                Intent signUpIntent= new Intent(MainActivity.this, SignUpPage.class);
                startActivity(signUpIntent);
            }
        });

    }

    //TO GET DATA FROM CLOUD
    @Override
    protected  void onStart()
    {
        super.onStart();
        conditionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Context context=getBaseContext();
                    Toast.makeText(context,"Cloud storage called",Toast.LENGTH_SHORT).show();

                    for (DataSnapshot expenses : dataSnapshot.getChildren()) {

                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void login(final String email, final String password)
    {
        /*Toast.makeText(this,"Successful Login with "+email+" "+password
                ,Toast.LENGTH_SHORT).show();*/

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            Context context=MainActivity.this;
                            /*Toast.makeText(context,"Successful Login with "+email+" "+password
                                    ,Toast.LENGTH_SHORT).show();*/

                            Intent intent=new Intent(MainActivity.this,LoginPage.class);
        startActivity(intent);

                        }
                        else {
                            Context context=MainActivity.this;
                            Toast.makeText(context,"Unsuccessful Login with "+email +" "+password
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
