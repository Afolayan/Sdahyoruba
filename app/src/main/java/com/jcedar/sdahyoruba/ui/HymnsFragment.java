package com.jcedar.sdahyoruba.ui;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcedar.sdahyoruba.R;
import com.jcedar.sdahyoruba.provider.DataContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class HymnsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARGS_ALL_STUDENT_ID = "all_student_id";
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = HymnsFragment.class.getSimpleName();
    private static final String ARGS_HYMN_ID = "hymn_id";
    private Handler handler;
    private  Uri dataUri;
    private TextView hymnTitle, hymnText, hymnEnglishVersion, hymnNumber;
    private long hymnId ;
    private String nameStr = "";


    public HymnsFragment() {
    }


    public static HymnsFragment newInstance(long hymnId) {
        HymnsFragment fragment = new HymnsFragment();

        Bundle args = new Bundle();
        args.putLong(ARGS_HYMN_ID, hymnId);

        fragment.setArguments(args);

        return fragment;
    }
    public static HymnsFragment newInstance(int position,
                                                        NewDashBoardActivity hostActivity) {

        HymnsFragment fragment = new HymnsFragment();
        Bundle args = new Bundle();
        //long _id = hostActivity.fragments.get(position);
        args.putLong(ARGS_HYMN_ID, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        Bundle args = getArguments();
        if (args != null) {
            hymnId = args.getLong(ARGS_HYMN_ID);
            dataUri = DataContract.Hymns.buildHymnUri(hymnId);
            Log.e(TAG, dataUri+" uri inside ");

        }
       // setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_hymns, container, false);

        hymnNumber = (TextView) rootView.findViewById(R.id.tvHymnNumber);
        hymnTitle = (TextView) rootView.findViewById(R.id.tvHymnTitle);
        hymnText = (TextView) rootView.findViewById(R.id.tvHymnText);
        hymnEnglishVersion = (TextView) rootView.findViewById(R.id.tvEnglishVersion);

        hymnText.setTextIsSelectable(true);

        getLoaderManager().initLoader(0, Bundle.EMPTY, this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getActivity().setTitle("SDAH Yoruba");
            }
        });
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), dataUri,
                DataContract.Hymns.PROJECTION_ALL, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (getActivity() == null) {
            return;
        }

        if(data != null && data.moveToFirst()) {

            String hymnId = data.getString(
                    data.getColumnIndexOrThrow(DataContract.Hymns._ID));

             String numberStr = data.getString(
                    data.getColumnIndexOrThrow(DataContract.Hymns.SONG_ID));
            hymnNumber.setText(numberStr);

            nameStr = data.getString(
                                data.getColumnIndexOrThrow(DataContract.Hymns.SONG_NAME));
            hymnTitle.setText(nameStr);


            String engVersionStr = data.getString(
                    data.getColumnIndexOrThrow(DataContract.Hymns.ENGLISH_VERSION));
            hymnEnglishVersion.setText(engVersionStr);

            String text = data.getString(
                    data.getColumnIndexOrThrow(DataContract.Hymns.SONG_TEXT));

            String text1 = text.replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
            hymnText.setText( Html.fromHtml(text1));



            data.close();
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            if (!isAdded()) {
                return;
            }
            //getLoaderManager().restartLoader(0, null, HymnsFragment.this);
        }
    };



    public interface Listener{
        public void onFragmentAttached(Fragment fragment);
        public void onFragmentDetached(Fragment fragment);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
