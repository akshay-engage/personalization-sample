package com.example.personalization_sample

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.personalization_sample.model.DataModel
import java.util.logging.Logger

class ScreenDetails : AppCompatActivity() {
    val dataModel = DataModel.getInstance()
    var isCheked: Boolean = false

    private lateinit var sizeEdit: EditText
    private lateinit var screenEdit: EditText
    private lateinit var eventEdit: EditText
    private lateinit var addDataButton: Button
    private lateinit var saveButton: Button

    private lateinit var position: EditText
    private lateinit var height: EditText
    private lateinit var width: EditText
    private lateinit var propertyId: EditText
    private lateinit var isCustomView: CheckBox
    private lateinit var addViewBtn: Button
    private lateinit var checkBox: CheckBox
    private lateinit var idName: EditText
    private lateinit var idVal: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_details)
        sizeEdit = findViewById(R.id.sizeEdit)
        screenEdit = findViewById(R.id.screenEdit)
        eventEdit = findViewById(R.id.eventEdit)
        addDataButton = findViewById(R.id.addData)
        saveButton = findViewById(R.id.saveData)
        checkBox = findViewById(R.id.checkBox)
        idName = findViewById(R.id.idName)
        idVal = findViewById(R.id.idValue)


        addDataButton.setOnClickListener {
            addViewData()
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            isCheked = isChecked
        }

        saveButton.setOnClickListener {
            val size = sizeEdit.text.toString().toIntOrNull()
            val screenName = screenEdit.text.toString()
            val eventName = eventEdit.text.toString()
            val idName = idName.text.toString()
            val idValue = idVal.text.toString().toIntOrNull()
            val list = dataModel.getData()
            var isAlreadyExist  = false
            for(i in list) {
                if(screenName == i.screenName) {
                    isAlreadyExist = true
                }
            }
            if(!isAlreadyExist && !screenName.isNullOrEmpty() && size != null) {
                dataModel.setData(size, screenName, eventName, idName, idValue, isCheked)
                finish();
            } else {
                Toast.makeText(applicationContext, "Enter Valid Data", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addViewData() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_layout, null)
        val builder = AlertDialog.Builder(this).create()
        showDailog(dialogView, builder)
        addViewBtn = dialogView.findViewById(R.id.addWeViewBtn)
        position = dialogView.findViewById(R.id.position)
        height = dialogView.findViewById(R.id.height)
        width = dialogView.findViewById(R.id.width)
        propertyId = dialogView.findViewById(R.id.propertyId)
        isCustomView = dialogView.findViewById(R.id.isCustom)

        addViewBtn.setOnClickListener {
            Toast.makeText(this, "View Added", Toast.LENGTH_SHORT).show()
            if(!propertyId.text.toString().isNullOrEmpty()) {
                registerView()
                createList()
            }
            builder.dismiss()
        }
    }

    fun registerView() {

        val positionView: Int? = position.text.toString().toIntOrNull()
        val heightView: Int? = height.text.toString().toIntOrNull()
        val widthView: Int? = width.text.toString().toIntOrNull()
        val propertyIdView: String = propertyId.text.toString()
//        boolean isChecked = ((CheckBox) findViewById(R.id.checkBox1)).isChecked()
        val isCustomChecked: Boolean = isCustomView.isChecked
        dataModel.setViewData(positionView, isCustomChecked, heightView, widthView, propertyIdView)
    }

    fun showDailog (dialogView: View, builder: AlertDialog) {
        builder.setView(dialogView)
        builder.setTitle("Add View Data")
        builder.show()
    }
    override fun onResume() {
        super.onResume()
        createList()
    }

    fun createList() {
        val container = findViewById<LinearLayout>(R.id.screenList)
        container.removeAllViews()

        val list = dataModel.getViewData()
        val itemListLayout = LinearLayout(this)
        itemListLayout.orientation = LinearLayout.VERTICAL
        itemListLayout.setBackgroundResource(androidx.cardview.R.color.cardview_shadow_end_color)
        itemListLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        for (i in list) {
            val itemLayout = LinearLayout(this)
            itemLayout.orientation = LinearLayout.VERTICAL
            itemLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            // Add the screen name TextView
            val screenNameTextView = TextView(this)
            screenNameTextView.text = "PropertyId ${i.propertyId}"
            screenNameTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(screenNameTextView)

            // Add the value TextView
            val valueTextView = TextView(this)
            valueTextView.text = "position ${i.position}"
            valueTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(valueTextView)

            // Add the value TextView
            val eventTextView = TextView(this)
            eventTextView.text = "Hieght= ${i.height} | Width= ${i.width}"
            eventTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(eventTextView)


            val itemButtonLayout = LinearLayout(this)
            itemButtonLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            // Add the edit button
            val deleteButton = Button(this)
            deleteButton.text = "Delete"
            val layoutParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            deleteButton.layoutParams = layoutParam
            deleteButton.setOnClickListener {
                deleteViewEntry(i.propertyId)
            }
            itemButtonLayout.addView(deleteButton)

            // Add the list item to the parent LinearLayout
            itemListLayout.addView(itemLayout)
            itemListLayout.addView(itemButtonLayout)
        }

// Add the horizontal LinearLayout to the parent layout inside a ScrollView
        val scrollView = ScrollView(this)
        scrollView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        scrollView.addView(itemListLayout)
        container.addView(scrollView)
    }
    fun deleteViewEntry(propertyId: String) {
        dataModel.removeViewEntry(propertyId)
        createList()
    }
}