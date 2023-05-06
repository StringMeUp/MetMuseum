package com.sr.metmuseum.ui.main

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseFragment
import com.sr.metmuseum.databinding.MainFragmentBinding
import com.sr.metmuseum.util.afterTextChangedEvents
import com.sr.metmuseum.util.observeNonNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
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
        setLifecycle()
        _idsAdapter = IdsAdapter { item ->
            if (item.type == MainViewModel.ObjectType.ART) {
                viewModel.setItemId(item.id)
                findNavController().navigate(MainFragmentDirections.actionDetail())
            }
        }
    }

    @OptIn(FlowPreview::class)
    override fun setUpViewBinding() {
        super.setUpViewBinding()
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    searchView.afterTextChangedEvents().debounce(350).collectLatest {
                        if (it == viewModel.savedQuery.value) return@collectLatest
                        if (it.isNotBlank()) viewModel.searchIds(it)
                        else viewModel.setDefaultInvalidate()
                    }
                }
            }

            recyclerView.adapter = idsAdapter
        }
    }

    override fun setUpViewModelBinding() {
        super.setUpViewModelBinding()
        viewModel.artItems.observeNonNull(viewLifecycleOwner) {
            idsAdapter.items = it
        }
    }

    override fun onDestroyView() {
        viewModel.saveQuery(binding.searchView.text.toString())
        super.onDestroyView()
        _idsAdapter = null
    }

    private fun setLifecycle() {
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
}