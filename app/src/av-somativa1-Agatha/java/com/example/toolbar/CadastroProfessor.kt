package com.example.toolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.toolbar.databinding.ActivityCadastroProfessorBinding
import com.example.toolbar.databinding.ActivityListagemAlunoBinding

class CadastroProfessor : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroProfessorBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}