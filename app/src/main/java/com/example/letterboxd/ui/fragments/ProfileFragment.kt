package com.example.letterboxd.ui.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.letterboxd.R
import com.example.letterboxd.common.base.BaseFragment
import com.example.letterboxd.common.utils.getRotationDegrees
import com.example.letterboxd.common.utils.rotateBitmap
import com.example.letterboxd.databinding.FragmentProfileBinding
import com.example.letterboxd.common.SwipeToDeleteCallback
import com.example.letterboxd.ui.adapters.profile_page.FavouriteFilmsAdapter
import com.example.letterboxd.ui.adapters.profile_page.RecentReviewsAdapter
import com.example.letterboxd.ui.adapters.profile_page.RecentWatchedAdapter
import com.example.letterboxd.ui.view_models.ProfileViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel : ProfileViewModel by viewModels()

    private lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var pickMedia2 : ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var takePhoto :  ActivityResultLauncher<Uri>
    private lateinit var currentPhotoUri: Uri

    private val adapterFavourites = FavouriteFilmsAdapter(removeLikeFunction = { id, position ->
        openRemoveFavFilmDialog(id, position)
    }, function = {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFilmDetailsFragment(it.id))
    })

    @RequiresApi(Build.VERSION_CODES.O)
    private val adapterRecentWatched = RecentWatchedAdapter (removeMovieFunction = { id, position ->
        openRemoveWatchedFilmDialog(id, position)
    }){
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFilmDetailsFragment(it.filmId))
    }

    private val adapterRecentReviews = RecentReviewsAdapter (null, "you"){
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToFilmDetailsFragment(
                it
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapterFavourites
        binding.recyclerView2.adapter = adapterRecentWatched
        binding.recyclerView3.adapter = adapterRecentReviews

        // new
        val swipeHandler = SwipeToDeleteCallback(adapterRecentReviews, viewModel, binding, requireContext())
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView3)

        provideChooseFromGalleryFunctionalities()
        provideTakePhotoFunctionalities()
        provideBackgroundImageFunctionalities()

        observeViewModel()

        setListeners()

        provideUsername()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners(){
        binding.imageButtonChangeUsername.setOnClickListener {
            openChangeUsernameDialog()
        }

        binding.imageButtonBg.setOnClickListener {
            if (viewModel.sharedPreferencesBackground.getString("profileBackground", null) == null){
                pickMedia2.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else openChangeBackgroundDialog()
        }

        binding.imageButton.setOnClickListener {
            if (viewModel.sharedPreferencesPicture.getString("profile_picture_path", null) == null){
                openMediaTypeDialog()
            } else openChangeProfilePictureDialog()
        }

        binding.seeAllReviews.setOnClickListener {
            if (binding.seeAllReviews.text == "See All") {
                adapterRecentReviews.showAllReviews()
                binding.seeAllReviews.text = "See Less"
            }
            else {
                adapterRecentReviews.showLessReviews()
                binding.seeAllReviews.text = "See All"
            }
        }

        binding.textSeeAllRecentWatched.setOnClickListener {
            if (binding.textSeeAllRecentWatched.text == "See All") {
                adapterRecentWatched.showAllRecentWatchedMovies()
                binding.textSeeAllRecentWatched.text = "See Less"
            } else {
                adapterRecentWatched.showLessRecentWatchedMovies()
                binding.textSeeAllRecentWatched.text = "See All"
            }
        }

        binding.buttonGoBackk.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.favouriteFilmsToDisplay.collect{
                adapterFavourites.updateAdapter(it)
                binding.textFavFilms.text = it.size.toString()
            }
        }

        lifecycleScope.launch {
            viewModel.recentWatchedFilmsToDisplay.collect {
                adapterRecentWatched.updateAdapter(it)
                binding.textTotalFilms.text = it.size.toString()

                if (it.size <= 5) binding.textSeeAllRecentWatched.visibility = View.GONE
                else binding.textSeeAllRecentWatched.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.yearlyFilmList.collectLatest {
                binding.textFilmsThisYear.text = it.size.toString()
            }
        }

        lifecycleScope.launch {
            viewModel.reviewsToDisplay.collect {
                adapterRecentReviews.updateAdapter(it)
                binding.textReviews.text = it.size.toString()
                if (it.size <= 3) binding.seeAllReviews.visibility = View.GONE
                else binding.seeAllReviews.visibility = View.VISIBLE

                if (it.isEmpty()) binding.textView53.visibility = View.VISIBLE
                else binding.textView53.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.profilePictureBitmap.collectLatest{
                adapterRecentReviews.updatePhotos(it)
                binding.profileImage.setImageBitmap(it)
            }
        }

        lifecycleScope.launch {
            viewModel.backgroundImageBitmap.collectLatest {
                binding.imageView15.setImageBitmap(it)
            }
        }

        lifecycleScope.launch {
            viewModel.name.collect{
                adapterRecentReviews.updateName(it.toString())
            }
        }
    }

    private fun provideChooseFromGalleryFunctionalities() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.getProfilePictureBitmap(requireContext(), uri)
            } else {
                FancyToast.makeText(requireContext(), "No Media Selected", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
            }
        }
    }

    private fun provideTakePhotoFunctionalities(){
        takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                viewModel.getProfilePictureBitmap(requireContext(), currentPhotoUri)
            } else {
                FancyToast.makeText(requireContext(), "Failed to take picture", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }
        }
    }

    private fun provideBackgroundImageFunctionalities(){
        pickMedia2 = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.getBackgroundImageBitmap(requireContext(), uri)
            } else {
                FancyToast.makeText(requireContext(), "No Media Selected", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
            }
        }
    }

    private fun openChangeProfilePictureDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_edit_pp_layout, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialog = builder.create()

        val changeButton = dialogView.findViewById<Button>(R.id.buttonChange)
        val removeButton = dialogView.findViewById<Button>(R.id.buttonRemove)

        changeButton.setOnClickListener {
            openMediaTypeDialog()
            dialog.dismiss()
        }

        removeButton.setOnClickListener {
            binding.profileImage.setImageResource(R.drawable.anonymous_user)
            viewModel.sharedPreferencesPicture.edit().putString("profile_picture_path", null).apply()
            adapterRecentReviews.updatePhotos(BitmapFactory.decodeResource(resources, R.drawable.anonymous_user))
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show()
    }

    private fun openChangeBackgroundDialog(){
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_edit_bg_layout, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialog = builder.create()

        val changeButton = dialogView.findViewById<Button>(R.id.button5)
        val removeButton = dialogView.findViewById<Button>(R.id.button4)

        changeButton.setOnClickListener {
            pickMedia2.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            dialog.dismiss()
        }

        removeButton.setOnClickListener {
            binding.imageView15.setImageResource(R.drawable.default_bg)
            viewModel.sharedPreferencesBackground.edit().putString("profileBackground", null).apply()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show()
    }

    private fun openRemoveFavFilmDialog(id : Int, position : Int){
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_remove_fav_film_layout, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialog = builder.create()

        val cancelButton = dialogView.findViewById<Button>(R.id.button43)
        val removeButton = dialogView.findViewById<Button>(R.id.button53)

        cancelButton.setOnClickListener {
            dialog.cancel()
            dialog.dismiss()
        }

        removeButton.setOnClickListener {
            adapterFavourites.removeItem(position)
            viewModel.removeLikedMovie(id)
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openRemoveWatchedFilmDialog(id : Int, position : Int){
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_remove_watched_layout, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialog = builder.create()

        val cancelButton = dialogView.findViewById<Button>(R.id.button44)
        val removeButton = dialogView.findViewById<Button>(R.id.button54)

        cancelButton.setOnClickListener {
            dialog.cancel()
            dialog.dismiss()
        }

        removeButton.setOnClickListener {
            adapterRecentWatched.removeItem(position)
            if (binding.textSeeAllRecentWatched.text == "See Less"){
                binding.textSeeAllRecentWatched.text = "See All"
            }
            viewModel.removeWatchedMovie(id)
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show()
    }

    private fun openMediaTypeDialog(){
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_media_type_layout, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialog = builder.create()

        val chooseFromGalleryButton = dialogView.findViewById<Button>(R.id.button42)
        val takePhotoButton = dialogView.findViewById<Button>(R.id.button52)

        chooseFromGalleryButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            dialog.dismiss()
        }

        takePhotoButton.setOnClickListener {
            openCamera()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show()
    }

    private fun openCamera() {
        lifecycleScope.launch {
            val photoFile = File.createTempFile("IMG_", ".jpg", requireContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES))
            currentPhotoUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", photoFile)
            takePhoto.launch(currentPhotoUri)
        }
    }

    private fun openChangeUsernameDialog(){
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_change_username_layout, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextText2)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialog = builder.create()

        val cancelButton = dialogView.findViewById<Button>(R.id.button46)
        val saveButton = dialogView.findViewById<Button>(R.id.button56)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        saveButton.setOnClickListener {
            val newName = editText.text.toString().trim()

            FancyToast.makeText(requireContext(), "Your name has been changed to $newName", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
            binding.textViewName.text = newName
            binding.textView28.text = "${newName}'s Favourite Films"
            binding.textView29.text = "${newName}'s Recent Watched"
            binding.textView33.text = "${newName}'s Recent Reviews"

            viewModel.sharedPreferencesName.edit().putString("name", newName).apply()
            viewModel.getName()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setOnShowListener {
            editText.requestFocus()
            editText.postDelayed({
                val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }, 100)
        }
        dialog.show()
    }

    private fun provideUsername(){
        val name = viewModel.sharedPreferencesName.getString("name", "no name")
        binding.textViewName.text = name
        binding.textView28.text = "${name}'s Favourite Films"
        binding.textView29.text = "${name}'s Recent Watched"
        binding.textView33.text = "${name}'s Recent Reviews"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        viewModel.getAllFavouriteFilms()
        viewModel.getAllReviews()
        viewModel.getAllRecentWatchedFilms()
        viewModel.getName()

        val imagePathProfile = viewModel.sharedPreferencesPicture.getString("profile_picture_path", null)
        if (imagePathProfile != null) {
            val imageFile = File(requireContext().filesDir, imagePathProfile)
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            val rotationDegrees = getRotationDegrees(imageFile.absolutePath)
            val rotatedBitmap = rotateBitmap(bitmap, rotationDegrees)

            binding.profileImage.setImageBitmap(rotatedBitmap)
            adapterRecentReviews.updatePhotos(rotatedBitmap)
        } else {
            binding.profileImage.setImageResource(R.drawable.anonymous_user)
            adapterRecentReviews.updatePhotos(BitmapFactory.decodeResource(resources, R.drawable.anonymous_user))
        }

        val imagePathBackground = viewModel.sharedPreferencesBackground.getString("profileBackground", null)
        if (imagePathBackground != null) {
            val imageFile = File(requireContext().filesDir, imagePathBackground)
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            val rotationDegrees = getRotationDegrees(imageFile.absolutePath)
            val rotatedBitmap = rotateBitmap(bitmap, rotationDegrees)

            binding.imageView15.setImageBitmap(rotatedBitmap)
        } else {
            binding.imageView15.setImageResource(R.drawable.default_bg)
        }
    }
}