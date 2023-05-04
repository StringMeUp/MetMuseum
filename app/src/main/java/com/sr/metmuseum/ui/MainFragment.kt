package com.sr.metmuseum.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseFragment
import com.sr.metmuseum.databinding.MainFragmentBinding
import com.sr.metmuseum.util.afterTextChangedEvents
import com.sr.metmuseum.util.hide
import com.sr.metmuseum.util.observeNonNull
import com.sr.metmuseum.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.skip
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<MainFragmentBinding>(MainFragmentBinding::inflate) {

    override fun inflateBinding(): Class<MainFragmentBinding> = MainFragmentBinding::class.java
    override fun setContent(): Int = R.layout.main_fragment
    private val viewModel: MainViewModel by activityViewModels()

    private var _idsAdapter: IdsAdapter? = null
    private val idsAdapter: IdsAdapter
        get() = _idsAdapter!!

    override fun setUpView() {
        super.setUpView()
        _idsAdapter = IdsAdapter()
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    @OptIn(FlowPreview::class)
    override fun setUpViewBinding() {
        super.setUpViewBinding()
        binding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    searchView.afterTextChangedEvents().debounce(350)
                        .collectLatest {
                            if (it.isNotBlank()) viewModel.searchIds(it.toString())
                            else viewModel.invalidateSearch()
                        }
                }
            }

            recyclerView.adapter = idsAdapter
        }
    }

    override fun setUpViewModelBinding() {
        super.setUpViewModelBinding()
        viewModel.objectIds.observeNonNull(viewLifecycleOwner) {
            idsAdapter.differ.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _idsAdapter = null
    }
}