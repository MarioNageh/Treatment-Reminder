package com.marionageh.treatmentreminder;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.marionageh.treatmentreminder.customClasses.CustomAsyanTask;
import com.marionageh.treatmentreminder.models.Treatment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class splash_screen extends AppCompatActivity implements CustomAsyanTask.CustomAsyncListener {
    //The Image will Anmiate
    @BindView(R.id.imageViewSplash_logo)
    ImageView animated_Image;
    @BindView(R.id.progressBar_Splash)
    ProgressBar progressBar_Splash;

    private final int SECOND_FRO_LUNCH = 2;
    long realsecond = SECOND_FRO_LUNCH * 1000;

    /*
    in this activity no need for saving instant state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //For Butter Knife
        ButterKnife.bind(this);
        //For Animation of Splash by Second
        animateScreen();
        //Get data From DataBase
        getData();
    }

    private void getData() {
        new CustomAsyanTask(this, this).execute();
    }

    private void animateScreen() {
        Animation animS = AnimationUtils.loadAnimation(this, R.anim.scale_splashscreen_anim);
        animS.setDuration(realsecond);
        animated_Image.setAnimation(animS);

    }

    @Override
    public void onListReceived(final List<Treatment> treatmentList) {
        //Will Start the new activity after 2 second fro animation
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash_screen.this, MainScreen.class);
                //send the list to activity
                intent.putExtra(MainScreen.Data_FROM_DATABASE, (ArrayList<? extends Parcelable>)  treatmentList);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, realsecond);

    }
}
