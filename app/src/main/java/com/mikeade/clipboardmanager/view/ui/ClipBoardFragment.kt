package com.mikeade.clipboardmanager.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikeade.clipboardmanager.R
import com.mikeade.clipboardmanager.service.model.ClipboardEntity
import com.mikeade.clipboardmanager.utils.CustomDateUtils
import com.mikeade.clipboardmanager.view.adapter.ClipBoardAdapter
import com.mikeade.clipboardmanager.viewModel.ClipBoardListViewModel
import com.mikeade.clipboardmanager.viewModel.ClipBoardViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.preference.PreferenceManager


class ClipBoardFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    lateinit var clipBoardAdapter: ClipBoardAdapter
    lateinit var binding: com.mikeade.clipboardmanager.databinding.FragmentMainActivityBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var clipBoardList: List<ClipboardEntity>
    private lateinit var viewModel: ClipBoardListViewModel

    companion object {
        fun newInstance() = ClipBoardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        viewModel = ViewModelProviders.of(this, context?.let { ClipBoardViewModelFactory(it) }).get(ClipBoardListViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_activity, container, false)
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.setHasFixedSize(true)
        setUpAdapter()
        setDeleteOnSwipe()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val text = activity?.intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        if (text != null) {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-DD", Locale.ENGLISH)
            val  date = simpleDateFormat.format(Date(System.currentTimeMillis()))
            viewModel.insertNote((ClipboardEntity(note = text.toString(), date = Date(System.currentTimeMillis()), formattedDate = date, number = 2)))
        }
        //fetch the list upon app restart
        viewModel.getAllNotes().observe(this, Observer<List<ClipboardEntity>> { list ->
            val listRange = sharedPreferences.getString("date_list", "2")
                val sortedList = list?.let { CustomDateUtils.sortList(listRange.toInt(), it) }
                sortedList?.let { clipBoardAdapter.setNotes(it) }
                        Log.d("getAll","")
                })
    }

    private fun setDeleteOnSwipe() {
        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, p1: Int) {
                val clipboardEntity = clipBoardAdapter.getNoteAt(viewHolder.adapterPosition)
                viewModel.delete(clipboardEntity)
            }

        }).attachToRecyclerView(binding.recycler)

    }

    private fun setUpAdapter() {
        clipBoardList = ArrayList()
        clipBoardAdapter = ClipBoardAdapter(context, clipBoardList)
        binding.recycler.adapter = clipBoardAdapter
    }

   /* fun createDialog(clipboardEntity: ClipboardEntity) {
        val position: Int = clipboardEntity.id!!

        //Toast.makeText(context, position,LENGTH_LONG).show()

        val alertDialogBuilder = AlertDialog.Builder(context?.applicationContext)
        val item = clipboardEntity
        val options = arrayOfNulls<String>(2)
        if (item.note.length > 15) {
            options[0] = "Edit  " + item.note.substring(0, 10) + "..."
            options[1] = "Delete  " + item.note.substring(0, 10) + "..."
        } else {
            options[0] = "Edit " + item.note
            options[1] = "Delete " + item.note
        }
        alertDialogBuilder.setItems(options) { dialog, which ->
            if (which == 0) {
                (activity as MainActivity).show(clipboardEntity)
            } else if (which == 1) {
                deleteItem(clipboardEntity)
            }
        }
        alertDialogBuilder.create().show()
    }*/

    private fun shareText(position: Int) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, clipBoardList.get(position).note);
        startActivity(Intent.createChooser(shareIntent,"Send Via...."))
    }

    fun deleteItem(clipboardEntity: ClipboardEntity) {
        viewModel.delete(clipboardEntity)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        viewModel.getAllNotes().observe(this,
            Observer<List<ClipboardEntity>> { list ->
                if (key.equals("date_list")){
                    val listRange = sharedPreferences.getString(key, "2")
                    val sortedList = list?.let { CustomDateUtils.sortList(listRange.toInt(), it) }
                    sortedList?.let { clipBoardAdapter.setNotes(it) }
                    Log.d("getAll","")
                }
            })
    }

    private fun setupSharedPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        this.sharedPreferences = sharedPreferences
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSharedPreferences()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeSharedPreference()
    }

    private fun removeSharedPreference(){
        PreferenceManager.getDefaultSharedPreferences(context)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}
