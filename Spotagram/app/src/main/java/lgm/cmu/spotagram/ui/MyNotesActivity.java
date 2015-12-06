package lgm.cmu.spotagram.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.adapter.NoteListAdapter;
import lgm.cmu.spotagram.fragment.myNotesFragment;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.request.MyNotesRequest;

public class MyNotesActivity extends AppCompatActivity {
    private ListView mListView;
    private OnNoteSelectListener mOnNoteSelectListener;
    private List<Note> mNotes;
    private NoteListAdapter mNoteListAdapter;
    private TextView IDtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView)findViewById(R.id.myNotes_list);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        IDtext=(TextView)findViewById(R.id.userID);
        int userID=Integer.parseInt(IDtext.getText().toString());
        MyNotesRequest request=new MyNotesRequest(userID);
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
        void onNoteSelect(Note note);
    }

}
