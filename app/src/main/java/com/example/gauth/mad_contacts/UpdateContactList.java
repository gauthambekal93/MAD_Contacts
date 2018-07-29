package com.example.gauth.mad_contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateContactList extends AppCompatActivity {


    public DatabaseReference databaseReference;
    public DatabaseReference conditionReference;
    private FirebaseAuth mAuth;
    EditText contactNameEdit;
    EditText contactPhonenoEdit;
    EditText contactEmailEdit;
    Button save;
    Button update;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance("gs://firecast-app-65037.appspot.com");
    ImageView image_view;
    String path;
    Bitmap bitmap;
    long megabyte = 1024 * 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_contact_list);

        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        conditionReference=databaseReference.child("ContactDetails");
        conditionReference.child(mAuth.getUid().toString()); //add a new signed in user
         contactNameEdit=(EditText) findViewById(R.id.contactNameEdit);
         contactPhonenoEdit=(EditText) findViewById(R.id.contactPhonenoEdit);
         contactEmailEdit=(EditText) findViewById(R.id.contactEmailEdit);
        save=(Button)findViewById(R.id.saveContact);
        update=(Button)findViewById(R.id.updateContact);
        image_view=findViewById(R.id.contactImageEdit);

save.setVisibility(View.VISIBLE);
         update.setVisibility(View.VISIBLE);


         Intent intent=getIntent();
         if((intent.getStringExtra("Key").length())>1)
         {
             Toast.makeText(this,"UPDATE CONTACT",Toast.LENGTH_SHORT).show();
             contactNameEdit.setText(intent.getStringExtra("Name"));
             contactPhonenoEdit.setText(intent.getStringExtra("PhoneNo"));
             contactEmailEdit.setText(intent.getStringExtra("Email"));

             if(intent.getStringExtra("ImageId").length()>1) {
                 StorageReference tempRef = FirebaseStorage.getInstance().getReference().
                         child("ContactPhotos").child(intent.getStringExtra("ImageId") + ".png");

                 tempRef.getBytes(megabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                     @Override
                     public void onSuccess(byte[] bytes) {
                         BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                         image_view.setAdjustViewBounds(true);
                         image_view.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                     }
                 });
             }
             save.setVisibility(View.INVISIBLE);
         }else{
             Toast.makeText(this,"ADD NEW CONTACT",Toast.LENGTH_SHORT).show();
             update.setVisibility(View.INVISIBLE);
         }


//IMAGE UPLOAD STARTS HERE
image_view.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {

        Log.i("Check button","Clicked!!!");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            Log.i("Check button","Clicked!!!");
            int permissionCheck=UpdateContactList.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck+=UpdateContactList.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if(permissionCheck!=0)
            {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            else {
                Log.i("No need","To check permission");
                getPhoto();
            }
        }else {getPhoto();}
    }

});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED);
            {
                getPhoto();
            }

        }
    }


    public  void getPhoto()
    {
        Toast.makeText(this,"get photo called!!",Toast.LENGTH_SHORT).show();
        Intent intent_photo=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  intent_photo.setType("image/*");
        startActivityForResult(intent_photo,1);
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK &&data!=null)
            try {
                Uri selectedImage = data.getData();
                // imageContainer.setDrawingCacheEnabled(true);
                //         Bitmap bitmap=imageContainer.getDrawingCache();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                image_view.setImageBitmap(bitmap);

            } catch (Exception e) {
                Log.i("Some error", "in converting the image!!!");
            }
    }



    //Add a new contact to a given signed in user into Firebase

    public void saveContact(View view)
{   String ImageUid="";
    Toast.makeText(this,"SAVE CLICKED",Toast.LENGTH_SHORT).show();
    //ADD NEW IMAGE UPLOAD HERE
    if(bitmap!=null){
   ImageUid= uploadPhoto();

    }
    conditionReference.child(mAuth.getUid().toString()).push()
            .setValue(new ContactDetails(contactNameEdit.getText().toString(),contactPhonenoEdit.getText().toString(),
                    contactEmailEdit.getText().toString(),ImageUid.toString()));
    Toast.makeText(this, "UID IS!!  "+ImageUid,Toast.LENGTH_LONG).show();

    LoginPage.contactObjects2.clear();
    LoginPage.storageCaller = true;
    finish();

}

public String uploadPhoto()
{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    //   imageContainer.setDrawingCacheEnabled(false);
    byte[] byteArray = baos.toByteArray();
    final String random = UUID.randomUUID().toString();
    path = "ContactPhotos/" + random + ".png";
    storageReference = firebaseStorage.getReference(path);

    UploadTask uploadTask = storageReference.putBytes(byteArray); //this line
    uploadTask.addOnSuccessListener(UpdateContactList.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
          //  Toast.makeText(UpdateContactList.this, "Photo Uploaded!!", Toast.LENGTH_SHORT).show();
            StorageReference tempRef = FirebaseStorage.getInstance().getReference().child("ContactPhotos").child(random + ".png");
            Log.i("Address is", tempRef.toString());
            long megabyte = 1024 * 1024;
            tempRef.getBytes(megabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    image_view.setAdjustViewBounds(true);
                    image_view.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            });
        }
    });
    return random;
}


//CANCEL THE CHANGES MADE ON CONTACT

public void cancelChanges(View view)
{
    Toast.makeText(this,"CANCEL CLICKED",Toast.LENGTH_SHORT).show();

    contactNameEdit.setText("");
    contactPhonenoEdit.setText("");
    contactEmailEdit.setText("");
Toast.makeText(this,conditionReference.child(mAuth.getUid().toString()).getKey().toString(),
        Toast.LENGTH_LONG).show();
    Log.i("Key is",conditionReference.child(mAuth.getUid().toString()).getKey().toString());

}

//UPDATE THE CONTACT

public void  updateContact(View view) throws InterruptedException {
    Toast.makeText(this,"UPDATE CLICKED",Toast.LENGTH_SHORT).show();
    String ImageUid="";
    Intent intent=getIntent();
    if(bitmap!=null){
        ImageUid= uploadPhoto();

    }
    Map<String,Object> taskMap=new HashMap<>();
    taskMap.put("Name",contactNameEdit.getText().toString());
    taskMap.put("PhoneNo",contactPhonenoEdit.getText().toString());
    taskMap.put("Email",contactEmailEdit.getText().toString());
    taskMap.put("ImageId", ImageUid.toString());

    conditionReference.child(mAuth.getUid().toString())
            .child(intent.getStringExtra("Key")).setValue(taskMap);

    LoginPage.contactObjects2.clear();
    LoginPage.storageCaller=true;
finish();
}

//DELETE FROM FIREBASE
public  void  deleteContact(View view)
{Toast.makeText(this,"DELETE CLICKED",Toast.LENGTH_SHORT).show();
    Intent intent=getIntent();
    conditionReference.child(mAuth.getUid().toString()).child(intent.getStringExtra("Key")).removeValue();
    LoginPage.contactObjects2.clear();
    LoginPage.storageCaller=true;
    finish();
}
}
