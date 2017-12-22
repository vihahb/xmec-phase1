package com.xtelsolution.xmec.view.fragment.mbr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.activity.mbr.MbrCreateActivity;
import com.xtelsolution.xmec.view.fragment.IFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class MbrCreateFragment extends IFragment {

    @BindView(R.id.layout_1)
    LinearLayout layout1;
    Unbinder unbinder;

    public static MbrCreateFragment newInstance() {
        return new MbrCreateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mbr_create, container, false);
        return onCheckCreateView(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isViewCreated()) {
            unbinder = ButterKnife.bind(this, view);
            initActionListener();
        }
    }

    private void initActionListener() {
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MbrCreateActivity.class);
                getActivity().startActivityForResult(intent, HomeActivity.REQUEST_MBR_CREATE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }
}