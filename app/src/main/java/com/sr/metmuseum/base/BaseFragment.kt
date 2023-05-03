package com.sr.metmuseum.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    open fun setUpView() {}
    open fun setUpViewBinding() {}
    open fun setUpViewModelBinding() {}

    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    protected abstract fun inflateBinding(): Class<VB>
    protected abstract fun setContent(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initViewBinding(inflater, container)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpViewBinding()
        setUpViewModelBinding()
    }

    private fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = inflate.invoke(inflater, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}