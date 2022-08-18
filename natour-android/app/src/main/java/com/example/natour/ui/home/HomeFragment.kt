package com.example.natour.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.databinding.FragmentHomeBinding
import com.example.natour.ui.MainUserViewModel
import com.example.natour.ui.home.trail.detail.TrailDetailsViewModel
import com.example.natour.ui.home.trail.favorites.FavoriteTrailsViewModel
import com.example.natour.ui.home.user.UserDetailsDialogFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mMainUserViewModel: MainUserViewModel by activityViewModels()

    private val mHomeViewModel: HomeViewModel by hiltNavGraphViewModels(R.id.home_nav_graph)

    private val mTrailDetailsViewModel: TrailDetailsViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    private val mFavoriteTrailsViewModel: FavoriteTrailsViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mTrailListAdapter: TrailListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupBottomHomeMenu()
        setupMainUser()
        mFavoriteTrailsViewModel.loadFavoriteTrails()
        setupRecyclerView()
        setupSwipeRefreshLayout()

        return binding.root
    }

    private fun setupBottomHomeMenu() {
        with(mHomeViewModel) {
            with(binding) {
                if (isOnHome) {
                    highlightHomeButton()
                } else {
                    highlightFavoriteTrailsButton()
                }
            }
        }
    }

    private fun setupMainUser() {
        with(mMainUserViewModel) {
            if (mainUser.value == null) loadMainUser()
        }
    }

    private fun setupRecyclerView() {
        mRecyclerView = binding.recyclerViewHome
        val trailListAdapter = TrailListAdapter { trailClicked ->
            mTrailDetailsViewModel.thisTrail = trailClicked
            goToTrailDetailsFragment()
        }
        mTrailListAdapter = trailListAdapter
        mRecyclerView.adapter = trailListAdapter

        addOnFinishedTrailsRecyclerViewListener()

        with(mHomeViewModel) {
            trails.observe(viewLifecycleOwner) { listTrails ->
                if (!isOnHome) return@observe
                trailListAdapter.submitList(listTrails, isFavoriteList = false)
            }

            mFavoriteTrailsViewModel.mapOfFavoriteTrails.observe(viewLifecycleOwner) { mapTrails ->
                if (isOnHome) return@observe
                trailListAdapter.submitList(mapTrails.values.toList(), isFavoriteList = true)
            }
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

    fun onUserDetailsClick() {
        showUserDetailsDialogFragment()
    }

    private fun showUserDetailsDialogFragment() {
        UserDetailsDialogFragment().show(
            childFragmentManager,
            UserDetailsDialogFragment.TAG
        )
    }

    fun onHomeClick() {
        mHomeViewModel.isOnHome = true
        highlightHomeButton()
        mTrailListAdapter.submitList(
            mHomeViewModel.trails.value!!,
            isFavoriteList = false
        )
    }

    private fun highlightHomeButton() {
        with(binding) {
            homeImageButton.setImageDrawable(MainActivity.getDrawable(R.drawable.home_black))
            heartImageButton.setImageDrawable(MainActivity.getDrawable(R.drawable.heart_dark_gray))
        }
    }

    fun onFavoriteTrailsClick() {
        mHomeViewModel.isOnHome = false
        highlightFavoriteTrailsButton()
        mTrailListAdapter.submitList(
            mFavoriteTrailsViewModel.mapOfFavoriteTrails.value!!.values.toList(),
            isFavoriteList = true
        )
    }

    private fun highlightFavoriteTrailsButton() {
        with(binding) {
            homeImageButton.setImageDrawable(MainActivity.getDrawable(R.drawable.home_dark_gray))
            heartImageButton.setImageDrawable(MainActivity.getDrawable(R.drawable.heart_black))
        }
    }
}