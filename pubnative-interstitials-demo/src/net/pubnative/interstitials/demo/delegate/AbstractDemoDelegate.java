/**
 * Copyright 2014 PubNative GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pubnative.interstitials.demo.delegate;

import static org.droidparts.util.ui.ViewUtils.setInvisible;
import net.pubnative.interstitials.demo.PubNativeInterstitialsDemoActivity;
import net.pubnative.interstitials.demo.R;
import net.pubnative.interstitials.demo.contract.PubNativeDemoInterstitialsType;
import net.pubnative.interstitials.persist.InMem;
import net.pubnative.interstitials.util.ScreenUtil;
import net.pubnative.library.PubNative;
import net.pubnative.library.inner.PubNativeWorker;
import net.pubnative.library.model.AdFormat;
import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.NativeAd;

import org.droidparts.util.ui.ViewUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class AbstractDemoDelegate implements OnClickListener {

	public static boolean backgroundRedirectEnabled = true;

	public static AbstractDemoDelegate get(
			PubNativeInterstitialsDemoActivity act,
			PubNativeDemoInterstitialsType type, int adCount) {
		switch (type) {
		case NATIVE:
			return new NativeDelegate(act, adCount);
		case LIST:
			return new ListDelegate(act, adCount);
		case CAROUSEL:
			return new CarouselDelegate(act, adCount);
		case VIDEO_BANNER:
			return new VideoBannerDelegate(act);
		case VIDEO_IN_FEED:
			return new VideoInFeedDelegate(act);
		default:
			throw new IllegalArgumentException(type.toString());
		}
	}

	protected final PubNativeInterstitialsDemoActivity act;
	protected final int adCount;

	protected View contentView;
	protected View holderView;

	protected View closeBtn;

	public AbstractDemoDelegate(PubNativeInterstitialsDemoActivity act,
			int adCount) {
		this.act = act;
		this.adCount = adCount;
	}

	public void onCreate() {
		contentView = LayoutInflater.from(act).inflate(getContentLayoutId(),
				null);
		setInvisible(true, contentView);
		closeBtn = findViewById(R.id.btn_close);
		closeBtn.setOnClickListener(this);
		holderView = findViewById(R.id.view_holder);
		act.setContentView(contentView);
	}

	public final AdRequest getAdRequest(String appKey) {
		AdRequest req = new AdRequest(appKey, getAdFormat());
		req.fillInDefaults(act);
		req.setAdCount(adCount);
		return req;
	}

	protected AdFormat getAdFormat() {
		return AdFormat.NATIVE;
	}

	public abstract AdHolder<?>[] getAdHolders();

	public void onRotate() {
	}

	public void onLoadingDone() {
		setInvisible(false, contentView);
	}

	@Override
	public void onClick(View v) {
		if (v == closeBtn) {
			act.finish();
		}
	}

	//

	public View getContentView() {
		return contentView;
	}

	public void onResume() {
		PubNativeWorker.onResume();
	}

	public void onPause() {
		PubNativeWorker.onPause();
	}

	public void onActivityFinish() {
	}

	//

	public abstract PubNativeDemoInterstitialsType getType();

	protected abstract int getContentLayoutId();

	protected final <T extends View> T findViewById(int id) {
		return ViewUtils.findViewById(contentView, id);
	}

	//

	protected final void showInPlayStore(NativeAd ad) {
		if (backgroundRedirectEnabled) {
			PubNative.showInPlayStoreViaDialog(act, ad);
		} else {
			PubNative.showInPlayStoreViaBrowser(act, ad);
		}
	}

	//

	public static void init(Context ctx, String appKey) {
		AbstractDemoDelegate.ctx = ctx.getApplicationContext();
		InMem.appKey = appKey;
	}

	//

	public static void show(Activity act, PubNativeDemoInterstitialsType type,
			int adCount) {
		callingActivityIsFullScreen = ScreenUtil.isFullScreen(act);
		showCalled = true;
		Intent intent = PubNativeInterstitialsDemoActivity.getShowPromosIntent(
				ctx, callingActivityIsFullScreen, type, adCount);
		ctx.startActivity(intent);
	}

	//

	private static boolean callingActivityIsFullScreen;

	private static boolean showCalled;

	//

	private static Context ctx;

	static void onException(Exception e) {
		if (showCalled) {
			showCalled = false;
			Intent intent = PubNativeInterstitialsDemoActivity.getFinishIntent(
					ctx, callingActivityIsFullScreen);
			ctx.startActivity(intent);
		}
	}

}
