package com.demo.developer.deraesw.demomoviewes.ui.upcoming


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.demo.developer.deraesw.demomoviewes.adapter.UpcomingMoviesAdapter
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentUpcomingBinding
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingFragment : Fragment(), UpcomingMoviesAdapter.UpcomingMovieAdapterInterface {

    private val viewModel: UpcomingMoviesViewModel by viewModels()
    private lateinit var binding: FragmentUpcomingBinding

    private val adapter = UpcomingMoviesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)

        binding.rvUpcomingMovies.apply {
            setLinearLayout(hasDivider = false)
            adapter = this@UpcomingFragment.adapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.apply {
            upcomingMovieList.observe(viewLifecycleOwner, {
                populateUpcomingMoviesWithGenre(it)
            })

            upcomingMoviesWithGender.observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })
        }
    }

    override fun clickOnItem(id: Int) {
        val destination = HomeFragmentDirections.actionHomeFragmentToMovieDetailActivityFragment(id)
        this.findNavController().navigate(destination)
    }
}
