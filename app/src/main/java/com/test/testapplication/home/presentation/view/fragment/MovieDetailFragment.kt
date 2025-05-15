package com.test.testapplication.home.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.testapplication.R
import com.test.testapplication.core.AppConstants
import com.test.testapplication.core.application.MyApplication
import com.test.testapplication.core.di.DaggerViewModelFactory
import com.test.testapplication.core.network.Status
import com.test.testapplication.core.util.ImageUtil
import com.test.testapplication.databinding.MovieDetailFragmentBinding
import com.test.testapplication.home.data.MovieDetail
import com.test.testapplication.home.data.Search
import com.test.testapplication.home.presentation.viewModel.HomeViewModel
import javax.inject.Inject

class MovieDetailFragment : Fragment(), View.OnClickListener {

    lateinit var binding: MovieDetailFragmentBinding

    private lateinit var viewModel: HomeViewModel

    var imdbId: String = ""

    @Inject
    lateinit var daggerViewModelFactory: DaggerViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MyApplication.appContext.appComponent.doInject(this)
        viewModel = ViewModelProvider(this, daggerViewModelFactory)[HomeViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        if (arguments != null) {
            val data: Search? = requireArguments().getParcelable(AppConstants.MOVIE_DATA)
            ImageUtil.setImage(binding.image, data?.Poster)
            imdbId = data?.imdbID ?: ""
        }
        binding.imgBack.setOnClickListener(this)
        viewModel.getMovieDetailInternetCheck(imdbId, AppConstants.API_KEY)

        if (viewModel.serviceLiveData.hasObservers()) {
            viewModel.serviceLiveData.removeObservers(this)
        } else {
            observeRemoteNotify()
        }
    }

    private fun getLayout(): Int {
        return R.layout.movie_detail_fragment
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.imgBack.id -> {
                findNavController().navigateUp()
            }
        }
    }


    private fun observeRemoteNotify() {
        viewModel.serviceLiveData.observe(viewLifecycleOwner) {
            viewModel.serviceLiveData.value?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        viewModel.setMovieData(it.data as MovieDetail)
                    }

                    Status.FAIL -> {
                        Snackbar.make(
                            binding.root, it.data.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    else -> {
                        Snackbar.make(
                            binding.root, getString(R.string.default_error),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        viewModel.notifyLiveData.observe(viewLifecycleOwner) {
            viewModel.notifyLiveData.value?.let {
                when (it.status) {
                    Status.SHOW_PROGRESS -> {
                        if (it.data == true) {
                            binding.movieCard.shimmer.root.visibility = View.VISIBLE
                        } else {
                            binding.movieCard.shimmer.root.visibility = View.GONE
                        }
                    }

                    else -> {
                        Snackbar.make(
                            binding.root, getString(R.string.default_error),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


}