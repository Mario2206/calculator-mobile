package com.example.calculator

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly

class MainActivity : AppCompatActivity() {

    private var input : Float? = null
    private var previousInput: Float? = null
    private var result: Float? = null
    private var symbol: String? = null
    private var isDecimal : Boolean = false

    companion object {
        private val INPUT_BUTTONS = listOf(
            listOf("","","C", "CE"),
            listOf("1", "2", "3", "/"),
            listOf("4", "5", "6", "*"),
            listOf("7", "8", "9", "-"),
            listOf("0", ".", "=", "+")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addCells(findViewById(R.id.calculator_input_container_line0), 0)
        addCells(findViewById(R.id.calculator_input_container_line1), 1)
        addCells(findViewById(R.id.calculator_input_container_line2), 2)
        addCells(findViewById(R.id.calculator_input_container_line3), 3)
        addCells(findViewById(R.id.calculator_input_container_line4), 4)
    }

    private fun addCells(linearLayout: LinearLayout, position: Int) {
        for (x in INPUT_BUTTONS[position].indices) {
            linearLayout.addView(
                TextView(
                    ContextThemeWrapper(this, R.style.CalculatorInputButton)
                ).apply {
                    text = INPUT_BUTTONS[position][x]
                    setOnClickListener { onCellClicked(this.text.toString()) }
                },
                LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
                )
            )
        }
    }


    private fun onCellClicked(value: String) {
        when {
            value.isNum() -> {
                updateInput(value.toFloat())
            }
            value == "CE" -> onCleanAll()
            value == "C" -> onClean()
            value == "." -> onPointClicked()
            value == "=" -> onEqualsClicked()
            listOf("/", "*", "-", "+").contains(value) -> onSymbolClicked(value)
        }
    }

    private fun onSymbolClicked(symbol: String) {
        this.symbol = symbol
        previousInput = input
        input = null

    }

    private fun onEqualsClicked() {
        if (input == null || previousInput == null || symbol == null) {
            return
        }

        result = when (symbol) {
            "+" -> input!! + previousInput!!
            "-" -> input!! - previousInput!!
            "*" -> input!! * previousInput!!
            "/" -> previousInput!! / input!!
            else -> null
        }

        updateDisplayContainer(if(result !== null) result.toString() else "ERROR")

        input = result
        previousInput = null
        symbol = null
    }

    private fun onPointClicked() {
        val integer = input?.toInt()
        updateDisplayContainer(integer.toString() + ".")
        isDecimal = true
    }

    private fun onClean() {
        isDecimal = false

        if(input !== null) {
            input = null
            updateDisplayContainer("")
        } else if(symbol !== null) {
            symbol = null
        }
    }

    private fun onCleanAll() {
        input = null
        previousInput = null
        updateDisplayContainer("")

    }

    private fun updateDisplayContainer(value: Any) {
        findViewById<TextView>(R.id.calculator_display_container).text = value.toString()
    }

    private fun updateInput(value: Float) {
        if(isDecimal) {
            createDecimal(value)
            updateDisplayContainer(input.toString())
            return
        }
        input = value
        isDecimal = false
        updateDisplayContainer(value)
    }



    private fun createDecimal(value: Float) {
        input = input!! + (value / 10)
        isDecimal = false
    }

}

fun String.isNum(): Boolean {
    return length == 1 && isDigitsOnly()
}