package com.example.picturegallery.ui.dialog

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.picturegallery.R
import com.example.picturegallery.databinding.InputTwoStringsDialogBinding
import kotlinx.parcelize.Parcelize

class InputTwoStringsDialog(private val data: InputData) :
    DialogFragment(R.layout.input_two_strings_dialog) {
    private val binding by viewBinding(InputTwoStringsDialogBinding::bind)
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
        val (
            header,
            firstInputHint,
            secondInputHint,
            negativeButtonText,
            initFirst,
            initSecond,
            positiveButtonText
        ) = data
        headerText.setText(header)
        with(first) {
            setText(initFirst)
            doAfterTextChanged {
                positiveButton.isEnabled = it?.isNotEmpty() ?: false
            }
        }
        firstInput.setHint(firstInputHint)
        secondInput.setHint(secondInputHint)
        second.setText(initSecond)

        with(positiveButton) {
            setText(positiveButtonText)
            setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    INPUT_DIALOG_RESULT,
                    bundleOf(
                        INPUT_RESULT_VALUE to InputResult(
                            first = first.text.toString(),
                            second = second.text.toString()
                        )
                    )
                )
                dismiss()
            }
        }
        with(negativeButton) {
            setText(negativeButtonText)
            setOnClickListener {
                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.DialogTheme

    data class InputData(
        @StringRes val header: Int = -1,
        @StringRes val firstInputHint: Int = -1,
        @StringRes val secondInputHint: Int = -1,
        @StringRes val negativeButtonText: Int = -1,
        val initFirstInput: String = "",
        val initSecondInput: String = "",
        @StringRes val positiveButtonText: Int = -1,
    )

    @Parcelize
    data class InputResult(
        val first: String = "",
        val second: String = ""
    ) : Parcelable

    companion object {
        const val INPUT_DIALOG_RESULT = "INPUT_DIALOG_RESULT"
        const val INPUT_RESULT_VALUE = "INPUT_RESUL_VALUE"
    }
}