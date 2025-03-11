package com.example.todolist.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.domain.todo.model.Todo
import com.example.todolist.BuildConfig
import com.example.todolist.databinding.FragmentCalendarBinding
import com.example.todolist.ui.calendar.adapter.CalendarTodoListRecyclerAdapter
import com.example.todolist.ui.calendar.dialog.AddCalendarDialog
import com.example.todolist.ui.calendar.viewmodel.CalendarViewModel
import com.example.todolist.ui.main.viewmodel.MainViewModel
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    private val viewModel : CalendarViewModel by viewModels()

    private lateinit var adapter: CalendarTodoListRecyclerAdapter

    companion object {
        private const val CLIENT_ID = BuildConfig.NAVER_CLIENT_ID
        private const val CLIENT_SECRET = BuildConfig.NAVER_CLIENT_SECRET
        private const val STATE = "TODO_LIST"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 임시 test 용 item
        viewModel.todoList.add(Todo("Test", false, "Test"))
        viewModel.todoList.add(Todo("Test", false, "Test"))
        viewModel.todoList.add(Todo("Test", false, "Test"))

        adapter = CalendarTodoListRecyclerAdapter(viewModel.todoList, object : CalendarTodoListRecyclerAdapter.OnItemClickListener{
            override fun onItemClick() {
                super.onItemClick()
                onItemClickDialog()
            }
        })
        binding.todoList.adapter = adapter
        binding.todoList.addItemDecoration(DividerItemDecoration(context, 1))
        adapter.update(viewModel.todoList)

        if(!isLogined()){
            lockUntilNaverLogin()
        }else {
            unlockAfterNaverLogin()
        }

        NaverIdLoginSDK.initialize(
            context = requireContext(),
            clientId = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            clientName = "TodoList"
        )

        binding.naverLoginBtn.setOnClickListener {
            loginNaver()
        }
        binding.naverLogoutBtn.setOnClickListener {
            logoutNaver()
        }

        viewModel.userName.observe(this.viewLifecycleOwner, Observer { userName ->
            binding.userNameTv.text = userName + " 님"
        })
    }

    private fun onItemClickDialog() {
        val dialog = AddCalendarDialog()
        parentFragmentManager.setFragmentResultListener("result", this) {Key, bundle ->
            val result = bundle.getString("result")
            result?.run{
            }
        }
        dialog.show(parentFragmentManager,"")
    }

    private fun isLogined() : Boolean {
        return !MainViewModel.NaverLoginData.accessToken.isNullOrEmpty()
    }
    private fun loginNaver() {
        NaverIdLoginSDK.authenticate(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
                MainViewModel.NaverLoginData.accessToken = NaverIdLoginSDK.getAccessToken()
                MainViewModel.NaverLoginData.refreshToken = NaverIdLoginSDK.getRefreshToken()
                Log.d("NaverLogin", "Access Token: ${MainViewModel.NaverLoginData.accessToken}")
                Log.d("NaverLogin", "Refresh Token: ${MainViewModel.NaverLoginData.refreshToken}")
                unlockAfterNaverLogin()
                viewModel.getNaverUserName()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e("NaverLogin", "Error: $message")
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e("NaverLogin", "Error: $message")
            }
        })
    }

    private fun logoutNaver() {
        NaverIdLoginSDK.logout()
        lockUntilNaverLogin()
        MainViewModel.NaverLoginData.accessToken = null
        MainViewModel.NaverLoginData.refreshToken = null
        MainViewModel.NaverLoginData.userName = null
    }

    private fun lockUntilNaverLogin(){
        binding.naverLoginTv.isVisible = true
        binding.naverLoginBtn.isVisible = true
        binding.todoList.isVisible = false
        binding.userNameTv.isVisible = false
        binding.naverLogoutBtn.isVisible = false
    }

    private fun unlockAfterNaverLogin(){
        binding.naverLoginTv.isVisible = false
        binding.naverLoginBtn.isVisible = false
        binding.todoList.isVisible = true
        binding.userNameTv.isVisible = true
        binding.naverLogoutBtn.isVisible = true
        viewModel.userName.value = MainViewModel.NaverLoginData.userName
    }
}