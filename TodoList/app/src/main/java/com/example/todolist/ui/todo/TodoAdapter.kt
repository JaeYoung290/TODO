package com.example.todolist.ui.todo

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.todo.TodoEntity
import com.example.todolist.databinding.FragmentTodoTodoListItemBinding
import com.example.todolist.ui.main.MainActivity

class TodoAdapter(
    private val itemList: MutableList<TodoEntity>,
    private val activity: MainActivity
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<TodoEntity>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged() // 리스트 갱신
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class TodoViewHolder(val binding: FragmentTodoTodoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var editing = false

        init {
            // 클릭 블로커 클릭 시 0.3초 동안 사라졌다가 다시 나타나도록 하기
            // 더블 클릭시 클릭 블로커가 사라지면서 EditText에 포커스 들어감
            binding.linearLayoutTouchBlocker.setOnClickListener {
                binding.linearLayoutTouchBlocker.visibility = View.INVISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    if (!editing) {
                        binding.linearLayoutTouchBlocker.visibility = View.VISIBLE
                    }
                }, 300) // 0.3초 뒤에 다시 나타나도록 함
            }

            binding.editTextTodo.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    editing = true
                    binding.linearLayoutTouchBlocker.visibility = View.GONE
                    // bottomNavi 숨기기
                    activity.setBottomNavVisibility(false)
                    if (binding.editTextTodo.text.toString().isEmpty()) {
                        val newItem = TodoEntity(
                            date = itemList[0].date,
                            checked = false,
                            todo = ""
                        )
                        itemList.add(newItem)
                        notifyItemInserted(itemList.size - 1)
                    }
                } else {
                    // 포커스가 빠져나가면 clickblocker 다시 표시
                    editing = false
                    binding.linearLayoutTouchBlocker.visibility = View.VISIBLE
                    // bottomNavi 다시 표시
                    activity.setBottomNavVisibility(true)
                    // 내용이 없다면 해당 항목 삭제
                    if (binding.editTextTodo.text.toString().isEmpty()) {
                        val item = itemList[adapterPosition]
                        itemList.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                }
            }

            // editText의 내용이 변경될 때마다 처리
            binding.editTextTodo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                    val item = itemList[adapterPosition]
                    item.todo = charSequence.toString()
                    // 데이터베이스나 ViewModel에 변경 사항을 반영할 수 있습니다
                }

                override fun afterTextChanged(editable: Editable?) {}
            })
        }
    }

    fun removeItem(position: Int) {
        if (position in itemList.indices && position != itemList.size - 1) {
            itemList.removeAt(position)
            notifyItemRemoved(position)
        } else {
            notifyItemChanged(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = FragmentTodoTodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = itemList[position]
        holder.apply {
            binding.checkboxCheckbox.isChecked = item.checked
            binding.editTextTodo.setText(item.todo)
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun getItemList(): List<TodoEntity> {
        return itemList
    }
}
