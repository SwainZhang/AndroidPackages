package com.emery.test.emeryutils;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.emery.test.emeryutils.annotation.ContentView;
import com.emery.test.emeryutils.annotation.OnClick;
import com.emery.test.emeryutils.annotation.OnLongClick;
import com.emery.test.emeryutils.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.tv_hello)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView.setText("Hello,EmeryUtils");
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @OnClick(R.id.btn_click)
    public void click(View view) {
        mTextView.setText("点击事件设置成功");
    }

    @OnLongClick(R.id.btn_long)
    public boolean longClick(View view) {
        mTextView.setText("长按点击事件设置成功");
        return false;
    }
}
