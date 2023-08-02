package Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentGMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GMapFragment : SupportMapFragment(), OnMapReadyCallback {
    override fun onMapReady(googleMap: GoogleMap){
        val location = LatLng(-36.8731621,174.7179202)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap.isMyLocationEnabled = true;
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID;
        googleMap.uiSettings.isMapToolbarEnabled = false;
        googleMap.addMarker(MarkerOptions().position(location).title("My Location"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }

    override fun onResume() {
        super.onResume()
        getMapAsync(this)
    }
}