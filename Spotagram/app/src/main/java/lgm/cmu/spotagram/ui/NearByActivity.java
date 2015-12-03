package lgm.cmu.spotagram.ui;


import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.fragment.NearByFragment;
import lgm.cmu.spotagram.fragment.PostDetailFragment;
import lgm.cmu.spotagram.model2.Comment;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.request.CommentsRequest;
import lgm.cmu.spotagram.request.NearByRequest;

public class NearByActivity extends AppCompatActivity implements NearByFragment.OnNoteSelectListener {
    private static final String TAG ="NearByActivity";
    private NearByFragment mNearByFragment;
    private PostDetailFragment mPostDetailFragment;
    private FrameLayout mNearByLayout;
    private FrameLayout mPostDetailLayout;

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
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

    public void initViews() {
        // check whether has nearby layout
        mNearByLayout = (FrameLayout) findViewById(R.id.layout_near_by);
        if (mNearByLayout != null) {
            mNearByFragment = new NearByFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(mNearByLayout.getId(), mNearByFragment,
                    NearByFragment.class.getName());
            fragmentTransaction.commit();
        }

        // check whether has detail layout, only contains when the device is tablet
        mPostDetailLayout = (FrameLayout) findViewById(R.id.layout_post_detail);
        if (mPostDetailLayout != null) {
            mPostDetailFragment = new PostDetailFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(mPostDetailLayout.getId(), mPostDetailFragment,
                    PostDetailFragment.class.getName());
            fragmentTransaction.commit();
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
        NearByRequest request = new NearByRequest(10, 10, 800000, 100, 0);
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

    // nothing, test function
    private void setTextData() {
        List<Note> notes = new ArrayList<>();
        Note note = new Note(1, 1, new Date(System.currentTimeMillis()), "haha", 0, 0, "jack", "info", "");
        note.setId(1);
        notes.add(note);

        mNearByFragment.setNotes(notes);
    }

    @Override
    public void onNoteSelect(Note note) {
        if (mPostDetailLayout == null) {
            // device is phone, switch to detail fragment
            mPostDetailFragment = new PostDetailFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(mNearByLayout.getId(), mPostDetailFragment, PostDetailFragment.class.getName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            Log.e(TAG, "noteId: " + note.getId());
            updatePosetDetail(note);
        } else {
            // device is tablet, show the info on detail fragment
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
