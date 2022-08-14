package com.example.natour.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentTrailPhotoBinding
import com.example.natour.ui.home.viewmodels.TrailDetailsViewModel
import com.igreenwood.loupe.Loupe

class TrailPhotoFragment: Fragment() {

    private val mTrailDetailsViewModel: TrailDetailsViewModel
            by hiltNavGraphViewModels(R.id.home_nav_graph)

    private var _binding: FragmentTrailPhotoBinding? = null
    private val binding get() = _binding!!

    private lateinit var mImageView: ImageView
    private lateinit var mFrameLayout: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrailPhotoBinding.inflate(inflater, container, false)

        binding.trailDetailsViewModel = mTrailDetailsViewModel
        binding.trailPhotoFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mImageView = binding.image
        mFrameLayout = binding.container

        Loupe.create(mImageView, mFrameLayout) {
            onViewTranslateListener = object : Loupe.OnViewTranslateListener {

                override fun onStart(view: ImageView) {
                    // called when the view starts moving
                }

                override fun onViewTranslate(view: ImageView, amount: Float) {
                    // called whenever the view position changed
                }

                override fun onRestore(view: ImageView) {
                    // called when the view drag gesture ended
                }

                override fun onDismiss(view: ImageView) {
                    view.findNavController().popBackStack()
                }
            }
        }
    }

    fun onGpsTrailPhotoClick() {
        mTrailDetailsViewModel.gpsTrailPhotoButtonClicked = true
        view?.findNavController()?.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}