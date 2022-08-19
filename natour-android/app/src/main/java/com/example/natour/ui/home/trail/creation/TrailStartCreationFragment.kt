package com.example.natour.ui.home.trail.creation

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.data.model.TrailDifficulty
import com.example.natour.databinding.FragmentTrailStartCreationBinding
import com.example.natour.network.IllegalContentImageDetectorApiService
import com.example.natour.util.ConstantRegex
import com.example.natour.util.createProgressAlertDialog
import com.example.natour.util.showCustomAlertDialog
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class TrailStartCreationFragment : Fragment() {

    private var _binding: FragmentTrailStartCreationBinding? = null
    private val binding get() = _binding!!

    private val mTrailCreationViewModel: TrailCreationViewModel
        by hiltNavGraphViewModels(R.id.trail_creation_nav_graph)

    @Inject
    lateinit var mIllegalContentImageDetector: IllegalContentImageDetectorApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_trail_start_creation,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.trailStartCreationFragment = this
        binding.trailStartCreationViewModel = mTrailCreationViewModel

        setupCustomBackToolbar()
        setupRouteDifficultyDropDownList()
        setupTextChangedListeners()
    }

    private fun setupCustomBackToolbar(){
        val toolbar = binding.customToolbarRouteCreation
        toolbar.setNavigationIcon(R.drawable.ic_back_40)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupRouteDifficultyDropDownList() {
        val difficultyAutoComplete = binding.difficultyAutoCompleteTextView
        difficultyAutoComplete.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item_difficulty,
                resources.getStringArray(R.array.listDifficulties)
            )
        )
        mTrailCreationViewModel.difficulty?.let { trailDifficulty ->
            difficultyAutoComplete.setText(trailDifficulty.toString(), false)
        }
    }

    private fun setupTextChangedListeners() {
        binding.trailNameTextInputEditText.addTextChangedListener { checkTrailName() }
    }

    fun onSelectImageClick() {
        ImagePicker.with(this)
            .crop()
            .createIntent { intent ->
                getImageLauncher.launch(intent)
            }
    }

    private val getImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data!!.data!!
                checkContentImageBeforeUpload(imageUri)
            }
        }

    private fun checkContentImageBeforeUpload(imageUri: Uri) {
        val progressDialog = createProgressAlertDialog("Uploading the photo...", requireContext())
        progressDialog.show()
        lifecycleScope.launch(Dispatchers.IO) {
            mIllegalContentImageDetector
                .detectIllegalContent(imageUri.path!!).apply {
                    withContext(Dispatchers.Main) {
                        observe(viewLifecycleOwner) { isIllegalImage ->
                            if (isIllegalImage) showIllegalContentImageAlertDialog()
                            else binding.uploadImageButton.setDrawableFromImageUri(imageUri)
                            progressDialog.dismiss()
                        }
                    }
                }
        }
    }

    private fun showIllegalContentImageAlertDialog() {
        showCustomAlertDialog(
            "Image with illegal content",
            "The image contains explicit or suggestive adult content, " +
                    "or violent content.",
            requireContext()
        )
    }

    private fun ImageButton.setDrawableFromImageUri(imageUri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val drawable = Drawable.createFromStream(inputStream, imageUri.toString())
        background = drawable
    }

    fun onConfirmButtonClick() {
        if (!isValidForm()) return
        setFormToViewModelProperties()
        goToTrailTypeCreationFragment()
    }

    private fun setFormToViewModelProperties() {
        with(mTrailCreationViewModel) {
            with(binding) {
                trailName   = trailNameTextInputEditText.textString()
                difficulty  = TrailDifficulty.toEnumValue(difficultyAutoCompleteTextView.textString())
                minutes     = Integer.parseInt(minutesTextInputEditText.textString())
                hours       = Integer.parseInt(hoursTextInputEditText.textString())
                days        = Integer.parseInt(daysTextInputEditText.textString())
                months      = Integer.parseInt(monthsTextInputEditText.textString())
                description = descriptionEditText.text?.toString() ?: ""
                image       = uploadImageButton.background
            }
        }
    }

    private fun goToTrailTypeCreationFragment() {
        val action = TrailStartCreationFragmentDirections
            .actionTrailCreationFragmentToTrailTypeCreationFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun isValidForm() =
        checkTrailName()    &&
            checkDuration()     &&
            checkDifficulty()   &&
            checkImage()

    private fun checkImage(): Boolean {
        val isValidImage = isValidImage(binding.uploadImageButton.background)
        if (!isValidImage) showAlertDialog("Please upload a photo")
        return isValidImage
    }

    private fun isValidImage(drawable: Drawable) =
        drawable != MainActivity.getDrawable(R.drawable.ic_baseline_image_24)

    private fun checkDifficulty(): Boolean {
        val isValidDifficulty = binding.difficultyAutoCompleteTextView.text!!.isNotBlank()
        binding.difficultyTextInputLayout.setError(
            isValidDifficulty,
            if (isValidDifficulty) null else "A difficulty is required"
        )
        return isValidDifficulty
    }

    private fun checkTrailName(): Boolean {
        val trailName = binding.trailNameTextInputEditText.textString()
        val isValidTrailName = isValidTrailName(trailName)
        binding.trailNameTextInputLayout.setError(
            isValidTrailName.also { if (!it) showAlertDialog("Invalid trail name") },
            if (isValidTrailName) null else "Invalid name"
        )
        return isValidTrailName
    }

    private fun isValidTrailName(trailName: String) =
        trailName.isNotBlank() && trailName.matches(ConstantRegex.NAME_REGEX)

    private fun TextInputLayout.setError(error: Boolean, errorMessage: String? = null) {
        this.isErrorEnabled = error
        this.error = errorMessage
    }

    private fun checkDuration(): Boolean {
        val minutes = binding.minutesTextInputEditText.textString()
        val hours   = binding.hoursTextInputEditText.textString()
        val days    = binding.daysTextInputEditText.textString()
        val months  = binding.monthsTextInputEditText.textString()
        val isValidDuration = isValidDuration(minutes, hours, days, months)
        if (!isValidDuration) showAlertDialog("Invalid duration")
        return isValidDuration
    }

    private fun isValidDuration(minutes: String, hours: String, days: String, months: String) =
        minutes.matches("^[0-9]\$|^[1-5][0-9]?\$|^60\$".toRegex()) &&
        hours.matches("^[0-9]\$|^[1-2][0-4]?\$".toRegex()) &&
        days.matches("^[0-9]$|^[1-2][0-9]?$|^30$".toRegex()) &&
        months.matches("^[0-9]\$|^[1-9][0-9]?\$".toRegex()) &&
                (minutes.matches("^[^0]".toRegex()) || hours.matches("^[^0]".toRegex()) ||
                        days.matches("^[^0]".toRegex()) || months.matches("^[^0].*".toRegex()))

    private fun showAlertDialog(title: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun TextInputEditText.textString()      = text!!.toString()
    private fun AutoCompleteTextView.textString()   = text!!.toString()
}