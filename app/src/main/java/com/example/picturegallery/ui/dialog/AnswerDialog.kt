package com.example.picturegallery.ui.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun initViews() = with(binding) {
        headerText.text = data.header
        answerText.text = data.answer

        positiveButton.apply {
            text = data.positiveButtonText
            setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    ANSWER_DIALOG_RESULT, bundleOf(
                        ANSWER_DIALOG_RESULT_VALUE to POSITIVE_BUTTON_CLICK
                    )
                )
                dismiss()
            }
        }

        negativeButton.apply {
            text = data.negativeButtonText
            setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    ANSWER_DIALOG_RESULT, bundleOf(
                        ANSWER_DIALOG_RESULT_VALUE to NEGATIVE_BUTTON_CLICK
                    )
                )
                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.DialogTheme

    companion object {
        const val ANSWER_DIALOG_RESULT = "ANSWER_DIALOG_RESULT"
        const val ANSWER_DIALOG_RESULT_VALUE = "ANSWER_DIALOG_RESULT_VALUE"
        private const val POSITIVE_BUTTON_CLICK = true
        private const val NEGATIVE_BUTTON_CLICK = false
    }

    data class DialogData(
        val header: String = "",
        val answer: String = "",
        val positiveButtonText: String = "",
        val negativeButtonText: String = ""
    )

}
