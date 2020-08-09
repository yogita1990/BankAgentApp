package com.shawinfosolutions.bankagentapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.ViewHolder> {
    ArrayList<String> PaymentList, mobileNoList;
    Context context;
    private AlertDialog alertDialog;




    public PaymentListAdapter(Context context, ArrayList<String> PaymentList, ArrayList<String> mobileNoList) {
        this.context = context;
        this.PaymentList = PaymentList;
        this.mobileNoList = mobileNoList;
    }

    @Override
    public PaymentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout._cust_list_item, parent, false);
        PaymentListAdapter.ViewHolder viewHolder = new PaymentListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PaymentListAdapter.ViewHolder holder, int position) {
        //  final MyListData myListData = listdata[position];
        //  holder.CustomerTokenTxt.setText("Payment Details :" + PaymentList.get(position));
       // holder.CustomerTokenTxt.setText("Payment Details :" + mobileNoList.get(position));
        holder.CustomerMobileTxt.setText("MobileNo :" + mobileNoList.get(position));

     /*   holder.CustomerTokenTxt.setImageBitmap(BitmapFactory.decodeFile(
                PaymentList.get(position)));*/
        Picasso.with(context).load(PaymentList.get(position)).into(holder.CustomerTokenTxt);

    }


    @Override
    public int getItemCount() {
        return PaymentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  CustomerMobileTxt;
        public ImageView CustomerTokenTxt;
        public LinearLayout CustomerDeatilsLay;

        public ViewHolder(View itemView) {
            super(itemView);
            this.CustomerTokenTxt = (ImageView) itemView.findViewById(R.id.CustomerTokenTxt);
            this.CustomerMobileTxt = (TextView) itemView.findViewById(R.id.CustomerMobileTxt);
            this.CustomerDeatilsLay = (LinearLayout) itemView.findViewById(R.id.CustomerDeatilsLay);

        }
    }
}