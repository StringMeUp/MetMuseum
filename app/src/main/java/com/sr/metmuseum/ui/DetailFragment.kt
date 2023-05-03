package com.sr.metmuseum.ui

import androidx.fragment.app.activityViewModels
import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseFragment
import com.sr.metmuseum.databinding.DetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailFragmentBinding>(DetailFragmentBinding::inflate) {

    override fun inflateBinding(): Class<DetailFragmentBinding> = DetailFragmentBinding::class.java
    override fun setContent(): Int = R.layout.detail_fragment
    private val viewModel: MainViewModel by activityViewModels()

    override fun setUpView() {
        super.setUpView()
    }

    override fun setUpViewBinding() {
        super.setUpViewBinding()
    }

    override fun setUpViewModelBinding() {
        super.setUpViewModelBinding()
    }
}