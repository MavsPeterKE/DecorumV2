package com.rapid.Decorum.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.rapid.Decorum.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SliderItemFragment extends Fragment {

    private static final String ARG_POSITION = "slider-position";
    // prepare all title ids arrays
    @StringRes
    private static final int[] PAGE_TITLES =
            new int[]{R.string.discover_living_room, R.string.kitchen_title, R.string.office, R.string.bedroom};

    // prepare all subtitle ids arrays
    @StringRes
    private static final int[] PAGE_TEXT =
            new int[]{
                    R.string.discover_text, R.string.kitchen_text, R.string.office_text, R.string.rewards_text
            };
    // prepare all subtitle images arrays
    @StringRes
    private static final int[] PAGE_IMAGE =
            new int[]{
                    0, 0, 0, 0
            };

    // prepare all background images arrays
    @StringRes
    private static final int[] BG_IMAGE = new int[]{
            R.drawable.home_deco, R.drawable.kitchen_, R.drawable.office_,
            R.drawable.bed_
    };

    private int position;

    public SliderItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @return A new instance of fragment SliderItemFragment.
     */
    public static SliderItemFragment newInstance(int position) {
        SliderItemFragment fragment = new SliderItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_slider_item, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvTitleText)
    TextView titleText;

    @BindView(R.id.imageView)
    ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set page background
        view.setBackground(requireActivity().getDrawable(BG_IMAGE[position]));


        // set page title
        tvTitle.setText(PAGE_TITLES[position]);
        // set page sub title text
        titleText.setText(PAGE_TEXT[position]);
        // set page image
        imageView.setImageResource(PAGE_IMAGE[position]);

    }
}
