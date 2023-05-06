package com.sr.metmuseum.ui.detail

import android.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseFragment
import com.sr.metmuseum.databinding.DetailFragmentBinding
import com.sr.metmuseum.ui.main.MainViewModel
import com.sr.metmuseum.util.observeNonNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailFragmentBinding>(DetailFragmentBinding::inflate),
    OnItemClickListener {

    override fun inflateBinding(): Class<DetailFragmentBinding> = DetailFragmentBinding::class.java
    override fun setContent(): Int = R.layout.detail_fragment
    private val viewModel: MainViewModel by activityViewModels()

    private var _galleryAdapter: GalleryAdapter? = null
    private val galleryAdapter: GalleryAdapter
        get() = _galleryAdapter!!


    private var _alert: AlertDialog? = null
    private val alert: AlertDialog
        get() = _alert!!

    private val gridManager by lazy {
        GridLayoutManager(context, 3).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) 3 else 1
                }
            }
        }
    }

    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen.spacing) }

    override fun setUpView() {
        super.setUpView()
        setLifecycle()
        _galleryAdapter = GalleryAdapter(this)
    }

    override fun setUpViewBinding() {
        super.setUpViewBinding()
        binding.recyclerView.apply {
            adapter = galleryAdapter
            layoutManager = gridManager
            addItemDecoration(MarginDecorator(spacing))
        }
    }

    override fun setUpViewModelBinding() {
        super.setUpViewModelBinding()
        viewModel.itemId.observeNonNull(viewLifecycleOwner){
            it?.let { viewModel.getItemDetails(it) }
        }

        viewModel.galleryItems.observeNonNull(viewLifecycleOwner) {
            galleryAdapter.submitList(it)
        }

        viewModel.error.observeNonNull(viewLifecycleOwner) {
            if (it) showErrorDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.invalidateGallery()
        _alert = null
        _galleryAdapter = null
    }

    override fun onItemClick(position: Int) {
        viewModel.updateGallery(position)
    }

    private fun setLifecycle() {
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private fun showErrorDialog() {
        if (_alert == null){
            _alert = AlertDialog.Builder(requireContext())
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    findNavController().apply {
                        viewModel.invalidate()
                        popBackStack()
                        navigateUp()
                    }
                }
                .setCancelable(false)
                .create()
        }

        if (!alert.isShowing) alert.show()
    }
}