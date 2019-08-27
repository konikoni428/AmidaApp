package com.masahiro.amidaapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.masahiro.amidaapp.AmidaData
import com.masahiro.amidaapp.AmidaView
import com.masahiro.amidaapp.R
import kotlin.random.Random

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var amidaView : AmidaView

    //あみだ表示範囲
    private lateinit var amidaRect : Array<Float>

    //あみだのサイズ
    private var amidaHorizontalSize :Int = 1
    private var amidaVerticalSize :Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(activity!!).get(PageViewModel::class.java)

        amidaHorizontalSize = pageViewModel.amidaHorizontalSize
        amidaVerticalSize = pageViewModel.amidaVerticalSize


        if(pageViewModel.yPosRatio.value.isNullOrEmpty()){
            val yposRatio: MutableList<Double> = mutableListOf()
            for(i in 0 until amidaHorizontalSize){
                val rand = Random.nextDouble(0.05, 0.95)
                yposRatio.add(rand)
            }
            pageViewModel.yPosRatio.value = yposRatio.toList()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val root = inflater.inflate(R.layout.fragment_result, container, false)
        amidaView = AmidaView(context!!)

        amidaView.amidaVerticalSize = amidaVerticalSize
        amidaView.amidaHorizontalSize = amidaHorizontalSize

        amidaView.startDataset = pageViewModel.startDataset
        amidaView.finishDataset = pageViewModel.finishDataset

        amidaView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //登録解除しないと無限に呼ばれる時がある
                amidaView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // do whatever
                val height = view?.height ?: 0
                val width = view?.width ?: 0

                amidaRect = arrayOf(0f, height*0.1f, width.toFloat(), height*0.9f)

                if(pageViewModel.amidaRect.value.isNullOrEmpty()){
                    Log.d("TAG", "${amidaRect[0]}:${amidaRect[1]}:${amidaRect[2]}:${amidaRect[3]}")
                    pageViewModel.amidaRect.postValue(amidaRect)
                }
                amidaView.amidaRect = amidaRect

                if(pageViewModel.amidaXPosList.isEmpty()){
                    val amidaXPosList : ArrayList<Float> = arrayListOf()
                    //あみだ縦線生成
                    for(i in 1..amidaVerticalSize){
                        val xPos = amidaView.width/(amidaVerticalSize+1f)*i
                        amidaXPosList.add(xPos)
                    }
                    pageViewModel.amidaXPosList = amidaXPosList.toTypedArray()
                }
                amidaView.amidaXPosList = pageViewModel.amidaXPosList


                if(pageViewModel.amidaDataList.value.isNullOrEmpty()){
                    val amidaViewList = amidaView.createAmidaDataList(pageViewModel.yPosRatio.value ?: listOf())
                    pageViewModel.amidaDataList.value = amidaViewList
                }
                amidaView.amidaDataList = pageViewModel.amidaDataList.value ?: listOf()

                val sectionNumber = arguments?.get(ARG_SECTION_NUMBER) as Int
                amidaView.drawResultPath(sectionNumber, amidaRect)
                //amidaView.draw()

                pageViewModel.resultPosDataset.value = amidaView.createPosPairDataset()
            }
        })

        return amidaView
    }


    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}