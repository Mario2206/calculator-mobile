
import React from 'react';
import {
    View,
    Text,
    StyleSheet,
    TouchableHighlight
} from 'react-native';



const style = StyleSheet.create({
    inputButton: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        borderWidth: 0.5,
        borderColor: '#91AA9D'
    },

    inputButtonText: {
        fontSize: 22,
        fontWeight: 'bold',
        color: 'white'
    }
})

export default function InputButton({value, onPress}) {

    
    return (
        <TouchableHighlight style={style.inputButton} onPress={onPress} underlayColor="#193441">
            <Text style={style.inputButtonText}>{value}</Text>
        </TouchableHighlight>
    )
}