package lgm.cmu.spotagram.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.adapter.NoteListAdapter;
import lgm.cmu.spotagram.fragment.PostDetailFragment;
import lgm.cmu.spotagram.fragment.myNotesFragment;
import lgm.cmu.spotagram.model2.Comment;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.request.CommentsRequest;
import lgm.cmu.spotagram.request.MyNotesRequest;
import lgm.cmu.spotagram.request.NearByRequest;

public class MyNotesActivity extends AppCompatActivity {

    private ListView mListView;
    private OnNoteSelectListener mOnNoteSelectListener;
    private List<Note> mNotes;
    private NoteListAdapter mNoteListAdapter;
    private TextView IDtext;
    private Context mContext;

    private PostDetailFragment mPostDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView)findViewById(R.id.myNotes_list2);
        initComponments();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnNoteSelectListener != null) {
                    mOnNoteSelectListener.onNoteSelect(mNotes.get(position));
                }
            }
        });

        if (mNoteListAdapter != null) {
            mListView.setAdapter(mNoteListAdapter);
        }


        IDtext=(TextView)findViewById(R.id.ID_OfSetting);
//        int userID=Integer.parseInt(IDtext.getText().toString());
        int userID=4;
        MyNotesRequest request=new MyNotesRequest(userID);
        request.setOnMyNotesReadyListener(new MyNotesRequest.OnMyNotesReadyListener() {
            @Override
            public void onNoteReady(boolean isSuccess, List<Note> notes) {
                if (isSuccess) {
                    setNotes(notes);
                } else {
                    Toast.makeText(getApplicationContext(), "Network err", Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.execute();
        if(mListView==null){
            Toast.makeText(getApplicationContext(), "mlistview is null", Toast.LENGTH_SHORT).show();
        }



    }

    public void initComponments() {
        mPostDetailFragment = new PostDetailFragment();

        mOnNoteSelectListener= new OnNoteSelectListener() {
            @Override
            public void onNoteSelect(Note note) {

//                updatePosetDetail(note);
            }
        };


        mContext = MyNotesActivity.this;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
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



    public void setNotes(List<Note> notes) {
        mNotes = notes;
        mNoteListAdapter = new NoteListAdapter(getApplicationContext(),notes);
        mListView.setAdapter(mNoteListAdapter);
    }

    public void setmOnNoteSelectListener(OnNoteSelectListener lsn) {
        mOnNoteSelectListener = lsn;
    }

    public interface OnNoteSelectListener {

        public void onNoteSelect(Note note);

    }



}
