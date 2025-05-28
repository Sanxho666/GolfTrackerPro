class ScorecardFragment : Fragment() {
    private var _binding: FragmentScorecardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ScorecardViewModel
    private lateinit var adapter: HoleScoreAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScorecardBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this).get(ScorecardViewModel::class.java)
        adapter = HoleScoreAdapter { hole, score ->
            viewModel.updateScore(hole, score)
        }
        
        binding.recyclerViewHoles.adapter = adapter
        binding.recyclerViewHoles.layoutManager = LinearLayoutManager(context)
        
        setupObservers()
    }
    
    private fun setupObservers() {
        viewModel.getCurrentRound().observe(viewLifecycleOwner) { round ->
            round?.let {
                adapter.submitList(it.holes)
                updateTotalScores(it)
            }
        }
    }
    
    private fun updateTotalScores(round: Round) {
        val totalStrokes = round.holes.sumOf { it.score ?: 0 }
        val totalPar = round.course.holes.sumOf { it.par }
        
        binding.textTotalStrokes.text = totalStrokes.toString()
        binding.textTotalPar.text = totalPar.toString()
        binding.textTotalScore.text = (totalStrokes - totalPar).toString()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class HoleScoreAdapter(
    private val onScoreUpdated: (Hole, Int) -> Unit
) : ListAdapter<Hole, HoleScoreAdapter.HoleViewHolder>(HoleDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoleViewHolder {
        val binding = ItemHoleScoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HoleViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: HoleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class HoleViewHolder(private val binding: ItemHoleScoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
            
        fun bind(hole: Hole) {
            binding.textHoleNumber.text = hole.number.toString()
            binding.textPar.text = hole.par.toString()
            binding.textYards.text = hole.yardage.toString()
            
            binding.editScore.setText(hole.score?.toString() ?: "")
            
            binding.editScore.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val score = binding.editScore.text.toString().toIntOrNull() ?: 0
                    onScoreUpdated(hole, score)
                    true
                } else {
                    false
                }
            }
        }
    }
    
    class HoleDiffCallback : DiffUtil.ItemCallback<Hole>() {
        override fun areItemsTheSame(oldItem: Hole, newItem: Hole): Boolean {
            return oldItem.number == newItem.number
        }
        
        override fun areContentsTheSame(oldItem: Hole, newItem: Hole): Boolean {
            return oldItem == newItem
        }
    }
}