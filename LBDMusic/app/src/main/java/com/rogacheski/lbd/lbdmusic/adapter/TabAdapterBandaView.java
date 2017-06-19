package com.rogacheski.lbd.lbdmusic.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rogacheski.lbd.lbdmusic.R;
import com.rogacheski.lbd.lbdmusic.entity.BandEntity;
import com.rogacheski.lbd.lbdmusic.fragments.BandAboutUsPage;
import com.rogacheski.lbd.lbdmusic.fragments.BandCalendarPage;
import com.rogacheski.lbd.lbdmusic.fragments.BandReviewsPage;


/**
 * Criado por Vinícius on 28-Apr-17.
 */

public class TabAdapterBandaView  extends FragmentPagerAdapter{

    Context context;
    BandEntity banda;

    public TabAdapterBandaView(FragmentManager fm, Context context, BandEntity banda){
        super(fm);
        this.context = context;
        this.banda = banda;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                BandAboutUsPage fragmentoAboutUs = new BandAboutUsPage();
                fragmentoAboutUs.setArgument(banda);
                return fragmentoAboutUs;
            case 1:
                BandCalendarPage fragmentoCalendar = new BandCalendarPage();
                fragmentoCalendar.setArgument(banda);
                return fragmentoCalendar;
            case 2:
                BandReviewsPage fragmentoReviews = new BandReviewsPage();
                fragmentoReviews.setArgument(banda);
                return fragmentoReviews;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.sobre_nos);
            case 1:
                return context.getString(R.string.calendario);
            case 2:
                return context.getString(R.string.avaliacoes);
            default:
                return "";
        }
    }
}
