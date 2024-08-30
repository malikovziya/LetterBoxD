package com.example.letterboxd.common

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxd.databinding.FragmentProfileBinding
import com.example.letterboxd.domain.model.ReviewItem
import com.example.letterboxd.ui.adapters.profile_page.RecentReviewsAdapter
import com.example.letterboxd.ui.view_models.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SwipeToDeleteCallback(
    private val adapter: RecentReviewsAdapter,
    private val viewModel: ProfileViewModel,
    private val binding: FragmentProfileBinding,
    private val context : Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    private var removedMovie : ReviewItem? = null

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition

        if (binding.seeAllReviews.text == "See Less"){
            binding.seeAllReviews.text = "See All"
        }

        removedMovie = adapter.longResponse[position]

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val reviewEntity = viewModel.daoReviews.getReviewById(removedMovie!!.filmId)
            viewModel.removeReview(removedMovie!!.filmId)

            delay(500)
            binding.textReviews.text = binding.textReviews.text.toString().toInt().minus(1).toString()
            adapter.handleSwipe(position)

            Snackbar.make(binding.recyclerView3, "Review deleted -> ${removedMovie!!.filmName}", Snackbar.LENGTH_LONG).setAction("Undo") {
                scope.launch {
                    viewModel.daoReviews.insertReview(reviewEntity!!)
                    adapter.handleUndo(position, removedMovie!!)
                    binding.seeAllReviews.text = "See All"

                    if (adapter.longResponse.size == 4){
                        binding.seeAllReviews.visibility = View.VISIBLE
                    }
                }
                binding.textReviews.text = binding.textReviews.text.toString().toInt().plus(1).toString()
            }.show()
            viewModel.getAllReviews()
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addBackgroundColor(ContextCompat.getColor(context, com.example.letterboxd.R.color.red))
            .addActionIcon(com.example.letterboxd.R.drawable.rubbish_bin)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}