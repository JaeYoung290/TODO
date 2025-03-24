package com.example.todolist.ui.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.todolist.BuildConfig
import com.example.todolist.databinding.FragmentCalendarBinding
import com.example.todolist.ui.calendar.adapter.CalendarTodoListRecyclerAdapter
import com.example.todolist.ui.calendar.dialog.AddCalendarDialog
import com.example.todolist.ui.calendar.viewmodel.CalendarViewModel
import com.example.todolist.ui.main.viewmodel.MainViewModel
import com.google.android.material.internal.TextWatcherAdapter
import com.google.android.material.snackbar.Snackbar
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    private val viewModel : CalendarViewModel by viewModels()

    private lateinit var adapter: CalendarTodoListRecyclerAdapter

    private lateinit var snackbar : Snackbar

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

        setSnackbar()
        setToggleASC()
        binding.progressbarTodoList.isVisible = false

        adapter = CalendarTodoListRecyclerAdapter(viewModel.todoList.toList(), object : CalendarTodoListRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(title : String, date : String) {
                onItemClickDialog(title, date)
            }
        })
        binding.todoList.adapter = adapter
        binding.todoList.addItemDecoration(DividerItemDecoration(context, 1))

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

        binding.buttonNaverLogin.setOnClickListener {
            loginNaver()
        }
        binding.buttonNaverLogout.setOnClickListener {
            logoutNaver()
        }

        viewModel.userName.observe(this.viewLifecycleOwner, Observer { userName ->
            binding.textViewUserName.text = userName + " 님"
        })

        viewModel.addScheduleSuccess.observe(this.viewLifecycleOwner, Observer { isSuccess ->
            isSuccess?.let{
                if(isSuccess) {
                    showSnackbar("캘린더 추가 완료")
                }else {
                    showSnackbar("캘린더 추가에 실패했습니다.")
                }
            }
        })
        viewModel.setTodoListSuccess.observe(this.viewLifecycleOwner, Observer { isSuccess ->
            isSuccess?.let{
                if(isSuccess) {
                    adapter.update(viewModel.todoList.toList())
                    Log.e("MY","${viewModel.todoList}")
                    binding.progressbarTodoList.isVisible = false
                }else {
                    showSnackbar("해당되는 todoList가 없습니다.")
                    binding.progressbarTodoList.isVisible = false
                }
            }
        })
        setFragmentResultListener()
        viewModel.searchEtDebounce(binding.editTextKeyword.textChangedFlow())
        binding.toggleButtonAsc.setOnClickListener {
            setToggleASC()
        }
        binding.toggleButtonDesc.setOnClickListener {
            setToggleDESC()
        }
    }

    private fun EditText.textChangedFlow(): Flow<String> { // edittext 에서 flow 반환
        return callbackFlow<String> {
            val watcher = @SuppressLint("RestrictedApi") object : TextWatcherAdapter() {
                override fun afterTextChanged(p0: Editable) {
                    trySend(p0.toString())
                    binding.progressbarTodoList.isVisible = true
                }
            }
            addTextChangedListener(watcher)
            awaitClose { removeTextChangedListener(watcher)
            }
        }.conflate()
    }
    private fun setTodoList(keyword: String = "") {
        binding.progressbarTodoList.isVisible = true
        viewModel.setTodoList(keyword)
    }

    private fun setFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            "icalendarData", this.viewLifecycleOwner
        ) { _, bundle ->
            viewModel.addNaverCalendarSchedule(
                bundle.getString("title")?:"",
                bundle.getString("detail")?:"",
                bundle.getString("startDay")?:"",
                bundle.getString("endDay")?:"",
                bundle.getString("startTime")?:"",
                bundle.getString("endTime")?:""
            )
        }
    }

    private fun onItemClickDialog(title : String, date : String) {
        val bundle = Bundle().apply {
            putString("title",title)
            putString("date",date)
        }
        val dialog = AddCalendarDialog()
        dialog.arguments = bundle
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
        viewModel.todoList = mutableListOf()
    }

    private fun lockUntilNaverLogin(){
        binding.textViewNaverLogin.isVisible = true
        binding.buttonNaverLogin.isVisible = true
        binding.todoList.isVisible = false
        binding.textViewUserName.isVisible = false
        binding.buttonNaverLogout.isVisible = false
        binding.toggleButtonDesc.isVisible = false
        binding.toggleButtonAsc.isVisible = false
        binding.editTextKeyword.isVisible = false
    }

    private fun unlockAfterNaverLogin(){
        binding.textViewNaverLogin.isVisible = false
        binding.buttonNaverLogin.isVisible = false
        binding.todoList.isVisible = true
        binding.textViewUserName.isVisible = true
        binding.buttonNaverLogout.isVisible = true
        binding.toggleButtonDesc.isVisible = true
        binding.toggleButtonAsc.isVisible = true
        binding.editTextKeyword.isVisible = true
        viewModel.userName.value = MainViewModel.NaverLoginData.userName
        setTodoList()
    }
    private fun setSnackbar() {
        snackbar = Snackbar.make(binding.root,"", Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
    }
    private fun showSnackbar(msg : String) {
        snackbar.setText(msg).show()
    }
    private fun setToggleASC() {
        viewModel.toggleASC = true
        binding.toggleButtonAsc.isChecked = true
        binding.toggleButtonDesc.isChecked = false
        setTodoList(binding.editTextKeyword.text.toString())
    }
    private fun setToggleDESC() {
        viewModel.toggleASC = false
        binding.toggleButtonAsc.isChecked = false
        binding.toggleButtonDesc.isChecked = true
        setTodoList(binding.editTextKeyword.text.toString())
    }
}