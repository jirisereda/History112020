package com.history

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.history.common.ui.views.LoadingState
import com.history.core.events.EmptyArgs
import com.history.core.screen.Screen
import com.e_com_app.core.system_bars.platform.SystemBarMode
import com.e_com_app.core.system_bars.platform.isNightMode
import com.e_com_app.databinding.StreamsScreenBinding
import com.e_com_app.platform.requireMainActivity
import com.e_com_app.screen.streams.adapter.StreamsTabsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoricalEventsScreen :
    Screen<StreamsViewModel, StreamsScreenBinding, EmptyArgs>() {
    override val viewModel: StreamsViewModel by viewModels()
    override val layoutId: Int = R.layout.streams_screen
    override val screenToolbar: Toolbar? = null

    override val dataBindViewModel: StreamsScreenBinding.(StreamsViewModel) -> Unit = {
        viewModel = it
        tabsAdapter = StreamsTabsAdapter(
            this@StreamsScreen.childFragmentManager, viewLifecycleOwner, it
        )
    }

    override val screenArgs = EmptyArgs

    override var statusBarMode: SystemBarMode? = null

    override var navigationBarMode: SystemBarMode? = null

    override fun setupViews() {
        statusBarMode = defaultBarMode()
        navigationBarMode = defaultBarMode()
        observeLoadingState()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.streamsIndicator.run { hideStreamsTabsBehavior?.slideDown(this) }
                requireMainActivity().showBottomNavigation()
            }
        })
        var mediator: TabLayoutMediator? = null
        viewModel.state.map { it.streams }.distinctUntilChanged()
            .observe(viewLifecycleOwner) { headerStreams ->
                val labels = headerStreams.map { it.label }
                if (labels.isEmpty()) {
                    return@observe
                }
                mediator?.detach()
                mediator = TabLayoutMediator(binding.streamsIndicator, binding.viewPager) { tab, position ->
                    tab.text = labels[position]
                    tab.view[1].setBackgroundResource(R.drawable.stream_tab_item_text)
                }.also { it.attach() }
            }

        binding.streamsIndicator.hideStreamsTabsBehavior?.mainActivity = requireMainActivity()
    }

    private fun defaultBarMode() : SystemBarMode = SystemBarMode.Fixed(
        drawBehind = true,
        background = android.R.color.transparent,
        darkIcons = !isNightMode
    )

    private fun observeLoadingState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            setBarMode(state.loadingState)
        }
    }

    fun setBarMode(loadingState: LoadingState) {
        statusBarMode = when(loadingState) {
            is LoadingState.Data ->
                SystemBarMode.Fixed(
                    drawBehind = true,
                    background = android.R.color.transparent,
                    darkIcons = false
                )
            else -> defaultBarMode()
        }
        navigationBarMode = statusBarMode
        statusBarSetup.update(statusBarMode)
        navigationBarSetup.update(navigationBarMode)
    }
}

private val View.hideStreamsTabsBehavior: HideStreamsTabsBehavior?
    get() = (layoutParams as? CoordinatorLayout.LayoutParams)?.behavior as? HideStreamsTabsBehavior
