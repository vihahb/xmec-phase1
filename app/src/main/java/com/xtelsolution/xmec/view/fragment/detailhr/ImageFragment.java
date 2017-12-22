package com.xtelsolution.xmec.view.fragment.detailhr;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.UserDrugImages;
import com.xtelsolution.xmec.view.activity.detailhr.ViewImageActivity;

import java.io.File;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public class ImageFragment extends Fragment {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    Unbinder unbinder;

    public static ImageFragment newInstance(Object object) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.OBJECT, (Serializable) object);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();
        initActionListener();
    }

    private void getData() {
        Object object = null;

        try {
            object = getArguments().getSerializable(Constants.OBJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (object == null) {
            imageView.setImageResource(R.mipmap.ic_small_avatar_default);
        } else {
            if (object instanceof HealthRecordImages) {
                setHealthRecordImage((HealthRecordImages) object);
            } else if (object instanceof UserDrugImages) {
                setUserDrugImages((UserDrugImages) object);
            }
        }
    }

    private void setHealthRecordImage(HealthRecordImages healthRecordImage) {
        if (healthRecordImage.getImagePath() != null) {
            File file = new File(healthRecordImage.getImagePath());

            if (file.exists()) {
                WidgetUtils.setImageFileFull(imageView, file, R.mipmap.ic_small_avatar_default, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                    }

                    @Override
                    public void onError() {
                        hideProgressBar();
                    }
                });
            } else {
                WidgetUtils.setImageUrlFull(imageView, healthRecordImage.getUrlImage(), R.mipmap.ic_small_avatar_default, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                    }

                    @Override
                    public void onError() {
                        hideProgressBar();
                    }
                });
            }
        } else {
            WidgetUtils.setImageUrlFull(imageView, healthRecordImage.getUrlImage(), R.mipmap.ic_small_avatar_default, new Callback() {
                @Override
                public void onSuccess() {
                    hideProgressBar();
                }

                @Override
                public void onError() {
                    hideProgressBar();
                }
            });
        }
    }

    private void setUserDrugImages(UserDrugImages userDrugImages) {
        if (userDrugImages.getImagePath() != null) {
            File file = new File(userDrugImages.getImagePath());

            if (file.exists()) {
                WidgetUtils.setImageFileFull(imageView, file, R.mipmap.ic_small_avatar_default, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                    }

                    @Override
                    public void onError() {
                        hideProgressBar();
                    }
                });
            } else {
                WidgetUtils.setImageUrlFull(imageView, userDrugImages.getUrlImage(), R.mipmap.ic_small_avatar_default, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                    }

                    @Override
                    public void onError() {
                        hideProgressBar();
                    }
                });
            }
        } else {
            WidgetUtils.setImageUrlFull(imageView, userDrugImages.getUrlImage(), R.mipmap.ic_small_avatar_default, new Callback() {
                @Override
                public void onSuccess() {
                    hideProgressBar();
                }

                @Override
                public void onError() {
                    hideProgressBar();
                }
            });
        }
    }

    private void initActionListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();

                if (activity instanceof ViewImageActivity) {
                    ((ViewImageActivity) activity).showOrHideView();
                }
            }
        });
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}