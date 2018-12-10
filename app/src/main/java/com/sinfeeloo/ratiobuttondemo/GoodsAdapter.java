package com.sinfeeloo.ratiobuttondemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sinfeeloo.ratiobutton.RatiolBtn;
import com.sinfeeloo.ratiobutton.OnCountChangedListener;

import java.util.List;

/**
 * @author: mhj
 * @date: 2018/12/7 13:59
 * @desc:
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder> {


    List<GoodsBean> mData;

    public GoodsAdapter(List<GoodsBean> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods, viewGroup, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, final int position) {
        GoodsBean item = mData.get(position);
        holder.tvGoodsName.setText(item.getName());
        holder.tvGoodsPrice.setText(String.format("¥%s", item.getPrice()));
        holder.ratioBtn.setRatio(item.getRatio());
        holder.ratioBtn.setMaxCount(item.getStorage());
        holder.ratioBtn.setUnit(item.getSmallUnit(), item.getBigUnit());
        holder.ratioBtn.setDisplayCount(item.getSmallcount(), item.getBigCount());
        Log.i("mhj","bigCount：" + item.getBigCount() + "    smallCount：" + item.getSmallcount());
        holder.ratioBtn.setOnCountChangedLisener(new OnCountChangedListener() {
            @Override
            public void onCountChange(View view, int bigCount, int smallCount) {
                Log.e("mhj", "bigCount：" + bigCount + "    smallCount：" + smallCount);
                mData.get(position).setBigCount(bigCount);
                mData.get(position).setSmallcount(smallCount);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class GoodsViewHolder extends RecyclerView.ViewHolder {

        private RatiolBtn ratioBtn;
        private TextView tvGoodsName;
        private TextView tvGoodsPrice;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            ratioBtn = itemView.findViewById(R.id.bsb_goods);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = itemView.findViewById(R.id.tv_goods_price);
        }
    }
}
