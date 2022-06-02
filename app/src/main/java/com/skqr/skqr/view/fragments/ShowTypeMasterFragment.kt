package com.skqr.skqr.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skqr.skqr.R
import com.skqr.skqr.data.models.TypeMasterModel
import com.skqr.skqr.databinding.ActivityMainBinding
import com.skqr.skqr.databinding.FragmentShowTypeMasterBinding
import com.skqr.skqr.view.adapters.TypeMasterAdapter
import com.skqr.skqr.viewModel.ShowTypeMasterViewModel
import com.skqr.skqr.viewModel.ShowTypeMasterViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShowTypeMasterFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentShowTypeMasterBinding
    private lateinit var viewModel: ShowTypeMasterViewModel
    private lateinit var viewModelFactory: ShowTypeMasterViewModelFactory

    private var param1: String? = null
    private var param2: String? = null
    var adapter: TypeMasterAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModelFactory = ShowTypeMasterViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShowTypeMasterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentShowTypeMasterBinding.inflate(inflater, container, false)
        val listener = activity as SelectedTypeListener

        adapter = TypeMasterAdapter(object : TypeMasterAdapter.OnItemClickListener {
            override fun onItemClick(typeMasterModel: TypeMasterModel) {
                //Toast.makeText(activity, typeMasterModel.tm_name, Toast.LENGTH_SHORT).show()

                binding.btnGenerateQr.isEnabled = true
                adapter?.data?.forEach {
                    it.selected = false
                }
                typeMasterModel.selected = true
                adapter?.notifyDataSetChanged()
                //dismiss()
            }
        })
        binding.recyclerViewAllTypeMaster.adapter = adapter
        binding.recyclerViewAllTypeMaster.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        viewModel.listOfTypes.observe(viewLifecycleOwner) { response ->
            binding.progressLoader.visibility = View.GONE
            if(response.isSuccessful) {
                response?.let {
                    it.body()?.let {
                        adapter?.data = it.data
                    }
                }
            } else {
                Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGenerateQr.setOnClickListener {
            listener.onSelectedType(adapter?.data?.first { x -> x.selected }?.tm_id)
            dismiss()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowTypeMasterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    internal interface SelectedTypeListener {
        fun onSelectedType(typeId: String?)
    }
}