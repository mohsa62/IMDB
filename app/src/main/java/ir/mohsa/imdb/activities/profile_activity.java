package ir.mohsa.imdb.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import ir.mohsa.imdb.R;
import ir.mohsa.imdb.fragments.profileFragment;

/**r
 * Created by 3801261697 on 11/11/2017.
 */

public class profile_activity extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        FragmentTransaction profileTransaction = getSupportFragmentManager().beginTransaction();
        profileTransaction.replace(R.id.profile_activity_container,new profileFragment());
        profileTransaction.commit();
    }
}
