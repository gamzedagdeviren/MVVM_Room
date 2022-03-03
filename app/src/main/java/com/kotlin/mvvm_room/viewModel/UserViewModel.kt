package com.kotlin.mvvm_room.viewModel

import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.*
import com.kotlin.mvvm_room.Event
import com.kotlin.mvvm_room.model.User
import com.kotlin.mvvm_room.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()
    val inputPhone = MutableLiveData<String>()
    val inputUsername = MutableLiveData<String>()
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private lateinit var userToUpdateOrDelete: User
    private var isUpdateOrDelete = false

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (inputUsername.value == null) {
            statusMessage.value = Event("Please enter user name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter user's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter a correct email address")
        } else {
            if (isUpdateOrDelete) {
                userToUpdateOrDelete.name = inputName.value!!
                userToUpdateOrDelete.email = inputEmail.value!!
                userToUpdateOrDelete.phone = inputPhone.value!!
                userToUpdateOrDelete.username = inputUsername.value!!
                updateUser(userToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                val phone = inputPhone.value!!
                val username = inputUsername.value!!
                insertUser(User(0, name, email, username, phone))
                inputName.value = ""
                inputEmail.value = ""
                inputUsername.value = ""
                inputPhone.value = ""
            }
        }
    }

    private fun updateUser(user: User) = viewModelScope.launch {
        val noOfRows = repository.update(user)
        if (noOfRows > 0) {
            inputName.value = ""
            inputEmail.value = ""
            inputUsername.value = ""
            inputPhone.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun insertUser(user: User) = viewModelScope.launch {val newRowId = repository.insert(user)
        if (newRowId > -1) {
            statusMessage.value = Event("User Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
        //repository.insert(user)
    }

    fun getSavedSubscribers() = liveData {
        repository.subscribers.collect {
            emit(it)
        }
    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Subscribers Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteUser(userToUpdateOrDelete)
        } else {
            clearAll()
        }
    }
    private fun deleteUser(user: User) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(user)
        if (noOfRowsDeleted > 0) {
            inputName.value = ""
            inputEmail.value = ""
            inputPhone.value = ""
            inputUsername.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }


    fun initUpdateAndDelete(user: User) {
        inputName.value = user.name
        inputEmail.value = user.email
        inputPhone.value = user.phone
        inputUsername.value = user.username
        isUpdateOrDelete = true
        userToUpdateOrDelete = user
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }
}