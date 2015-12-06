package lgm.cmu.spotagram.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.fragment.myNotesFragment;
import lgm.cmu.spotagram.request.NewPasswordRequest;
import lgm.cmu.spotagram.request.UploadProfileRequest;


public class SettingsActivity extends AppCompatActivity {

    private static final int IMAGE_PICK = 1;
    private static final int IMAGE_CAPTURE = 2;
    private ImageView imageView;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private int userId;
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

        imageView= (ImageView)findViewById(R.id.photo_image);
        text1=(TextView)findViewById(R.id.userName);
        text2=(TextView)findViewById(R.id.introduction);
        text3=(TextView)findViewById(R.id.ID_OfSetting);
        userId=Integer.parseInt(text3.getText().toString());

        if (findViewById(R.id.notes_fragment) != null) {
            isTwoPane = true;
        } else {
            isTwoPane = false;
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
        Toast.makeText(this, "Aaaaaaaa 3", Toast.LENGTH_SHORT).show();

    }

    public void photo_upload(int userID,String imageName,String imagePath){

        UploadProfileRequest request= new UploadProfileRequest(userID,imageName,imagePath);
        request.setOnPhotoUpdateListener(new UploadProfileRequest.OnPhotouploadListener() {
            @Override
            public void onPhotoReplied(boolean isSuccess, int notes) {
                if (isSuccess) {
                    Toast.makeText(getApplicationContext(), "Photo uplode succeed ", Toast.LENGTH_SHORT).show();

//                            if (mNearByFragment != null) {
//                                mNearByFragment.setNotes(notes);
//                            } else {
//                                Toast.makeText(mContext, "Fragment err", Toast.LENGTH_SHORT).show();
//                            }
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

                    }
                }).setNegativeButton("Cancel", null).show();

    }

    public void set_general(View v){

    }
    public void set_notify(View v){

    }
    public void set_privacy(View v){
        Intent intent = new Intent(this, New_password_Activity.class);
        startActivity(intent);
    }

    public void open_Mynotes(View v){
//        if (isTwoPane) {
//            Fragment fragment = new myNotesFragment();
//            getFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();
//        } else {
            Intent intent = new Intent(this, MyNotesActivity.class);
            startActivity(intent);
//        }
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
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

        Toast.makeText(this, "Aaaaaaaa 1", Toast.LENGTH_SHORT).show();

//        photo_upload(userId, imageName, filePath);
//        Toast.makeText(this, "Aaaaaaaa 2", Toast.LENGTH_SHORT).show();
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

        //text3=(TextView)findViewById(R.id.ID_OfSetting);
//        if(text3==null) {
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Toast.makeText(this, text3.getText().toString(), Toast.LENGTH_SHORT).show();
//        int userId=Integer.parseInt(text3.getText().toString());
        String[] temp=filePath.split("/");
        String imageName=temp[temp.length-1];
        Log.v(imageName, filePath+userId);
        Toast.makeText(this, "Aaaaaaaa 1", Toast.LENGTH_SHORT).show();

//        photo_upload(userId, imageName, filePath);
//        Toast.makeText(this, "Aaaaaaaa 2", Toast.LENGTH_SHORT).show();
    }

}
