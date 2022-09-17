package com.example.rentapp.use_cases

import com.example.rentapp.models.car_models.FastSelectPlans

class FastSelectUseCase {
    private val fastSelectPlans = arrayListOf<FastSelectPlans>()
    val fastSelectPlansDates = listOf<Int>(7, 14, 30, 60)
    fun calculatePricePerDays(price: Int): ArrayList<FastSelectPlans>{

        fastSelectPlansDates.forEach {
            val totalPrice = price * it
            fastSelectPlans.add(
                FastSelectPlans(totalPrice, it, "$it Days")
            )
        }

        return fastSelectPlans
    }
}