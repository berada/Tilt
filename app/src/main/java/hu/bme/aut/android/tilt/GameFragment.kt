package hu.bme.aut.android.tilt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.tilt.databinding.FragmentGameBinding
import hu.bme.aut.android.tilt.viewmodel.GameViewModel

class GameFragment : FragmentWithOptionsMenu() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentGameBinding.inflate(layoutInflater, container, false)

        val stageId = GameFragmentArgs.fromBundle(requireArguments()).stageId
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.getById(stageId).observe(
            viewLifecycleOwner,
            Observer { stage ->
                binding.tvId.text = stage?.id.toString()
            }
        )
        return binding.root
    }
}
