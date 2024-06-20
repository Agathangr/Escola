package com.example.toolbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toolbar.Api.EnderecoApi
import com.example.toolbar.Api.RetrofitHelper
import com.example.toolbar.adapter.AlunoAdapter
import com.example.toolbar.databinding.ActivityListagemAlunoBinding
import com.example.toolbar.model.Aluno
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListagemAluno : AppCompatActivity() {

    private val binding by lazy {
        ActivityListagemAlunoBinding.inflate(layoutInflater)
    }

    //variável do Adapter
    private  lateinit var alunoAdapter: AlunoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        setupRecyclerView()

        //variáveis deo retrofit
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.getAlunos()

        //Body Alunos
        call.enqueue(object : Callback<List<Aluno>> {
            override fun onResponse(call: Call<List<Aluno>>, response: Response<List<Aluno>>) {
                if (response.isSuccessful){
                    response.body()?.let { alunos -> alunoAdapter.setData(alunos)
                    }
                }
            }

            override fun onFailure(call: Call<List<Aluno>>, t: Throwable) {
                //Falha
            }
        })





        binding.EncaminharA.setOnClickListener {
            val intent = Intent (this, CadastroAluno::class.java)
            startActivity( intent )
        }

    }

    private fun setupRecyclerView() {
        alunoAdapter = AlunoAdapter(this) { alunoId ->
            deleteAluno(alunoId)
        }
        binding.alunosRecyclerView.layoutManager = LinearLayoutManager (this)
        binding.alunosRecyclerView.adapter = alunoAdapter
    }

    private fun loadAlunos() {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.getAlunos()

        call.enqueue(object : Callback<List<Aluno>> {
            override fun onResponse(call: Call<List<Aluno>>, response: Response<List<Aluno>>) {
                if (response.isSuccessful) {
                    response.body()?.let { alunos ->
                        alunoAdapter.setData(alunos)
                    }
                } else {
                    Toast.makeText(
                        this@ListagemAluno,
                        "Falha ao carregar alunos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Aluno>>, t: Throwable) {
                Toast.makeText(this@ListagemAluno, "Erro: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun deleteAluno(alunoId: Int) {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.excluirAluno(alunoId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ListagemAluno,
                        "Aluno excluído com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadAlunos() // Atualizar a lista de alunos após exclusão
                } else {
                    Toast.makeText(
                        this@ListagemAluno,
                        "Falha ao excluir aluno",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ListagemAluno, "Erro: ${t.message}", Toast.LENGTH_SHORT)
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