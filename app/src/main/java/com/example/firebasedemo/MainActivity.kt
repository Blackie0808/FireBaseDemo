package com.example.firebasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasedemo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("myDB")


        if (auth.currentUser != null){ //already sign in
            binding.tvStatus.text = auth.currentUser!!.email
        }


        binding.btnRegister.setOnClickListener(){

            registerUser("teh@gmail.com","123456")

        }

        binding.btnSignIn.setOnClickListener(){

            signIn("teh@gmail.com","123456")
        }

        binding.btnSignOut.setOnClickListener(){
            signOut()
        }

        ////////////////////////

        binding.btnInsert.setOnClickListener(){
            val newStudent = Student("W223","Tan Ah Lok","RSF")
            addNewStudent(newStudent)
        }

        binding.btnRead.setOnClickListener(){
            readData("W123")
        }

        binding.btnDelete.setOnClickListener(){
            deleteData("W123")
        }

    }

    private fun deleteData(id: String) {
        database.child("Student").child(id).removeValue()
            .addOnSuccessListener {
                binding.tvStatus.text ="Record deleted"
            }

            .addOnFailureListener { e->
                binding.tvStatus.text =e.message
            }
    }

    private fun readData(id :String) {
        database.child("Student").child(id).get()
            .addOnSuccessListener { rec->

                if(rec.child("id").value !=null){ //successful found the id
                    binding.tvStatus.text = rec.child("name").value.toString()
                }else{
                    binding.tvStatus.text ="Record not found"
                }
            }
            .addOnFailureListener {e->
                binding.tvStatus.text =e.message
            }
    }

    private fun addNewStudent(newStudent: Student) {
        database.child("Student")
            .child(newStudent.id).setValue(newStudent)
            .addOnSuccessListener {
                binding.tvStatus.text = "New student added"
            }

            .addOnFailureListener {e->
                binding.tvStatus.text = e.message

            }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        binding.tvStatus.text = "Sign Out"
    }

    private fun signIn(email: String, psw: String) {
        auth.signInWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                binding.tvStatus.text = email
            }
            .addOnFailureListener{ e->
                binding.tvStatus.text = e.message
            }
    }

    private fun registerUser(email: String, psw:String) {
        auth.createUserWithEmailAndPassword(email,psw)
            .addOnSuccessListener {
                binding.tvStatus.text = "New user:${email}"
            }
            .addOnFailureListener{ e->
                binding.tvStatus.text = e.message
            }
    }

}