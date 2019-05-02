package com.example.clipboardmanager.view.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT

import com.example.clipboardmanager.R
import com.example.clipboardmanager.service.model.ClipDatabase
import com.example.clipboardmanager.service.model.ClipboardEntity
import com.example.clipboardmanager.viewModel.ClipBoardListViewModel
import com.example.clipboardmanager.viewModel.ClipBoardViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EditClipBoardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EditClipBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EditClipBoardFragment : Fragment() , View.OnClickListener{

    lateinit var args: Bundle
    lateinit var editText: EditText
    var object_id: Int = 0
    private lateinit var clipboard: ClipboardManager
    private lateinit var viewModel: ClipBoardListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        args = arguments!!
        val id = args.getInt("pass",0)
        object_id = id

        viewModel = ViewModelProviders.of(this).get(ClipBoardListViewModel::class.java)
        clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_clip_board, container, false)
        editText = view.findViewById(R.id.edit_query)

        val btn_update: Button = view.findViewById(R.id.btn_update)

        viewModel.loadData(object_id).observe(this, Observer<ClipboardEntity>(){
            viewModel.loadData(object_id).removeObservers(this)
            editText.setText(it?.note)
        })

        btn_update.setOnClickListener(this)

        return view
    }

    /**
     * To update data
     */
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_update) {

            val clipboardEntity = ClipboardEntity(object_id, editText.text.toString(), System.currentTimeMillis().toString())
            Toast.makeText(context, editText.text.toString(), LENGTH_SHORT).show()
            viewModel.updateNote(clipboardEntity)
            Toast.makeText(context, "Saved!", LENGTH_SHORT).show()
            activity?.onBackPressed()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
      //val clipboardEntity = ClipboardEntity(object_id, editText.text.toString())
        when(item?.itemId){
            android.R.id.home -> activity?.onBackPressed()
            R.id.action_share -> shareText()
            R.id.action_copy -> copyText()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun copyText() {
        val clip: ClipData = ClipData.newPlainText("simple text", editText.text.toString())
        clipboard.primaryClip = clip
        Toast.makeText(context,"Copied!", LENGTH_SHORT).show()
    }

    private fun shareText() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, editText.text.toString());
        startActivity(Intent.createChooser(shareIntent,"Send Via...."))
    }
}