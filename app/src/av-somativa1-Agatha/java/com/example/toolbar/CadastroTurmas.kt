package com.example.toolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.toolbar.databinding.ActivityCadastroTurmasBinding
import com.example.toolbar.databinding.ActivityListagemAlunoBinding

class CadastroTurmas : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroTurmasBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}