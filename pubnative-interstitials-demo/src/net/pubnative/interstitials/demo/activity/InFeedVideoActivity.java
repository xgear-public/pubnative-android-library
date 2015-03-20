package net.pubnative.interstitials.demo.activity;

import net.pubnative.library.model.AdFormat;

import org.droidparts.util.ui.ViewUtils;

import android.view.View;

public class InFeedVideoActivity extends VideoBannerActivity {

	@Override
	protected AdFormat getAdFormat() {
		return AdFormat.VIDEO;
	}

	@Override
	protected View makeView() {
		View v = super.makeView();
		ViewUtils.setGone(true, v.findViewById(holder.playButtonViewId));
		holder.playButtonViewId = -1;
		return v;
	}

}
