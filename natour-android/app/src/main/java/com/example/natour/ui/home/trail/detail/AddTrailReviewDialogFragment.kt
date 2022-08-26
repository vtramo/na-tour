package com.example.natour.ui.home.trail.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.example.natour.R
import com.example.natour.databinding.DialogFragmentAddTrailReviewBinding
import com.example.natour.ui.MainUserViewModel
import com.example.natour.util.createProgressAlertDialog
import com.example.natour.util.showErrorAlertDialog
import java.text.SimpleDateFormat
import java.util.*

class AddTrailReviewDialogFragment: DialogFragment() {

    private val mMainUserViewModel: MainUserViewModel by activityViewModels()
    private val mTrailDetailsViewModel: TrailDetailsViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    private lateinit var mListOfStarImageView: List<ImageView>
    private var stars = 0

    private var _binding: DialogFragmentAddTrailReviewBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "AddTrailReviewDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentAddTrailReviewBinding.inflate(layoutInflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainUserViewModel = mMainUserViewModel
        binding.addTrailReviewDialogFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogWindow()
        addStarImageViewsToList()
    }

    private fun addStarImageViewsToList() {
        with(binding) {
            mListOfStarImageView = listOf(
                starOneImageView,
                starTwoImageView,
                starThreeImageView,
                starFourImageView,
                starFiveImageView
            )
        }
    }

    private fun setupDialogWindow() {
        val dialog = dialog!!
        val window = dialog.window!!
        dialog.setContentView(binding.root)
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        val params = window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = params
        window.setGravity(Gravity.BOTTOM)
    }

    fun onStarClick(stars: Int) {
        resetStars()
        setStars(total = stars)
        binding.submitButton.isEnabled = true
    }

    private fun setStars(total: Int) {
        stars = total
        for (i in 0 until total) {
            mListOfStarImageView[i].setImageResource(R.drawable.yellow_star_image)
        }
    }

    private fun resetStars() {
        with(binding) {
            val emptyStar = R.drawable.empty_star_image
            starOneImageView.setImageResource(emptyStar)
            starTwoImageView.setImageResource(emptyStar)
            starThreeImageView.setImageResource(emptyStar)
            starFourImageView.setImageResource(emptyStar)
            starFiveImageView.setImageResource(emptyStar)
        }
    }

    fun onSubmitClick() {
        val date = getCurrentDate()
        val description = binding.reviewTextInputEditText.text!!.toString()

        val progressDialog = createProgressAlertDialog("Creating a review...", requireContext())
        progressDialog.show()
        with(mTrailDetailsViewModel) {
            reviewSuccessfullyAddedLiveData.observe(viewLifecycleOwner) { reviewSuccessfullyAdded ->
                if (!reviewSuccessfullyAdded) {
                    showErrorAlertDialog(
                        "An error occurred in trying to save the review. Please try again.",
                        requireContext()
                    )
                }
                progressDialog.dismiss()
                dismiss()
            }
            addReview(description, stars, date)
        }
    }

    private fun getCurrentDate(): String = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    ).format(Calendar.getInstance().time)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}