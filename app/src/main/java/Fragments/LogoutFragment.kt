package Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensemanagementsystem.Activities.LoginActivity
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentCategoryBinding
import com.example.expensemanagementsystem.databinding.FragmentLogoutBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogoutFragment : Fragment() {
    private lateinit var binding: FragmentLogoutBinding
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)

        binding.logoutBtn.setOnClickListener{
            auth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}