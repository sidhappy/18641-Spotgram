package lgm.cmu.spotagram.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.fragment.myNotesFragment;
import lgm.cmu.spotagram.request.NewPasswordRequest;
import lgm.cmu.spotagram.request.UploadProfileRequest;
import lgm.cmu.spotagram.utils.ConstantValue;
import lgm.cmu.spotagram.utils.ParameterUtils;


public class SettingsActivity extends AppCompatActivity {

    private static final int IMAGE_PICK = 1;
    private static final int IMAGE_CAPTURE = 2;
    private ImageView imageView;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private int userId=0;
    /**
     * Check whether it's two pages mode(large equipment )
     */
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ParameterUtils.initPreference(SettingsActivity.this);

        imageView= (ImageView)findViewById(R.id.photo_image);
        text1=(TextView)findViewById(R.id.userName);
//        text2=(TextView)findViewById(R.id.introduction);
        text3=(TextView)findViewById(R.id.ID_OfSetting);

        if(ParameterUtils.getIntValue(ConstantValue.KEY_USER_ID)!=0){
            text3.setText(""+ParameterUtils.getIntValue(ConstantValue.KEY_USER_ID));
        }

        if(ParameterUtils.getStringValue(ConstantValue.KEY_USERNAME)!=null){
            text1.setText(""+ParameterUtils.getStringValue(ConstantValue.KEY_USERNAME));
        }


        if(ParameterUtils.getImageString(ConstantValue.IMAGE_URL_PATH)!=null){
            String imageString=ParameterUtils.getImageString(ConstantValue.IMAGE_URL_PATH);
            byte[] bitmapByte = Base64.decode(imageString, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(bitmapByte);
            Bitmap bitmap = BitmapFactory.decodeStream(bais);
            imageView.setImageBitmap(bitmap);
        }



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

    public void photo_upload(int userID,String imageName,String imagePath){

        UploadProfileRequest request= new UploadProfileRequest(userID,imageName,imagePath);
        request.setOnPhotoUpdateListener(new UploadProfileRequest.OnPhotouploadListener() {
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


    public void set_name(View v){
        final EditText inputServer = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Insert A New Name")
                .setIcon(android.R.drawable.ic_input_add)
                .setView(inputServer)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text1.setText(inputServer.getText().toString());
                        ParameterUtils.setStringValue(ConstantValue.KEY_USERNAME,inputServer.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null).show();


    }

    public void set_introduction(View v){
        final EditText inputServer2 = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Briefly introduce yourself")
                .setIcon(android.R.drawable.ic_input_add)
                .setView(inputServer2)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text2.setText(inputServer2.getText().toString());
                        ParameterUtils.setStringValue(ConstantValue.KEY_INFO, inputServer2.getText().toString());

                    }
                }).setNegativeButton("Cancel", null).show();

    }

    public void logout(View v){

    }
    public void set_notify(View v){

    }
    public void set_privacy(View v){
        Intent intent = new Intent(this, New_password_Activity.class);
        startActivity(intent);
    }

    public void open_Mynotes(View v){
            Intent intent = new Intent(this, MyNotesActivity.class);
            startActivity(intent);

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

        //store into preference
        ParameterUtils.setImageByte2String(ConstantValue.IMAGE_URL_PATH, bitmapByte);

    }

    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String [] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        this.imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));

        Bitmap bmp=BitmapFactory.decodeFile(filePath);
        bmp = scaleDownBitmap(bmp, 100, this);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();

        //store into preference
        ParameterUtils.setImageByte2String(ConstantValue.IMAGE_URL_PATH, bitmapByte);


        String[] temp=filePath.split("/");
        String imageName=temp[temp.length-1];
        userId=ParameterUtils.getIntValue(ConstantValue.KEY_USER_ID);
        Log.v("AAAAAAAA", filePath + userId);

        if (userId!=0){
           photo_upload(userId, imageName, filePath);
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
}
