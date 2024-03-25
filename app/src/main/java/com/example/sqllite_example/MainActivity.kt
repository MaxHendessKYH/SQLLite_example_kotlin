package com.example.sqllite_example

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.sqllite_example.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dbHandler : DatabaseHandler


    private var selectedITemPosition = -1
    var currentUserItem: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHandler = DatabaseHandler(
            this)
        showUsers()
        //Buttons
        binding.btnAdd.setOnClickListener {
            addUser()
        }
        binding.btnUpdate.setOnClickListener {

            if(currentUserItem != null){
                updateUser()
            }
        }
        //Lists
        binding.lvUsers.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position , id ->
      val selectedItem = parent.getItemAtPosition(position) as User
            deleteUser(selectedItem)
        }


        binding.lvUsers.onItemClickListener = AdapterView.OnItemClickListener {parent, view, position , id ->
            if(selectedITemPosition != -1)
            {
                val previousSelectedItem = parent.getChildAt(selectedITemPosition)
                previousSelectedItem.setBackgroundColor(Color.TRANSPARENT)
            }
            selectedITemPosition = position
            currentUserItem = parent.getItemAtPosition(position) as User
            view.setBackgroundColor(Color.GREEN)
        }
    }
    //#region CRUD
    private fun updateUser(){
        try {

            val name = binding.etName.text.toString()
            val score = binding.etScore.text.toString().toDouble()
            val isHuman = binding.switchIshooman.isChecked

            val user = User(currentUserItem!!.id, name ,score, isHuman)
            dbHandler.updateUser(user)

            binding.etName.text.clear()
            binding.etScore.text.clear()
            showUsers()

        }catch(e: Exception) {
            println(e.stackTrace)
            Toast.makeText(this,"error update user", Toast.LENGTH_SHORT).show()
        }
    }
private fun deleteUser(user:User): Boolean{
    val result = dbHandler.deleteUser(user)
    showUsers()
    return result
}
    private fun showUsers(){
        val users = dbHandler.getAllUsers()
        val arrayAdapter = ArrayAdapter(this , android.R.layout.simple_list_item_1, users)

        binding.lvUsers.adapter = arrayAdapter
    }
    private fun addUser()
    {
        try {

        val name = binding.etName.text.toString()
        val score = binding.etScore.text.toString().toDouble()
        val isHuman = binding.switchIshooman.isChecked

        val user = User(-1, name ,score, isHuman)
        dbHandler.addUser(user)
            binding.etName.text.clear()
            binding.etScore.text.clear()
            showUsers()
        }catch(e: Exception) {
            println(e.stackTrace)
            Toast.makeText(this,"error add user", Toast.LENGTH_SHORT).show()
        }
    }
    //#endregion
}