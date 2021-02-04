package com.rapid.furnitureaugmentreal;


import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;



/**
 * A simple {@link Fragment} subclass.
 */
public class TermsandConditionsFragment extends DialogFragment {


    public TermsandConditionsFragment() {
        // Required empty public constructor
    }



    View view;

    Button btn_ok;

    TextView txtTitle,txtDtDelivery;

    int selected=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_termsand_conditions, container, false);


        selected=getArguments().getInt("Key",0);

        btn_ok=view.findViewById(R.id.btn_ok);
        txtDtDelivery=view.findViewById(R.id.txtDtDelivery);
        txtTitle=view.findViewById(R.id.txtTitle);
        showDetails();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();



            }
        });

        return view;
    }

    public void showDetails()
    {
        switch (selected)
        {

            case 0:

                txtTitle.setText("Chair");
                txtDtDelivery.setText("One of the basic pieces of furniture, a chair is a type of seat.Its primary features are two pieces of a durable material, attached as back and seat to one another at a 90° or slightly greater angle, with usually the four corners of the horizontal seat attached in turn to four legs—or other parts of the seat's underside attached to three legs or to a shaft about which a four-arm turnstile on rollers can turn—strong enough to support the weight of a person who sits on the seat");

                break;

            case 1:

                txtTitle.setText("Table");
                txtDtDelivery.setText("A table is an item of furniture with a flat top and one or more legs, used as a surface for working at, eating from or on which to place things.[1][2] Some common types of table are the dining room table, which is used for seated persons to eat meals; the coffee table, which is a low table used in living rooms to display items or serve refreshments; and the bedside table, which is used to place an alarm clock and a lamp. There are also a range of specialized types of tables, such as drafting tables, used for doing architectural drawings, and sewing tables");


                break;

            case 2:
                txtTitle.setText("TV stand");
                txtDtDelivery.setText("Furniture refers to movable objects intended to support various human activities such as seating (e.g., chairs, stools, and sofas),");

                break;

            case 3:

                txtTitle.setText("Cot");
                txtDtDelivery.setText("Temporary beds include the inflatable air mattress and the folding camp cot. Some beds contain neither a padded mattress nor a bed frame");

                break;

            case 4:

                txtTitle.setText("cupboard");
                txtDtDelivery.setText("The term cupboard was originally used to describe an open-shelved side table for displaying dishware, more specifically plates, cups and saucers");

                break;

            case 5:

                txtTitle.setText("Bench");
                txtDtDelivery.setText("A bench is a long seat on which multiple people may sit at the same time. Benches are typically made of wood, but may also be made of metal");

                break;

            case 6:

                txtTitle.setText("Desk");
                txtDtDelivery.setText("A desk or bureau is a piece of furniture with a flat table-style work surface used in a school, office, home or the like for academic");

                break;


        }
    }



    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();



        getDialog().getWindow().setLayout(width, height);
    }
}
