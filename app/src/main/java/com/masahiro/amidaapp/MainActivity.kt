package com.masahiro.amidaapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), SettingsFragment.OnFragmentInteractionListener {

    var startDataset: MutableList<String> = mutableListOf()
    var finishDataset: MutableList<String> = mutableListOf()

    //var nowWindow = 0 //0->name setting, 1->result setting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Fragmentを作成します
        val fragment = SettingsFragment.newInstance("名前を入れてください", startDataset.toTypedArray())
        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
        val transaction = supportFragmentManager.beginTransaction()
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        transaction.add(R.id.container, fragment)
        // 最後にcommitを使用することで変更を反映します
        transaction.commit()
    }

    override fun onFragmentInteraction(cardDataset: MutableList<String>, nowWindow: Int) {
        if(nowWindow == 0){
            if((1 < cardDataset.size) and (cardDataset.size < 8)){
                startDataset = cardDataset
                val fragment = SettingsFragment.newInstance("結果の選択肢を入れてください", finishDataset.toTypedArray())
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }else{
                Snackbar.make(window.decorView.findViewById(android.R.id.content), "人数を２−７人にしてください", Snackbar.LENGTH_LONG).show()
            }
        }else if(nowWindow == 1){
            if(startDataset.size == cardDataset.size) {
                finishDataset = cardDataset
                finishDataset.shuffle()
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(ResultActivity.START_DATASET, startDataset.toTypedArray())
                intent.putExtra(ResultActivity.FINISH_DATASET, finishDataset.toTypedArray())
                startActivity(intent)
            }else{
                Snackbar.make(window.decorView.findViewById(android.R.id.content), "結果を${startDataset.size}個作成してください", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    override fun onFragmentAddInteraction(cardDataset: MutableList<String>, nowWindow: Int) {
        if(nowWindow == 0){
            startDataset = cardDataset
        }else{
            finishDataset = cardDataset
        }
    }
}
