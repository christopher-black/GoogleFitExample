package com.blackcj.fitdata.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.blackcj.fitdata.R;
import com.blackcj.fitdata.Utilities;
import com.blackcj.fitdata.database.CacheManager;
import com.blackcj.fitdata.database.CupboardSQLiteOpenHelper;
import com.blackcj.fitdata.database.DataManager;
import com.blackcj.fitdata.fragment.AddEntryFragment;
import com.blackcj.fitdata.fragment.RecentFragment;
import com.blackcj.fitdata.model.Workout;
import com.blackcj.fitdata.service.CacheResultReceiver;

import butterknife.ButterKnife;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by chris.black on 7/2/15.
 */
public class RecentActivity extends BaseActivity implements DataManager.IDataManager, IMainActivityCallback {
    public static final String TAG = "RecentActivity";
    private static SQLiteDatabase mDb;

    RecentFragment fragment;
    public static final String ARG_ACTIVITY_TYPE = "ARG_ACTIVITY_TYPE";
    private CupboardSQLiteOpenHelper mHelper;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Recent History");
        }

        mHelper = new CupboardSQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();

        int activityType = getIntent().getExtras().getInt(ARG_ACTIVITY_TYPE);

        fragment = RecentFragment.create();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, fragment, RecentFragment.TAG);
        transaction.commit();

        mCursor = cupboard().withDatabase(mDb).query(Workout.class).withSelection("type != ?", "" + 3).orderBy("start DESC").query().getCursor();

    }

    @Override
    protected void onDestroy() {
        mCursor.close();
        mHelper.close();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void launch(View transitionView, Workout workout) {

    }

    @Override
    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finishAfterTransition();
                // Reverse the animation back to the previous view.
                //finish();
                //push from top to bottom
                //overridePendingTransition(R.anim.no_anim, R.anim.exit_anim);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void launch(BaseActivity activity, int activityType) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.enter_anim, R.anim.no_anim);
        Intent intent = new Intent(activity, RecentActivity.class);
        intent.putExtra(ARG_ACTIVITY_TYPE, activityType);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    public void insertData(Workout workout) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
