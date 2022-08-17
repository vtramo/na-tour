package com.example.natour.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.natour.R
import com.example.natour.databinding.FragmentHomeBinding
import com.example.natour.ui.MainUserViewModel
import com.example.natour.ui.home.trail.detail.TrailDetailsViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mMainUserViewModel: MainUserViewModel by activityViewModels()
    private val mHomeViewModel: HomeViewModel by hiltNavGraphViewModels(R.id.home_nav_graph)
    private val mTrailDetailsViewModel: TrailDetailsViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSwipeRefreshLayout()

        return binding.root
    }

    private fun setupRecyclerView() {
        mRecyclerView = binding.recyclerViewHome
        val trailListAdapter = TrailListAdapter { trailClicked ->
            mTrailDetailsViewModel.thisTrail = trailClicked
            goToTrailDetailsFragment()
        }
        mRecyclerView.adapter = trailListAdapter

        addOnFinishedTrailsRecyclerViewListener()

        mHomeViewModel.trails.observe(viewLifecycleOwner) { listTrails ->
            trailListAdapter.submitList(listTrails)
        }
    }

    private fun goToTrailDetailsFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToTrailDetailFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun addOnFinishedTrailsRecyclerViewListener() {
        mRecyclerView.addOnScrollListener(object : OnScrollListener() {
            private val DIRECTION_DOWN = 1

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (recyclerView.canScrollVertically(DIRECTION_DOWN)) return
                if (mHomeViewModel.isLoadingTrails) return
                if (mHomeViewModel.pagesAreFinished) return

                loadNextTrailPage()
            }
        })
    }

    private fun loadNextTrailPage() {
        mHomeViewModel.currentPage++
        mHomeViewModel.loadTrails()
    }

    private fun setupSwipeRefreshLayout() {
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            mHomeViewModel.isRefreshingLiveData.observe(viewLifecycleOwner) {
                swipeRefreshLayout.isRefreshing = it
            }
            mHomeViewModel.refreshTrails()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authenticationViewModel = mMainUserViewModel
        binding.homeFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    fun goToTrailStartCreationFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToTrailStartCreationFragment()
        view?.findNavController()?.navigate(action)
    }

    fun onLogout() {
        mMainUserViewModel.logout()
        goToLoginFragment()
    }

    private fun goToLoginFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
        view?.findNavController()?.navigate(action)
    }
}