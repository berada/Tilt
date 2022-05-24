package hu.bme.aut.android.tilt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.tilt.databinding.StageListItemBinding
import hu.bme.aut.android.tilt.model.Stage

class StageRecyclerViewAdapter : ListAdapter<Stage, StageRecyclerViewAdapter.ViewHolder>(itemCallback) {
    companion object {
        object itemCallback : DiffUtil.ItemCallback<Stage>() {
            override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var binding: StageListItemBinding;

    private var stageList: List<Stage> = emptyList()

    var itemClickListener: TodoItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = StageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmpStage = stageList[position]
        holder.stage = tmpStage
        holder.tvItemNum.text = tmpStage.id.toString()
        holder.tvItemRating.text = "${tmpStage.rating.toString()} / 5"
        holder.tvItemNick.text = "${tmpStage.hs_nick} | ${tmpStage.hs_moves.toString()} moves"
    }

    fun addItem(stage: Stage?) {
        stage ?: return

        stageList += stage
        submitList(stageList)
    }

    fun addAll(stages: List<Stage>) {
        stageList += stages
        submitList(stageList)
    }

    fun deleteRow(position: Int) {
        stageList = stageList.filterIndexed { index, _ -> index != position }
        submitList(stageList)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemNum: TextView = binding.tvItemNum
        val tvItemRating: TextView = binding.tvItemRating
        val tvItemNick: TextView = binding.tvItemNick

        var stage: Stage? = null

        init {
            itemView.setOnClickListener {
                stage?.let { itemClickListener?.onItemClick(it) }
            }
        }
    }

    interface TodoItemClickListener {
        fun onItemClick(stage: Stage)
    }
}