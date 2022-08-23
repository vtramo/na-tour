package com.example.natour.ui.home.trail.creation

import android.app.Activity
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.ImageView
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
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
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

        setupSlider()
        setupImagePicker()
        setupNumberPickers()
    }

    private fun setupSlider() {
        binding.slider.setLabelFormatter { labelFormatter(it) }
        binding.slider.addOnChangeListener { _, value, _ -> labelFormatter(value) }
        binding.slider.value = mTrailCreationViewModel.difficulty?.ordinal?.toFloat() ?: 0f
    }

    private fun labelFormatter(value: Float): String =
        with(binding.textDifficulty) {
            when (value) {
                0f -> {
                    setTextColor(TrailDifficulty.VERY_EASY.color)
                    TrailDifficulty.VERY_EASY.toString().also { text = it }
                }
                1f -> {
                    setTextColor(TrailDifficulty.EASY.color)
                    TrailDifficulty.EASY.toString().also { text = it }
                }
                2f -> {
                    setTextColor(TrailDifficulty.MORE_DIFFICULT.color)
                    TrailDifficulty.MORE_DIFFICULT.toString().also { text = it }
                }
                3f -> {
                    setTextColor(TrailDifficulty.VERY_DIFFICULT.color)
                    TrailDifficulty.VERY_DIFFICULT.toString().also { text = it }
                }
                else -> {
                    setTextColor(TrailDifficulty.EXTREMELY_DIFFICULT.color)
                    TrailDifficulty.EXTREMELY_DIFFICULT.toString().also { text = it }
                }
            }
        }

    private fun setupImagePicker() {
        if (mTrailCreationViewModel.image != null) {
            binding.iconUploadImageView.visibility = View.GONE
        }
    }

    private fun setupNumberPickers() {
        with(binding) {
            minutesNumberPicker.minValue = 0
            minutesNumberPicker.maxValue = 59
            minutesNumberPicker.value = mTrailCreationViewModel.minutes!!
            hoursNumberPicker.minValue = 0
            hoursNumberPicker.maxValue = 23
            hoursNumberPicker.value = mTrailCreationViewModel.hours!!
            daysNumberPicker.minValue = 0
            daysNumberPicker.maxValue = 30
            daysNumberPicker.value = mTrailCreationViewModel.days!!
            monthsNumberPicker.minValue = 0
            monthsNumberPicker.maxValue = 12
            monthsNumberPicker.value = mTrailCreationViewModel.months!!
        }
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

            val progressDialog = createProgressAlertDialog(
                "Uploading the photo...",
                requireContext()
            )
            progressDialog.show()

            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data!!.data!!

                detectIllegalContentImage(imageUri) { isIllegalImage ->
                    if (isIllegalImage) {
                        showIllegalContentImageAlertDialog()
                    } else {
                        setImageFromUri(imageUri)
                    }
                    progressDialog.dismiss()
                }
            }
        }


    private fun detectIllegalContentImage(trailImageUri: Uri, reaction: (Boolean) -> (Unit)) =
        lifecycleScope.launch(Dispatchers.IO) {
            mIllegalContentImageDetector
                .detectIllegalContent(trailImageUri.path!!).apply {
                    withContext(Dispatchers.Main) {
                        observe(viewLifecycleOwner) { isIllegalImage -> reaction(isIllegalImage) }
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

    private fun setImageFromUri(uri: Uri) {
        binding.iconUploadImageView.visibility = View.GONE
        binding.uploadImageButton.setDrawableFromImageUri(uri)
    }

    private fun ImageView.setDrawableFromImageUri(imageUri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val drawable = Drawable.createFromStream(inputStream, imageUri.toString())
        setImageDrawable(drawable)
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
                difficulty  = TrailDifficulty.toEnumValue(slider.value.toInt())
                minutes     = minutesNumberPicker.value
                hours       = hoursNumberPicker.value
                days        = daysNumberPicker.value
                months      = monthsNumberPicker.value
                description = descriptionEditText.text?.toString() ?: ""
                image       = uploadImageButton.drawable
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
            checkImage()

    private fun checkImage(): Boolean {
        val isValidImage = binding.uploadImageButton.drawable != null
        if (!isValidImage) showAlertDialog("Please upload a photo")
        return isValidImage
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
        val minutes = binding.minutesNumberPicker.value
        val hours   = binding.hoursNumberPicker.value
        val days    = binding.daysNumberPicker.value
        val months  = binding.monthsNumberPicker.value
        val isValidDuration = isValidDuration(minutes, hours, days, months)
        if (!isValidDuration) showAlertDialog("Invalid duration")
        return isValidDuration
    }

    private fun isValidDuration(minutes: Int, hours: Int, days: Int, months: Int) =
        (minutes != 0 || hours != 0 || days != 0 || months != 0)

    private fun showAlertDialog(title: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun TextInputEditText.textString()      = text!!.toString()

    fun onBackClick() {
        view?.findNavController()?.popBackStack()
    }
}