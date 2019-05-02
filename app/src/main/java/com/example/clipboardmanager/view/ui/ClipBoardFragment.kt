package com.example.clipboardmanager.view.ui

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clipboardmanager.R
import com.example.clipboardmanager.service.model.ClipboardEntity
import com.example.clipboardmanager.view.adapter.ClipBoardAdapter
import com.example.clipboardmanager.viewModel.ClipBoardListViewModel




class ClipBoardFragment : Fragment() {


    lateinit var recyclerView: RecyclerView
    lateinit var clipBoardAdapter: ClipBoardAdapter


    companion object {
        fun newInstance() = ClipBoardFragment()
    }
    private lateinit var clipBoardList: List<ClipboardEntity>
    private lateinit var viewModel: ClipBoardListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_activity2_fragment, container, false)
        recyclerView = view.findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        setUpAdapter()

        return view
    }


    private fun setUpAdapter() {
        clipBoardList = ArrayList()

        clipBoardAdapter = ClipBoardAdapter(context, clipBoardList, object : ClipBoardAdapter.Callback{

            override fun onItemClick(clipboardEntity: ClipboardEntity) {
                Log.d("position",clipboardEntity.toString())
                //viewModel.delete(clipboardEntity)
                createDialog(clipboardEntity)
            }
        })

        recyclerView.adapter = clipBoardAdapter
    }

    private fun createDialog(clipboardEntity: ClipboardEntity) {
        val position: Int = clipboardEntity.id!!

        //Toast.makeText(context, position,LENGTH_LONG).show()

        val alertDialogBuilder = AlertDialog.Builder(context)
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
                (activity as MainActivity).show(position, clipboardEntity)
            } else if (which == 1) {
                deleteItem(clipboardEntity)
            }
        }
        alertDialogBuilder.create().show()
    }

    private fun shareText(position: Int) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, clipBoardList.get(position).note);
        startActivity(Intent.createChooser(shareIntent,"Send Via...."))
    }

    private fun deleteItem(clipboardEntity: ClipboardEntity) {
        viewModel.delete(clipboardEntity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClipBoardListViewModel::class.java)
        viewModel.getAllNotes().observe(this,
            Observer<List<ClipboardEntity>> { t -> clipBoardAdapter.setNotes(t!!)
            Log.d("getAll","getAll")})
    }
}
