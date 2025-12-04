package com.bignerbranch.android.zd2_v3_zykova

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.graphics.drawable.GradientDrawable
import android.graphics.Color
import android.widget.ImageButton

class DataListActivity : AppCompatActivity() {
    private lateinit var dataContainer: LinearLayout
    private lateinit var tvHistoryTitle: TextView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_list)

        Log.d("DataListActivity", "Activity created")

        dataContainer = findViewById(R.id.dataContainer)
        tvHistoryTitle = findViewById(R.id.tvHistoryTitle)
        db = AppDatabase.getInstance(this)

        tvHistoryTitle.setOnClickListener {
            finish()
        }

        loadData()
    }

    private fun loadData() {
        Thread {
            try {
                Log.d("DataListActivity", "Loading data from database...")
                val healthDataList = db.healthDataDao().getAll()
                Log.d("DataListActivity", "Loaded ${healthDataList.size} records")

                runOnUiThread {
                    showData(healthDataList)
                }
            } catch (e: Exception) {
                Log.e("DataListActivity", "Error loading data: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@DataListActivity, "Ошибка загрузки: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun showData(dataList: List<HealthData>) {
        dataContainer.removeAllViews()
        Log.d("DataListActivity", "Showing ${dataList.size} records")

        if (dataList.isEmpty()) {
            val emptyText = TextView(this)
            emptyText.text = "Нет сохраненных данных"
            emptyText.setTextColor(resources.getColor(android.R.color.holo_blue_bright))
            emptyText.textSize = 18f
            emptyText.setPadding(32, 32, 32, 32)
            dataContainer.addView(emptyText)
            Log.d("DataListActivity", "No data to display")
            return
        }

        for (healthData in dataList) {
            Log.d("DataListActivity", "Displaying record: Steps=${healthData.steps}, Sleep=${healthData.sleep}, Weight=${healthData.weight}")


            val itemContainer = LinearLayout(this)
            itemContainer.orientation = LinearLayout.VERTICAL
            itemContainer.setPadding(32, 32, 32, 32)
            itemContainer.setBackground(createRoundedRectangle())

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, 32)
            itemContainer.layoutParams = layoutParams

            fun getSugarStatus(sugar: Float): String {
                return if (sugar >= 3.3f && sugar <= 5.5f) {
                    "норма"
                } else {
                    "не норма"
                }
            }

            val itemText = TextView(this)
            itemText.text = buildString {
                append("Дата: ${healthData.date}\n")
                append("Шаги: ${healthData.steps}\n")
                append("Сон: ${healthData.sleep} ч\n")
                append("Вес: ${healthData.weight} кг\n")
                append("Сахар: ${healthData.sugar} ${getSugarStatus(healthData.sugar)}")
            }
            itemText.setTextColor(resources.getColor(android.R.color.holo_blue_bright))
            itemText.textSize = 16f
            itemText.setPadding(0, 0, 0, 16)

            val deleteButton = Button(this).apply {
                text = "Удалить"
                setBackgroundColor(resources.getColor(android.R.color.holo_red_dark, null))
                setTextColor(resources.getColor(android.R.color.white, null))
                textSize = 12f

                val radius = 24f
                background = GradientDrawable().apply {
                    cornerRadius = radius
                    setColor(resources.getColor(android.R.color.holo_red_dark, null))
                }
            }

            val buttonParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            buttonParams.setMargins(0, 0, 0, 0)
            deleteButton.layoutParams = buttonParams
            deleteButton.setPadding(32, 8, 32, 8)

            deleteButton.setOnClickListener {
                deleteItem(healthData)
            }

            itemContainer.addView(itemText)
            itemContainer.addView(deleteButton)

            dataContainer.addView(itemContainer)
        }
    }

    private fun createRoundedRectangle(): GradientDrawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setStroke(4, Color.BLUE)
        shape.setColor(Color.TRANSPARENT)
        shape.cornerRadius = 16f
        return shape
    }

    private fun deleteItem(healthData: HealthData) {
        Thread {
            try {
                db.healthDataDao().delete(healthData)
                runOnUiThread {
                    Toast.makeText(this@DataListActivity, "Данные удалены", Toast.LENGTH_SHORT).show()
                    loadData()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@DataListActivity, "Ошибка удаления: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}