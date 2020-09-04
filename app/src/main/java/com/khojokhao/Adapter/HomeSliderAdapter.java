package com.khojokhao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.khojokhao.Model.BannerModel;
import com.khojokhao.R;

import java.util.ArrayList;

public class HomeSliderAdapter extends SliderViewAdapter<HomeSliderAdapter.HomeSliderAdapterVH> {

    private Context context;
    ArrayList<BannerModel> bannerModelArrayList;

    public HomeSliderAdapter(Context context,ArrayList<BannerModel> bannerModelArrayList) {
        this.context = context;
        this.bannerModelArrayList = bannerModelArrayList;
    }

   /* public void addItem(BannerModel sliderItem) {
        this.bannerModelArrayList.add(sliderItem);
        notifyDataSetChanged();
    }*/


    @Override
    public HomeSliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new HomeSliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(HomeSliderAdapterVH viewHolder, int position) {
        BannerModel bannerModel = bannerModelArrayList.get(position);

        Glide.with(viewHolder.itemView)
                .load(bannerModel.getBanner())
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return bannerModelArrayList.size();
    }

    class HomeSliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
//        TextView textViewDescription;

        public HomeSliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
//            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
