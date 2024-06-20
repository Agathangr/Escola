package com.example.toolbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.toolbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAluno.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.ImgP.setOnClickListener {
            val intent = Intent(this, ListagemProfessor::class.java)
            startActivity(intent)
        }
        binding.ImgA.setOnClickListener {
            val intent = Intent(this, ListagemAluno::class.java)
            startActivity(intent)
        }
        binding.ImgT.setOnClickListener {
            val intent = Intent(this, ListagemTurma::class.java)
            startActivity(intent)
        }

    }
}