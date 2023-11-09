package com.lazzy.stories.tools.control

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.lazzy.stories.R

class EditTextPassword : AppCompatEditText {

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAtt: Int) : super(context, attributeSet, defStyleAtt){
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                when{
                    password.isBlank() -> error = context.getString(R.string.error_empty_password)
                    password.length < 8 -> error = context.getString(R.string.error_length_password)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}