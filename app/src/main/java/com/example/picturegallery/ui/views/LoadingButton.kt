package com.example.picturegallery.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.picturegallery.R
import com.example.picturegallery.databinding.LoadingButtonBinding

class LoadingButton(context: Context, attrs: AttributeSet?, defStyleAttrs: Int, defStyleRes: Int) : FrameLayout(context, attrs, defStyleAttrs, defStyleRes) {

    private var binding: LoadingButtonBinding
    private var buttonText = ""
    private var isEnabled: Boolean = false

    constructor(context: Context, attrs: AttributeSet?, desStyle: Int) : this(
        context,
        attrs,
        desStyle,
        0
    )

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.loading_button, this, true)
        binding = LoadingButtonBinding.bind(this)
        readAttrs(attrs, defStyleAttrs, defStyleRes)
        initViews()
    }

    private fun readAttrs(attrs: AttributeSet?, defStyleAttrs: Int, defStyleRes: Int) {
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.LoadingButton,
                defStyleAttrs,
                defStyleRes
            )
            buttonText = typeArray.getString(R.styleable.LoadingButton_buttonText) ?: ""
            isEnabled = typeArray.getBoolean(R.styleable.LoadingButton_isButtonEnabled, false)

            typeArray.recycle()
        } ?: return
    }

    private fun initViews() = with(binding) {
        text.text = buttonText
        this@LoadingButton.isEnabled = isEnabled
        buttonLayout.isEnabled = isEnabled
    }

    fun setLoadingState(isLoading: Boolean) = with(binding) {
        text.isVisible = !isLoading
        progress.isVisible = isLoading
        this@LoadingButton.isEnabled = !isLoading && isEnabled
        buttonLayout.isEnabled = !isLoading && isEnabled
    }

    fun isEnabled(isEnabled: Boolean) = with(binding) {
        this@LoadingButton.isEnabled = isEnabled
        buttonLayout.isEnabled = isEnabled
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        binding.buttonLayout.setOnClickListener(l)
    }

}