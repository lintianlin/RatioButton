package com.sinfeeloo.ratiobuttondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<GoodsBean> mList;
    private GoodsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }


    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            GoodsBean bean = new GoodsBean();
            bean.setId(i);
            bean.setName("一品景芝 芝香 53度500ml*4整箱好酒口感芝麻香型白酒2礼品袋");
            bean.setPrice("3592.00");
            bean.setBigCount(0);
            bean.setSmallcount(0);
            bean.setBigUnit("箱");
            bean.setSmallUnit("瓶");
            bean.setRatio(6);
            bean.setStorage(20);
            mList.add(bean);
        }
    }

    private void initView() {
        adapter = new GoodsAdapter(mList);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setAdapter(adapter);
    }
}
