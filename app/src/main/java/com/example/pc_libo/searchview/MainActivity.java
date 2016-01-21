package com.example.pc_libo.searchview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private static final int CLICK_RIGHT = 1;
    private static final int CLICK_LEFT = 2;

    private SearchView searchView;
    private Context context;
    private String[] strarr = {"shandong", "shangxi", "shangcai", "sdong2", "sxi2", "scai2"};
    private ListView queryResultListView;
    private EditText queryEditText;
    private TextView tvBDP;

    private ImageView ivBackground;
    private  ImageView ivQueryRight;
    private  ImageView ivQyeryLeft;
    private  ArrayList<String> queryResultList;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CLICK_RIGHT) {
                ivQyeryLeft.setVisibility(View.VISIBLE);
                ivQueryRight.setVisibility(View.INVISIBLE);
                queryEditText.setVisibility(View.VISIBLE);
            } else if (msg.what == CLICK_LEFT) {
                ivQyeryLeft.setVisibility(View.INVISIBLE);
                ivQueryRight.setVisibility(View.VISIBLE);
                tvBDP.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view1);

        context = this;

        initView();
        bindListener();
        //init();


    }

    private void initView() {
        tvBDP = (TextView) findViewById(R.id.tv_bdp);
        queryEditText = (EditText) findViewById(R.id.et_query);
        queryResultListView = (ListView) findViewById(R.id.lv_query_content);
        ivQueryRight = (ImageView) findViewById(R.id.iv_query_right);
        ivQyeryLeft = (ImageView) findViewById(R.id.iv_query_left);
        ivBackground = (ImageView) findViewById(R.id.iv_backgroud);
        queryEditText.setVisibility(View.INVISIBLE);
        ivBackground.setVisibility(View.INVISIBLE);
        ivQyeryLeft.setVisibility(View.INVISIBLE);

    }

    private void bindListener() {
        ivQueryRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimationRightToLeft();

            }
        });

        ivQyeryLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryEditText.setText("");
                addAnimationLeftToRight();
            }
        });

        queryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryResultList = query(s.toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, queryResultList);
                queryResultListView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        queryResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                queryEditText.setText(queryResultList.get(position));
            }
        });
    }

    private void addAnimationRightToLeft() {
        tvBDP.setVisibility(View.GONE);
        ivBackground.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, -1 * (queryEditText.getWidth() + ivQueryRight.getWidth()), 0, 0);
        translateAnimation.setDuration(1000);
        ivQueryRight.startAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        ivBackground.startAnimation(alphaAnimation);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(CLICK_RIGHT);
            }
        }, 1000);

    }

    private void addAnimationLeftToRight() {
        queryEditText.setVisibility(View.INVISIBLE);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, (queryEditText.getWidth() + ivQyeryLeft.getWidth()), 0, 0);
        translateAnimation.setDuration(1000);
        ivQyeryLeft.startAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        ivBackground.startAnimation(alphaAnimation);
        ivBackground.setVisibility(View.INVISIBLE);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(CLICK_LEFT);
            }
        }, 1000);
    }

    /**
     * 下面为SearchView的测试代码
     */
    public void init() {
        queryResultListView = (ListView) findViewById(R.id.lv_query_content);

        searchView = (SearchView) this.findViewById(R.id.my_sv);
        //searchView.setQueryHint("hint");//设置搜索框内的默认显示的提示文本
        searchView.setIconifiedByDefault(true);//置搜索框默认是否自动缩小为图标
        searchView.setSubmitButtonEnabled(false);//设置是否显示搜索按钮


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.requestFocus();
                //searchView.performClick();
                searchView.setIconified(false);//促发点击查询图标
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(context, query, Toast.LENGTH_SHORT).show();
                ArrayList<String> strs = query(query);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, strs);
                queryResultListView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> strs = query(newText);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, strs);
                queryResultListView.setAdapter(adapter);
                return false;
            }
        });
    }

    private ArrayList<String> query(String queryStr) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < strarr.length; i++) {
            if (strarr[i].contains(queryStr)) {
                result.add(strarr[i]);
            }
        }
        return result;

    }

}
