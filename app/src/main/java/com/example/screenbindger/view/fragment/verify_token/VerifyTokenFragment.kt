package com.example.screenbindger.view.fragment.verify_token

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.screenbindger.databinding.FragmentVerifyTokenBinding
import com.example.screenbindger.util.extensions.snack

class VerifyTokenFragment : Fragment() {

    private var _binding: FragmentVerifyTokenBinding? = null
    private val binding get() = _binding!!

    private val args: VerifyTokenFragmentArgs by navArgs()
    private val requestToken: String by lazy { args.requestToken }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyTokenBinding.inflate(inflater, container, false)

        showWebView()

        return binding.root
    }

    fun showWebView() {
        binding.wvCreateSession.apply {
            webChromeClient = object : WebChromeClient() {
                // popup webview!
                @SuppressLint("SetJavaScriptEnabled")
                override fun onCreateWindow(
                    view: WebView,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message
                ): Boolean {
                    val newWebView: WebView = createWebView()

                    view.addView(newWebView)
                    resultMsg.obj = newWebView
                    resultMsg.sendToTarget()

                    newWebView.webChromeClient = object : WebChromeClient() {
                        override fun onCloseWindow(window: WebView) {
                            super.onCloseWindow(window)

                            binding.wvCreateSession.removeView(newWebView)
                        }
                    }
                    return true
                }

                override fun onCloseWindow(window: WebView) {
                    super.onCloseWindow(window)
                }
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
                ): Boolean {
                    view.loadUrl(url)
                    return false
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.action == MotionEvent.ACTION_UP
                    && canGoBack()
                ) {
                    goBack()
                    return@setOnKeyListener true
                } else {

                    return@setOnKeyListener false
                }
            }

            loadUrl("https://www.themoviedb.org/authenticate/$requestToken")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun createWebView(): WebView = WebView(requireActivity()).apply {
        settings.apply {
            javaScriptEnabled = true; // Done above
            domStorageEnabled = true; // Try
            setSupportZoom(false);
            allowFileAccess = true;
            allowContentAccess = true;
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}