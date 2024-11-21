package com.example.sqlite
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
//import android.widget.RecyclerView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.adapter.UserAdapter
import com.example.sqlite.data.AppDatabase
import com.example.sqlite.model.User
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var saveButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var userList: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de Room Database
        db = AppDatabase.getDatabase(this)

        // Configuración de inputs y botones
        nameInput = findViewById(R.id.nameInput)
        ageInput = findViewById(R.id.ageInput)
        saveButton = findViewById(R.id.saveButton)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Configuración del RecyclerView y el Adapter
        userAdapter = UserAdapter()
        userList = findViewById(R.id.userList)
        userList.layoutManager = LinearLayoutManager(this) // Establece un layout lineal
        userList.adapter = userAdapter // Asocia el adapter con el RecyclerView

        // Cargar usuarios existentes en la base de datos
        loadUsers()

        // Configuración de botones para guardar, actualizar y eliminar
        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull()
            if (name.isNotEmpty() && age != null) {
                saveUser(name, age)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        updateButton.setOnClickListener {
            val id = userAdapter.getSelectedUserId()
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull()
            if (id != null && name.isNotEmpty() && age != null) {
                updateUser(id, name, age)
            } else {
                Toast.makeText(this, "Selecciona un usuario para actualizar.", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            val id = userAdapter.getSelectedUserId()
            if (id != null) {
                deleteUser(id)
            } else {
                Toast.makeText(this, "Selecciona un usuario para eliminar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para cargar todos los usuarios desde la base de datos
    private fun loadUsers() {
        lifecycleScope.launch {
            val users = db.userDao().getAllUsers()
            userAdapter.submitList(users)
        }
    }

    // Función para guardar un nuevo usuario
    private fun saveUser(name: String, age: Int) {
        lifecycleScope.launch {
            val user = User(name = name, age = age)
            db.userDao().insert(user)
            loadUsers()
            clearInputs()
            Toast.makeText(this@MainActivity, "Usuario guardado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para actualizar un usuario existente
    private fun updateUser(id: Int, name: String, age: Int) {
        lifecycleScope.launch {
            val user = User(id = id, name = name, age = age)
            db.userDao().updateUser(user)
            loadUsers()
            clearInputs()
            Toast.makeText(this@MainActivity, "Usuario actualizado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para eliminar un usuario por ID
    private fun deleteUser(id: Int) {
        lifecycleScope.launch {
            db.userDao().deleteUserById(id)
            loadUsers()
            clearInputs()
            Toast.makeText(this@MainActivity, "Usuario eliminado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para limpiar los campos de entrada
    private fun clearInputs() {
        nameInput.text.clear()
        ageInput.text.clear()
    }
}
