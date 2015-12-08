package lgm.cmu.spotagram.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.model2.Note;

/**
 * Created by yulei on 2015/12/1.
 */
public class NoteListAdapter extends BaseAdapter {
    private List<Note> mNotes;
    private Context mContext;
    private SimpleDateFormat mSimpleDateFormat;

    public NoteListAdapter(Context context, List<Note> notes) {
        mNotes = notes;
        mContext = context;
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_note, null);
            holder = new ViewHolder();

            holder.mImageView = (ImageView) convertView.findViewById(R.id.imageview_user_profile);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.textview_user_name);
            holder.mContentTextView = (TextView) convertView.findViewById(R.id.textview_note_info);
            holder.mDateTextView = (TextView) convertView.findViewById(R.id.textview_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = mNotes.get(position);
        holder.mNameTextView.setText(note.getUsername());
        holder.mContentTextView.setText(note.getContent());
        holder.mDateTextView.setText(mSimpleDateFormat.format(note.getDate()));

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(convertView.getContext()));
        imageLoader.displayImage(note.getUserURL(), holder.mImageView);

        return convertView;
    }

    public class ViewHolder {
        public ImageView mImageView;
        public TextView mNameTextView;
        public TextView mContentTextView;
        public TextView mDateTextView;
    }
}
