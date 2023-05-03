package com.sr.metmuseum.ui

import androidx.fragment.app.activityViewModels
import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseFragment
import com.sr.metmuseum.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<MainFragmentBinding>(MainFragmentBinding::inflate) {

    override fun inflateBinding(): Class<MainFragmentBinding> = MainFragmentBinding::class.java
    override fun setContent(): Int = R.layout.main_fragment
    private val viewModel: MainViewModel by activityViewModels()

    override fun setUpView() {
        super.setUpView()
        viewModel.getIds()
    }

    override fun setUpViewBinding() {
        super.setUpViewBinding()
    }

    override fun setUpViewModelBinding() {
        super.setUpViewModelBinding()
    }
}