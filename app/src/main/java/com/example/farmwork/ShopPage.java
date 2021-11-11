package com.example.farmwork;

import static java.lang.Double.parseDouble;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ShopPage extends AppCompatActivity {

    int[] sampleImages = {R.drawable.farmland, R.drawable.worker, R.drawable.machinery};
    CarouselView carouselView;

    Button mapBtn;
    TextView shopname, shopaddress, shopdesc, totalcut, totalharvest, totalplanter, totalspray, totalshredders, totaltillers, totalseeding, totaltractors, totalweeders;
    String latitide, longitude, shopadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);

        shopname = findViewById(R.id.shop_title);
        shopaddress = findViewById(R.id.shop_address);
        shopdesc = findViewById(R.id.description);

        totalcut = findViewById(R.id.total_cutters);
        totalharvest = findViewById(R.id.total_harvesters);
        totalplanter = findViewById(R.id.total_planters);
        totalseeding = findViewById(R.id.total_seeders);
        totalshredders = findViewById(R.id.total_shredders);
        totalspray = findViewById(R.id.total_sprayers);
        totaltillers = findViewById(R.id.total_tillers);
        totaltractors = findViewById(R.id.total_tractors);
        totalweeders = findViewById(R.id.total_weeders);

        mapBtn = findViewById(R.id.map);

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        SharedPreferences preferences = this.getSharedPreferences("Address", Context.MODE_PRIVATE);
        if (preferences.getString("User_Latitude", "") != null && preferences.getString("User_Longitude", "") != null) {
            latitide = preferences.getString("User_Latitude", "");
            longitude = preferences.getString("User_Longitude", "");
        }

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            ShopsViewModel shopsViewModel = (ShopsViewModel) intent.getSerializableExtra("data");
            shopname.setText(shopsViewModel.getShopName());
            shopadd = shopsViewModel.getShopStreet()+",+"+shopsViewModel.getShopVillage()+",+"+shopsViewModel.getShopTown()+",+"+shopsViewModel.getShopState()+"+"+shopsViewModel.getShoppincode();
            shopaddress.setText(shopsViewModel.getShopDoorNo()+", "+shopsViewModel.getShopStreet()+", "+shopsViewModel.getShopVillage()+", "+shopsViewModel.getShopTown()+", "+shopsViewModel.getShopState()+", "+shopsViewModel.getShoppincode());
            shopdesc.setText(shopsViewModel.getDescription());
            totalcut.setText(shopsViewModel.getTotalcut());
            totalharvest.setText(shopsViewModel.getTotalharvest());
            totalplanter.setText(shopsViewModel.getTotalplanter());
            totalspray.setText(shopsViewModel.getTotalspray());
            totalshredders.setText(shopsViewModel.getTotalshredders());
            totaltillers.setText(shopsViewModel.getTotaltillers());
            totalseeding.setText(shopsViewModel.getTotalseeding());
            totaltractors.setText(shopsViewModel.getTotaltractors());
            totalweeders.setText(shopsViewModel.getTotalweeders());
        }

        //String mapLink = "https://www.google.com/maps/dir/"+latitide+","+longitude+"/"+shopadd+"/";

        String mapLink = "https://www.google.com/maps/dir/"+latitide+","+longitude+"/"+shopadd+"/";

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMap = new Intent(Intent.ACTION_VIEW);
                //intentWhatsapp.setClassName("com.whatsapp", "com.whatsapp.w4b");
                String url =mapLink;
                intentMap.setData(Uri.parse(url));
                //intentWhatsapp.setPackage("com.whatsapp");
                v.getContext().startActivity(intentMap);
            }
        });
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
}