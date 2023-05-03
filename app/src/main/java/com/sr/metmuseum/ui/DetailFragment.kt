package com.sr.metmuseum.ui

import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseFragment
import com.sr.metmuseum.databinding.DetailFragmentBinding

class DetailFragment : BaseFragment<DetailFragmentBinding>(DetailFragmentBinding::inflate) {

    override fun inflateBinding(): Class<DetailFragmentBinding> = DetailFragmentBinding::class.java
    override fun setContent(): Int = R.layout.detail_fragment

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