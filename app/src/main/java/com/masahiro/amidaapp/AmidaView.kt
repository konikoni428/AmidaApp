package com.masahiro.amidaapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.masahiro.amidaapp.ui.main.PageViewModel
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random
import kotlin.random.nextInt

class AmidaView: View {
    companion object{
        val TAG = AmidaView::class.java.simpleName
    }
    private val paint = Paint()
    private val resultPath = Path()
    //private var mCanvas: Canvas? = null
    //あみだのサイズ
    var amidaVerticalSize = 1
    var amidaHorizontalSize = 1

    var amidaVerticalPos = 1

    var startDataset : Array<String> = arrayOf()
    var finishDataset : Array<String> = arrayOf()

    //あみだ縦線のxのリスト
    var amidaXPosList : Array<Float> = arrayOf()

    var amidaDataList: List<AmidaData> = listOf()

    var amidaRect : Array<Float> = arrayOf()

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //canvas?.drawColor(0, PorterDuff.Mode.CLEAR)

        paint.apply {
            color = Color.argb(255,0,0, 0) //black
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }

        //あみだ横線
        if(amidaDataList.isNotEmpty()){
            for(i in 0 until amidaHorizontalSize){
                canvas?.drawLine(amidaDataList[i].path.start.x, amidaDataList[i].path.start.y, amidaDataList[i].path.stop.x, amidaDataList[i].path.stop.y, paint)
            }
        }

        //あみだ縦線
        if(amidaXPosList.isNotEmpty()) {
            for (i in 0 until amidaVerticalSize) {
                canvas?.drawLine(amidaXPosList[i], amidaRect[1] - 30, amidaXPosList[i], amidaRect[3] + 30, paint)
            }
            paint.apply {
                style = Paint.Style.FILL_AND_STROKE
                strokeWidth = 5f
                textSize = 60f
                color = Color.argb(255,0,0, 0) //black
            }
            //名前と結果の文字
            finishDataset.forEachIndexed{i, fin ->
                canvas?.drawText(fin, amidaXPosList[i] - 50, amidaRect[3] + 100, paint)
            }
            startDataset.forEachIndexed { i, name ->
                canvas?.drawText(name, amidaXPosList[i] - 50, amidaRect[1] - 50, paint)
            }
        }

        paint.apply {
            color = Color.argb(255,255,0, 0) //red
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }

        //あみだ結果
        canvas?.drawPath(resultPath, paint)

        //if(amidaDataList.size == amidaHorizontalSize) return

        //Log.d(TAG, "onDraw")
        // (x1,y1,x2,y2,paint) 左上の座標(x1,y1), 右下の座標(x2,y2)
        //Log.d(TAG, "${amidaRect[0]}:${amidaRect[1]}:${amidaRect[2]}:${amidaRect[3]}")
        //canvas?.drawRect(amidaRect[0], amidaRect[1], amidaRect[2], amidaRect[3], strokePaint)
    }

    fun createAmidaDataList(yPosRatio: List<Double>): List<AmidaData>{
        val amidaDataList : MutableList<AmidaData> = mutableListOf()
        //あみだ横線生成
        for(i in 1..amidaHorizontalSize){
            //ex) verticalSize=3なら0または1が生成される。これをあみだの横線を引く始点の縦線番号とする
            val amidaVerticalLeft = i % (amidaVerticalSize - 1)
            //横線を引くためのy座標
            //val randY = Random.nextInt(amidaRect[1].toInt(), amidaRect[3].toInt())
            val amidaPath = AmidaPath(Position(amidaXPosList[amidaVerticalLeft], (yPosRatio[i-1]*height*0.8).toFloat() + amidaRect[1]), Position(amidaXPosList[amidaVerticalLeft+1], (yPosRatio[i-1]*height*0.8).toFloat() + amidaRect[1]))
            val amidaData = AmidaData(amidaVerticalLeft+1, amidaVerticalLeft+2, amidaPath)
            amidaDataList.add(amidaData)

            //canvas?.drawLine(amidaData.path.start.x, amidaData.path.start.y, amidaData.path.stop.x, amidaData.path.stop.y, strokePaint)
        }

        amidaDataList.sortBy {
            it.path.start.y
        }
        return amidaDataList.toList()
    }


    fun draw(){
        invalidate()
    }


    fun drawResultPath(verticalPos: Int, amidaRect: Array<Float>){
        createAmidaResultPath(verticalPos, amidaRect)
        invalidate()
    }

    private fun createAmidaResultPath(verticalPos: Int, amidaRect: Array<Float>):Path{
        //verticalPos -> 1..amidaVerticalSize
        amidaVerticalPos = verticalPos
        resultPath.moveTo(amidaXPosList[verticalPos-1], amidaRect[1]-30)
        var nowY = 0f
        var nowVerticalPos = verticalPos
        //あみだLogic
        while(true){
            val amidaData = amidaDataList.firstOrNull {
                ((it.first == nowVerticalPos) or (it.second == nowVerticalPos)) and (nowY < it.path.start.y)
            }
            if(amidaData == null) break

            when(nowVerticalPos){
                amidaData.first -> {
                    resultPath.lineTo(amidaData.path.start.x, amidaData.path.start.y)
                    resultPath.lineTo(amidaData.path.stop.x, amidaData.path.stop.y)
                    nowVerticalPos = amidaData.second
                }
                amidaData.second -> {
                    resultPath.lineTo(amidaData.path.stop.x, amidaData.path.stop.y)
                    resultPath.lineTo(amidaData.path.start.x, amidaData.path.start.y)
                    nowVerticalPos = amidaData.first
                }
            }
            nowY = amidaData.path.stop.y
        }
        resultPath.lineTo(amidaXPosList[nowVerticalPos-1], amidaRect[3] + 30)
        return resultPath
    }

    fun createPosPairDataset(): List<Pair<Int, Int>>{
        val resultPairDataset : MutableList<Pair<Int, Int>> = mutableListOf()
        //verticalPos -> 1..amidaVerticalSize
        for(verticalPos in 1..amidaVerticalSize){
            var nowVerticalPos = verticalPos
            //あみだLogic
            amidaDataList.forEach {
                when(nowVerticalPos){
                    it.first -> {
                        nowVerticalPos = it.second
                    }
                    it.second -> {
                        nowVerticalPos = it.first
                    }
                }
            }
            resultPairDataset.add(Pair(verticalPos, nowVerticalPos))
        }
        return resultPairDataset.toList()
    }
}