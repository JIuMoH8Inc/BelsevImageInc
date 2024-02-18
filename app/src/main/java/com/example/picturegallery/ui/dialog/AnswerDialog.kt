package com.example.picturegallery.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.AnswerDialogLayoutBinding

class AnswerDialog(private val data: DialogData) : DialogFragment(R.layout.answer_dialog_layout) {
    private val binding by viewBinding(AnswerDialogLayoutBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        headerText.text = data.header
        answerText.text = data.answer

        positiveButton.apply {
            text = data.positiveButtonText
            setOnClickListener {
                data.positiveButtonClick()
                dismiss()
            }
        }

        negativeButton.apply {
            text = data.negativeButtonText
            setOnClickListener {
                data.negativeButtonClick()
                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.DialogTheme

    data class DialogData(
        val header: String = "",
        val answer: String = "",
        val positiveButtonText: String = "",
        val negativeButtonText: String = "",
        val positiveButtonClick: () -> Unit = {},
        val negativeButtonClick: () -> Unit = {}
    )

}
