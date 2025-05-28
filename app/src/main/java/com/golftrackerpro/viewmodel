class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private lateinit var viewModel: MapViewModel
    private var currentRound: Round? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        
        setupObservers()
        setupClickListeners()
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocation()
        loadCourseData()
    }
    
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }
    }
    
    private fun loadCourseData() {
        // Load course data from ViewModel
        viewModel.getCurrentCourse().observe(viewLifecycleOwner) { course ->
            course?.let {
                val courseBounds = LatLngBounds.Builder()
                it.holes.forEach { hole ->
                    val teeLocation = LatLng(hole.teeLatitude, hole.teeLongitude)
                    val pinLocation = LatLng(hole.pinLatitude, hole.pinLongitude)
                    
                    courseBounds.include(teeLocation)
                    courseBounds.include(pinLocation)
                    
                    map.addMarker(
                        MarkerOptions()
                            .position(teeLocation)
                            .title("Hole ${hole.number} Tee")
                    )
                    
                    map.addMarker(
                        MarkerOptions()
                            .position(pinLocation)
                            .title("Hole ${hole.number} Pin")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                }
                
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(courseBounds.build(), 100))
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.fabMarkBall.setOnClickListener {
            currentRound?.let { round ->
                val currentHole = round.getCurrentHole()
                val ballPosition = map.myLocation?.let { location ->
                    BallPosition(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        holeNumber = currentHole.number,
                        shotNumber = currentHole.shots.size + 1
                    )
                }
                
                ballPosition?.let {
                    viewModel.addShotToRound(round.id, it)
                    addBallMarker(it)
                }
            }
        }
    }
    
    private fun addBallMarker(ballPosition: BallPosition) {
        map.addMarker(
            MarkerOptions()
                .position(LatLng(ballPosition.latitude, ballPosition.longitude))
                .title("Shot ${ballPosition.shotNumber}")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )
    }
    
    private fun setupObservers() {
        viewModel.getCurrentRound().observe(viewLifecycleOwner) { round ->
            currentRound = round
            round?.let {
                updateMapWithRoundData(it)
            }
        }
    }
    
    private fun updateMapWithRoundData(round: Round) {
        map.clear()
        loadCourseData()
        
        round.holes.forEach { hole ->
            hole.shots.forEach { shot ->
                addBallMarker(shot)
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}