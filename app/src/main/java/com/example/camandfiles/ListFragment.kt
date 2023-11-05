package com.example.camandfiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camandfiles.databinding.FragmentListBinding
import kotlin.io.path.Path
import kotlin.io.path.readText

class ListFragment : Fragment() {
    private lateinit var viewBinding: FragmentListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = FragmentListBinding.inflate(layoutInflater)
        val path = Path("${requireContext().filesDir}/photos/date")
        viewBinding.recycler.adapter = PhotoDatesAdapter(path.readText())
        viewBinding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }
}