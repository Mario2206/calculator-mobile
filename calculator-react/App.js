import { StatusBar } from 'expo-status-bar';
import React, { useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import InputButton from "./src/InputButton"

const inputButtons = [
  ["", "", "C", "CE"], 
  [1, 2, 3, '/'],
  [4, 5, 6, '*'],
  [7, 8, 9, '-'],
  [0, '.', '=', '+']
]

const styles = StyleSheet.create({
  rootContainer: {
    flex: 1
  },
  inputContainer: {
      flex: 8,
      backgroundColor: '#3E606F'
  },
  inputRow: {
    flex: 1,
    flexDirection: 'row'
  },
  displayContainer: {
    flex: 2,
    backgroundColor: '#193441',
    justifyContent: 'center'
  },
  displayText: {
      color: 'white',
      fontSize: 38,
      fontWeight: 'bold',
      textAlign: 'right',
      padding: 20
  },
});


export default function App() {

  const [inputValue, setInputValue] = useState(null)
  const [previousInputValue, setPreviousInputValue] = useState(null)
  const [selectedSymbol, setSelectedSymbol] = useState("")
  const [isDecimal, setIsDecimal] = useState(false)
 

  function onInputButtonPressed(input) {
    switch (typeof input) {
      case 'number':
        return handleNumberInput(input)
      case 'string':
        return handleStringInput(input) 
    }
  }

  function clear() {
    if(inputValue === null) {
      setSelectedSymbol("")
      setInputValue(previousInputValue)
      setPreviousInputValue(null)
    }
    else {
      const newValue = Number.parseFloat( inputValue.toString().slice(0, inputValue.toString().length - 1 ) )
      setInputValue(newValue || null)
    }
  }

  function clearAll() {
      setSelectedSymbol("")
      setPreviousInputValue(null)
      setInputValue(null)
      setIsDecimal(false)
  }


  function calcul() {
    if(selectedSymbol === "/" && inputValue == 0) {
      alert("La division par 0 n'est pas possible !")
      return
    }
    setInputValue(eval(previousInputValue + selectedSymbol + inputValue))
    setSelectedSymbol("")
    setPreviousInputValue(null)
    setIsDecimal(false)
  }

  function handleNumberInput(num) {
    
    if(isDecimal) {
      const numberParts = inputValue.toString().split(".")
      const decimalValue = numberParts.length === 2 ? numberParts[1] : null
      setInputValue(numberParts[0] + "." + (decimalValue || "") + num)
      return
    }

    setInputValue(prevState => prevState * 10 + num   )
  }

  function handleStringInput(val) {

    if(["/", "*", "+", "-"].includes(val) && inputValue) {
      setSelectedSymbol(val)
      setPreviousInputValue(inputValue)
      setInputValue(null)
      setIsDecimal(false)
    }
    else if( val === "=" && inputValue !== null  && previousInputValue !== null) {
      calcul()
    }
    else if(val === "CE") {
      clearAll()
    }
    else if(val === "C") {
      clear()
    }
    else if (val === ".") {
      setIsDecimal(true)
    }

  }

  function renderInputButtons() {
      let views = inputButtons.map((row, index) => (
        <View style={styles.inputRow} key={"row-" + index}>{
          row.map((item, i) => <InputButton onPress={() => onInputButtonPressed(item)} value={item} key={index + "-" + i} />)
        }</View>
      ));
      return views;
  }

  

  return (
    <View style={styles.rootContainer}>
      <View style={styles.displayContainer}>
        <Text style={styles.displayText}>
          {previousInputValue} {selectedSymbol} {inputValue}{isDecimal && inputValue % 1 === 0 ? "." : null}
        </Text>
      </View>
      <View style={styles.inputContainer}>
        {renderInputButtons()}
      </View>
    </View>
  );
}
