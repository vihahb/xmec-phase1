package com.xtelsolution.xmec.adapter.mbr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.fragment.mbr.MbrCreateFragment;
import com.xtelsolution.xmec.view.fragment.mbr.MbrItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/11/2017
 * Email: leconglongvu@gmail.com
 */
public class MbrsAdapter extends FragmentStatePagerAdapter {
    private List<Mbr> listData;

    public MbrsAdapter(FragmentManager fm) {
        super(fm);
        listData = new ArrayList<>();
    }

    public void removeItem(int position) {
        try {
            listData.remove(position);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItemByID(int mbrID) {
        for (Mbr mbr : listData) {
            if (mbr.getMrbId() == mbrID) {
                listData.remove(mbr);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void updateItem(Mbr mbr) {
        for (int i = listData.size() - 2; i >= 0; i--) {
            if (listData.get(i).getMrbId() == mbr.getMrbId()) {
                listData.remove(i);
                listData.add(i, mbr);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void addItem(Mbr mbr) {
        if (mbr == null)
            return;

        if (listData.size() > 1) {
            for (int i = 0; i < listData.size() - 1; i++) {
                Mbr mbr1 = listData.get(i);

                if (mbr1 != null && mbr1.getShareType() > 0) {
                    listData.add(i, mbr);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        int last = listData.size() > 0 ? listData.size() - 1 : 0;
        listData.add(last, mbr);
        notifyDataSetChanged();
    }

    public void setListData(List<Mbr> list) {
        listData.clear();
        listData.addAll(list);
        listData.add(null);
        notifyDataSetChanged();
    }

    public List<Mbr> getListData() {
        return listData;
    }

    public Mbr getMbr(int position) {
        return listData.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        Mbr mbr = listData.get(position);
        if (mbr != null) {
            return MbrItemFragment.newInstance(mbr.getMrbId());
        } else {
            return MbrCreateFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}