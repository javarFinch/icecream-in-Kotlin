package com.example.maclab.icecreamkotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_ice_cream.*
import kotlinx.android.synthetic.main.content_main_activity_ice_cream.*
import java.io.Serializable
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivityIceCream : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    var orderItem = Order()
    var orders = ArrayList<Order>()
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        orderItem.howMuchFudge = progress
        seekBarTextView.text = progress.toString() + " oz"
        calculatePrice()
        priceView.text = priceToString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    var price = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ice_cream)
        setSupportActionBar(toolbar)
        seekBarTextView.visibility = View.GONE
        seekBar.visibility = View.GONE
        hotFudgePrompt.visibility = View.GONE
        flavorSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0)
                    orderItem.flavor = "Vanilla"
                else if (position == 1)
                    orderItem.flavor = "Chocolate"
                else
                    orderItem.flavor = "StrawBerry"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        sizeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0)
                    orderItem.size = "Small"
                else if (position == 1)
                    orderItem.size = "Medium"
                else
                    orderItem.size = "Large"
                calculatePrice()
                priceView.text = priceToString()
            }

        }
        seekBar.setOnSeekBarChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main_activity_ice_cream, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.aboutItem) {
            val message = "Frank loves ice cream and really wants to make money off of it, the only problem is Frank keeps eating his own merchandise. So to solve this problem Frank got some professional help to put his addiction onto his customers."
            val a = Intent(this, AboutActivity::class.java)
            a.putExtra("Apple", message)
            startActivity(a)
        } else if (id == R.id.orderHistoryItem) {
            val o = Intent(this, orderHistoryActivity::class.java)
            o.putExtra("orders", orders )
            startActivity(o)
        }
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    public fun  theWorksClicked (view:View) {
        sizeSpinner.setSelection(2)
        mmsCheckBox.setChecked(true)
        oreosCheckBox.setChecked(true)
        peanutsCheckBox.setChecked(true)
        gummyBearsCheckBox.setChecked(true)
        almondsCheckBox.setChecked(true)
        hotFudgeCheckBox.setChecked(true)
        page.setVisibility(View.VISIBLE)
        brownieCheckBox.setChecked(true)
        strawberriesCheckBox.setChecked(true)
        marshmallowsCheckBox.setChecked(true)
        calculatePrice()
        seekBar.setProgress(3, true)
        seekBar.visibility = View.VISIBLE
        hotFudgePrompt.visibility = View.VISIBLE
        seekBarTextView.setVisibility(View.VISIBLE)
        seekBarTextView.text =  seekBar.progress.toString() + " oz"
        priceView.text = priceToString()
    }

    public fun resetClicked(view:View) {
        flavorSpinner.setSelection(0)
        sizeSpinner.setSelection(0)
        Log.d("Debug", sizeSpinner.selectedItem.toString())
        peanutsCheckBox.isChecked = false
        almondsCheckBox.isChecked = false
        hotFudgeCheckBox.isChecked = true
        seekBar.progress = 1;
        seekBar.visibility = View.VISIBLE
        seekBarTextView.text = seekBar.progress.toString() +  " oz"
        seekBarTextView.visibility = View.VISIBLE
        brownieCheckBox.isChecked = false;
        mmsCheckBox.isChecked = false
        oreosCheckBox.isChecked = false
        strawberriesCheckBox.isChecked = false
        gummyBearsCheckBox.isChecked = false
        marshmallowsCheckBox.isChecked = false
        calculatePrice()
        priceView.text = priceToString()
    }

    public fun orderClicked(view:View) {
        Toast.makeText(this, "Your sundae is on the way. Enjoy!!",Toast.LENGTH_SHORT).show()
       /* orderItem.flavor = flavorSpinner.selectedItem.toString()
        orderItem.size = sizeSpinner.selectedItem.toString()
        orderItem.howMuchFudge = seekBar.progress*/
        setUpOrderSizeAndFlavor()
        calculatePrice()
        Log.d("Debug", orderItem.toString())
        orders.add(orderItem)
        orderItem = Order()
    }

    public fun checkBoxChanged(view:View) {
        calculatePrice()
        priceView.text = priceToString()
        if (view == hotFudgeCheckBox) {
            if (hotFudgeCheckBox.isChecked) {
                seekBar.progress = 1
                seekBarTextView.text = seekBar.progress.toString() + " oz"
                hotFudgePrompt.visibility = View.VISIBLE
                seekBar.visibility = View.VISIBLE
                seekBarTextView.visibility = View.VISIBLE
            } else {
                seekBar.visibility = View.GONE
                seekBarTextView.visibility = View.GONE
                hotFudgePrompt.visibility = View.GONE
            }
        }

    }



    private fun priceToString(): String {
        val price = NumberFormat.getCurrencyInstance(Locale.US)
        return price.format(this.price)
    }

    private fun calculatePrice() {
        var price = 0.0;
        orderItem.numberOfToppings = 0
        if (sizeSpinner.selectedItemPosition == 0)
            price += 2.99
        else if (sizeSpinner.selectedItemPosition == 1)
            price += 3.99
        else
            price += 4.99
        if (hotFudgeCheckBox.isChecked) {
            orderItem.addTopping("Hot Fudge")
            if (seekBar.progress == 0)
            price += 0.0;
            else if (seekBar.progress == 1) {
                price += .15
                orderItem.howMuchFudge = 1
            }else if (seekBar.progress == 2) {
                price += .25
                orderItem.howMuchFudge = 2
            }else {
                price += .30
                orderItem.howMuchFudge = 3
            }

        }
        if (peanutsCheckBox.isChecked) {
            price += .15
            orderItem.addTopping("Peanuts")
        }
        if (mmsCheckBox.isChecked) {
            price += .25
            orderItem.addTopping("M&Ms")
        }
        if (almondsCheckBox.isChecked) {
            price += .15
            orderItem.addTopping("Almonds")
        }
        if (brownieCheckBox.isChecked) {
            price += .20
            orderItem.addTopping("Brownies")
        }
        if (strawberriesCheckBox.isChecked) {
            price += .20
            orderItem.addTopping("Strawberries")
        }
        if (oreosCheckBox.isChecked) {
            price += .20
            orderItem.addTopping("Oreos")
        }
        if (gummyBearsCheckBox.isChecked) {
            price += .20
            orderItem.addTopping("Gummy Bears")
        }
        if (marshmallowsCheckBox.isChecked) {
            price += .15
            orderItem.addTopping("Marshmallows")
        }
        this.price = price
        orderItem.orderTotal = price

    }

    public fun setUpOrderSizeAndFlavor() {
        if (flavorSpinner.selectedItemPosition == 0)
            orderItem.flavor = "Vanilla"
        else if (flavorSpinner.selectedItemPosition == 1)
            orderItem.flavor = "Chocolate"
        else
            orderItem.flavor = "Strawberry"
        if (sizeSpinner.selectedItemPosition == 0) {
            orderItem.size = "Small"
        }else if (sizeSpinner.selectedItemPosition == 1) {
            orderItem.size = "Medium"
        }else {
            orderItem.size = "Large"
        }
    }
}
