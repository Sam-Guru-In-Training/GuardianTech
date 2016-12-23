package com.newsreader.guardian.guardiantech;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class SimpleFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private Context context;

    final int PAGE_COUNT = 3;
    private String TAB_TITLES[] = new String[] { "Windows",
            "Android", "Android" };
    private int[] imageResId = {
            R.drawable.ic_windows,
            R.drawable.ic_android,
            R.drawable.ic_watch,
    };

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return ListOfStoriesFragment.newInstance(position);
    }

    /** yeild the title */
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        //return TAB_TITLES[position];
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
    /** need the number of tabs to set up in MainActivity */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
