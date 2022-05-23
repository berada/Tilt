package hu.bme.aut.android.tilt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.tilt.adapter.StageRecyclerViewAdapter
import hu.bme.aut.android.tilt.databinding.FragmentListBinding
import hu.bme.aut.android.tilt.model.Stage
import hu.bme.aut.android.tilt.viewmodel.StageListViewModel

class ListFragment : FragmentWithOptionsMenu(), StageRecyclerViewAdapter.TodoItemClickListener {
    private lateinit var binding: FragmentListBinding;
    private lateinit var stageListViewModel: StageListViewModel
    private lateinit var stageRecyclerViewAdapter: StageRecyclerViewAdapter

    private fun setupRecyclerView(view: View) {
        stageRecyclerViewAdapter = StageRecyclerViewAdapter()
        stageRecyclerViewAdapter.itemClickListener = this
        val rvStageList = view.findViewById<RecyclerView>(R.id.rvStageList)
        rvStageList.adapter = stageRecyclerViewAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        stageListViewModel = ViewModelProvider(this).get(StageListViewModel::class.java)
        stageListViewModel.stageList.observe(
            viewLifecycleOwner,
            Observer { stageRecyclerViewAdapter.addAll(it) })
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        setupRecyclerView(binding.root)
        binding.tvWelcome.text =
            "Üdvözlünk, ${ListFragmentArgs.fromBundle(requireArguments()).nameToShow}"
        return binding.root
    }

    override fun onItemClick(stage: Stage) {
        val action = ListFragmentDirections.actionGame(stage.id!!)
        findNavController().navigate(action)
    }
}