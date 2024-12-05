package com.arista.antifourtwenty.common.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.databinding.CustomDialogBinding

class CustomDialog (context: Context) : Dialog(context) {

    private var title: String = ""
    private var message: String = ""
    private var buttonText: String = "OK"
    private var onButtonClickListener: View.OnClickListener? = null
    private lateinit var binding: CustomDialogBinding

    fun setTitle(title: String) {
        this.title = title
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun setButtonText(buttonText: String) {
        this.buttonText = buttonText
    }

    fun setOnButtonClickListener(listener: View.OnClickListener) {
        this.onButtonClickListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.errTitle.text = title
        binding.errText.text = message
        binding.okButton.text = buttonText

        binding.okButton.setOnClickListener {
            onButtonClickListener?.onClick(it)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}