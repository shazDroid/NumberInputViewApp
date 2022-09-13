package com.shazdroid.numberinputview

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class ShazNumberInputView @JvmOverloads constructor(
    context: Context, attr: AttributeSet? = null, def: Int = 0
) : ConstraintLayout(context, attr, def) {
    private lateinit var titleTV: TextView
    private lateinit var editText: EditText
    private lateinit var countIncreaseIV: ImageView
    private lateinit var countDecreaseIV: ImageView

    private var minValue: Int = 0
    private var maxValue: Int = 0
    private var title: String? = ""
    private var type: Int = 0
    private var allowNegativeValue: Boolean = false
    private var defaultValue: String? = "0"

    init {
        init(attr)
    }

    private fun init(attr: AttributeSet?) {
        View.inflate(context, R.layout.number_input_view, this)
        titleTV = findViewById(R.id.titleTv)
        editText = findViewById(R.id.editTextTv)
        countIncreaseIV = findViewById(R.id.countIncrease)
        countDecreaseIV = findViewById(R.id.countDecrease)

        val ta = context.obtainStyledAttributes(attr, R.styleable.ShazNumberInputView)
        try {
            // getting attrs
            defaultValue = ta.getString(R.styleable.ShazNumberInputView_defaultValue).toString()
            type = ta.getInteger(R.styleable.ShazNumberInputView_valueType,0)
            maxValue = ta.getInteger(R.styleable.ShazNumberInputView_maxValue, 0)
            minValue = ta.getInteger(R.styleable.ShazNumberInputView_minValue, 0)
            title = ta.getString(R.styleable.ShazNumberInputView_title)
            allowNegativeValue = ta.getBoolean(R.styleable.ShazNumberInputView_allowNegativeValues,false)

            // setting title
            if (title.isNullOrEmpty()){
                titleTV.visibility = View.GONE
            } else {
                titleTV.visibility = View.VISIBLE
                titleTV.text = title
            }

            // check if default is set or not
            if (defaultValue.isNullOrEmpty()){
                defaultValue = if (type == 0){
                    "0"
                } else {
                    "0.0"
                }
                editText.setText(defaultValue)
            } else {
                editText.setText(defaultValue)
            }

        } catch (e: Exception) {
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
        } finally {
            ta.recycle()
        }

        // call listeners
        setListeners()

        // set type & validation
        setTypeAndValidation(type)
    }

    /**
     * Setting up validations & type
     */
    private fun setTypeAndValidation(type: Int){
        // if default value is not set set default value
        when(type){
            // whole number
            0 -> {
                if (defaultValue.isNullOrEmpty()){
                    defaultValue = "0"
                }
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                editText.setText(defaultValue)
            }

            // decimal point
            1 -> {
                if (defaultValue.isNullOrEmpty()){
                    defaultValue = "0.0"
                }
                editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                editText.setText(defaultValue)
            }
        }
    }

    /**
     *  Listener
     */
    @SuppressLint("SetTextI18n")
    private fun setListeners(){
        // increase count
        countIncreaseIV.setOnClickListener {
            if (type == 0) {
                val currentVal = editText.text.toString().toInt()
                editText.setText("${currentVal + 1}")
            } else {
                val currentVal = editText.text.toString().toFloat()
                editText.setText("${currentVal + 1.0}")
            }
        }

        // decrease count
        countDecreaseIV.setOnClickListener {
            if (type == 0) {
                val currentVal = editText.text.toString().toInt()
                editText.setText("${currentVal - 1}")
            } else {
                val currentVal = editText.text.toString().toFloat()
                editText.setText("${currentVal - 1.0}")
            }
        }
    }
}