package com.history

import androidx.activity.viewModels
import com.history.core.NavigationActivity
import com.history.databinding.MainActivityBinding


class MainActivity : NavigationActivity<MainActivityBinding>() {



    private val viewModel: MainViewModel by viewModels()

    override val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }
//
    override val graphId: Int
        get() = viewModel.graphId
//
//    override val appBarConfiguration: AppBarConfiguration = AppBarConfiguration(
//        topLevelDestinationIds = setOf(
//            R.id.homePageScreen,
//            R.id.menuScreen,
//            R.id.accountScreen,
//            R.id.cartScreen
//        )
//    )
//
//    private val hideBottomViewBehavior: HideBottomViewOnScrollBehavior<View>
//        get() = ((binding.bottomNavigationView.layoutParams as CoordinatorLayout.LayoutParams)
//            .behavior as HideBottomViewOnScrollBehavior)
//
//    var navHostDefaultBehavior: CoordinatorLayout.Behavior<View>?
//        get() = (binding.navHostDefault.layoutParams as CoordinatorLayout.LayoutParams).behavior
//        set(value) {
//            (binding.navHostDefault.layoutParams as CoordinatorLayout.LayoutParams).behavior = value.also {
//                value?.onDependentViewChanged(
//                    binding.coordinator, binding.navHostDefault, binding.bottomNavigationView
//                )
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.AppTheme)
//        super.onCreate(savedInstanceState)
//
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        setTabBarVisibility()
//
//        with (binding.bottomNavigationView) {
//            itemIconTintList = null
//            setMargins(
//                bottom = marginBottom + navigationBarHeight
//            )
//            setupWithNavController(navController)
//            navController.addOnDestinationChangedListener { _, _, _ ->
//                binding.root.dismissKeyboard()
//                hideBottomViewBehavior.slideUp(this)
//            }
//        }
//
//        handleEvents()
//        observeThemeMode()
//        observeCartItemCount()
//        setupMagicButton()
//    }
//
//    private fun handleEvents() {
//        viewModel.events.observe(this) {
//            when (it) {
//                is MainViewModel.ShowForceUpdate -> startActivity(
//                    Intent(this, ForceUpdateActivity::class.java).apply {
//                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    }
//                )
//                is SwitchNavigationGraphEvent -> switchNavigationGraph()
//            }
//        }
//    }
//
//    private fun observeThemeMode() {
//        viewModel.themeMode.observe(this) { it.setNightMode() }
//    }
//
//    private fun observeCartItemCount() {
//        viewModel.cartItemCount.observe(this) { binding.bottomNavigationView.setBadge(R.id.cartScreen, it) }
//    }
//
//    private fun setTabBarVisibility() = when (viewModel.tabbarVisible) {
//        true -> binding.bottomNavigationView.visible()
//        false -> binding.bottomNavigationView.gone()
//    }
//
//    private fun switchNavigationGraph() {
//        navController.setGraph(graphId)
//        setTabBarVisibility()
//    }
//
//    fun showBottomNavigation() {
//        hideBottomViewBehavior.slideUp(binding.bottomNavigationView)
//    }
//
//    fun hideBottomNavigation() {
//        hideBottomViewBehavior.slideDown(binding.bottomNavigationView)
//    }
}
