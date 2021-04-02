package com.example.farmwork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.parseDouble;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterVh> {

    private List<NotificationViewModel> NotificationViewModelList;
    private List<NotificationViewModel> locationList;
    private final List<NotificationViewModel> getNotificationViewModelListFiltered;
    NotificationFragment notificationFragment;

    public NotificationAdapter(List<NotificationViewModel> NotificationViewModelList, NotificationFragment notificationFragment) {
        this.NotificationViewModelList = NotificationViewModelList;
        this.notificationFragment = notificationFragment;
        this.getNotificationViewModelListFiltered = NotificationViewModelList;
    }


    @NonNull
    @Override
    public NotificationAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);
        return new NotificationAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapterVh holder, int position) {
        NotificationViewModel notificationViewModel = NotificationViewModelList.get(position);

        holder.tvNotifyText.setText(notificationViewModel.getNotifi());
    }

    @Override
    public int getItemCount() {
        return NotificationViewModelList.size();
    }


    public class NotificationAdapterVh extends RecyclerView.ViewHolder {

        TextView tvNotifyText;
        public NotificationAdapterVh(@NonNull View itemView) {
            super(itemView);

            tvNotifyText = itemView.findViewById(R.id.notify_text);

        }
    }
}
