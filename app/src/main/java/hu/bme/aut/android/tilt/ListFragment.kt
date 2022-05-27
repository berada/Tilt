package hu.bme.aut.android.tilt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.tilt.adapter.StageRecyclerViewAdapter
import hu.bme.aut.android.tilt.databinding.FragmentListBinding
import hu.bme.aut.android.tilt.model.Stage

class ListFragment : FragmentWithOptionsMenu(), StageRecyclerViewAdapter.TodoItemClickListener {
    private lateinit var binding: FragmentListBinding;
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
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        setupRecyclerView(binding.root)
        binding.tvWelcome.text =
            "Üdvözlünk, ${ListFragmentArgs.fromBundle(requireArguments()).nameToShow}"
        binding.btnCreateStage.setOnClickListener { btnCreateStageClick() }
        initStagesListener()

        return binding.root
    }

    private fun btnCreateStageClick() {
        val action = ListFragmentDirections.actionCreate()
        findNavController().navigate(action)
    }

    override fun onItemClick(stage: Stage) {
        val action = ListFragmentDirections.actionGame(stage.id.toString())
        findNavController().navigate(action)
    }

    private fun initStagesListener() {
        val db = Firebase.firestore
        db.collection("stages")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> stageRecyclerViewAdapter.addItem(dc.document.toObject<Stage>())
                        //DocumentChange.Type.MODIFIED -> Toast.makeText(context, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                        //DocumentChange.Type.REMOVED -> Toast.makeText(context, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}