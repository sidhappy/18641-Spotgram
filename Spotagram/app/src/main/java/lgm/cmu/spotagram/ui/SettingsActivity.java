package lgm.cmu.spotagram.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import lgm.cmu.spotagram.R;

public class SettingsActivity extends AppCompatActivity {

    private static final int IMAGE_PICK = 1;
    private static final int IMAGE_CAPTURE = 2;
    private ImageView imageView;
    private TextView text1;
    private TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView= (ImageView)findViewById(R.id.photo_image);
        text1=(TextView)findViewById(R.id.userName);
        text2=(TextView)findViewById(R.id.introduction);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
                .setNegativeButton("Cancel",null).show();
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
                }).setNegativeButton("Cancel",null).show();

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
    }

}
