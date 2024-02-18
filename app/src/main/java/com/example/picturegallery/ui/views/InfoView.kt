package com.example.picturegallery.ui.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.picturegallery.R
import com.example.picturegallery.databinding.EmptyStateLayoutBinding
import kotlinx.parcelize.Parcelize

class InfoView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrSet, defStyle) {

    private val binding: EmptyStateLayoutBinding

    @StringRes
    private val header: Int

    @StringRes
    private val subtitle: Int

    @StringRes
    private val buttonText: Int
    init {
        LayoutInflater.from(context).inflate(R.layout.empty_state_layout, this, true)
        binding = EmptyStateLayoutBinding.bind(this)

        val emptyUi = EmptyUi()

        context.obtainStyledAttributes(attrSet, R.styleable.InfoView, defStyle, 0).let { attrs ->
            header = attrs.getResourceId(R.styleable.InfoView_header, R.string.empty_album_list_header)
            subtitle = attrs.getResourceId(R.styleable.InfoView_subtitle, R.string.empty_album_list_subtitle)
            buttonText = attrs.getResourceId(R.styleable.InfoView_emptyButtonText, R.string.create)

            attrs.recycle()
        }

        setResUiState(
            emptyUi.copy(
                header = header, subtitle = subtitle, buttonText = buttonText
            )
        )
        setVisible(false)
    }

    fun setResUiState(
        state: EmptyUi
    ) = with(binding) {
        val (header, subtitle, buttonText, onButtonClick) = state
        emptyItemHeader.setText(header)
        emptyItemSubtitle.setText(subtitle)
        with(emptyItemButton) {
            setText(buttonText)
            setOnClickListener {
                onButtonClick()
            }
        }
    }

    fun setVisible(isVisible: Boolean) {
        this.isVisible = isVisible
    }

}

@Parcelize
data class EmptyUi(
    @StringRes val header: Int = R.string.empty_album_list_header,
    @StringRes val subtitle: Int = R.string.empty_album_list_subtitle,
    @StringRes val buttonText: Int = R.string.create,
    val onButtonClick: () -> Unit = {}
) : Parcelable