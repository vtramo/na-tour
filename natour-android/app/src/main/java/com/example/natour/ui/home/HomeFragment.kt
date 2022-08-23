package com.example.natour.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.natour.MainActivity.Companion.getDrawable
import com.example.natour.R
import com.example.natour.data.model.Trail
import com.example.natour.databinding.FragmentHomeBinding
import com.example.natour.exceptions.ErrorMessages
import com.example.natour.ui.MainUserViewModel
import com.example.natour.ui.home.trail.detail.TrailDetailsViewModel
import com.example.natour.ui.home.trail.favorites.FavoriteTrailChanger
import com.example.natour.ui.home.trail.favorites.FavoriteTrailsViewModel
import com.example.natour.ui.home.user.UserDetailsDialogFragment
import com.example.natour.util.LateTask
import com.example.natour.util.PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE
import com.example.natour.util.PermissionUtils.requestLocationPermissions
import com.example.natour.util.showErrorAlertDialog
import com.example.natour.util.showSomethingWentWrongAlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLocationPermissions(
            requireActivity() as AppCompatActivity,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupMainUser()
        loadTrails()

        return binding.root
    }

    private fun setupMainUser() {
        with(mMainUserViewModel) {
            if (mainUser.value == null) loadMainUser()
        }
    }

    fun loadTrails() {
        binding.connectionErrorLinearLayout.visibility = View.GONE
        binding.progressBarRecyclerView.visibility = View.VISIBLE

        with(mFavoriteTrailsViewModel) {
            observePossibleErrors()
            observeLoadingFavoriteTrails()
            loadFavoriteTrails()
        }
    }

    private fun FavoriteTrailsViewModel.observePossibleErrors() {
        connectionErrorLiveData.observe(viewLifecycleOwner) { isConnectionError ->
            if (isConnectionError) {
                binding.progressBarRecyclerView.visibility = View.GONE
                binding.connectionErrorLinearLayout.visibility = View.VISIBLE
                showSomethingWentWrongAlertDialog(
                    ErrorMessages.CONNECTION_ERROR,
                    ErrorMessages.CONNECTION_ERROR_SERVER,
                    requireContext()
                )
            }
        }
    }

    private fun FavoriteTrailsViewModel.observeLoadingFavoriteTrails() {
        hasLoadedFavoriteTrailsLiveData.observe(viewLifecycleOwner) {
            with(mHomeViewModel) {
                firstLoadFinishedLiveData.observe(viewLifecycleOwner) {
                    binding.connectionErrorLinearLayout.visibility = View.GONE
                    setupToolbarTitle()
                    setupBottomHomeMenu()
                    setupRecyclerView()
                    setupSwipeRefreshLayout()
                    binding.progressBarRecyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun setupToolbarTitle() {
        val toolbarTitleTextView = binding.toolbarTitleTextView
        with(mHomeViewModel) {
            if (isOnHome) {
                toolbarTitleTextView.text = getString(R.string.explore_new_trails)
            } else {
                toolbarTitleTextView.text = getString(R.string.your_favorite_trails)
            }
        }
    }

    private fun setupBottomHomeMenu() {
        with(mHomeViewModel) {
            if (isOnHome) {
                highlightHomeButton()
            } else {
                highlightFavoriteTrailsButton()
            }
        }
    }

    private fun setupRecyclerView() {
        mRecyclerView = binding.recyclerViewHome
        mTrailListAdapter = setupTrailListAdapter()
        mRecyclerView.adapter = mTrailListAdapter

        addOnFinishedTrailsRecyclerViewListener()

        observeTrailListChanges()
        observeMapOfFavoriteTrailsChanges()
    }

    private fun setupTrailListAdapter(): TrailListAdapter =
         TrailListAdapter(
            trailCardClickListener = { trailClicked ->
                mTrailDetailsViewModel.thisTrail = trailClicked
                goToTrailDetailsFragment()
            },
            favoriteTrailClickListener = { trailClicked, heartImageButton ->
                val favoriteTrailChanger =
                    FavoriteTrailChanger(
                        heartImageButton,
                        trailClicked,
                        mFavoriteTrailsViewModel::addFavoriteTrail,
                        mFavoriteTrailsViewModel::removeFavoriteTrail,
                        mFavoriteTrailsViewModel.favoriteTrailSuccessfullyChangedLiveData,
                        getDrawable(R.drawable.heart_white_red)!!,
                        getDrawable(R.drawable.heart_white_transparent)!!
                    )
                favoriteTrailChanger.run().observe(viewLifecycleOwner) { isSuccess ->
                    if (!isSuccess) {
                        showErrorAlertDialog(
                            "It is currently not possible to perform this operation",
                            requireContext()
                        )
                    }
                }
            }
        )

    private fun observeTrailListChanges() = with(mHomeViewModel) {
        trails.observe(viewLifecycleOwner) { listTrails ->
            if (!isOnHome) return@observe
            mTrailListAdapter.submitList(listTrails)
        }
    }

    private fun observeMapOfFavoriteTrailsChanges() = with(mHomeViewModel) {
        mFavoriteTrailsViewModel.mapOfFavoriteTrails.observe(viewLifecycleOwner) { mapTrails ->
            updateFavoriteTrailsInTheList(mapTrails)
            if (isOnHome) return@observe
            setThereAreNoFavoriteTrailsTextView(mapTrails.isEmpty())
            mTrailListAdapter.submitList(mapTrails.values.toList())
        }
    }

    private fun setThereAreNoFavoriteTrailsTextView(empty: Boolean) {
        with(binding.thereAreNoFavoriteTrailsTextView) {
            if (!empty) {
                visibility = View.GONE
            } else {
                val lateTask = LateTask(delayMs = 100) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) { visibility = View.VISIBLE }
                    }
                }
                lateTask.start()
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
        setThereAreNoFavoriteTrailsTextView(false)
        highlightHomeButton()
        binding.toolbarTitleTextView.text = getString(R.string.explore_new_trails)
        safeSubmitListToTrailListAdapter(
            mHomeViewModel.trails.value ?: listOf(),
        )
    }

    private fun highlightHomeButton() {
        with(binding) {
            homeImageButton.setImageDrawable(getDrawable(R.drawable.home_black))
            heartImageButton.setImageDrawable(getDrawable(R.drawable.heart_dark_gray))
        }
    }

    fun onFavoriteTrailsClick() {
        mHomeViewModel.isOnHome = false
        highlightFavoriteTrailsButton()
        binding.toolbarTitleTextView.text = getString(R.string.your_favorite_trails)
        safeSubmitListToTrailListAdapter(mFavoriteTrailsViewModel.listOfFavoriteTrails)
    }

    private fun highlightFavoriteTrailsButton() {
        with(binding) {
            homeImageButton.setImageDrawable(getDrawable(R.drawable.home_dark_gray))
            heartImageButton.setImageDrawable(getDrawable(R.drawable.heart_black))
        }
    }

    private fun safeSubmitListToTrailListAdapter(listOfTrails: List<Trail>) {
        if (this@HomeFragment::mTrailListAdapter.isInitialized) {
            mTrailListAdapter.submitList(listOfTrails) {
                setThereAreNoFavoriteTrailsTextView(listOfTrails.isEmpty())
            }
        }
    }
}