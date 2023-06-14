package com.example.a7minworkout

import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW
    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.btnBMICalculator?.setBackgroundColor(Color.GREEN)
        MakeMetricUnitsVisible()

        binding?.rgButtons?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                MakeMetricUnitsVisible()
            } else {
                MakeUsUnitsVisible()
            }
        }

        binding?.btnBMICalculator?.setOnClickListener {
            CalculateUnits()
        }
    }

    private fun MakeMetricUnitsVisible() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tiWeightBMI?.visibility = View.VISIBLE
        binding?.tiHeightBMI?.visibility = View.VISIBLE
        binding?.tiWeightBMIUs?.visibility = View.GONE
        binding?.llUsHeightContainer?.visibility = View.GONE
        binding?.etHeightBMI?.text!!.clear()
        binding?.etWeightBMI?.text!!.clear()
        binding?.llBMIDisplay?.visibility = View.INVISIBLE
    }

    private fun MakeUsUnitsVisible() {
        currentVisibleView = US_UNITS_VIEW
        binding?.tiWeightBMI?.visibility = View.GONE
        binding?.tiHeightBMI?.visibility = View.GONE
        binding?.tiWeightBMIUs?.visibility = View.VISIBLE
        binding?.llUsHeightContainer?.visibility = View.VISIBLE
        binding?.etWeightBMIUs?.text!!.clear()
        binding?.etHeightBMIUsFeet?.text!!.clear()
        binding?.etHeightBMIUsInch?.text!!.clear()
        binding?.llBMIDisplay?.visibility = View.INVISIBLE
    }

    private fun DisplayBMI(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String
        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "very severely underweight"
            bmiDescription = "You need to go on a proper diet.Eat More!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "severely underweight"
            bmiDescription = "You need to go on a proper diet.Eat More!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "underweight"
            bmiDescription = "You need to go on a proper diet.Eat More!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations!Your BMI is normal."
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "OOPS!You need to workout"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese class! (Moderately Obese)"
            bmiDescription = "OOPS!You need to workout"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese class! (Severely Obese)"
            bmiDescription = "OOPS!You need to workout"
        } else {
            bmiLabel = "Obese class! (very Severely Obese)"
            bmiDescription = "OOPS!You need to workout"
        }
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llBMIDisplay?.visibility = View.VISIBLE
        binding?.tvBmiValue?.text = bmiValue
        binding?.tvBmiType?.text = bmiLabel
        binding?.tvBmiDescription?.text = bmiDescription
    }

    private fun ValidateMetricUnits(): Boolean {
        var isValid = true
        if (binding?.etWeightBMI?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etHeightBMI?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }

    private fun CalculateUnits(){
         if(currentVisibleView== METRIC_UNITS_VIEW){
             if (ValidateMetricUnits()) {
                 val height: Float = binding?.etHeightBMI?.text.toString().toFloat() / 100
                 val weight: Float = binding?.etWeightBMI?.text.toString().toFloat()
                 val bmi = weight / (height * height)
                 DisplayBMI(bmi)

             } else {
                 Toast.makeText(
                     this@BMIActivity, "Invalid values",
                     Toast.LENGTH_SHORT
                 ).show()
             }
         }
        else{
            if(ValidateUsUnits()){
                val heightUsFeet: Float =
                    binding?.etHeightBMIUsFeet?.text.toString().toFloat() * 12
                val heightUsInch: Float = binding?.
                etHeightBMIUsInch?.text.toString().toFloat()
                val weightUSUnit:Float=binding?.
                etWeightBMIUs?.text.toString().toFloat()
                val heightValue=heightUsFeet+heightUsInch
                val bmi=703*(weightUSUnit/(heightValue*heightValue))
                DisplayBMI(bmi)
            }
             else{
                Toast.makeText(
                    this@BMIActivity, "Invalid values",
                    Toast.LENGTH_SHORT
                ).show()
            }
         }
    }


    private fun ValidateUsUnits(): Boolean {
        var isValid = true
        if (binding?.etWeightBMIUs?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etHeightBMIUsFeet?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etHeightBMIUsInch?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }
}