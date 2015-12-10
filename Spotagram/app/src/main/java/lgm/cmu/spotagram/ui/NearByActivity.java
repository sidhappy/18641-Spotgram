package lgm.cmu.spotagram.ui;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.fragment.NearByFragment;
import lgm.cmu.spotagram.fragment.PostDetailFragment;
import lgm.cmu.spotagram.model.Comment;
import lgm.cmu.spotagram.model.Note;
import lgm.cmu.spotagram.request.CommentsRequest;
import lgm.cmu.spotagram.request.NearByRequest;
import lgm.cmu.spotagram.utils.ConstantValue;

public class NearByActivity extends AppCompatActivity implements NearByFragment.OnNoteSelectListener {
    private static final String TAG ="NearByActivity";
    private NearByFragment mNearByFragment;
    private PostDetailFragment mPostDetailFragment;
    private FrameLayout mNearByLayout;
    private FrameLayout mPostDetailLayout;

    private double mLat;
    private double mLon;
    private ArrayList<String> markers;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponments();
        initViews();
        initListeners();
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


    public void initComponments() {
        mContext = NearByActivity.this;
        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(displayOptions).build();
        ImageLoader.getInstance().init(config);

        Intent intent1 = getIntent();
        mLat = intent1.getDoubleExtra(ConstantValue.KEY_LOC_LATITUDE, 0.0);
        mLon = intent1.getDoubleExtra(ConstantValue.KEY_LOC_LONGITUDE, 0.0);
        markers = intent1.getStringArrayListExtra(ConstantValue.KEY_LOC_STRING_ARR);

    }

    public void initViews() {
        // check whether has nearby layout
        mNearByLayout = (FrameLayout) findViewById(R.id.layout_near_by);
        if (mNearByLayout != null) {
            mNearByFragment = new NearByFragment();

            Bundle data = new Bundle();
            data.putString(ConstantValue.KEY_LOC_LATITUDE, mLat+"");
            data.putString(ConstantValue.KEY_LOC_LONGITUDE, mLon+"");
            data.putStringArrayList(ConstantValue.KEY_LOC_STRING_ARR, markers);
            mNearByFragment.setArguments(data);


            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(mNearByLayout.getId(), mNearByFragment,
                    NearByFragment.class.getName());
            fragmentTransaction.commit();
        }

        // check whether has detail layout, only contains when the device is tablet
        mPostDetailLayout = (FrameLayout) findViewById(R.id.layout_post_detail);
        if (mPostDetailLayout != null) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public void initListeners() {
        mNearByFragment.setmOnNoteSelectListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setTextData();
        sendNoteRequest();
    }

    private void sendNoteRequest() {
        NearByRequest request = new NearByRequest((float)mLon, (float)mLat, 10, 100, 0);
        request.setOnNoteReadyListener(new NearByRequest.OnNoteReadyListener() {
            @Override
            public void onNoteReady(boolean isSuccess, List<Note> notes) {
                if (isSuccess) {
                    if (mNearByFragment != null) {
                        mNearByFragment.setNotes(notes);
                    } else {
                        Toast.makeText(mContext, "Fragment err", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Network err", Toast.LENGTH_SHORT).show();
                }
            }
        });

        request.execute();
    }


    @Override
    public void onNoteSelect(Note note) {
        if (mPostDetailLayout == null) {
            // device is phone, switch to detail fragment
            if (mPostDetailFragment == null) {
                mPostDetailFragment = new PostDetailFragment();
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(mNearByLayout.getId(), mPostDetailFragment, PostDetailFragment.class.getName());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            Log.e(TAG, "noteId: " + note.getId());
            updatePosetDetail(note);
        } else {
            // device is tablet, show the info on detail fragment
            if (mPostDetailFragment == null) {
                mPostDetailFragment = new PostDetailFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(mPostDetailLayout.getId(), mPostDetailFragment,
                        PostDetailFragment.class.getName());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            updatePosetDetail(note);
        }

    }

    private void updatePosetDetail(final Note note) {

        int noteId = note.getId();

        CommentsRequest request = new CommentsRequest(noteId);
        request.setOnCommentReadyListener(new CommentsRequest.OnCommentsReadyListener() {
            @Override
            public void onCommentsReady(boolean isSuccess, List<Comment> comments) {
                if (isSuccess) {
                    if (mPostDetailFragment != null) {
                        mPostDetailFragment.setNoteInfo(note);
                        mPostDetailFragment.setComments(comments);
                    } else {
                        Toast.makeText(mContext, "Detail Fragment err", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Network err", Toast.LENGTH_SHORT).show();
                }

            }
        });

        request.execute();
    }
}
