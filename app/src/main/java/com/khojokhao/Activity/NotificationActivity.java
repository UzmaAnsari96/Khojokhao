package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.khojokhao.R;

public class NotificationActivity extends AppCompatActivity {

    ImageView img_back;
    RecyclerView rec_notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
        onClick();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        img_back = findViewById(R.id.img_back);
        rec_notification = findViewById(R.id.rec_notification);
        rec_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        rec_notification.setAdapter(new NotificationAdapter());
    }

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(NotificationActivity.this).inflate(R.layout.notification_item_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_notification,txt_date;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_notification = findViewById(R.id.txt_notification);
                txt_date = findViewById(R.id.txt_date);
            }
        }
    }
}
