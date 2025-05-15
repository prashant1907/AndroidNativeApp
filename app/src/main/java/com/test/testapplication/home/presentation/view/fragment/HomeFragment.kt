package com.test.testapplication.home.presentation.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.testapplication.R
import com.test.testapplication.core.AppConstants
import com.test.testapplication.core.application.MyApplication
import com.test.testapplication.core.di.DaggerViewModelFactory
import com.test.testapplication.core.network.Status
import com.test.testapplication.databinding.FragmentHomeBinding
import com.test.testapplication.home.data.Movies
import com.test.testapplication.home.presentation.view.adapter.MoviesAdapter
import com.test.testapplication.home.presentation.viewModel.HomeViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class HomeFragment : Fragment() {


    private lateinit var viewModel: HomeViewModel

    lateinit var binding: FragmentHomeBinding

    private lateinit var movesAdapter: MoviesAdapter
    private var isFragmentRestoreState = false


    @Inject
    lateinit var daggerViewModelFactory: DaggerViewModelFactory

    private fun init() {

        if (!isFragmentRestoreState) {
            viewModel.getdataInternetCheck(AppConstants.DEFAULT_SEARCH, AppConstants.API_KEY,true)
            searchLogic()
        }

        if (viewModel.serviceLiveData.hasObservers()) {
            viewModel.serviceLiveData.removeObservers(this)
        } else {
            observeRemoteNotify()
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getdataInternetCheck(AppConstants.DEFAULT_SEARCH, AppConstants.API_KEY,true)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MyApplication.appContext.appComponent.doInject(this)
        if (this::binding.isInitialized) {
            isFragmentRestoreState = true
            return binding.root
        }
        viewModel = ViewModelProvider(this, daggerViewModelFactory)[HomeViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun getLayout(): Int {
        return R.layout.fragment_home
    }

    private fun observeRemoteNotify() {
        viewModel.serviceLiveData.observe(viewLifecycleOwner) {
            viewModel.serviceLiveData.value?.let {
                stopRefreshing()
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.moviesRecycler.layoutManager =
                            GridLayoutManager(activity, 3)
                        val data = it.data as Movies
                        movesAdapter = MoviesAdapter(data.Search)
                        binding.moviesRecycler.adapter = movesAdapter
                        if (data.Search.isEmpty()) {
                            binding.errorTxt.visibility = View.VISIBLE
                        } else {
                            binding.errorTxt.visibility = View.GONE
                        }
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
                            binding.errorTxt.visibility = View.GONE
                            binding.homeShimmer.visibility = View.VISIBLE
                            binding.moviesRecycler.visibility = View.GONE
                        } else {
                            binding.homeShimmer.visibility = View.GONE
                            binding.moviesRecycler.visibility = View.VISIBLE
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


    private fun stopRefreshing() {
        if (binding.refreshLayout.isRefreshing) {
            binding.refreshLayout.isRefreshing = false
        }
    }


    private fun searchLogic() {
        viewModel.compositeDisposable.add(
            Observable.create((ObservableOnSubscribe<String> { emitter ->
                binding.searchBar.edSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        //do nothing
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0.toString().length > 3)
                            viewModel.getdataInternetCheck(p0.toString(), AppConstants.API_KEY,false)

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        if (p0.toString().isEmpty() ) {
                            viewModel.getdataInternetCheck(AppConstants.DEFAULT_SEARCH, AppConstants.API_KEY,false)

                        }
                    }

                })
            })).debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                   if(it.isNotEmpty()){
                           viewModel.getdataInternetCheck(it.toString(), AppConstants.API_KEY,false)
                   }
                })
    }


}

