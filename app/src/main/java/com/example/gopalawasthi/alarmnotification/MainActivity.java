package com.example.gopalawasthi.alarmnotification;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.messgebutton).setOnClickListener(this);
    }


    public void sharetext(View view) {
        Intent shareintent  = new Intent();
        shareintent.setAction(Intent.ACTION_SEND);
        shareintent.setType("text/plain");
        shareintent.putExtra(Intent.EXTRA_TEXT ,"welcome to android");
        startActivity(Intent.createChooser(shareintent,"send message"));

    }

    public void callme(View view){
    int permissiongranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
    if(permissiongranted == PackageManager.PERMISSION_GRANTED){
        callus();
    }else{
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE},1);
    }
    }

    private void callus() {
        Intent shareintent = new Intent();
        shareintent.setAction(Intent.ACTION_CALL);
        shareintent.setData(Uri.parse("tel:999999999"));
        shareintent.setPackage("com.android.server.telecom");
        startActivity(shareintent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            int permit = grantResults [0];
            if(permit == PackageManager.PERMISSION_GRANTED){
                callus();
            }else{
                Toast.makeText(this,"permission is denied",Toast.LENGTH_SHORT).show();
            }

        } else if(requestCode == 2){
            int permit = grantResults[0];
            if(permit == PackageManager.PERMISSION_GRANTED){
                showMessage();
            }else{
                Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == 3){
            int permit = grantResults[0];
            if(permit == PackageManager.PERMISSION_GRANTED){
                SendMessage();
            } else{
                Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void opengmail(View view) {
        Intent shareintent = new Intent();
        shareintent.setData(Uri.parse("mailto:gopalawasthi383@gmail.com"));
        shareintent.putExtra(Intent.EXTRA_SUBJECT , "custom mail");
        shareintent.putExtra(Intent.EXTRA_TEXT,"this app is great");
        startActivity(shareintent);
    }

    public void openurl(View view) {
        Intent shareintent = new Intent();
        shareintent.setData(Uri.parse("https://www.amazon.in/OnePlus-Midnight-Black-128GB-memory/dp/B0757142K9/ref=sr_1_1?ie=UTF8&qid=1520099256&sr=8-1&keywords=oneplus+5t+mobile+128+gb+dual+sim"));

        startActivity(Intent.createChooser(shareintent,"social life"));
    }

    public void broadcast(View view) {
       int permissioncheck = ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);
       if(permissioncheck == PackageManager.PERMISSION_GRANTED){

           SendMessage();
       }else{

           ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.SEND_SMS},3);
       }

    }

    private void SendMessage() {
        String number = "+919999999999";
        String smsBody = "Testing";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number,null,smsBody,null,null);

    }

    public void camera(View view) {
        Intent shareintent = new Intent();
        shareintent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        startActivity(Intent.createChooser(shareintent,"click a pic"));
    }


ArrayList<String > arrayList = new ArrayList<String>();
    private void showMessage() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query( Uri.parse( "content://sms/inbox" ), null, null, null, null);

        int indexBody = cursor.getColumnIndex( NotificationReceiver.BODY );
        int indexAddr = cursor.getColumnIndex( NotificationReceiver.ADDRESS );

        if ( indexBody < 0 || !cursor.moveToFirst() ) return;

        arrayList.clear();

        do
        {
            String str = "Sender: " + cursor.getString( indexAddr ) + "\n" + cursor.getString( indexBody );
            arrayList.add( str );
        }
        while( cursor.moveToNext() );

      ListView  listView =  findViewById( R.id.listview );
        listView.setAdapter( new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList));
        listView.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(this,"access ",Toast.LENGTH_SHORT).show();
       try
        {
            String[] splitted = arrayList.get(position).split("\n");
            String sender = splitted[0];
            String encryptedData = "";
            for ( int i = 1; i < splitted.length; ++i )
            {
                encryptedData += splitted[i];
            }
            String data = sender + "\n" + SmsCrypt.decrypt( new String(NotificationReceiver.PASSWORD), encryptedData );
            Toast.makeText( this, data, Toast.LENGTH_SHORT ).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        int permissionquery = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS);
        if(permissionquery == PackageManager.PERMISSION_GRANTED){
            showMessage();
        } else if(permissionquery == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS},2);
        }
    }
}

