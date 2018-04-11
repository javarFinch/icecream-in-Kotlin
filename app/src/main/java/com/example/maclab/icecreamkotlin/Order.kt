package com.example.maclab.icecreamkotlin

import java.text.NumberFormat
import java.util.*
import java.io.Serializable

/**
 * Created by maclab on 4/5/18.
 */

class Order:Serializable  {
    var flavor = ""
    var size = ""
    var howMuchFudge = 0
    var orderTotal = 0.0
    var toppings = Array<String>(9) {""}
    var numberOfToppings = 0
    init {
        flavor = ""
        size = ""
        howMuchFudge = 0
        orderTotal = 0.0
        toppings = Array<String>(9){""}
        numberOfToppings = 0
    }

    public fun addTopping(topping:String) {
        if (numberOfToppings < toppings.size) {
            toppings[numberOfToppings] = topping
            numberOfToppings++
        }
    }

    public fun toppingsToString():String {
        if (numberOfToppings <= 0)
            return "None"
        else {
            var temp = toppings[0]
            for (i: Int in 1..numberOfToppings - 1)
                temp += ", " + toppings[i]
            return temp;
        }
    }

    public fun getOrderTotal():String {
        val price = NumberFormat.getCurrencyInstance(Locale.US)
        return price.format(orderTotal)
    }

     override fun toString():String {
        return "Order:" + " size:" + size + "\n" +
                "flavor:" + flavor + "\n" +
                "toppings:" + toppingsToString() + "\n" +
                "Fudge:" + howMuchFudge + " oz" + "\n" +
                "Total=" + getOrderTotal();
    }


}