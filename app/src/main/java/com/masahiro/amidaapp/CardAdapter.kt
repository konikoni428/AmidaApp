package com.masahiro.amidaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_input.view.*

class CardAdapter(private val myDataset: MutableList<String>, private val onDatasetChangedListener: OnDatasetChangedListener) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class CardViewHolder(val cardView: View) : RecyclerView.ViewHolder(cardView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CardAdapter.CardViewHolder {
        // create a new view
        val nameCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_input, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return CardViewHolder(nameCardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cardView.card_text_view.text = myDataset[position]
        holder.cardView.delete_button.setOnClickListener {
            myDataset.removeAt(position)
            onDatasetChangedListener.onDatasetChanged(myDataset)
            notifyDataSetChanged()
        }
    }

    interface OnDatasetChangedListener {
        fun onDatasetChanged(cardDataset: MutableList<String>)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}