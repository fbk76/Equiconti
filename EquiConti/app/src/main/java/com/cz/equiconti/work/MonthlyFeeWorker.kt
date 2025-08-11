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
    @Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val repo = Repo.from(applicationContext)
            repo.generateMonthlyFees(LocalDate.now())
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}
