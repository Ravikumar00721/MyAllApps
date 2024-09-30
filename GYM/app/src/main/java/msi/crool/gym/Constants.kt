package msi.crool.gym

object Constants {
    fun defaultExcerciseList():ArrayList<ExerciseModel>
    {
        val excerciseList=ArrayList<ExerciseModel>()

        val plank=ExerciseModel(
            1,
            "Plank",
            R.drawable.ic_plank,
            false,
            false
        )
        excerciseList.add(plank)

        val Lunge=ExerciseModel(
            2,
            "Lunge",
            R.drawable.ic_lunge,
            false,
            false
        )
        excerciseList.add(Lunge)

        val pushups=ExerciseModel(
            3,
            "Push Ups",
            R.drawable.ic_push_up,
            false,
            false
        )
        excerciseList.add(pushups)

        val squats=ExerciseModel(
            4,
            "Squats",
            R.drawable.ic_squat,
            false,
            false
        )
        excerciseList.add(squats)

        val Chair=ExerciseModel(
            5,
            "Chair",
            R.drawable.ic_step_up_onto_chair,
            false,
            false
        )
        excerciseList.add(Chair)


        return excerciseList
    }
}