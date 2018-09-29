package com.lmy.codec.texture.impl.sticker

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.lmy.codec.helper.Resources
import com.lmy.codec.texture.impl.BaseTexture
import javax.microedition.khronos.opengles.GL10

abstract class BaseSticker(var frameBuffer: IntArray,
                           var width: Int,
                           var height: Int,
                           name: String = "BaseSticker") : BaseTexture(frameBuffer, name) {
    private var aPositionLocation = 0
    private var aTextureCoordinateLocation = 0
    private var uTextureLocation = 0
    private var texture: IntArray = IntArray(1)

    open fun init() {
        createProgram()
        createTexture(texture)
    }

    private fun createProgram() {
        shaderProgram = createProgram(Resources.instance.readAssetsAsString("shader/vertex_sticker.glsl"),
                Resources.instance.readAssetsAsString("shader/fragment_sticker.glsl"))
        aPositionLocation = getAttribLocation("aPosition")
        uTextureLocation = getUniformLocation("uTexture")
        aTextureCoordinateLocation = getAttribLocation("aTextureCoord")
    }

    fun createTexture(texture: IntArray) {
        GLES20.glGenTextures(texture.size, texture, 0)
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, texture[0])
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE.toFloat())
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, GLES20.GL_NONE)
    }

    fun bindTexture(bitmap: Bitmap) {
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, texture[0])
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, GLES20.GL_NONE)
    }

    fun active(samplerLocation: Int) {
        GLES20.glUseProgram(shaderProgram!!)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0])
        setUniform1i(samplerLocation, 0)
    }

    fun inactive() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_NONE)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, GLES20.GL_NONE)
        GLES20.glUseProgram(GLES20.GL_NONE)
        GLES20.glFlush()
    }

    override fun release() {
        super.release()
        GLES20.glDeleteTextures(texture.size, texture, 0)
    }
}