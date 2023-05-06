package com.sr.metmuseum.ui.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.sr.metmuseum.R
import com.sr.metmuseum.base.BaseFragment
import com.sr.metmuseum.databinding.DetailFragmentBinding
import com.sr.metmuseum.util.observeNonNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailFragmentBinding>(DetailFragmentBinding::inflate), OnItemClickListener {

    override fun inflateBinding(): Class<DetailFragmentBinding> = DetailFragmentBinding::class.java
    override fun setContent(): Int = R.layout.detail_fragment
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    private var _galleryAdapter: GalleryAdapter? = null
    private val galleryAdapter: GalleryAdapter
        get() = _galleryAdapter!!

    private val gridManager by lazy {
        GridLayoutManager(context, 3).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) 3 else 1
                }
            }
        }
    }

    private val spacing by lazy {
        resources.getDimensionPixelSize(R.dimen.spacing)
    }

    override fun setUpView() {
        super.setUpView()
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
        viewModel.getItemDetails(args.artItem.id)
        viewModel.galleryItems.observeNonNull(viewLifecycleOwner) {
            galleryAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _galleryAdapter = null
    }

    override fun onItemClick(position: Int) {
      viewModel.updateGallery(position)
    }
}