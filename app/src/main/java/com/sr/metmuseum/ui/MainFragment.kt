package com.sr.metmuseum.ui

import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseFragment
import com.sr.metmuseum.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<MainFragmentBinding>(MainFragmentBinding::inflate) {

    override fun inflateBinding(): Class<MainFragmentBinding> = MainFragmentBinding::class.java
    override fun setContent(): Int = R.layout.main_fragment

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