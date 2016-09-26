package hdw.com.showhidetitlemenu;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements OnTouchListener {

    private ListView listview;
    private List<Map<String, Object>> data;
    private Context mContext;
    private View titleLayout;
    private float y_tmp1, y_tmp2;
    private boolean isFirstVisiable = true; //当前第一个listitem 是否可见
    private boolean isTitleMenuOpen = true; //标题是否显示
    private int titleMenuHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initData(); //初始化数据源

        initView(); //初始化控件

    }

    private void initView() {
        titleLayout = findViewById(R.id.titleLayout); //标题栏
        titleMenuHeight = getResources().getDimensionPixelSize(R.dimen.title_height); //标题菜单高度
        listview = (ListView) findViewById(R.id.listview);

        listview.addHeaderView(View
                .inflate(mContext, R.layout.item_blank, null));


        listview.setAdapter(new Myadapter());

        listview.setOnTouchListener(this);
        listview.setOnScrollListener(myOnScrollListener);
    }

    /**
     * 由于标题是覆盖在列表上面的，需要设置一个padding来防止列表被标题被覆盖
     */
    public void setPadding() {
        // padding.setVisibility(View.VISIBLE);
    }

    /**
     * 当列表往下滑动的时候，标题隐藏了，为了调整列表的高度使其填充整个屏幕，只需要去除padding控件即可
     * 之前使用设置跟布局padding的方法，交互不友好，会闪屏，所以采取隐藏padding控件和根布局动画属性的方法
     */
    public void reMovePadding() {
        // padding.setVisibility(View.GONE);
    }

    /**
     * 判断滑动方向，如果方向向上并且第一个item不可视，就隐藏标题
     * 为什么要判断第一个标题不可视呢，因为第一个标题可视是设置padding控件显示的关键，
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 获取当前坐标
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y_tmp1 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                y_tmp2 = y;
                if (y_tmp1 != 0 && y_tmp2 != 0) {
                    if (y_tmp1 > y_tmp2&& !isFirstVisiable
                            ) {// 向上滑动隐藏标题和导航栏
                        hideTitle();
                    }
                    if (y_tmp2 >y_tmp1) {// 向下滑动显示标题和导航栏
                        showTitle();
                    }

                }
                break;
        }
        return false;
    }

    /**
     * 隐藏标题，通过属性动画实现，和补间动画相比，属性动画真实改变了控件的位置，补间动画只是障眼法
     * 个人比较嫌弃补间动画
     */
    public void hideTitle() {
        if (isTitleMenuOpen) {
            ObjectAnimator
                    .ofFloat(titleLayout, "translationY", 0.0F,
                            -titleMenuHeight).setDuration(300).start();
            isTitleMenuOpen = false;
        }

    }


    /**
     * 显示标题
     */
    public void showTitle() {
        if (!isTitleMenuOpen) {
            ObjectAnimator
                    .ofFloat(titleLayout, "translationY", -titleMenuHeight,
                            0.0F).setDuration(300).start();
            isTitleMenuOpen = true;
        }
    }

    /**
     * 列表的滑动监听，关键在于监听列表是否滑动到顶部，如果是，就把padding控件显示出来，防止标题覆盖列表
     * 其他情况下，隐藏padding，达到列表填充整个屏幕的效果
     * 千万要记住，padding的出现和隐藏都会让列表的位置发生调整，这样给用户的体验很差，所以不要频繁调用setpadding和removepadding方法
     */
    private OnScrollListener myOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            if (firstVisibleItem == 0) { //当前屏幕显示的第一个listitem
                showTitle();//显示标题
                isFirstVisiable = true;
            } else {
                isFirstVisiable = false;
            }
        }
    };


    private void initData() {
        data = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 20; i++) {
            data.add(new HashMap<String, Object>());
        }
    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.list_item, null);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        private class ViewHolder {

        }

    }

}
