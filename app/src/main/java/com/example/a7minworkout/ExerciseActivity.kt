package com.example.a7minworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minworkout.databinding.ActivityExerciseBinding
import com.example.a7minworkout.databinding.DialogCustomBackPressedBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var exerciseBind: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration:Long=1
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration:Long=1
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null

    private var player: MediaPlayer? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exerciseBind = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(exerciseBind?.root)

        setSupportActionBar(exerciseBind?.toolBarExercise)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()
        tts = TextToSpeech(this, this)

        exerciseBind?.toolBarExercise?.setNavigationOnClickListener {
           onCustomDialogBackPressed()
        }
        setUpRestView()
        setUpExerciseStatusRecyclerView()
    }

    private fun onCustomDialogBackPressed(){
        val dialog=Dialog(this)
        var dialogBinding=DialogCustomBackPressedBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialogBinding.confirmBtn.setOnClickListener {
         this@ExerciseActivity.finish()
            dialog.dismiss()
        }
        dialogBinding.cancelBtn.setOnClickListener {
           dialog.dismiss()
        }
        dialog.show()
    }

    private fun setUpExerciseStatusRecyclerView() {
        exerciseBind?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        exerciseBind?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpRestView() {
        try {
            val soundUri = Uri.parse(
                "android.resource://com.example.a7minworkout/" +
                        R.raw.start
            )
            player = MediaPlayer.create(applicationContext, soundUri)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        exerciseBind?.flRestView?.visibility = View.VISIBLE
        exerciseBind?.tvExercise?.visibility = View.VISIBLE
        exerciseBind?.tvExerciseName?.visibility = View.INVISIBLE
        exerciseBind?.flContainerExercise?.visibility = View.INVISIBLE
        exerciseBind?.ivImage?.visibility = View.INVISIBLE
        exerciseBind?.tvUpcomingLabel?.visibility = View.VISIBLE
        exerciseBind?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        exerciseBind?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition + 1].getName()

        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        setRestProgressBar()
    }

    private fun setUpExerciseView() {
        exerciseBind?.flRestView?.visibility = View.INVISIBLE
        exerciseBind?.tvExercise?.visibility = View.INVISIBLE
        exerciseBind?.tvExerciseName?.visibility = View.VISIBLE
        exerciseBind?.flContainerExercise?.visibility = View.VISIBLE
        exerciseBind?.ivImage?.visibility = View.VISIBLE
        exerciseBind?.tvUpcomingLabel?.visibility = View.INVISIBLE
        exerciseBind?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName().toString())
        exerciseBind?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        exerciseBind?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        exerciseBind?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer(restTimerDuration+1000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                exerciseBind?.progressBar?.progress = 10 - restProgress
                exerciseBind?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter?.notifyDataSetChanged()
                setUpExerciseView()

            }

        }.start()
    }

    private fun setExerciseProgressBar() {
        exerciseBind?.progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration+1000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                exerciseBind?.progressBarExercise?.progress = 30 - exerciseProgress
                exerciseBind?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {


                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsFinished(true)
                    exerciseAdapter?.notifyDataSetChanged()
                    setUpRestView()
                } else {
                    finish()
                    val intent= Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        if (player != null) {
            player!!.stop()
        }
        exerciseBind = null
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}



