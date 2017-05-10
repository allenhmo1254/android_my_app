package com.example.jingzhongjie.tabbarviewtest.rank;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.jingzhongjie.tabbarviewtest.R;
import com.example.jingzhongjie.tabbarviewtest.app.ToolbarActivity;

public class RankActivity extends ToolbarActivity {

    private View redView;
    private View blueView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);//布局文件，宽和高
        params.gravity = Gravity.TOP | Gravity.LEFT;//针对于父视图的布局
        params.width = 100;
        params.height = 300;
        params.leftMargin = 50;
        redView = new View(this);
        redView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        this.addContentView(redView, params);

        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);//布局文件，宽和高
        blueParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;//针对于父视图的布局
        blueParams.width = 50;
        blueParams.height = 100;
        blueView = new View(this);

    }
}
