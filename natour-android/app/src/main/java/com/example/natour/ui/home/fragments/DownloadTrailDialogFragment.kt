package com.example.natour.ui.home.fragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.example.natour.R
import com.example.natour.databinding.DialogFragmentDownloadTrailBinding
import com.example.natour.databinding.PdfTrailDetailsBinding
import com.example.natour.ui.home.viewmodels.TrailDetailsViewModel
import io.jenetics.jpx.GPX

class DownloadTrailDialogFragment: DialogFragment() {

    private val mTrailDetailsViewModel: TrailDetailsViewModel
            by hiltNavGraphViewModels(R.id.home_nav_graph)

    private var _binding: DialogFragmentDownloadTrailBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentDownloadTrailBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding.downloadTrailDialogFragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }


    fun onDownloadPdfClick() {
        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_TITLE, getTrailFileTitle() + ".pdf")
            type = "application/pdf"
            pdfLauncher.launch(this)
        }
    }

    private fun getTrailFileTitle() =
        mTrailDetailsViewModel
            .thisTrail
            .name
            .replace(" ", "_")

    private val pdfLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result?.data?.data?.also { uri ->
                    val pdfDocument = createTrailDetailsPdfDocument()
                    pdfDocument.writeToFile(uri)
                    pdfDocument.close()
                    mTrailDetailsViewModel.trailDownloadedSuccessfullyLiveData.value =
                        TrailDownloadResult.SUCCESS
                }
            } else mTrailDetailsViewModel.trailDownloadedSuccessfullyLiveData.value = TrailDownloadResult.FAIL

            dismiss()
        }

    private fun createTrailDetailsPdfDocument(): PdfDocument {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(
            1500,
            2400,
            1
        ).create()
        val page = pdfDocument.startPage(pageInfo)
        val pdfTrailDetailsView = createTrailDetailsPdfView()
        pdfTrailDetailsView.draw(page.canvas)
        pdfDocument.finishPage(page)
        return pdfDocument
    }

    private fun createTrailDetailsPdfView(): View =
        DataBindingUtil.inflate<PdfTrailDetailsBinding>(
            layoutInflater,
            R.layout.pdf_trail_details,
            null,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            with(mTrailDetailsViewModel) {
                trailNameTextView.text = thisTrail.name
                trailPositionDetailsTextView.text = positionDetails
                trailDurationTextView.text = trailDuration
                trailDifficultyTextView.text = thisTrail.difficulty.toString()
                trailStartingPointsDetailsTextView.text = startingPointDetails
            }
        }.root.apply {
            measure(1500, 2400)
            layout(0, 0, 1500, 2400)
        }

    private fun PdfDocument.writeToFile(uri: Uri) {
        val fOut = requireContext().contentResolver.openOutputStream(uri)
        writeTo(fOut)
        fOut?.close()
    }


    fun onDownloadGpxClick() {
        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            putExtra(Intent.EXTRA_TITLE, getTrailFileTitle() + ".gpx")
            type = "application/gpx+xml"
            gpxLauncher.launch(this)
        }
    }

    private val gpxLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result?.data?.data?.also { uri ->
                    val gpx = GPX.builder()
                        .addTrack { track ->
                            track.addSegment { segment ->
                                mTrailDetailsViewModel.listOfRoutePoints.forEach { routePoint ->
                                    segment.addPoint { p ->
                                        p.lat(routePoint.latitude).lon(routePoint.longitude)
                                    }
                                }
                            }
                        }.build()
                    gpx.writeToFile(uri)
                    mTrailDetailsViewModel.trailDownloadedSuccessfullyLiveData.value =
                        TrailDownloadResult.SUCCESS
                }
            } else mTrailDetailsViewModel.trailDownloadedSuccessfullyLiveData.value = TrailDownloadResult.FAIL
            dismiss()
        }

    private fun GPX.writeToFile(uri: Uri) {
        val fOut = requireContext().contentResolver.openOutputStream(uri)
        GPX.write(this, fOut)
        fOut?.close()
    }

    override fun onDismiss(dialog: DialogInterface) {
        with(mTrailDetailsViewModel) {
            if (trailDownloadedSuccessfullyLiveData.value == TrailDownloadResult.NOT_SET) {
                mTrailDetailsViewModel.trailDownloadedSuccessfullyLiveData.value = TrailDownloadResult.DISMISS
            }
            resetTrailDownloadedSuccessfullyLiveData()
        }
        dismiss()
    }
}