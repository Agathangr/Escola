package com.example.toolbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.toolbar.databinding.ActivityListatagemAlunoItemBinding
import com.example.toolbar.databinding.ActivityMainBinding

class ListatagemAlunoItem : AppCompatActivity() {
    private val binding by lazy {
        ActivityListatagemAlunoItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}