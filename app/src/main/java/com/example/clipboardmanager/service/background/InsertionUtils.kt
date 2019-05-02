package com.example.clipboardmanager.service.background

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.firebase.jobdispatcher.*

class InsertionUtils  {

    private val REMINDER_JOB_TAG = "insertion_reminder_tag"

    private var sInitialized: Boolean = false

    @Synchronized
    fun scheduleChargingReminder(context: Context) {

        if (sInitialized){
            Toast.makeText(context, "New note has been copied times", LENGTH_SHORT).show()
            return
        }
        Toast.makeText(context, "Restarting", LENGTH_SHORT).show()
        val driver = GooglePlayDriver(context)
        val dispatcher = FirebaseJobDispatcher(driver)

        /* Create the Job to periodically create reminders to drink water */
        val constraintReminderJob = dispatcher.newJobBuilder()
            /* The Service that will be used to write to preferences */
            .setService(ClipBoardJobService::class.java)
            /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
            .setTag(REMINDER_JOB_TAG)
            /*
                 * Network constraints on which this Job should run. In this app, we're using the
                 * device charging constraint so that the job only executes if the device is
                 * charging.
                 *
                 * In a normal app, it might be a good idea to include a preference for this,
                 * as different users may have different preferences on when you should be
                 * syncing your application's data.
                 */

            /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
            .setLifetime(Lifetime.FOREVER)
            /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
            .setRecurring(false)
            /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
            //.setTrigger(Trigger.executionWindow(0,5))
            /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
            /* Once the Job is ready, call the builder's build method to return the Job */
            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
            .build()

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(constraintReminderJob)

        /* The job has been initialized */
        sInitialized = true
    }
}