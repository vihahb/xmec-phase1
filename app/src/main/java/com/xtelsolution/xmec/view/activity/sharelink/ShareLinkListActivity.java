package com.xtelsolution.xmec.view.activity.sharelink;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Button;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.fragment.listShareLink.LinkListFragment;
import com.xtelsolution.xmec.view.fragment.listShareLink.ShareListFragment;
import com.xtelsolution.xmec.view.widget.HeightWrappingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareLinkListActivity extends BasicActivity {

    @BindView(R.id.viewpager)
    HeightWrappingViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;

    @OnClick(R.id.btn_cancel)
    public void onCancel() {
        finish();
    }

    FragmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_link_list);
        ButterKnife.bind(this);
        getData();
    }

    private void getData() {
        int mbr_id = getIntExtra(Constants.MRB_ID);

        if (mbr_id == -1) {
            showErrorGetData(null);
            return;
        }

        initViewPager(mbr_id);
    }

    private void initViewPager(int mbr_id) {
        List<Fragment> list = new ArrayList<>();
        list.add(ShareListFragment.newInstance(mbr_id, false));
        list.add(LinkListFragment.newInstance(mbr_id));

        adapter = new FragmentsAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class FragmentsAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> listData;

        FragmentsAdapter(FragmentManager fragmentManager, List<Fragment> listData) {
            super(fragmentManager);
            this.listData = listData;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return listData.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "CHIA SẺ";
                    break;
                case 1:
                    title = "LIÊN KẾT";
                    break;
            }
            return title;
        }
    }
}
