package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.utils.DataBindFragment
import com.example.android.politicalpreparedness.utils.LocationPermissionsUtil
import org.koin.android.ext.android.inject
import java.text.DateFormat

class ElectionsFragment : DataBindFragment<FragmentElectionBinding>(), LocationPermissionsUtil.PermissionListener {

    private val viewModel: ElectionsViewModel by viewModel()
    private val dateFormatter: DateFormat by inject()

    private val permissionUtil = LocationPermissionsUtil(this)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentElectionBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.upcomingElectionsRecyclerView.adapter =
            ElectionListAdapter(dateFormatter, ElectionListener {
                viewModel.onUpcomingClicked(it)
            })

        binding.savedElectionsRecyclerView.adapter =
            ElectionListAdapter(dateFormatter, ElectionListener {
                viewModel.onSavedClicked(it)
            })
        binding.upcomingRefresh.setOnRefreshListener { viewModel.refresh() }

        viewModel.navigateTo.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.navigateCompleted()
                findNavController().navigate(it)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionUtil.requestPermissions(this)
    }

    override fun onDestroyView() {
        permissionUtil.unregister()
        super.onDestroyView()
    }

    override fun onGranted() {
        // Do nothing
    }

    override fun onDenied() {
        Toast.makeText(requireContext(), R.string.location_permission_denied, Toast.LENGTH_LONG).show()
    }

}