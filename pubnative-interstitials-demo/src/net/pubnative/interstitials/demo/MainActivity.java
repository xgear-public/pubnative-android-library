package net.pubnative.interstitials.demo;

import net.pubnative.interstitials.PubNativeInterstitials;
import net.pubnative.interstitials.api.PubNativeInterstitialsListener;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.demo.activity.BannerActivity;
import net.pubnative.interstitials.demo.contract.PubNativeDemoInterstitialsType;
import net.pubnative.interstitials.demo.delegate.AbstractDemoDelegate;
import net.pubnative.interstitials.demo.misc.DialogFactory;
import net.pubnative.interstitials.demo.misc.DialogFactory.SettingsDialogListener;
import net.pubnative.library.PubNative;
import net.pubnative.library.model.response.NativeAd;

import org.droidparts.activity.legacy.Activity;
import org.droidparts.util.L;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener,
		PubNativeInterstitialsListener {

	private DialogFactory dialogFactory;

	@Override
	public void onPreInject() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialogFactory = new DialogFactory(this);
		PubNativeInterstitials.init(this, Contract.APP_TOKEN);
		PubNativeInterstitials.addListener(this);
		for (int id : new int[] { R.id.btn_settings, R.id.btn_interstitial,
				R.id.btn_banner, R.id.btn_list_item_brief,
				R.id.btn_list_item_full, R.id.btn_carousel,
				R.id.btn_video_banner, R.id.btn_in_feed_video }) {
			findViewById(id).setOnClickListener(this);
		}
		AbstractDemoDelegate.init(this, Contract.APP_TOKEN);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PubNative.setImageReshaper(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_settings:
			dialogFactory.getSettingsDialog(adCount, l).show();
			break;
		case R.id.btn_interstitial:
			PubNativeInterstitials.show(this,
					PubNativeInterstitialsType.INTERSTITIAL, adCount);
			break;
		case R.id.btn_banner:
			startActivity(new Intent(this, BannerActivity.class));
			break;
		case R.id.btn_list_item_brief:
			AbstractDemoDelegate.show(this,
					PubNativeDemoInterstitialsType.LIST, adCount);
			break;
		case R.id.btn_list_item_full:
			AbstractDemoDelegate.show(this,
					PubNativeDemoInterstitialsType.NATIVE, adCount);
			break;
		case R.id.btn_carousel:
			AbstractDemoDelegate.show(this,
					PubNativeDemoInterstitialsType.CAROUSEL, adCount);
			break;
		case R.id.btn_video_banner:
			AbstractDemoDelegate.show(this,
					PubNativeDemoInterstitialsType.VIDEO_BANNER, adCount);
			break;
		case R.id.btn_in_feed_video:
			AbstractDemoDelegate.show(this,
					PubNativeDemoInterstitialsType.VIDEO_IN_FEED, adCount);
			break;
		}
	}

	private final SettingsDialogListener l = new SettingsDialogListener() {

		@Override
		public void onAdCountChanged(int count) {
			adCount = count;
		}

	};

	private int adCount = 5;

	//

	@Override
	public void onShown(PubNativeInterstitialsType type) {
		L.i("Shown %s.", type);
	}

	@Override
	public void onTapped(NativeAd ad) {
		L.i("Tapped %s.", ad);
	}

	@Override
	public void onClosed(PubNativeInterstitialsType type) {
		L.i("Closed %s", type);
	}

	@Override
	public void onError(Exception ex) {
		L.w(ex);
	}

}
