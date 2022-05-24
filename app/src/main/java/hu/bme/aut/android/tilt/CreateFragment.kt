package hu.bme.aut.android.tilt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.tilt.databinding.FragmentCreateBinding
import hu.bme.aut.android.tilt.extensions.validateNonEmpty
import hu.bme.aut.android.tilt.model.Stage

class CreateFragment : BaseFragment() {
    companion object {
        private const val REQUEST_CODE = 101
    }
    private lateinit var binding: FragmentCreateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
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

    private fun validateForm() = binding.etStageString.validateNonEmpty() && binding.etId.validateNonEmpty()

    private fun uploadStage() {
        val newStage = Stage(binding.etId.text.toString().toLong(), 0, 0, "",binding.etStageString.text.toString())

        val db = Firebase.firestore

        db.collection("stages")
            .add(newStage)
            .addOnSuccessListener { toast("Stage created") }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

}