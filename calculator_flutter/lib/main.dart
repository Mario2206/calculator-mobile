
import 'package:calculator_flutter/res/colors.dart';
import 'package:flutter/material.dart';

void main() {
 runApp(MyApp());
}

class MyApp extends StatelessWidget {
 @override
 Widget build(BuildContext context) {
   return MaterialApp(
     title: 'Calculatrice',
     theme: ThemeData(
       primaryColor: AppColors.inputContainerBackground,
       accentColor: AppColors.displayContainerBackground,
       primaryColorBrightness: Brightness.light,
     ),
     home: Calculator(),
   );
 }
}

class Calculator extends StatefulWidget {
 @override
 _CalculatorState createState() => _CalculatorState();
}

class _CalculatorState extends State<Calculator> {

  static const List<List<String>> grid = <List<String>>[
  <String>["", "", "C", "CE"],
  <String>["7", "8", "9", "-"],
  <String>["4", "5", "6", "*"],
  <String>["1", "2", "3", "/"],
  <String>["0", ".", "=", "+"],
  ];

  double? input;
  double? previousInput;
  String? symbol;

  void onItemClicked(String value) {
  print('On Click $value');

    switch (value) {
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        onNewDigit(value);
        break;
      case '+':
      case '-':
      case '/':
      case '*':
        onNewSymbol(value);
        break;
      case '=':
        onEquals();
        break;
      case 'CE':
        clearAll();
        break;
      case 'C' :
        clear();
    }

    // Force l'interface à se redessiner
    setState(() {});
  }

  void onNewDigit(String digit) {
    setState(() {
      input = double.parse(digit);
    });
  }

  void onNewSymbol(String digit) {
    setState(() {
      symbol = digit;
      previousInput = input;
      input = null;
    });
  }

  void onEquals() {
    double result = 0;
    switch(symbol) {
      case "+" :
        result = this.input! + this.previousInput!;
        break;
      case "-" :
        result = this.previousInput! - this.input!;
        break;
      case "*" :
        result = this.previousInput! * this.input!;
        break;
      case "/" :
        result = this.previousInput! / this.input!;
        break;
    }

   

    setState(() {
      input = result;
      previousInput = null;
      symbol = "";
    });
  }

  void clearAll() {
    setState(() {
      input = null;
      previousInput = null;
      symbol = null;
    });
  }

  void clear() {
    if(input != null) {
      String newValue = input.toString().substring(0, input.toString().length - 1);
      print(newValue);
    } else {
      setState(() {
        symbol = null;
        input = previousInput;
      });
    }



  }

 @override
 Widget build(BuildContext context) {
   return Scaffold(
   body: DefaultTextStyle(
     style: TextStyle(color: AppColors.white, fontSize: 22.0),
     child: Column(
       children: [
         Flexible(
           flex: 2,
           child: Container(
             color: AppColors.displayContainerBackground,
             alignment: AlignmentDirectional.centerEnd,
             padding: const EdgeInsets.all(22.0),
             child: Text( input != null && (input! % 1 == 0) ? input!.toInt().toString(): (input == null ? "0" : input.toString())),
           ),
         ),
         Flexible(
            flex: 8,

            // Column = vertical
            child: Column(
              // Pour chaque nouvelle ligne, on itère sur chaque cellule
              children: grid.map((List<String> line) {
                return Expanded(
                  child: Row(
                      children: line
                          .map(
                            (String cell) => Expanded(
                              child: InputButton(
                                label: cell,
                                onTap: onItemClicked,
                              ),
                            ),
                          )
                          .toList(growable: false)),
                );
              }).toList(growable: false),
            ),
          )
       ],
     ),
   ),
 );
 }
}

class InputButton extends StatelessWidget {
 final String label;
 final ValueChanged<String>? onTap;

 InputButton({required this.label, required this.onTap});

 @override
 Widget build(BuildContext context) {
   return InkWell(
     onTap: () => onTap?.call(label),
     child: Ink(
       height: double.infinity,
       decoration: BoxDecoration(
           border: Border.all(color: AppColors.white, width: 0.5),
           color: AppColors.inputContainerBackground),
       child: Center(
         child: Text(
           label,
         ),
       ),
     ),
   );
 }
}