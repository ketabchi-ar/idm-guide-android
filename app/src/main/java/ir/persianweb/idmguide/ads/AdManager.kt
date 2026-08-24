package ir.persianweb.idmguide.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

object AdManager {
    private const val TAG = "AdManager"

    const val TAPSELL_KEY = "dummy_tapsell_app_key"
    const val ZONE_ID_BANNER = "dummy_zone_standard_banner"
    const val ZONE_ID_INTERSTITIAL = "dummy_zone_interstitial"

    private var isInitialized = false

    fun initialize(context: Context) {
        try {
            TapsellPlus.initialize(context, TAPSELL_KEY, object : TapsellPlusInitListener {
                override fun onInitializeSuccess(adNetworks: AdNetworks) {
                    Log.d(TAG, "Tapsell initialized successfully")
                    isInitialized = true
                }

                override fun onInitializeFailed(adNetworks: AdNetworks, adNetworkError: AdNetworkError) {
                    Log.e(TAG, "Tapsell init failed: " + adNetworkError.errorMessage)
                    isInitialized = false
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Tapsell", e)
        }
    }

    fun showStandardBanner(activity: Activity, container: ViewGroup) {
        try {
            TapsellPlus.requestStandardBannerAd(
                activity,
                ZONE_ID_BANNER,
                TapsellPlusBannerType.BANNER_320x50,
                object : AdRequestCallback() {
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        TapsellPlus.showStandardBannerAd(
                            activity,
                            tapsellPlusAdModel.responseId,
                            container,
                            object : AdShowListener() {
                                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                                    super.onOpened(tapsellPlusAdModel)
                                    Log.d(TAG, "Banner Opened")
                                }

                                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                                    super.onError(tapsellPlusErrorModel)
                                    Log.e(TAG, "Banner Show Error: " + tapsellPlusErrorModel.errorMessage)
                                }
                            }
                        )
                    }

                    override fun error(message: String) {
                        super.error(message)
                        Log.e(TAG, "Banner Request Error: " + message)
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "showStandardBanner Exception", e)
        }
    }

    fun showInterstitialAd(activity: Activity, onAdClosed: (() -> Unit)? = null) {
        try {
            TapsellPlus.requestInterstitialAd(
                activity,
                ZONE_ID_INTERSTITIAL,
                object : AdRequestCallback() {
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        TapsellPlus.showInterstitialAd(
                            activity,
                            tapsellPlusAdModel.responseId,
                            object : AdShowListener() {
                                override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                                    super.onClosed(tapsellPlusAdModel)
                                    onAdClosed?.invoke()
                                }

                                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                                    super.onError(tapsellPlusErrorModel)
                                    onAdClosed?.invoke()
                                }
                            }
                        )
                    }

                    override fun error(message: String) {
                        super.error(message)
                        onAdClosed?.invoke()
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "showInterstitialAd Exception", e)
            onAdClosed?.invoke()
        }
    }
}
