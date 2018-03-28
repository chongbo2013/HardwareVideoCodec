package com.lmy.sample

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.TextureView
import com.lmy.codec.entity.Parameter
import com.lmy.codec.impl.CameraPreviewPresenter
import com.lmy.codec.util.debug_v
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    private val mPresenter: CameraPreviewPresenter = CameraPreviewPresenter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val param = Parameter()
        mPresenter.prepare(param)
        mTextureView.keepScreenOn = true
        mTextureView.surfaceTextureListener = this
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
        mPresenter.updatePreview(p1, p2)
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        mPresenter.stopPreview()
        return true
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
        if (null != p0)
            mPresenter.startPreview(p0, p1, p2)
        debug_v( "onSurfaceTextureAvailable")
    }
}
