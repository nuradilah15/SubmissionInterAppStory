package com.example.submissioninterappstory.cutomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide.init
import com.example.submissioninterappstory.R

class EditTextStory : AppCompatEditText, View.OnTouchListener {

    private lateinit var eyeshow : Drawable

    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        showEyes()

        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        hint = "masukan password anda"
    }

    private fun showEyes(){
        setButtomDrawablespass(endOfTheText = eyeshow)
    }

    private fun init(){
        eyeshow = ContextCompat.getDrawable(context, R.drawable.eyes) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length < 6) showErrorPass()
            }
        })
    }

    private fun showErrorPass(){
        error = "Masukan minimal 6 huruf"
    }

    private fun hideEyesButtonPass(){
        setButtomDrawablespass()
    }

    private fun setButtomDrawablespass(
        startOfTheNext: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheNext,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null){
            val eyeButtonStart: Float
            val eyeButtonEnd: Float
            var isEyeButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL){
                eyeButtonEnd = (eyeshow.intrinsicWidth + paddingStart). toFloat()
                if (event.x  < eyeButtonEnd) isEyeButtonClicked = true
            } else {
                eyeButtonStart = (width - paddingEnd - eyeshow.intrinsicWidth).toFloat()
                if (event.x > eyeButtonStart) isEyeButtonClicked = true
            }
            if (isEyeButtonClicked){
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        hideEyesButtonPass()
                        if (transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
                            transformationMethod = PasswordTransformationMethod.getInstance()
                            eyeshow = ContextCompat.getDrawable(context, R.drawable.ic_show_eyes) as Drawable
                            showEyes()
                        } else {
                            transformationMethod = HideReturnsTransformationMethod.getInstance()
                            eyeshow = ContextCompat.getDrawable(context, R.drawable.eyes) as Drawable
                            showEyes()
                        }
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }

}