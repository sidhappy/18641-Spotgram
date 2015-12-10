package lgm.cmu.spotagram.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import lgm.cmu.spotagram.model.Comment;
import lgm.cmu.spotagram.model.Note;
import lgm.cmu.spotagram.request.CommentsRequest;
import lgm.cmu.spotagram.request.MyNotesRequest;
import lgm.cmu.spotagram.utils.ConstantValue;
import lgm.cmu.spotagram.utils.ParameterUtils;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView)findViewById(R.id.myNotes_list2);
        initComponments();


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


        int userID=ParameterUtils.getIntValue(ConstantValue.KEY_USER_ID);


        MyNotesRequest request=new MyNotesRequest(userID);
        request.setOnMyNotesReadyListener(new MyNotesRequest.OnMyNotesReadyListener() {
            @Override
            public void onNoteReady(boolean isSuccess, List<Note> notes) {
                if (isSuccess) {
                    setNotes(notes);
                    if(notes.size()<1){
                        Toast.makeText(MyNotesActivity.this,"You haven't posted any notes", Toast.LENGTH_LONG).show();
                    }
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
        mNoteListAdapter = new NoteListAdapter(MyNotesActivity.this,notes);
        mListView.setAdapter(mNoteListAdapter);
    }

    public void setmOnNoteSelectListener(OnNoteSelectListener lsn) {
        mOnNoteSelectListener = lsn;
    }

    public interface OnNoteSelectListener {

        public void onNoteSelect(Note note);

    }



}
