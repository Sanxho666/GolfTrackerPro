class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_map -> navController.navigate(R.id.mapFragment)
                R.id.navigation_scorecard -> navController.navigate(R.id.scorecardFragment)
                R.id.navigation_history -> navController.navigate(R.id.historyFragment)
                else -> false
            }
            true
        }
    }
}
