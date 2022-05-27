package hu.bme.aut.android.tilt

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.tilt.databinding.FragmentGameBinding
import hu.bme.aut.android.tilt.model.Stage
import hu.bme.aut.android.tilt.model.TiltModel
import hu.bme.aut.android.tilt.model.TiltModel.DOWN
import hu.bme.aut.android.tilt.model.TiltModel.LEFT
import hu.bme.aut.android.tilt.model.TiltModel.RIGHT
import hu.bme.aut.android.tilt.model.TiltModel.UP
import hu.bme.aut.android.tilt.model.TiltModel.getModelString
import hu.bme.aut.android.tilt.model.TiltModel.initGame
import hu.bme.aut.android.tilt.model.TiltModel.isEnd
import hu.bme.aut.android.tilt.model.TiltModel.isLost
import hu.bme.aut.android.tilt.model.TiltModel.model
import hu.bme.aut.android.tilt.model.TiltModel.moves
import hu.bme.aut.android.tilt.view.TiltView

class GameFragment : FragmentWithOptionsMenu() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var stage: Stage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentGameBinding.inflate(layoutInflater, container, false)
        val stageId = GameFragmentArgs.fromBundle(requireArguments()).stageId
        var movesSaved = savedInstanceState?.getInt("MOVES")
        var stageSaved = savedInstanceState?.getString("STAGE")
        val db = Firebase.firestore
        db.collection("stages").document(stageId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    stage = Stage(stageId.toLong(),
                        document.get("rating").toString().toLong(),
                        document.get("hs_moves").toString().toLong(),
                        document.get("hs_nick").toString(),
                        document.get("stage").toString()
                    )

                    if (movesSaved != null) {
                        initGame(stageSaved!!)
                        moves = movesSaved
                        binding.tvMoves.text = "Moves: $moves"
                        binding.tiltView.visibility = View.VISIBLE
                        binding.tvResult.visibility = View.GONE
                        binding.tiltView.invalidate()
                    } else {
                        init()
                    }

                    binding.tvId.text = "Stage: ${stage.id}"
                    binding.btRestart.setOnClickListener { init() }
                    swipeSetUp()
                }
            }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("MOVES", moves)
        outState.putString("STAGE", getModelString(model))
    }


    private fun init() {
        initGame(stage.stage!!)
        binding.tvMoves.text = "Moves: $moves"
        binding.tiltView.visibility = View.VISIBLE
        binding.tvResult.visibility = View.GONE
        binding.tiltView.invalidate()
    }

    private fun checkIfOver() {
        if (isEnd) {
            if (isLost) {
                binding.tvResult.setTextColor(Color.rgb(230,0,0))
                binding.tvResult.text = "You have lost! Try again!"
            } else {
                binding.tvResult.setTextColor(Color.rgb(0, 153, 51))
                if (stage.hs_moves!!.toInt() > moves || stage.hs_moves!!.toInt() == 0) {
                    val modifiedStage = hashMapOf(
                        "hs_moves" to moves,
                        "hs_nick" to userEmail.toString(),
                        "id" to stage.id,
                        "rating" to stage.rating,
                        "stage" to stage.stage
                    )
                    val db = Firebase.firestore
                    db.collection("stages")
                        .document(stage.id.toString()).set(modifiedStage)
                    binding.tvResult.text = "Congrats! You have set a new record!"

                } else {
                    binding.tvResult.text = "Congrats! You have won!"
                }
            }
            binding.tvResult.visibility = View.VISIBLE
            binding.tiltView.visibility = View.GONE
        }
    }

    private fun swipeSetUp() {
        binding.tiltView.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                binding.tiltView.swipe(LEFT)
                binding.tvMoves.text = "Moves: $moves"
                checkIfOver()
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                binding.tiltView.swipe(RIGHT)
                binding.tvMoves.text = "Moves: $moves"
                checkIfOver()
            }
            override fun onSwipeUp() {
                super.onSwipeUp()
                binding.tiltView.swipe(UP)
                binding.tvMoves.text = "Moves: $moves"
                checkIfOver()
            }
            override fun onSwipeDown() {
                super.onSwipeDown()
                binding.tiltView.swipe(DOWN)
                binding.tvMoves.text = "Moves: $moves"
                checkIfOver()
            }
        })
    }

}
