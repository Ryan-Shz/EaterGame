package com.ryan.eater.game;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * created by 2019/3/9 12:49 PM
 *
 * @author Ryan
 */
class BestTimeSaver implements IBestTimeSaver {

    private static final String NAME_TIME_SAVER = "TimeSaver";
    private static final String KEY_BEST_TIME = "BestTime";
    private SharedPreferences mSp;

    BestTimeSaver(Context context) {
        mSp = context.getSharedPreferences(NAME_TIME_SAVER, Context.MODE_PRIVATE);
    }

    @Override
    public long save(long time) {
        long bestTime = mSp.getLong(KEY_BEST_TIME, 0L);
        if (time < bestTime || bestTime == 0) {
            mSp.edit().putLong(KEY_BEST_TIME, time).apply();
        }
        return bestTime == 0 ? time : Math.min(time, bestTime);
    }

}
