package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.utils.LocationPermissionsUtil
import com.example.android.politicalpreparedness.utils.fadeIn
import com.example.android.politicalpreparedness.utils.fadeOut
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.snackbar.Snackbar
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment(), LocationPermissionsUtil.PermissionListener {

    private val permissionUtil = LocationPermissionsUtil(this)
    lateinit var fusedLocationClient: FusedLocationProviderClient

    val viewModel: RepresentativeViewModel by viewModel()
    private lateinit var binding: FragmentRepresentativeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionUtil.requestPermissions(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentRepresentativeBinding.inflate(layoutInflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        //disable motion animation at the start
        binding.representativeContainer.setTransition(R.id.start, R.id.start)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.representativeDelete.adapter =
            RepresentativeListAdapter(RepresentativeListAdapter.RepresentativeListener {})

        viewModel.representatives.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.representativeContainer.setTransition(R.id.start, R.id.start)
            } else {
                binding.representativeContainer.setTransition(R.id.start, R.id.end)
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(getString(it))
            }
        }

        viewModel.messageString.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(it)
            }
        }
        viewModel.dataLoading.observe(viewLifecycleOwner) {
            if (it) {
                hideKeyboard()
            }
        }

        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setState(requireContext().resources.getStringArray(R.array.states)[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.buttonLocation.setOnClickListener {
//            permissionUtil.requestPermissions(this)

        }
        binding.executePendingBindings()
        return binding.root
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    private fun getLocation() {
        binding.representativesLoading.fadeIn()
        fusedLocationClient
            .getCurrentLocation(100, object : CancellationToken() {
            override fun isCancellationRequested(): Boolean {
                return false
            }
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                return this
            }
        })
            .addOnSuccessListener {
            val address = geoCodeLocation(it)
            viewModel.searchForRepresentatives(address)
        }
            .addOnCompleteListener {
                binding.representativesLoading.fadeOut()
            }
            .addOnFailureListener {
                binding.representativesLoading.fadeOut()
            }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode.orEmpty()
                )}
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onGranted() {
        getLocation()
    }

    override fun onDenied() {
        showSnackbar(getString(R.string.location_permission_denied))
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}