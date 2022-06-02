package hu.bme.aut.android.tilt

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.tilt.databinding.FragmentCreateBinding
import hu.bme.aut.android.tilt.databinding.FragmentResultBinding
import hu.bme.aut.android.tilt.extensions.validateNonEmpty
import hu.bme.aut.android.tilt.model.Stage
import hu.bme.aut.android.tilt.model.TiltModel
import hu.bme.aut.android.tilt.model.TiltModel.moves

class ResultFragment : BaseFragment() {

    private lateinit var binding: FragmentResultBinding
    private lateinit var stage: Stage
    private var oldRating: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        var stageId = ResultFragmentArgs.fromBundle(requireArguments()).stageId
        binding = FragmentResultBinding.inflate(layoutInflater, container, false)

        val db = Firebase.firestore
        db.collection("stages").document(stageId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    stage = Stage(stageId.toLong(),
                        document.get("rating").toString().toLong(),
                        document.get("hs_moves").toString().toLong(),
                        document.get("hs_nick").toString(),
                        document.get("ratings_count").toString().toLong(),
                        document.get("stage").toString()
                    )
                    val newRating = hashMapOf(
                        "rating" to 0,
                    )
                    db.collection("users").document(userEmail.toString()).collection("ratings").document(stageId).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                if (document.get("rating") == null) {
                                    db.collection("users").document(userEmail.toString()).collection("ratings").document(stageId).set(newRating)
                                } else {
                                    oldRating = document.get("rating").toString().toDouble()
                                    binding.ratingBar.rating = oldRating.toFloat()
                                }
                            }
                        }
                }
            }

        binding.tvId.text = "Stage: $stageId"
        binding.btnRating.setOnClickListener { submitRating() }

        return binding.root
    }

    private fun submitRating() {
        var rating: Long
        val db = Firebase.firestore

        if (oldRating == 0.0) {
            rating = (stage.rating!!*stage.ratings_count!! + binding.ratingBar.rating.toLong())/(stage.ratings_count!! + 1)
            val ratingsCount = stage.ratings_count!! + 1
            val modifiedStage = Stage(stage.id, rating, stage.hs_moves, stage.hs_nick, ratingsCount, stage.stage)
            db.collection("stages").document(stage.id.toString()).set(modifiedStage).addOnCompleteListener {
                toast("Rating was successfully recorded.")
            }
        } else {
            rating = (stage.rating!!*stage.ratings_count!! - oldRating.toLong() + binding.ratingBar.rating.toLong())/(stage.ratings_count!!)
            val modifiedStage = Stage(stage.id, rating, stage.hs_moves, stage.hs_nick, stage.ratings_count, stage.stage)
            db.collection("stages").document(stage.id.toString()).set(modifiedStage).addOnCompleteListener {
                toast("Rating was successfully changed.")
            }
        }
        val newRating = hashMapOf(
            "rating" to rating,
        )
        db.collection("users").document(userEmail.toString()).collection("ratings").document(stage.id.toString()).set(newRating)

    }

}