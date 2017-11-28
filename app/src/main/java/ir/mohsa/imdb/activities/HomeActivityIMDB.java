package ir.mohsa.imdb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.bumptech.glide.Glide;

import ir.mohsa.imdb.LoginHelper;
import ir.mohsa.imdb.fragments.IMDBimageListFragment;
import ir.mohsa.imdb.R;


/**f
 * Created by 3801261697 on 21/08/2017.
 */

public class HomeActivityIMDB extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The main layout
        setContentView(R.layout.activity_homeimdb);

        // The Floating Action Button
        FloatingActionButton profile_action_button = (FloatingActionButton) findViewById(R.id.home_profile);
        profile_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_intent = new Intent(HomeActivityIMDB.this,profile_activity.class);
                startActivity(profile_intent);
            }
        });
        Glide.with(this).load(LoginHelper.getImageUri(this)).into(profile_action_button);

        // The fragment class is IMDBimageListFragment,
        // which will be replaced by layout:Home_Fragment_ContainerIMDB
        FragmentTransaction IMDBHomeTransaction = getSupportFragmentManager().beginTransaction();
        IMDBHomeTransaction.replace(R.id.Home_Fragment_ContainerIMDB,new IMDBimageListFragment());
        IMDBHomeTransaction.commit();
    }
}
