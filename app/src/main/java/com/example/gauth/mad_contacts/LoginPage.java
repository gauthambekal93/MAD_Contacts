package com.example.gauth.mad_contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity
{
    public DatabaseReference databaseReference;
    public DatabaseReference conditionReference;
    private FirebaseAuth mAuth;
   static ArrayList<ContactDetails> contactObjects=new ArrayList<ContactDetails>();
    static ArrayList<ContactDetails2> contactObjects2=new ArrayList<ContactDetails2>();
    ListView listView;
    AddContacts adapter;
    static boolean storageCaller=true;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
    switch (item.getItemId())
    {
        case R.id.addContacts:
            Log.i("add","contacts");
            return true;
        case R.id.logout:
        Log.i("log","out");
        return true;
         default:
        return false;
    }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login_page1);

        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        conditionReference=databaseReference.child("ContactDetails");

        //CONTACT LIST

    /*    contactObjects.add(0,new ContactDetails("ram","222","ram@222"));
        contactObjects.add(1,new ContactDetails("sham","333","sham@333"));
        contactObjects.add(2,new ContactDetails("ghansham","444","ghansham@444"));
        contactObjects.add(3,new ContactDetails("gaut","555","gaut@555"));*/


         listView=(ListView)findViewById(R.id.listView);
         adapter=new AddContacts(this,R.layout.layout,contactObjects2);
         listView.setAdapter(adapter);


        //ITEM CLICKED
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

       //HERE WE WILL CALL UpdateContactList

        Intent intent=new Intent(LoginPage.this,UpdateContactList.class);
        intent.putExtra("Name",contactObjects2.get(position).Name.toString());
        intent.putExtra("PhoneNo",contactObjects2.get(position).PhoneNo.toString());
        intent.putExtra("Email",contactObjects2.get(position).Email.toString());
        intent.putExtra("ImageId",contactObjects2.get(position).ImageId.toString());
        intent.putExtra("Key",contactObjects2.get(position).Key.toString());
        startActivity(intent);
    }
});

        }

        //ADD CONTACTS TO SIGNED IN USER

        public void  addContacts(View view)
        {Toast.makeText(LoginPage.this,"ADD CONTACT CLICKED",Toast.LENGTH_SHORT).show();

Intent intent=new Intent(LoginPage.this,UpdateContactList.class);
intent.putExtra("Key","");
startActivity(intent);
        }

public  void logout(View view)
{
    FirebaseAuth.getInstance().signOut();
    finish();
}
        //GET DATA FROM FIREBASE

    @Override
    protected  void onStart()
    {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onStart();
        conditionReference.child(mAuth.getUid().toString())
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(storageCaller) {
                    storageCaller=false;
                    Context context = getBaseContext();
                //    Toast.makeText(context, "Cloud storage called", Toast.LENGTH_SHORT).show();
                    contactObjects2.add(new ContactDetails2("DEFAULT", "111", "DEFAULT@111","c38be1e5-b7a1-4c80-8759-1c1093c2b1bf","DEFAULT KEY"));
                    for (DataSnapshot contactObjects : dataSnapshot.getChildren()) {
                        if (contactObjects != null) {
                            ContactDetails contacts = contactObjects.getValue(ContactDetails.class);
                            Log.i("Stored data is", contacts.Name);
                            Log.i("Stored data is", contacts.PhoneNo);
                            Log.i("Stored data is", contacts.Email);
                            Log.i("Stored data is", contacts.ImageId);
                            addToArrayList(contacts,contactObjects.getKey());
                        } else {
                            Log.i("Child", "Not available");
                        }
                        adapter.notifyDataSetChanged();
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addToArrayList(ContactDetails contacts,String Key)
    {
        contactObjects2.add(new ContactDetails2(contacts.Name,contacts.PhoneNo,contacts.Email,contacts.ImageId,Key));
        adapter.notifyDataSetChanged();
    }
}

