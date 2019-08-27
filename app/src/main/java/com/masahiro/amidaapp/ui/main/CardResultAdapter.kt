package com.masahiro.amidaapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.masahiro.amidaapp.R
import kotlinx.android.synthetic.main.card_input.view.*
import kotlinx.android.synthetic.main.card_result.view.*

class CardResultAdapter(private val resultPairDataset: List<Pair<String, String>>) :
    RecyclerView.Adapter<CardResultAdapter.CardViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class CardViewHolder(val cardView: View) : RecyclerView.ViewHolder(cardView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CardResultAdapter.CardViewHolder {
        // create a new view
        val nameCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_result, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return CardViewHolder(nameCardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cardView.cardViewFirst.textViewFirst.text = resultPairDataset[position].first
        holder.cardView.cardViewSecond.textViewSecond.text = resultPairDataset[position].second
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = resultPairDataset.size
}