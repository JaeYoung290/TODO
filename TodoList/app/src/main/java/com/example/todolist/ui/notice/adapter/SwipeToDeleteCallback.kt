package com.example.todolist.ui.notice.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.ui.notice.viewmodel.NoticeViewModel

class SwipeToDeleteCallback(
    private val context: Context,
    private val adapter: NoticeRecyclerAdapter,
    private val viewModel: NoticeViewModel
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val restoreIcon = ContextCompat.getDrawable(context, R.drawable.ic_restore)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        val item = adapter.notices[position]
        if (item.isDeleted) {
            viewModel.deleteNoticeById(item.id.toInt(), false)
            adapter.removeItem(position)
        } else {
            viewModel.deleteNoticeById(item.id.toInt(), true)
            adapter.removeItem(position)
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
        val position = viewHolder.bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        val item = adapter.notices[position]
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        val swipeWidth = Math.min(Math.abs(dX), itemView.width.toFloat())
        val swipeRect = RectF(
            itemView.right - swipeWidth,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
        )

        val backgroundColor =
            if (item.isDeleted) {
                if (Math.abs(dX) >= itemView.width * 0.45f)
                    Color.parseColor("#34C759")
                else
                    Color.parseColor("#D3D3D3")
            } else {
                if (Math.abs(dX) >= itemView.width * 0.45f)
                    Color.parseColor("#E82561")
                else
                    Color.parseColor("#D3D3D3")
            }
        c.drawRect(swipeRect, Paint().apply { color = backgroundColor })

        val icon = if (item.isDeleted) restoreIcon else deleteIcon
        icon?.let {
            val intrinsicWidth = it.intrinsicWidth
            val intrinsicHeight = it.intrinsicHeight
            val iconMargin = 20.dpToPx(context)
            val iconFixedPosition = 60.dpToPx(context)
            val left =
                if (Math.abs(dX) <= iconFixedPosition) itemView.right - swipeWidth + iconMargin else itemView.right - iconFixedPosition + iconMargin
            val right = left + intrinsicWidth
            val top = itemView.top + (itemHeight - intrinsicHeight) / 2
            val bottom = top + intrinsicHeight
            it.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            it.draw(c)
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            -swipeWidth,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, Paint())
    }

    private fun Int.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }
}