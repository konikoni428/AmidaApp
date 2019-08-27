package com.masahiro.amidaapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.masahiro.amidaapp.AmidaData

class PageViewModel : ViewModel() {

    var startDataset: Array<String> = arrayOf()
    var finishDataset: Array<String> = arrayOf()
    var amidaDataList: MutableLiveData<List<AmidaData>> = MutableLiveData()
    var yPosRatio:MutableLiveData<List<Double>> = MutableLiveData()
    var amidaRect : MutableLiveData<Array<Float>> = MutableLiveData()
    var amidaXPosList : Array<Float> = arrayOf()

    var amidaHorizontalSize = 1
    var amidaVerticalSize = 1

    var resultPosDataset : MutableLiveData<List<Pair<Int, Int>>> = MutableLiveData()

    fun getResultPairDataset(): List<Pair<String, String>>{
        val resultPairDataset = resultPosDataset.value?.map {
            Pair(startDataset[it.first-1], finishDataset[it.second-1])
        }
        return resultPairDataset ?: listOf()
    }
}