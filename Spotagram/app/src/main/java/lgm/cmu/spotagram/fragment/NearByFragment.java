package lgm.cmu.spotagram.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;
import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.adapter.NoteListAdapter;
import lgm.cmu.spotagram.model2.Note;

/**
 * Created by yulei on 2015/12/1.
 */
public class NearByFragment extends Fragment {
    private static final String TAG = "NearByFragment";

    private FrameLayout mMapFrameLayout;
    SupportMapFragment mMapFragment;
    private ListView mListView;
    private OnNoteSelectListener mOnNoteSelectListener;
    private List<Note> mNotes;
    private NoteListAdapter mNoteListAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.layout_map, mMapFragment);
        fragmentTransaction.commit();
        mListView = (ListView) view.findViewById(R.id.listview_notes);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // ...
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
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
        mNoteListAdapter = new NoteListAdapter(getActivity(),notes);
        mListView.setAdapter(mNoteListAdapter);
    }

    public void setmOnNoteSelectListener(OnNoteSelectListener lsn) {
        mOnNoteSelectListener = lsn;
    }

    public interface OnNoteSelectListener {
        void onNoteSelect(Note note);
    }
}
