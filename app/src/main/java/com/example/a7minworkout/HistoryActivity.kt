package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryAactivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }
        binding?.toolbarHistoryAactivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        val dao = (application as WorkOutApp).db.historyDao()
        getAllCompletedDates(dao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchallDates().collect { allCompletedDatesList ->
               if(allCompletedDatesList.isNotEmpty())
               {
                   binding?.tvCompletedExercise?.visibility= View.VISIBLE
                   binding?.rvDateList?.visibility=View.VISIBLE
                   binding?.tvNoData?.visibility=View.INVISIBLE

                  binding?.rvDateList?.layoutManager=LinearLayoutManager(this@HistoryActivity)

                   val dates=ArrayList<String>()
                   for(date in allCompletedDatesList){
                       dates.add(date.date)
                   }
                   val historyAdapter=HistoryAdapter(dates)
                   binding?.rvDateList?.adapter=historyAdapter
               }else{
                   binding?.tvCompletedExercise?.visibility= View.GONE
                   binding?.rvDateList?.visibility=View.GONE
                   binding?.tvNoData?.visibility=View.VISIBLE
               }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}