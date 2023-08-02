package Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var nav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = false

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.categoryBtn.setOnClickListener {
            replaceFragment(R.id.categoryFragment)
        }

        binding.expenseBtn.setOnClickListener {
            replaceFragment(R.id.expenseFragment)
        }

        binding.budgetBtn.setOnClickListener {
            replaceFragment(R.id.budgetFragment)
        }

        binding.receiptBtn.setOnClickListener {
            replaceFragment(R.id.receiptFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = requireActivity().findNavController(R.id.mainContainer)
    }

    private fun replaceFragment(view: Int)
    {
        nav.navigate(view)
    }
}