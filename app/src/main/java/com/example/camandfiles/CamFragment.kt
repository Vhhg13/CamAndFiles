package com.example.camandfiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.camandfiles.databinding.FragmentCamBinding
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.io.path.Path
import kotlin.io.path.appendText
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText


class CamFragment : Fragment() {
    private lateinit var viewBinding: FragmentCamBinding
    private lateinit var cameraController: LifecycleCameraController
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = FragmentCamBinding.inflate(layoutInflater)
        if(!hasPermissions(requireContext()))
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        else
            startCamera()
        mainActivity = requireActivity() as MainActivity
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
        viewBinding.list.setOnClickListener {
            if(Path("${requireContext().filesDir}/photos/date").exists())
                mainActivity.showList()
            else
                Toast.makeText(requireContext(), "Ни одного фото не сделано", Toast.LENGTH_SHORT).show()
        }
    }
    private fun takePhoto() {
        val date = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val root = requireContext().filesDir
        val dir = Path("$root/photos")
        if(!dir.exists())
            dir.createDirectories()
        val path = Path("$root/photos/date")
        Toast.makeText(requireContext(), "$root/photos/date", Toast.LENGTH_SHORT).show()
        if(!path.exists())
            path.createFile()
        path.appendText("$date\n")
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
            var permissionGranted = true
            permissions.entries.forEach{
                if(it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if(!permissionGranted){
                Toast.makeText(requireContext(), "Permissions request denied", Toast.LENGTH_SHORT).show()
            }else{
                startCamera()
            }
        }

    private fun startCamera() {
        val previewView: PreviewView = viewBinding.viewFinder
        cameraController = LifecycleCameraController(requireContext())
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        previewView.controller = cameraController
    }

    companion object{
        private const val TAG = "CamAndFiles"
        private const val FILENAME_FORMAT = "dd.MM.yyyy HH.mm.ss"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        fun hasPermissions(context: Context) = REQUIRED_PERMISSIONS.all{
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return viewBinding.root
    }

}