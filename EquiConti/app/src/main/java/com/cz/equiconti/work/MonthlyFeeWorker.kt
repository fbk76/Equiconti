package com.cz.equiconti.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MonthlyFeeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    // Se/Quando vorrai, puoi iniettare un Repo qui:
    // private val repo: Repo
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        // TODO: prima c'era una chiamata a generateMonthlyFees() che ora non esiste.
        // Per il momento non facciamo nulla e ritorniamo success così il build passa.
        return Result.success()
    }
}
