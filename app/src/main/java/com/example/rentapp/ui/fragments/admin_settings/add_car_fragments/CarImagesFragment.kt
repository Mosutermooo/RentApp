package com.example.rentapp.ui.fragments.admin_settings.add_car_fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentCarImagesBinding
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.models.add_car_cache_models.CachedCarImages
import com.example.rentapp.ui.activies.CapturedImagesActivity
import com.example.rentapp.ui.activies.GalleryImagesActivity
import com.example.rentapp.ui.activies.ShowImageActivity
import com.example.rentapp.uitls.GlobalState
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.CarViewModel
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File


class CarImagesFragment : Fragment() {

    private lateinit var binding: FragmentCarImagesBinding
    private lateinit var carViewModel: CarViewModel
    private var imageCapture: ImageCapture? = null
    private var car: CacheUploadCarModel? = null
    private lateinit var outputDirectory: File


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCarImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        car = GlobalState.cachedCarUpload
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        carViewModel = Initializers().initCarViewModel(requireActivity() as AppCompatActivity)
        outputDirectory = getOutputDirectory()
        binding.back.setOnClickListener {
            viewPager?.currentItem = 2
        }

        binding.takePhoto.setOnClickListener{
            takePhoto()
        }

        binding.TakenImages.setOnClickListener{
            val intent = Intent(requireContext(), CapturedImagesActivity::class.java)
            intent.putExtra("car_id_captured_images", car?.id!!.toString())
            startActivity(intent)
        }
        binding.GalleryImage.setOnClickListener {
            val intent = Intent(requireContext(), GalleryImagesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun takePhoto() {
        val imageCapture = this.imageCapture ?: return
        val photoFile = File(
            outputDirectory,"${System.currentTimeMillis()}.jpg"
        )

        val outPutOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outPutOption, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedImageUri = Uri.fromFile(photoFile)
                    val cachedCarImage = CachedCarImages(null, savedImageUri.toString(), car?.id!!)
                    carViewModel.addCarImages(cachedCarImage)
                    val snackBar = Snackbar.make(requireActivity().findViewById(android.R.id.content), "Image Saved", Snackbar.LENGTH_SHORT)
                    snackBar.setAction("View"){
                        val intent = Intent(requireContext(), ShowImageActivity::class.java)
                        intent.putExtra("image_uri", savedImageUri.toString())
                        startActivity(intent)
                    }
                    snackBar.show()

                }

                override fun onError(exception: ImageCaptureException) {
                    showSnackBar(
                        exception.message.toString(),
                        requireActivity() as AppCompatActivity
                    )
                }

            }
        )

    }

    private fun getOutputDirectory(): File{
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {mFile ->
            File(mFile, requireActivity().resources.getString(R.string.app_name)).apply {
                mkdir()
            }
        }


        return if(mediaDir != null && mediaDir.exists()) mediaDir else requireActivity().filesDir
    }

    override fun onResume() {
        super.onResume()
        askCameraPermission()
    }





    private fun askCameraPermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    startCamera()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    val snackBar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "Camera permission denied",
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.setAction(getString(R.string.grant_permission_text)){
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    snackBar.show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }
            }).onSameThread().check()
    }

    private fun startCamera() {
        val cameraProviderFeature = ProcessCameraProvider.getInstance(requireActivity())
        val cameraProvider = cameraProviderFeature.get()

        cameraProviderFeature.addListener({
            val preview = Preview.Builder()
                .build()
            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            }catch (e: Exception){
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }



}