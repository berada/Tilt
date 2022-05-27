package hu.bme.aut.android.tilt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.tilt.databinding.FragmentCreateBinding
import hu.bme.aut.android.tilt.extensions.validateNonEmpty
import hu.bme.aut.android.tilt.model.Stage

@Suppress("DEPRECATION")
class CreateFragment : BaseFragment() {
    companion object {
        private const val REQUEST_CODE = 101
    }
    private lateinit var binding: FragmentCreateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        var newId: String = "Error"
        val db = Firebase.firestore
        db.collection("maxId").document("maxId").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    newId = (document.get("maxId").toString().toInt() + 1).toString()
                    binding.tvId.text = newId
                }
            }

        binding = FragmentCreateBinding.inflate(layoutInflater, container, false)
        binding.btnSend.setOnClickListener { adStageClick() }

        return binding.root
    }

    private fun adStageClick() {
        if (!validateForm()) {
            return
        }
        uploadStage()
    }

    private fun validateForm() = binding.etStageString.validateNonEmpty()

    private fun uploadStage() {
        var newId : String = binding.tvId.text.toString()
        val newStage = Stage(newId.toLong(), 0, 0, "NA",binding.etStageString.text.toString())
        val db = Firebase.firestore

        //maxId növelése
        val maxId = hashMapOf(
            "maxId" to newId.toInt(),
        )
        db.collection("maxId").document("maxId").set(maxId)

        //új stage felvétele az adatbázisba
        db.collection("stages")
            .document(newId).set(newStage)
            .addOnSuccessListener { toast("Stage created") }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

}