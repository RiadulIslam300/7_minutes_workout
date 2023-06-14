package com.example.a7minworkout

object Constants {
    fun defaultExerciseList():ArrayList<ExerciseModel>{
      val exerciseList=ArrayList<ExerciseModel>()
        val jumpingJacks=ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false,
            false
        )
        exerciseList.add(jumpingJacks)

        val plank=ExerciseModel(
            2,
            "Plank",
            R.drawable.ic_plank,
            false,
            false
        )
        exerciseList.add(plank)

        val highKnees=ExerciseModel(
            3,
            "High Knees",
            R.drawable.ic_high_knees_running_in_place,
            false,
            false
        )
        exerciseList.add(highKnees)

        val crunch=ExerciseModel(
            4,
            "Abdominal Crunch",
            R.drawable.ic_abdominal_crunch,
            false,
            false
        )
        exerciseList.add(crunch)

        val pushUp=ExerciseModel(
            5,
            "Push Up",
            R.drawable.ic_push_up,
            false,
            false
        )
        exerciseList.add(pushUp)

        val squat=ExerciseModel(
            6,
            "Squat",
            R.drawable.ic_squat,
            false,
            false
        )
        exerciseList.add(squat)

        val lunges=ExerciseModel(
            7,
            "Lunges",
            R.drawable.ic_lunge,
            false,
            false
        )
        exerciseList.add(lunges)

        val pushUpRoatate=ExerciseModel(
            8,
            "Push up and Roatation",
            R.drawable.ic_push_up_and_rotation,
            false,
            false
        )
        exerciseList.add(pushUpRoatate)

        return  exerciseList
    }
}