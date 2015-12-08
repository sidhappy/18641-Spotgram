package lgm.cmu.spotagram.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.adapter.NoteListAdapter;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.request.MyNotesRequest;
import lgm.cmu.spotagram.ui.SettingsActivity;
import lgm.cmu.spotagram.utils.ConstantValue;
import lgm.cmu.spotagram.utils.ParameterUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link myNotesFragment} interface
 * to handle interaction events.
 * Use the {@link myNotesFragment#} factory method to
 * create an instance of this fragment.
 */
public class myNotesFragment extends Fragment {
    private static final String TAG = "myNotesFragment";

    private ListView mListView;
    private OnNoteSelectListener mOnNoteSelectListener;
    private List<Note> mNotes;
    private NoteListAdapter mNoteListAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_notes, container, false);
        mListView = (ListView) view.findViewById(R.id.myNotes_list);


        //int userID= 4;
        int userID=ParameterUtils.getIntValue(ConstantValue.KEY_USER_ID);


        MyNotesRequest request=new MyNotesRequest(userID);
        request.setOnMyNotesReadyListener(new MyNotesRequest.OnMyNotesReadyListener() {
            @Override
            public void onNoteReady(boolean isSuccess, List<Note> notes) {
                if (isSuccess) {

                    setNotes(notes);
                    if(notes.size()<1){
                        Toast.makeText(getActivity(),"You haven't posted any notes", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(),"Network Err", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        request.execute();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mOnNoteSelectListener != null) {
//                    mOnNoteSelectListener.onNoteSelect(mNotes.get(position));
//                }
//            }
//        });

        if (mNoteListAdapter != null) {
            mListView.setAdapter(mNoteListAdapter);
        }
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
        mNoteListAdapter = new NoteListAdapter(getActivity(),notes);
        mListView.setAdapter(mNoteListAdapter);
    }

    public void setOnNoteSelectListener(OnNoteSelectListener lsn) {
        mOnNoteSelectListener = lsn;
    }

    public interface OnNoteSelectListener {
        void onNoteSelect(Note note);
    }
}
