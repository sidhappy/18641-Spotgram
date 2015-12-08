package lgm.cmu.spotagram.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.model2.Comment;
import lgm.cmu.spotagram.request.NewNoteRequest;
import lgm.cmu.spotagram.request.ReplyNoteRequest;
import lgm.cmu.spotagram.request.UploadNoteImageRequest;
import lgm.cmu.spotagram.request.UploadProfileRequest;
import lgm.cmu.spotagram.utils.ConstantValue;
import lgm.cmu.spotagram.utils.ParameterUtils;

public class NewNoteActivity extends AppCompatActivity {

    private TextView locationTV;
    private String latitudeStr;
    private String longitudeStr;
    private ImageView imageView;
    private double mlat = 0.0;
    private double mlon = 0.0;
    private EditText noteET;
    private String targetURL = "noteservlet";
    private int userid;
    private String username;
    private int mNoteId;
    public static String imageName ;
    public static String filePath ;

    private static final int IMAGE_PICK = 1;
    private static final int IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView= (ImageView)findViewById(R.id.photo_image);

        ParameterUtils.initPreference(this);

        userid = ParameterUtils.getIntValue(ConstantValue.KEY_USER_ID);
        username = ParameterUtils.getStringValue(ConstantValue.KEY_USERNAME);


        Intent intent1 = getIntent();

        latitudeStr = intent1.getStringExtra("latitude");
        longitudeStr = intent1.getStringExtra("longitude");

        mlat = Double.valueOf(latitudeStr);
        mlon = Double.valueOf(longitudeStr);

        locationTV = (TextView)findViewById(R.id.textView);
        noteET = (EditText)findViewById(R.id.editText);

        locationTV.setText(latitudeStr + "," + longitudeStr);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void set_photo(View v){

        new AlertDialog.Builder(this)
                .setTitle("Take Photo Or Choose Photo")
                .setIcon(android.R.drawable.ic_input_add)
                .setNegativeButton("Take photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, IMAGE_CAPTURE);
                    }
                })
                .setPositiveButton("Choose photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Escolha uma Foto"), IMAGE_PICK);
                    }
                })
                .setCancelable(true)
                .show();

    }


    public void photo_upload(int noteid ,String imageName,String imagePath){

        UploadNoteImageRequest request= new UploadNoteImageRequest(noteid,imageName,imagePath);
        request.setOnPhotoUpdateListener(new UploadNoteImageRequest.OnPhotouploadListener() {
            @Override
            public void onPhotoReplied(boolean isSuccess, int uploadid) {
                if (isSuccess) {
                    Toast.makeText(getApplicationContext(), "Photo upload succeed ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Network err", Toast.LENGTH_SHORT).show();
                }
            }
        });

        request.execute();
    }

    private void imageFromCamera(int resultCode, Intent data) {
        this.imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));

        Bitmap bmp=(Bitmap)data.getExtras().get("data");

        MediaStore.Images.Media.insertImage(getContentResolver(),bmp,"title","description");
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

        Log.e(Uri.parse("file://" + Environment.getExternalStorageDirectory()).toString(), "Storage Location");

        bmp = scaleDownBitmap(bmp, 100, this);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();


    }

    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String [] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        this.imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));

        Bitmap bmp=BitmapFactory.decodeFile(filePath);
        bmp = scaleDownBitmap(bmp, 100, this);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();


        String[] temp=filePath.split("/");
        imageName=temp[temp.length-1];
        userid=ParameterUtils.getIntValue(ConstantValue.KEY_USER_ID);
        Log.v("AAAAAAAA", filePath + userid);



    }

    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }


    //send a new post, then return to map view
    public void send_and_return(View v){

        String content = noteET.getText().toString();
        if (content.equals("")){
            content = "What a Great Day!";
        }

        if (content != null && content.length() != 0) {

            NewNoteRequest request = new NewNoteRequest(userid, username, content, mlat, mlon);

            request.setOnNewNoteListener(new NewNoteRequest.OnNewNoteListener() {
                @Override
                public void onNewNote(boolean isSuccess, int noteId) {
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "Post success", Toast.LENGTH_SHORT).show();
                        mNoteId = noteId;

                        if (userid!=0){
                            photo_upload(mNoteId, imageName, filePath);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Internet err", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            request.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Note is null or no input", Toast.LENGTH_SHORT).show();
        }

        return_to_map();
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case IMAGE_PICK:
                    this.imageFromGallery(resultCode, data);
                    break;
                case IMAGE_CAPTURE:
                    this.imageFromCamera(resultCode, data);
                    break;
                default:
                    break;
            }
        }
    }


    //return to map view
    public void return_to_map(){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
