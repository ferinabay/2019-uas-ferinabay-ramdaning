package ferinaramdaning.com.project_uas.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mBooksFragments = new ArrayList<>();
    private final List<String> mBooksFragmentTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addBooksFragment(Fragment fragment,String title) {
        mBooksFragments.add(fragment);
        mBooksFragmentTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mBooksFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mBooksFragmentTitle.get(position);
    }

    @Override
    public int getCount() {
        return mBooksFragments.size();
    }

}
