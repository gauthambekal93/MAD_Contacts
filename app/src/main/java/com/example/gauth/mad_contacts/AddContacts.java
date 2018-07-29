package com.example.gauth.mad_contacts;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AddContacts extends ArrayAdapter<ContactDetails2> {
     ImageView ContactImageView;
    long megabyte = 1024 * 1024;
    public AddContacts(@NonNull Context context, int resource, @NonNull List<ContactDetails2> objects2) {
        super(context, resource, objects2);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
ContactDetails2 contactDetails2=getItem(position);
if(convertView==null)
{
    convertView= LayoutInflater.from(getContext()).inflate(R.layout.layout,parent,false);
}

        TextView ContactNameText=(TextView) convertView.findViewById(R.id.contactNameText);
        TextView ContactPhoneNoText=(TextView) convertView.findViewById(R.id.contactPhonenoText);
        TextView ContactEmailText=(TextView) convertView.findViewById(R.id.contactEmailText);
        TextView ContactKeyText=(TextView)convertView.findViewById(R.id.contactKeyText);
        ContactImageView=(ImageView)convertView.findViewById(R.id.contactImageText12);
        TextView ContactImageIdText=(TextView) convertView.findViewById(R.id.contactImageIdText);
        //SET CONTACT DETAILS

        ContactNameText.setText("NAME: "+contactDetails2.Name);
        ContactPhoneNoText.setText("Phone No: "+contactDetails2.PhoneNo);
        ContactEmailText.setText("Email: "+contactDetails2.Email);
        ContactKeyText.setText("KEY "+contactDetails2.Key);

        //SET IMAGE
if(contactDetails2.ImageId.length()>1) {
 ContactImageIdText.setText("Image Id  "+contactDetails2.ImageId);
    StorageReference tempRef = FirebaseStorage.getInstance().getReference().
            child("ContactPhotos").child(contactDetails2.ImageId + ".png");

    Context context=getContext();
  //  Toast.makeText(context, tempRef.toString(),Toast.LENGTH_LONG).show();

    tempRef.getBytes(megabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
        @Override
        public void onSuccess(byte[] bytes) {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            ContactImageView.setAdjustViewBounds(true);
            ContactImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

        }
    });
}
return convertView;
    }
}
