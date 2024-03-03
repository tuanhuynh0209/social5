package com.example.social5;

import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolder> {

    Context context;
    List<ModelUser> userList;

    String myUid;
    FirebaseAuth firebaseAuth;

    public void setFilteredList(List<ModelUser> filteredList) {
        this.userList = filteredList;
        notifyDataSetChanged();
    }
    public AdapterUser(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;

        firebaseAuth = FirebaseAuth.getInstance();
        myUid = firebaseAuth.getUid();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view =  LayoutInflater.from(context).inflate(R.layout.row_user, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        final String hisUID = userList.get(i).getUid();
        String userImage = userList.get(i).getImage();
        String userName = userList.get(i).getName();
        String userEmail = userList.get(i).getEmail();

        myHolder.mNameTv.setText(userName);
        myHolder.mEmailTv.setText(userEmail);

        try{
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.user)
                    .into(myHolder.mAvatarIv);
        }
        catch (Exception e)
        {

        }
        isFollowing(hisUID , myHolder.followBtn);


        myHolder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myHolder.followBtn.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getUid())
                            .child(hisUID).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getUid()).child(myUid).setValue(true);

                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getUid())
                            .child(hisUID).removeValue();


                }
            }
        });




        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+ userEmail, Toast.LENGTH_SHORT).show();
            }
        });
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ThereProfileActivity.class);
                intent.putExtra("uid",hisUID);
                context.startActivity(intent);

            }
        });


    }



    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mAvatarIv;
        Button followBtn;
        TextView mNameTv, mEmailTv;

        public MyHolder(@NonNull View itemView)
        {
            super(itemView);

            followBtn = itemView.findViewById(R.id.buttonfollow);

            mAvatarIv = itemView.findViewById(R.id.avaterIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
        }
    }

    private void isFollowing(final String userid,final Button button)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                child("Follow").child(firebaseAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userid).exists())
                {
                    button.setText("following");
                }
                else{
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
