package com.quinn.xmpp.ui.main;

import java.lang.reflect.Method;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.quinn.xmpp.R;
import com.quinn.xmpp.Intents.Builder;
import com.quinn.xmpp.ui.BaseActivity;
import com.quinn.xmpp.ui.main.MainPagerChangeListener.PagerCallback;
import com.quinn.xmpp.ui.widget.SlidingTabLayout;

/**
 * login activity
 * 
 * @author Quinn
 * @date 2015-1-24
 */
public class MainActivity extends BaseActivity {

	private final static int NUM_ITEMS = 2;

	@InjectView(R.id.vPager)
	ViewPager viewpager;

	@InjectView(R.id.sliding_tabs)
	SlidingTabLayout slidingTabLayout;

	private String titles[] = new String[] { "chatting", "contacts" };

	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		mAdapter = new MyAdapter(getSupportFragmentManager(), titles);
		viewpager.setAdapter(mAdapter);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("XMPP");
		setSupportActionBar(toolbar);

		DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
				mDrawerLayout, toolbar, R.string.setting_leftTxt_openfireIP,
				R.string.setting_leftTxt_openfireIP);
		toggle.syncState();
		mDrawerLayout.setDrawerListener(toggle);

		slidingTabLayout.setViewPager(viewpager);
		slidingTabLayout
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
					@Override
					public int getIndicatorColor(int position) {
						return Color.WHITE;
					}
				});
		System.out.println("all roster = " + smack.getAllRosterEntry());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	/**
	 * 设置menu显示icon
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {

		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm, String[] titles2) {
			super(fm);
			titles = titles2;
		}

		private String titles[];

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new ChattingFragment();
			case 1:
				return new ContactsFragment();
			default:
				return new ContactsFragment();
			}
		}
	}



	public static Intent createIntent() {
		Builder builder = new Builder("main.Main.View");
		return builder.toIntent();
	}

}
