package com.xtelsolution.xmec.view.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.view.fragment.BasicFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/11/2017
 * Email: leconglongvu@gmail.com
 */
public class SlideViewFragment extends BasicFragment {

    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    Unbinder unbinder;

    public static SlideViewFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(Constants.OBJECT, position);

        SlideViewFragment fragment = new SlideViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideview, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();
    }

    private void getData() {
        int position = -1;

        try {
            position = getArguments().getInt(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initData(position);
    }

    private void initData(int position) {
        int color;
        String title;
        String content;
        int resource;

        switch (position) {
            case 1:
                color = R.color.sv_background_1;
                title = getString(R.string.layout_sv_title_1);
                content = getString(R.string.layout_sv_content_1);
                resource = R.mipmap.im_slideview_1;
                break;
            case 2:
                color = R.color.sv_background_2;
                title = getString(R.string.layout_sv_title_2);
                content = getString(R.string.layout_sv_content_2);
                resource = R.mipmap.im_slideview_2;
                break;
            case 3:
                color = R.color.sv_background_3;
                title = getString(R.string.layout_sv_title_3);
                content = getString(R.string.layout_sv_content_3);
                resource = R.mipmap.im_slideview_3;
                break;
            case 4:
                color = R.color.sv_background_4;
                title = getString(R.string.layout_sv_title_4);
                content = getString(R.string.layout_sv_content_4);
                resource = R.mipmap.im_slideview_1;
                break;
            case 5:
                color = R.color.sv_background_5;
                title = getString(R.string.layout_sv_title_5);
                content = getString(R.string.layout_sv_content_5);
                resource = R.mipmap.im_slideview_2;
                break;
            default:
                return;
        }

        rootView.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), color));
        txtTitle.setText(title);
        txtContent.setText(content);
        imgLogo.setImageResource(resource);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}