package com.lazzy.stories.tools.control

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.lazzy.stories.R
import com.lazzy.stories.tools.isEmailValid

class EditTextEmail : AppCompatEditText {

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        init()
    }


    private fun init(){
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                when {
                    email.isBlank() -> error = context.getString(R.string.error_empty_email)
                    !email.isEmailValid() -> error = context.getString(R.string.error_valid_email)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}