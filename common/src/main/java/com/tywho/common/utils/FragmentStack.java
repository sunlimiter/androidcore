package com.tywho.common.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;


/**
 * FragmentManager does not supply developers with a fragment stack.
 * It gives us a fragment *transaction* stack.
 * <p>
 * To be sane, we need *fragment* stack.
 */
public class FragmentStack {

    public interface OnBackPressedHandlingFragment {
        boolean onBackPressed();
    }

    private Activity activity;
    private FragmentManager manager;
    private int containerId;

    public FragmentStack(Activity activity, FragmentManager manager, int containerId) {
        this.activity = activity;
        this.manager = manager;
        this.containerId = containerId;
    }

    /**
     * Returns the number of fragments in the stack.
     *
     * @return the number of fragments in the stack.
     */
    public int size() {
        return getFragments().size();
    }

    /**
     * Pushes a fragment to the top of the stack.
     */
    public void push(Fragment fragment) {

        Fragment top = peek();
        if (top != null) {
            manager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .setCustomAnimations(R.anim.finance_info_down_in_anim, R.anim.finance_info_down_out_anim,
//                            R.anim.finance_info_up_in_anim, R.anim.finance_info_up_out_anim)
                    .remove(top)
                    .add(containerId, fragment, indexToTag(manager.getBackStackEntryCount() + 1))
                    .addToBackStack(null)
                    .commit();
        } else {
            manager.beginTransaction()
                    .add(containerId, fragment, indexToTag(0))
                    .commit();
        }

        manager.executePendingTransactions();
    }

    /**
     * 使用add会出现点击穿透问题，记得在根布局添加 android:clickable="true"
     * 配置页面背景色
     *
     * @param fragment
     */
    public void add(Fragment fragment) {

        Fragment top = peek();
        if (top != null) {
            manager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .setCustomAnimations(R.anim.finance_info_down_in_anim, R.anim.finance_info_down_out_anim,
//                            R.anim.finance_info_up_in_anim, R.anim.finance_info_up_out_anim)
//                    .remove(top)
                    .add(containerId, fragment, indexToTag(manager.getBackStackEntryCount() + 1))
                    .addToBackStack(null)
                    .commit();
        } else {
            manager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(containerId, fragment, indexToTag(0))
                    .commit();
        }

        manager.executePendingTransactions();
    }

    /**
     * Pops the top item if the stack.
     * If the fragment implements {@link OnBackPressedHandlingFragment}, calls {@link OnBackPressedHandlingFragment#onBackPressed()} instead.
     * If {@link OnBackPressedHandlingFragment#onBackPressed()} returns false the fragment gets popped.
     *
     * @return true if a fragment has been popped or if {@link OnBackPressedHandlingFragment#onBackPressed()} returned true;
     */
    public boolean back() {
        Fragment top = peek();
        if (top instanceof OnBackPressedHandlingFragment) {
            if (((OnBackPressedHandlingFragment) top).onBackPressed())
                return true;
        }
        return pop();
    }

    /**
     * Pops the topmost fragment from the stack.
     * The lowest fragment can't be popped, it can only be replaced.
     *
     * @return false if the stack can't pop or true if a top fragment has been popped.
     */
    public boolean pop() {
        if (manager.getBackStackEntryCount() == 0)
            return false;
        manager.popBackStackImmediate();
        return true;
    }

    /**
     * Replaces stack contents with just one fragment.
     */
    public void replace(Fragment fragment) {
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        manager.beginTransaction()
                .replace(containerId, fragment, indexToTag(0))
                .commit();
        manager.executePendingTransactions();
    }

    /**
     * Returns the topmost fragment in the stack.
     */
    public Fragment peek() {
        return manager.findFragmentById(containerId);
    }

    /**
     * Returns a back fragment if the fragment is of given class.
     * If such fragment does not exist and activity implements the given class then the activity will be returned.
     *
     * @param fragment     a fragment to search from.
     * @param callbackType a class of type for callback to search.
     * @param <T>          a type of callback.
     * @return a back fragment or activity.
     */
    @SuppressWarnings("unchecked")
    public <T> T findCallback(Fragment fragment, Class<T> callbackType) {

        Fragment back = getBackFragment(fragment);

        if (back != null && callbackType.isAssignableFrom(back.getClass()))
            return (T) back;

        if (callbackType.isAssignableFrom(activity.getClass()))
            return (T) activity;

        return null;
    }

    private Fragment getBackFragment(Fragment fragment) {
        List<Fragment> fragments = getFragments();
        for (int f = fragments.size() - 1; f >= 0; f--) {
            if (fragments.get(f) == fragment && f > 0)
                return fragments.get(f - 1);
        }
        return null;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>(manager.getBackStackEntryCount() + 1);
        for (int i = 0; i < manager.getBackStackEntryCount() + 1; i++) {
            Fragment fragment = manager.findFragmentByTag(indexToTag(i));
            if (fragment != null)
                fragments.add(fragment);
        }
        return fragments;
    }

    private String indexToTag(int index) {
        return Integer.toString(index);
    }
}