package com.example.snapchat.fragment;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snapchat.R;
import com.example.snapchat.ShowMusicActivity;
import com.example.snapchat.ViewFollowersActivity;
import com.example.snapchat.loginRegistration.SplashScreenActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class MusicFragment extends Fragment {
    private static final String TAG = "MusicFragment";
    private int TIME_OUT=1500;
    private String userName;
    private ImageView mviewFolowers;

    public static MusicFragment newInstance(){
        MusicFragment fragment = new MusicFragment();
        return fragment;
    }

    List<String> list;
    String str;
   // ListAdapter listAdapter;
    MediaPlayer mediaPlayer;

    Button back;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music,container,false);
        ImageView mPost = view.findViewById(R.id.post);
        ImageView mlogout = view.findViewById(R.id.logout);
        mviewFolowers = view.findViewById(R.id.ViewFollowers);
        final TextView mtext = view.findViewById(R.id.userName);

        CheckPermission();

        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayer();
            }
        });
        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
        try{
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userDb = FirebaseDatabase.getInstance().getReference("users").child(userId).child("name");

            userDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userName = dataSnapshot.getValue().toString();
                    mtext.setText(userName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "User Not logged In", Toast.LENGTH_LONG).show();
        }

        try{

                    mviewFolowers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                            Intent intent = new Intent(getContext(), ViewFollowersActivity.class);

                            startActivity(intent);
                                }
                            },TIME_OUT);
                        }
                    });

        }
        catch (Exception e){

        }
        return view;
    }

    private void CheckPermission(){

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            return;
        }
    }

    private void MusicPlayer(){
        Intent intent = new Intent(getContext() ,ShowMusicActivity.class);

        //clear all other activities which were on top of stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(),SplashScreenActivity.class);

        //clear all other activities which were on top of stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;

    }
}