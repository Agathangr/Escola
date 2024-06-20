package com.example.toolbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.toolbar.Api.EnderecoApi
import com.example.toolbar.Api.RetrofitHelper
import com.example.toolbar.adapter.ProfessorAdapter
import com.example.toolbar.adapter.TurmaAdapter
import com.example.toolbar.databinding.ActivityListagemTurmaBinding
import com.example.toolbar.databinding.ActivityMainBinding
import com.example.toolbar.model.Professor
import com.example.toolbar.model.Turma
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListagemTurma : AppCompatActivity() {
    private val binding by lazy {
        ActivityListagemTurmaBinding.inflate(layoutInflater)
    }

    //variável do Adapter
    private  lateinit var turmaAdapter: TurmaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()

        //variáveis deo retrofit
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.getTurma()

        //Body
        call.enqueue(object : Callback<List<Turma>> {
            override fun onResponse(call: Call<List<Turma>>, response: Response<List<Turma>>) {
                if (response.isSuccessful){
                    response.body()?.let { turmas -> turmaAdapter.setData(turmas)
                    }
                }
            }

            override fun onFailure(call: Call<List<Turma>>, t: Throwable) {
                //Falha
            }
        })

        binding.EncaminharT.setOnClickListener {
            val intent = Intent (this, CadastroTurmas::class.java)
            startActivity( intent )
        }
    }

    private fun setupRecyclerView() {
        turmaAdapter = TurmaAdapter(this) { turmaId ->
            deleteTurma(turmaId)
        }
        binding.turmasRecyclerView.layoutManager = LinearLayoutManager (this)
        binding.turmasRecyclerView.adapter = turmaAdapter
    }
    private fun loadTurma() {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.getTurma()

        call.enqueue(object : Callback<List<Turma>> {
            override fun onResponse(call: Call<List<Turma>>, response: Response<List<Turma>>) {
                if (response.isSuccessful) {
                    response.body()?.let { turmas ->
                        turmaAdapter.setData(turmas)
                    }
                } else {
                    Toast.makeText(
                        this@ListagemTurma,
                        "Falha ao carregar alunos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Turma>>, t: Throwable) {
                Toast.makeText(this@ListagemTurma, "Erro: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun deleteTurma(turmaId: Int) {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.excluirTurma(turmaId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ListagemTurma,
                        "Turma excluída com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadTurma() // Atualizar a lista de alunos após exclusão
                } else {
                    Toast.makeText(
                        this@ListagemTurma,
                        "Falha ao excluir turma",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ListagemTurma, "Erro: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    //Ação de clique nos itens do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Testando a ação de clique
        when (item.itemId) {
            R.id.menu_home -> {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                val intent = Intent (this, MainActivity::class.java)
                startActivity( intent )
            }

            R.id.menu_alunos -> {
                Toast.makeText(this, "Aluno", Toast.LENGTH_SHORT).show()
                val intent = Intent (this, ListagemAluno::class.java)
                startActivity( intent )
            }

            R.id.menu_professores -> {
                Toast.makeText(this, "Professores", Toast.LENGTH_SHORT).show()
                val intent = Intent (this, ListagemProfessor::class.java)
                startActivity( intent )
            }

            R.id.menu_turmas -> {
                Toast.makeText(this, "Turmas", Toast.LENGTH_SHORT).show()
                val intent = Intent (this, ListagemTurma::class.java)
                startActivity( intent )
            }
            R.id.menu_sair -> {
                Toast.makeText(this, "Sair", Toast.LENGTH_SHORT).show()
                val intent = Intent (this, Login::class.java)
                startActivity( intent )
            }
        }
        return true
    }
}



