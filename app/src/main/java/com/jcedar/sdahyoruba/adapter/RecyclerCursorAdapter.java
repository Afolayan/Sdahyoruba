package com.jcedar.sdahyoruba.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jcedar.sdahyoruba.R;
import com.jcedar.sdahyoruba.provider.DataContract;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecyclerCursorAdapter extends RecyclerViewCursorAdapter<RecyclerCursorAdapter.HymnViewHolder>
        implements View.OnClickListener
{
    private final LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onLongClickListener;
    public Context context;

    public RecyclerCursorAdapter(final Context context) {
        super();

        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(final AdapterView.OnItemLongClickListener onItemClickListener) {
        this.onLongClickListener = onItemClickListener;
    }

    @Override
    public HymnViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        final View view = this.layoutInflater.inflate(R.layout.list_n_item, parent, false);
        view.setOnClickListener(this);

        return new HymnViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final HymnViewHolder holder, final Cursor cursor) {
        holder.bindData(cursor);
        String id = cursor.getString(cursor.getColumnIndex(DataContract.Hymns.SONG_ID));
        holder.setTag(id);
    }

     /*
     * View.OnClickListener
     */

    @Override
    public void onClick(final View view)
    {
        if (this.onItemClickListener != null)
        {
            final RecyclerView recyclerView = (RecyclerView) view.getParent();
            final int position = recyclerView.getChildLayoutPosition(view);
            if (position != RecyclerView.NO_POSITION) {
                final Cursor cursor = this.getItem(position);
                this.onItemClickListener.onItemClicked(cursor);
            }
        }
    }

    public static class HymnViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.lblName)
        TextView textViewName;

        @Bind(R.id.lblEnglish)
        TextView textViewEnglish;



        Context context;

        View view;

        public HymnViewHolder(final View itemView, Context context) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        public void bindData(final Cursor cursor) {
            String userId = cursor.getString(
                    cursor.getColumnIndex(DataContract.Hymns._ID));

            final String num = cursor.getString(cursor.getColumnIndex(DataContract.Hymns.SONG_ID));
            final String name = cursor.getString(cursor.getColumnIndex(DataContract.Hymns.SONG_NAME));

            String display = num +" - "+name;

            this.textViewName.setText(display);


            final String eng = cursor.getString(cursor.getColumnIndex(DataContract.Hymns.ENGLISH_VERSION));
            this.textViewEnglish.setText(eng);


        }
        public void setTag(String string){
            this.view.setTag( string);
        }
        public String getTag(){
           return this.view.getTag().toString();
        }

    }

    public interface OnItemClickListener
    {
        void onItemClicked(Cursor cursor);
    }
}