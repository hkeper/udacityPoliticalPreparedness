package com.example.android.politicalpreparedness.election

import android.location.Geocoder
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.utils.LocationPermissionsUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.text.DateFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class VoterInfoFragment : Fragment(), LocationPermissionsUtil.PermissionListener {

    private val permissionUtil = LocationPermissionsUtil(this)
    private val params: VoterInfoFragmentArgs by navArgs()
    private val viewModel: VoterInfoViewModel by viewModel { parametersOf(params.election) }
    private lateinit var binding: FragmentVoterInfoBinding
    lateinit var fusedLocationClient: FusedLocationProviderClient

    val dateFormatter: DateFormat by inject()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentVoterInfoBinding.inflate(layoutInflater, container, false)

        binding.dateFormatter = dateFormatter
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.stateLocations.movementMethod = LinkMovementMethod.getInstance()
        binding.stateBallot.movementMethod = LinkMovementMethod.getInstance()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel.navigateBack.observe(viewLifecycleOwner) { navigateBack ->
            if (navigateBack) {
                viewModel.navigateCompleted()
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showSnackbar(getString(it))
                binding.addressGroup.visibility = View.GONE
                binding.stateGroup.visibility = View.GONE
            }
        }

        viewModel.electionDetails.observe(viewLifecycleOwner) { state ->
            state?.let {
                binding.constraintLayout.transitionToEnd()
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

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onGranted() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    try {
                        val address = Geocoder(requireContext()).getFromLocation(it.latitude, it.longitude, 1).firstOrNull()
                        viewModel.loadDetails(address)
                    } catch (ex: Exception) {
                        showSnackbar(getString(R.string.failed_get_address_from_location))
                        ex.printStackTrace()
                    }
                }
            }
    }

    override fun onDenied() {
        showSnackbar(getString(R.string.location_permission_denied))
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}