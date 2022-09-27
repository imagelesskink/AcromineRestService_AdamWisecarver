package com.example.acrominerestservice_adamwisecarver.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acrominerestservice_adamwisecarver.R
import com.example.acrominerestservice_adamwisecarver.data.model.AbbreviationItem
import com.example.acrominerestservice_adamwisecarver.data.model.AbbreviationList
import com.example.acrominerestservice_adamwisecarver.databinding.AbiListItemBinding

class AcromineAdapter(
    private val list: MutableList<AbbreviationItem> = mutableListOf()
) : RecyclerView.Adapter<AcromineAdapter.AcromineViewHolder>(){

    lateinit var showVars: (List<AbbreviationItem>) -> Unit

    constructor(vars: (List<AbbreviationItem>) -> Unit) : this()  {
        showVars = vars
    }

    inner class AcromineViewHolder(private val binding: AbiListItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val res = binding.root.context.resources

        fun onBind(item: AbbreviationItem) {
            binding.apply {
                tvAbiName.text = res.getString(R.string.item_name,item.lf)
                tvAbiFreq.text = res.getString(R.string.item_freq, item.freq.toString())
                tvAbiSince.text = res.getString(R.string.item_since, item.since.toString())

                if (!item.vars.isNullOrEmpty()) {
                    btnVars.apply {
                        visibility = View.VISIBLE
                        setOnClickListener { showVars(item.vars as List<AbbreviationItem>) }
                    }
                }
            }
        }
    }

    fun setNewList(newList: List<AbbreviationItem>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcromineViewHolder =
        AcromineViewHolder(
            AbiListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AcromineViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

