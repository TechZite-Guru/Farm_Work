package com.example.farmwork;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*  Created by Aravind Babu Jagarlamudi
    Project Name: Farm Work
    Date: 28/12/2020
    Team: Swapna Bharathi, Nagamani, Chandana sree, Sai Krupa
*/

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.DatesAdapterVh> {

    private List<DatesViewModel> datesViewModelList;
    BookingPage bookingPage;

    public DatesAdapter(List<DatesViewModel> datesViewModelList, BookingPage bookingPage) {
        this.datesViewModelList = datesViewModelList;
        this.bookingPage = bookingPage;
    }

    @NonNull
    @Override
    public DatesAdapter.DatesAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nextday_layout, parent, false);
        return new DatesAdapter.DatesAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DatesAdapter.DatesAdapterVh holder, int position) {
        DatesViewModel datesViewModel = datesViewModelList.get(position);
        String normal_date = datesViewModel.getDate();



        //Log.d("Book_Date:", "-"+book_date);
        Log.d("Normal_Date:", ""+normal_date);

        /*if (book_date != null) {
            if (book_date.equals(normal_date)) {
                holder.tvdates_list_item.setBackgroundColor(ContextCompat.getColor(context, R.color.red_alpha_bg));
            }
        }*/
        holder.tvdate.setText(datesViewModel.getDate());

    }

    @Override
    public int getItemCount() {
        return datesViewModelList.size();
    }

    public interface Booking_Details {
        void bookingDetails(DatesViewModel datesViewModel);
    }

    public class DatesAdapterVh extends RecyclerView.ViewHolder {

        TextView tvdate;
        Button tvButton;
        LinearLayout tvdates_list_item;

        public DatesAdapterVh(@NonNull View itemView) {
            super(itemView);

            tvdate = itemView.findViewById(R.id.date);
            tvButton = itemView.findViewById(R.id.bookbtn_todetails);
            tvdates_list_item = itemView.findViewById(R.id.linear_layout_listitem);

            tvButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookingPage.bookingDetails(datesViewModelList.get(getAdapterPosition()));
                }
            });
        }
    }
}
