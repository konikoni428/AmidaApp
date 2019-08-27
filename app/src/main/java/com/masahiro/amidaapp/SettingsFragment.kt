package com.masahiro.amidaapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "explain"
private const val ARG_DATASET = "dataset"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SettingsFragment : Fragment(), CardAdapter.OnDatasetChangedListener{

    private var listener: SettingsFragment.OnFragmentInteractionListener? = null

    private lateinit var explain: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val cardDataset : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            explain = it.getString(ARG_PARAM1) ?: "null"
            cardDataset.addAll(it.getStringArray(ARG_DATASET) ?: arrayOf())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        //explain_text.text = explain
        viewManager = LinearLayoutManager(root.context)
        viewAdapter = CardAdapter(cardDataset, this)

        recyclerView = root.findViewById<RecyclerView>(R.id.card_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        val editText = root.findViewById<EditText>(R.id.editText)

        root.findViewById<TextView>(R.id.explain_text).apply {
            text = explain
        }

        root.findViewById<Button>(R.id.add_button).apply{
            setOnClickListener {
                if (!editText.text.isNullOrEmpty()) {
                    cardDataset.add(editText.text.toString())
                    editText.text.clear()
                    viewAdapter.notifyDataSetChanged()
                    onDatasetChanged(cardDataset)
                }
            }
        }

        root.findViewById<Button>(R.id.submit_button).apply {
            setOnClickListener{
                onSubmitButtonPressed(cardDataset)
            }
        }
        // Inflate the layout for this fragment
        return root
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onSubmitButtonPressed(cardDataset: MutableList<String>) {
        val nowWindow = if(explain == "名前を入れてください") 0 else 1
        listener?.onFragmentInteraction(cardDataset, nowWindow)
    }

    override fun onDatasetChanged(cardDataset: MutableList<String>) {
        val nowWindow = if(explain == "名前を入れてください") 0 else 1
        listener?.onFragmentAddInteraction(cardDataset, nowWindow)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingsFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(cardDataset: MutableList<String>, nowWindow: Int)
        fun onFragmentAddInteraction(cardDataset: MutableList<String>, nowWindow: Int)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 explaint Parameter.
         * @return A new instance of fragment SettingsFragment.
         */
        @JvmStatic
        fun newInstance(explain: String, dataset: Array<String>) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, explain)
                    putStringArray(ARG_DATASET, dataset)
                }
            }
    }
}
