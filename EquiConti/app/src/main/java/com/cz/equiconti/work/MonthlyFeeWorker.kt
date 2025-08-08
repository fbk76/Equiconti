package com.cz.equiconti.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cz.equiconti.data.Repo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class MonthlyFeeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repo: Repo
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val today = LocalDate.now()
        try {
            repo.generateMonthlyFees(today)
        } catch (e: Exception) {
            return Result.retry()
        }
        return Result.success()
    }
}
