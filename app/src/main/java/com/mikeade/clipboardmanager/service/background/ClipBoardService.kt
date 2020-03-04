package com.mikeade.clipboardmanager.service.background

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.mikeade.clipboardmanager.R
import com.mikeade.clipboardmanager.service.model.ClipboardEntity
import com.mikeade.clipboardmanager.service.repository.ClipBoardRepository
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.PixelFormat
import android.graphics.Point
import android.view.*
import android.widget.RelativeLayout
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.Toast.LENGTH_LONG
import com.mikeade.clipboardmanager.utils.CustomDateUtils
import com.mikeade.clipboardmanager.view.adapter.FloatingAdapter


class ClipBoardService : Service(),View.OnTouchListener, View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private var added: Boolean = false
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX: Float = Float.MIN_VALUE
    private var initialTouchY: Float = Float.MIN_VALUE

    private lateinit var clipboard : ClipboardManager

    private lateinit var clipBoardRepository: ClipBoardRepository

    private var mPreviousText=""

    private lateinit var windowManager: WindowManager
    private lateinit var mFloatingLayout: View
    private lateinit var imageView: ImageView
    private lateinit var closeButton: ImageView
    private lateinit var closeExpandedView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var openedLayout: RelativeLayout
    private lateinit var floatingLayout: RelativeLayout
    private lateinit var frameLayout:FrameLayout
    private lateinit var params: WindowManager.LayoutParams
    private val context: Context = this
    private lateinit var sharedPreferences: SharedPreferences
    private var sortedList: MutableList<ClipboardEntity>? = null

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key :String?) {
        if (key.equals("date_list")){
            val listRange = sharedPreferences?.getString(key, "2")
            sortedList =  CustomDateUtils.sortList(listRange?.toInt(), clipBoardRepository.getNotes()).toMutableList()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
        setupSharedPreferences()
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoardRepository = ClipBoardRepository(application)
        clipboard.addPrimaryClipChangedListener(primaryClipChangedListener)
        Log.d("ClipboardService", "Service started")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val notificationId = "clipboardNotification"
            val channelName = "My Background Service"
            val chan = NotificationChannel(notificationId, channelName, NotificationManager.IMPORTANCE_NONE)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)

            val notificationBuilder =  NotificationCompat.Builder(this, notificationId)
            val notifications = notificationBuilder.setOngoing(true)
                    .setContentTitle("Clipboard service running")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build()
            try {
                startForeground(90, notifications)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        setFloatingLayout()

        //google i/0 videos
        //retrofit videos
        //dagger2 videos
        //Coroutines
        //using lambda functions in click listeners and other functions
        //pluralSight scholarship videos
        //with, also, apply
        //use of RX java
        //threads and handlers
        //Cloud FireStore
    }

    private fun setupSharedPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        this.sharedPreferences = sharedPreferences
    }

    private fun removeSharedPreference(){
        PreferenceManager.getDefaultSharedPreferences(context)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.close_btn -> {
               windowManager.removeView(mFloatingLayout)
            }

            /*R.id.floatingIcon -> {
                openedLayout.visibility = VISIBLE

                floatingLayout.visibility = GONE


                inflater = LayoutInflater.from(this)
                val view  = inflater.inflate(R.layout.expanded_layout, null)

                val  builder = AlertDialog.Builder(context)
                builder.setView(view)
                val dialog = builder.create()
                dialog?.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val window = dialog.window!!.attributes
                window.gravity = Gravity.TOP or Gravity.START
                //window.y = 100
                dialog.show()
                Toast.makeText(context, "clicked", LENGTH_SHORT).show()
            }*/

            R.id.expanded_close_btn ->{
                //dialog.show()
                openedLayout.visibility = GONE
                floatingLayout.visibility = VISIBLE
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent): Boolean {

        when(event.action) {
            MotionEvent.ACTION_DOWN ->{
                //remember the initial position.
                initialX = params.x
                initialY = params.y
                //get the touch location
                initialTouchX = event.rawX
                initialTouchY = event.rawY

                Log.d("position", "$initialX,$initialY,$initialTouchX, $initialTouchY")
                return true
            }
            MotionEvent.ACTION_UP -> {
                val xDiff =  (event.rawX - initialTouchX)
                val yDiff =  (event.rawY - initialTouchY)

                //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                //So that is click event.
                if (xDiff < 10 && yDiff < 10) {
                    if (isViewCollapsed()) {
                        /*//When user clicks on the image view of the collapsed layout,
                        //visibility of the collapsed layout will be changed to "View.GONE"
                        //and expanded view will become visible.
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        //typetext.requestFocus();
*/
                        openedLayout.visibility = VISIBLE
                        floatingLayout.visibility = GONE
                        setUpExpandedLayout()
                    }
                }
            }
            MotionEvent.ACTION_MOVE ->{
                //Calculate the X and Y coordinates of the view.
                params.x = initialX +  (event.rawX - initialTouchX).toInt()
                params.y = initialY +  (event.rawY - initialTouchY).toInt()
                //Update the layout with new X & Y coordinate
                windowManager.updateViewLayout(mFloatingLayout, params)

                Log.d("position2", "$initialX,$initialY,${event.rawX}, ${event.rawY} /n ,${params.x},${params.x}")
                return true
            }
        }
        return false
    }

    private fun isViewCollapsed(): Boolean {
        return floatingLayout.visibility == VISIBLE
    }


    private fun setUpExpandedLayout() {
        val listRange = sharedPreferences.getString("date_list", "2")
        sortedList =  CustomDateUtils.sortList(listRange?.toInt(), clipBoardRepository.getNotes()).toMutableList()

        val floatingAdapter = FloatingAdapter(object : FloatingAdapter.FloatingClickListener{
            override fun clickListener(clipboardEntity: ClipboardEntity) {
                val clip: ClipData = ClipData.newPlainText("simple text", clipboardEntity.note)
                clipboard.primaryClip = clip
                Toast.makeText(context,"Copied!", LENGTH_SHORT).show()
            }
        })
        recyclerView.adapter = floatingAdapter
        sortedList?.let { floatingAdapter.setNotes(it) }
    }

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    private fun setFloatingLayout() {
        mFloatingLayout = LayoutInflater.from(this).inflate(R.layout.layout, null)
        frameLayout = mFloatingLayout.findViewById(R.id.frame_layout)
        recyclerView = mFloatingLayout.findViewById(R.id.expanded_recycler)
        imageView =  mFloatingLayout.findViewById(R.id.floatingIcon)
        floatingLayout = mFloatingLayout.findViewById(R.id.collapseView)
        openedLayout = mFloatingLayout.findViewById(R.id.expanded_layout)
        closeButton = mFloatingLayout.findViewById(R.id.close_btn)
        closeExpandedView = mFloatingLayout.findViewById(R.id.expanded_close_btn)
        closeButton.setOnClickListener(this)
        floatingLayout.setOnTouchListener(this)
        closeExpandedView.setOnClickListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        setWindowManager()
    }

    private fun setWindowManager() {
        val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay.height
        val size =  Point()
        //display.getSize(size)
        //val width = size.x
        //val height = size.y

        params.gravity = Gravity.TOP or Gravity.START

        /*params.x = 0
        params.y = height/2 */
        params.x = 0
        params.y = display/2
        setWindowView()
    }

    private fun setWindowView(){
        val foreground = ForegroundCheckTask().execute(context).get()
        if (!foreground && frameLayout.parent == null){
            windowManager.addView(mFloatingLayout,params)
            added = true
        }
    }

    private val primaryClipChangedListener = object: ClipboardManager.OnPrimaryClipChangedListener{
        override fun onPrimaryClipChanged() {

            val pasteData: String
            if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)){
                val item = clipboard.primaryClip?.getItemAt(0)?.text
                pasteData = item.toString()
                if(mPreviousText == pasteData) return
                else {
                    checkForDuplicate(pasteData)
                }
            }
            setWindowView()
        }
    }

    private fun checkForDuplicate(pasteData: String) {
        val list = clipBoardRepository.getNotes()
        val size = list.size
        Log.d("######", size.toString())

        val copies = 1
        for (notes in 0 until size){
            if (list[notes].note == pasteData) {
                Log.i("#####", "Text has been copied before")
                return
            }
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-DD", Locale.ENGLISH)
        val  date = simpleDateFormat.format(Date(System.currentTimeMillis()))
        val clipboardEntity = ClipboardEntity(note = pasteData, date = Date(System.currentTimeMillis()), formattedDate = date, number = copies)
        clipBoardRepository.insert(clipboardEntity)
        mPreviousText = pasteData
        Toast.makeText(applicationContext, "New note copied: $pasteData\nNote has been copied $copies times" , LENGTH_LONG).show()
    }

    override fun onDestroy() {
        Log.w("ClipboardService", "Service is closing")
        windowManager.removeView(mFloatingLayout)
        removeSharedPreference()
        super.onDestroy()
    }

    class ForegroundCheckTask: AsyncTask<Context, Void, Boolean>() {

        override fun doInBackground(vararg params: Context): Boolean {
            val context = params[0].applicationContext
            return isAppOnForeground(context)
        }

      private fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val  appProcesses = activityManager.runningAppProcesses ?: return false
          val packageName = context.packageName
          for ( appProcess in  appProcesses) {
              if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                  && appProcess.processName == packageName) {
                  return true
              }
          }
        return false
      }
    }
}



