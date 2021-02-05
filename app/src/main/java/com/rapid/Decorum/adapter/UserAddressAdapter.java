package com.rapid.Decorum.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapid.Decorum.R;
import com.rapid.Decorum.activities.AddAddressActivity;
import com.rapid.Decorum.activities.AddressActivity;
import com.rapid.Decorum.domain.UserAddress;
import com.rapid.Decorum.progress.ProgressFragment;

import java.util.List;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.UserAddressHolder> {

    Context context;
    List<UserAddress>userAddresses;

   ProgressFragment progressFragment;

    public UserAddressAdapter(Context context, List<UserAddress> userAddresses) {
        this.context = context;
        this.userAddresses = userAddresses;
    }

    public class UserAddressHolder extends RecyclerView.ViewHolder{

        RadioButton rabBtnSelect;
        TextView txtName,txtAddress;
        Button btnDelete,btnEdit;

        View viewAddress;
        LinearLayout layoutManipulate;

        public UserAddressHolder(@NonNull View itemView) {
            super(itemView);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            rabBtnSelect=itemView.findViewById(R.id.rabBtnSelect);

            txtName=itemView.findViewById(R.id.txtName);
            txtAddress=itemView.findViewById(R.id.txtAddress);
            btnEdit=itemView.findViewById(R.id.btnEdit);

            viewAddress=itemView.findViewById(R.id.viewAddress);
            layoutManipulate=itemView.findViewById(R.id.layoutManipulate);

            rabBtnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (UserAddress userAddress:userAddresses
                         ) {
                        userAddress.setSelected(0);
                        
                    }

                    userAddresses.get(getAdapterPosition()).setSelected(1);

                    ((AddressActivity)context).setSelectedAddress(userAddresses.get(getAdapterPosition()).getId());

                    notifyItemRangeChanged(0,userAddresses.size());
                    
                    
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (UserAddress userAddress:userAddresses
                    ) {
                        userAddress.setSelected(0);

                    }

                    userAddresses.get(getAdapterPosition()).setSelected(1);
                    notifyItemRangeChanged(0,userAddresses.size());
                }
            });


            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(context, AddAddressActivity.class);
                    intent.putExtra("SelectedAddress",userAddresses.get(getAdapterPosition()));
                    context.startActivity(intent);


                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final UserAddress userAddress=userAddresses.get(getAdapterPosition());

                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle(R.string.app_name);
                    builder.setMessage("Do you want to delete address ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            progressFragment = new ProgressFragment();
                            progressFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "dkdskksd");


                            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

                            mFirestore.collection("Address").document(userAddress.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    progressFragment.dismiss();

                                    int p=getAdapterPosition();
                                    userAddresses.remove(p);
                                    notifyItemRemoved(p);

                                    ((AddressActivity)context).getAllAddress();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    progressFragment.dismiss();

                                }
                            });





                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });

                    builder.show();





                }
            });
        }
    }

    @NonNull
    @Override
    public UserAddressHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_useraddressadapter,viewGroup,false);



        return new UserAddressHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAddressHolder userAddressHolder, int i) {
        userAddressHolder.txtName.setText(userAddresses.get(i).getName());
        userAddressHolder.txtAddress.setText(userAddresses.get(i).getHousename()+"\n"+
                userAddresses.get(i).getFlatno()+"\n"+
                userAddresses.get(i).getLandMark()+"\n"+
                userAddresses.get(i).getDistrict()+"\n"+
                userAddresses.get(i).getState()+"\n"+
                userAddresses.get(i).getCountry()+"\n"+
                userAddresses.get(i).getPincode()+"\n"+
                userAddresses.get(i).getMobilenumber());

        if(userAddresses.get(i).getSelected()==0)
        {

            userAddressHolder.viewAddress.setVisibility(View.GONE);
            userAddressHolder.layoutManipulate.setVisibility(View.GONE);
            userAddressHolder.rabBtnSelect.setChecked(false);
        }
        else {
            userAddressHolder.viewAddress.setVisibility(View.VISIBLE);
            userAddressHolder.layoutManipulate.setVisibility(View.VISIBLE);
            userAddressHolder.rabBtnSelect.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return userAddresses.size();
    }


}
