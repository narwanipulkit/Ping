package com.grapejuice.ping;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.*;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by pulkitnarwani on 30/10/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.viewHold> {

    List<data> d;
    static Context con;


    RVAdapter(List<data> d,Context c)
    {
        this.d=d;
        con=c;
    }





    @Override
    public viewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        viewHold v1=new viewHold(v);
        return v1;
    }

    @Override
    public void onBindViewHolder(viewHold holder, int position) {
        holder.tv1.setText(d.get(position).nws);
        holder.tv2.setText(d.get(position).usr);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return d.size();
    }

    public static class viewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView tv1,tv2;

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public viewHold(View itemView) {
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.cv);
            tv1=(TextView)itemView.findViewById(R.id.headline1);
            tv2=(TextView)itemView.findViewById(R.id.by1);

            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast t=Toast.makeText(con,"Here It is",Toast.LENGTH_LONG);
            t.show();
        }
    }
}
