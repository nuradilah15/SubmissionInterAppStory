package com.example.submissioninterappstory.cutomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide.init
import com.example.submissioninterappstory.R
import com.google.android.material.textfield.TextInputEditText

class EditTextEmailStory : TextInputEditText, View.OnTouchListener {

    private lateinit var clearButtontext: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textSize = 15f
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        hint = "masukkan email anda"
    }

    private fun init() {
        clearButtontext = ContextCompat.getDrawable(
            context,
            R.drawable.ic_baseline_close_24
        ) as Drawable // x button

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()

                if (!isEmailakunValid(s)) {
                    showError()
                }
            }
        })
    }

    private fun isEmailakunValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showError() {
        error = "Masukan email yang valid"
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtontext)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtontext.intrinsicWidth + paddingStart).toFloat()
                if (event.x < clearButtonEnd) isClearButtonClicked = true
            } else {
                clearButtonStart = (width - paddingEnd - clearButtontext.intrinsicWidth).toFloat()
                if (event.x > clearButtonStart) isClearButtonClicked = true
            }

            if (isClearButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtontext = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
                        showClearButton() // show button x
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtontext = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
                        if (text != null) text?.clear() // clear text
                        hideClearButton()  // hide button x
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }

}