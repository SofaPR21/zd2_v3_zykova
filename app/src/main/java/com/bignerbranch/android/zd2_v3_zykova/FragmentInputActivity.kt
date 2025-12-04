package com.bignerbranch.android.zd2_v3_zykova

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class FragmentInputActivity : AppCompatActivity() {
    private lateinit var etSteps: EditText
    private lateinit var etSleep: EditText
    private lateinit var etWeight: EditText
    private lateinit var etSugar: EditText
    private lateinit var btnSave: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_input)

        initViews()
        db = AppDatabase.getInstance(this)

        btnSave.setOnClickListener {
            val steps = etSteps.text.toString().toIntOrNull()
            val sleep = etSleep.text.toString().toFloatOrNull()
            val weight = etWeight.text.toString().toFloatOrNull()
            val sugar = etSugar.text.toString().toFloatOrNull()

            if (steps == null || sleep == null || weight == null || sugar == null) {
                Toast.makeText(this, "Заполните все поля корректно", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (sugar < 0 || sugar > 30) {
                Toast.makeText(this, "Уровень сахара должен быть от 0 до 30 ммоль/л", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val healthData = HealthData(
                steps = steps,
                sleep = sleep,
                weight = weight,
                sugar = sugar,
                date = currentDate
            )

            Thread {
                try {
                    db.healthDataDao().insert(healthData)
                    runOnUiThread {
                        Toast.makeText(this@FragmentInputActivity, "Данные сохранены", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@FragmentInputActivity, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

    private fun initViews() {
        etSteps = findViewById(R.id.etSteps)
        etSleep = findViewById(R.id.etSleep)
        etWeight = findViewById(R.id.etWeight)
        etSugar = findViewById(R.id.etSugar) // ← добавлено
        btnSave = findViewById(R.id.btnSave)
    }
}