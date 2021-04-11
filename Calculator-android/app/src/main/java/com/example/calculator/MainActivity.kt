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
    private var symbol: String? = null
    private var isDecimal : Boolean = false
    private var isFirstDecimal : Boolean = false;
    private var errorMessage : String = ""

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
                updateInput(value)
            }
            value == "CE" -> onCleanAll()
            value == "C" -> onClean()
            value == "." -> onPointClicked()
            value == "=" -> onEqualsClicked()
            listOf("/", "*", "-", "+").contains(value) -> onSymbolClicked(value)
        }
    }

    private fun onSymbolClicked(symbolValue: String) {
        if(input == null) return
        symbol = symbolValue
        previousInput = input
        input = null
        isDecimal = false
        updateDisplayContainerWithCalcul();

    }

    private fun onEqualsClicked() {
        if (input == null || previousInput == null || symbol == null) {
            return
        }

        input = when (symbol) {
            "+" -> input!! + previousInput!!
            "-" -> input!! - previousInput!!
            "*" -> input!! * previousInput!!
            "/" -> onDivision()
            else -> null
        }

        previousInput = null
        symbol = null
        isDecimal = false
        updateDisplayContainerWithCalcul();
    }

    private fun onDivision() : Float {
        if(input == 0F) {
            errorMessage = "La division par 0 n'est pas possible"
            return 0F;
        }

        return previousInput!! / input!!;
    }

    private fun onPointClicked() {
        if(isDecimal) return
        isDecimal = true
        isFirstDecimal = true
        updateDisplayContainerWithCalcul();
    }

    private fun onClean() {

        if(input !== null)
        {
            val removeScope : Int =  if(input!! % 1 == 0F ) 3 else 1;
            val newValue = input.toString().substring(0, input.toString().length - removeScope)
            input =if(newValue == "") null else  newValue.toFloat()
            updateDisplayContainerWithCalcul();
        }
        else
        {
            symbol = null
            input = previousInput
            previousInput = null
        }

        updateDisplayContainerWithCalcul()
    }

    private fun onCleanAll() {
        input = null
        previousInput = null
        isDecimal = false
        isFirstDecimal = false
        updateDisplayContainerWithCalcul();

    }

    private fun formatNumberStr(number : Float?): String {
        val numberValue : Float = number ?: 0F;
        return if(number == null) "" else if (numberValue % 1 == 0F) number?.toInt().toString() else number.toString();
    }

    private fun updateDisplayContainer(value: String) {
        findViewById<TextView>(R.id.calculator_display_container).text = value
    }

    private fun updateDisplayContainerWithCalcul() {
        if(errorMessage != "") {
            updateDisplayContainer(errorMessage)
            errorMessage = ""
            return;
        }
        val prevStr = formatNumberStr(previousInput)
        val symbolStr = if(symbol != null) symbol else "";
        val inputStr = formatNumberStr(input)
        val pointStr = if(isFirstDecimal) "." else "";
        updateDisplayContainer("$prevStr $symbolStr $inputStr$pointStr")
    }

    private fun updateInput(value: String) {
        if(isDecimal) {
            createDecimalInput(value.toInt())
        }
        else {
            createIntegerInput(value.toInt())
        }
        updateDisplayContainerWithCalcul()
    }


    private fun createIntegerInput(value: Int) {
        val inputValue = input?.toInt() ?: 0;
        input = (inputValue.toString() + value.toString()).toFloat()
    }

    private fun createDecimalInput(value: Int) {
        if(isFirstDecimal) {
            isFirstDecimal = false;
            input = (input?.toInt().toString() + "." + value.toString()).toFloat()
            return;
        }
        input = (input.toString()  + value.toString()).toFloat()
    }

}

fun String.isNum(): Boolean {
    return length == 1 && isDigitsOnly()
}