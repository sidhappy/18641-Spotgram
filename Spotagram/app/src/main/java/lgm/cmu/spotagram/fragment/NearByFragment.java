package lgm.cmu.spotagram.fragment;

import android.content.Context;
import android.location.Location;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.adapter.NoteListAdapter;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.utils.ConstantValue;
import lgm.cmu.spotagram.utils.MapUtils;

/**
 * Created by yulei on 2015/12/1.
 */
public class NearByFragment extends Fragment{
    private static final String TAG = "NearByFragment";

    private FrameLayout mMapFrameLayout;
    SupportMapFragment mMapFragment;
    private ListView mListView;
    private OnNoteSelectListener mOnNoteSelectListener;
    private List<Note> mNotes;
    private NoteListAdapter mNoteListAdapter;

    private GoogleMap mMap;
    private double mLat;
    private double mLon;
    private ArrayList<String> markers;

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

        Bundle data = getArguments();//获得从activity中传递过来的值
        mLat = Double.valueOf(data.getString(ConstantValue.KEY_LOC_LATITUDE));
        mLon = Double.valueOf(data.getString(ConstantValue.KEY_LOC_LONGITUDE));
        markers = data.getStringArrayList(ConstantValue.KEY_LOC_STRING_ARR);

        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                MapUtils.setMapFreeze(mMap);
                setCurrentLocation(mLat, mLon);
                setMarkers();
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


    public void setCurrentLocation(double lat, double lon){

        LatLng cur = new LatLng(lat,lon);

        mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(cur)
                .title("You are HERE"));

        CameraPosition cp = new CameraPosition.Builder()
                .target(cur)
                .zoom(15)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp), 3000, null);
    }

    public void setMarkers(){
        if (markers == null || markers.size() <= 3|| markers.size() % 3 != 0){
            return;
        }
        else {
            for (int i = 0; i < markers.size(); i+= 3){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(markers.get(i)), Double.valueOf(markers.get(i+1))))
                        .title(markers.get(i+2))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }

        }
    }

}
