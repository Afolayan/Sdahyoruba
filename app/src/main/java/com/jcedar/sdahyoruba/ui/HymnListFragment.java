package com.jcedar.sdahyoruba.ui;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jcedar.sdahyoruba.R;
import com.jcedar.sdahyoruba.adapter.RecyclerCursorAdapterAll;
import com.jcedar.sdahyoruba.provider.DataContract;

import java.util.Arrays;

public class HymnListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    protected static final String NAIRA = "\u20A6";
    private static final String TAG = HymnListFragment.class.getSimpleName();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_IDS = "ids";
    private static final String SEARCH_KEY = "SEARCH_KEY";

    // TODO: Rename and change types of parameters
    private int mPosition;
    private String mParam2;
    private TextView tvError;
    private Bundle mHomeBundle = Bundle.EMPTY;
    private String _POSITION = "position";
    private String LOADER_KEY = "loader_key";
    private Listener mCallback;
    static String context;

    RecyclerView recyclerView;
    RecyclerCursorAdapterAll resultsCursorAdapter;
    View rootView;
    String[] idsToLoad;

    private static final int NORMAL_LOADER_ID = 1;
    private static final int A_Z_LOADER_ID = 2;
    private static final int SEARCH_LOADER_ID = 3;

    static int presentId;
    MenuItem aToZ, numerical;

    public static HymnListFragment newInstance(int position) {
        HymnListFragment fragment = new HymnListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);


        fragment.setArguments(args);
        return fragment;
    }
    public static HymnListFragment newInstance(String[] ids) {
            HymnListFragment fragment = new HymnListFragment();
            Bundle args = new Bundle();
            args.putStringArray(ARG_IDS, ids);


            fragment.setArguments(args);
            return fragment;
        }

    public HymnListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize loader
        //getLoaderManager().initLoader(NORMAL_LOADER_ID, null, this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( savedInstanceState != null){
            presentId = savedInstanceState.getInt(LOADER_KEY);
        } else {
            presentId = NORMAL_LOADER_ID;
        }

        if (getArguments() != null) {
            idsToLoad = getArguments().getStringArray(ARG_IDS);
            Log.e(TAG, "ids " + Arrays.toString(idsToLoad));
            getLoaderManager().initLoader(A_Z_LOADER_ID, null, this);
        }


        context = getActivity().getClass().getSimpleName();

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LOADER_KEY, presentId);
        Log.e(TAG, "present id " + presentId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if( rootView != null ){
            if(  rootView.getParent() != null ){
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ((ViewGroup) rootView.getParent()).removeView(rootView);
                    }
                });

            }
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        tvError = (TextView) rootView.findViewById(R.id.tvErrorMag);


        recyclerView = (RecyclerView) rootView.findViewById( R.id.recyclerview );
        resultsCursorAdapter = new RecyclerCursorAdapterAll( getActivity() );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setLayoutManager(new WrappingLinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        recyclerView.setAdapter(resultsCursorAdapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getActivity().setTitle("Topical Index");
            }
        });
        resultsCursorAdapter.setOnItemClickListener(new RecyclerCursorAdapterAll.OnItemClickListener() {
            @Override
            public void onItemClicked(Cursor data) {

                long Id = data.getLong(
                        data.getColumnIndex(DataContract.Hymns._ID));

                // add position to bundle
                //mHomeBundle.putInt(_POSITION, position);
                mCallback.onHymnSelected(Id);


            }
        });

        return rootView;
    }



    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        aToZ = menu.findItem(R.id.action_alpha);
        numerical = menu.findItem(R.id.action_numerical);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case ( R.id.action_numerical):
                if(item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Toast.makeText(getActivity(), "Numerical", Toast.LENGTH_SHORT).show();
                getLoaderManager().restartLoader(NORMAL_LOADER_ID, null, this);
                break;
            case R.id.action_alpha:
                if(item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Toast.makeText(getActivity(), "A-Z", Toast.LENGTH_SHORT).show();
                getLoaderManager().restartLoader(A_Z_LOADER_ID, null, this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_hymn_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SEARCH_KEY, query);

                    getLoaderManager().restartLoader(SEARCH_LOADER_ID,
                            bundle, HymnListFragment.this);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SEARCH_KEY, newText);

                    getLoaderManager().restartLoader(SEARCH_LOADER_ID,
                            bundle, HymnListFragment.this);
                }
                return false;
            }
        });
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DataContract.Hymns.CONTENT_URI;

        switch (id){
            case NORMAL_LOADER_ID:
                presentId = NORMAL_LOADER_ID;
                return new CursorLoader(
                    getActivity(),
                    uri,
                    DataContract.Hymns.PROJECTION_ALL,
                    null,    // selection
                    null,           // arguments
                    DataContract.Hymns._ID + " ASC"
            );
            case A_Z_LOADER_ID:{
                presentId = A_Z_LOADER_ID;

                return new CursorLoader(
                        getActivity(),
                        uri,
                        DataContract.Hymns.PROJECTION_ALL,
                        null,    // selection
                        null,           // arguments
                        DataContract.Hymns.SONG_NAME + " ASC");
            }

            case SEARCH_LOADER_ID: {
                presentId = SEARCH_LOADER_ID;

                if (args != null) {
                    String query = args.getString(SEARCH_KEY);

                    String selection1 = DataContract.Hymns.SONG_NAME + " LIKE '%" +query + "%'";

                    return new CursorLoader(
                            getActivity(),
                            uri,
                            DataContract.Hymns.PROJECTION_ALL,
                            selection1,    // selection
                            null,           // arguments
                            DataContract.Hymns.SONG_NAME + " ASC");

                    } else {
                    return new CursorLoader(
                            getActivity(),
                            uri,
                            DataContract.Hymns.PROJECTION_ALL,
                            null,    // selection
                            null,           // arguments
                            DataContract.Hymns.SONG_NAME + " ASC");
                    }
                }

            default:{
                presentId = NORMAL_LOADER_ID;
                return new CursorLoader(
                        getActivity(),
                        uri,
                        DataContract.Hymns.PROJECTION_ALL,
                        null,    // selection
                        null,           // arguments
                        DataContract.Hymns.SONG_NAME + " ASC");
            }
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        if(cursor.moveToFirst()) {
            resultsCursorAdapter.swapCursor(cursor);

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //resultsCursorAdapter.swapCursor(null);
    }


    interface Listener {
        void onHymnSelected(long hymnId);
        void onFragmentDetached(Fragment fragment);
        void onFragmentAttached(Fragment fragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof Listener) {
            mCallback = (Listener) activity;
            mCallback.onFragmentAttached(this);
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement fragments listener");
        }
        activity.getContentResolver().registerContentObserver(
                DataContract.Hymns.CONTENT_URI, true, mObserver);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        super.onDetach();
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentDetached(this);
        }
        getActivity().getContentResolver().unregisterContentObserver(mObserver);
    }

    private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            if (!isAdded()) {
                return;
            }
            getLoaderManager().restartLoader(presentId, null, HymnListFragment.this);
        }
    };
    private void updateDashboard() {
        try {
            getLoaderManager().restartLoader(presentId, null, this);
        } catch (Exception e) {
            Log.e(TAG, "" + e);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateDashboard();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


}
