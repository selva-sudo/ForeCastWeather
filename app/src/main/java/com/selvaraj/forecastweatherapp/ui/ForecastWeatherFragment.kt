package com.selvaraj.forecastweatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.selvaraj.forecastweatherapp.adapter.DayWeatherAdapter
import com.selvaraj.forecastweatherapp.databinding.FragmentForecastWeatherBinding
import com.selvaraj.forecastweatherapp.model.DayWeather
import com.selvaraj.forecastweatherapp.viewmodel.ForecastViewModelFactory
import com.selvaraj.forecastweatherapp.viewmodel.ForecastWeatherViewModel


class ForecastWeatherFragment : Fragment() {

    private var dayAdapter: DayWeatherAdapter? = null
    private val args: ForecastWeatherFragmentArgs by navArgs()
    private val viewModel: ForecastWeatherViewModel by lazy {
        ViewModelProvider(this, ForecastViewModelFactory(args.weatherResponse))
            .get(ForecastWeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentForecastWeatherBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
            var isToolbarShown = false

            // scroll change listener begins at Y = 0 when image is fully collapsed
            weatherDetailScrollview.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->

                    // User scrolled past image to height of toolbar and the title text is
                    // underneath the toolbar, so the toolbar should be shown.
                    val shouldShowToolbar = scrollY > toolbar.height

                    // The new state of the toolbar differs from the previous state; update
                    // appbar and toolbar attributes.
                    if (isToolbarShown != shouldShowToolbar) {
                        isToolbarShown = shouldShowToolbar

                        // Use shadow animator to add elevation if toolbar is shown
                        appbar.isActivated = shouldShowToolbar

                        toolbarLayout.isTitleEnabled = shouldShowToolbar
                    }
                }
            )

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
            setAdapter(rvCurrentWeather)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getDayWeatherData().observe(requireActivity(), Observer { list ->
            dayAdapter?.setList(list)
        })
    }

    private fun setAdapter(rvCurrentWeather: RecyclerView) {
        val dayItems: MutableList<DayWeather> = mutableListOf()
        dayAdapter = DayWeatherAdapter(dayItems)
        rvCurrentWeather.adapter = dayAdapter
        rvCurrentWeather.setHasFixedSize(true)
    }

}
