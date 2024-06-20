package com.example.toolbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toolbar.Api.EnderecoApi
import com.example.toolbar.Api.RetrofitHelper
import com.example.toolbar.adapter.AlunoAdapter
import com.example.toolbar.adapter.ProfessorAdapter
import com.example.toolbar.databinding.ActivityListagemAlunoBinding
import com.example.toolbar.databinding.ActivityListagemProfessorBinding
import com.example.toolbar.model.Aluno
import com.example.toolbar.model.Professor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListagemProfessor : AppCompatActivity() {
    private val binding by lazy {
        ActivityListagemProfessorBinding.inflate(layoutInflater)
    }

    //variável do Adapter
    private  lateinit var professorAdapter: ProfessorAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()

        //variáveis deo retrofit
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.getProfessor()

        //Body
        call.enqueue(object : Callback<List<Professor>> {
            override fun onResponse(call: Call<List<Professor>>, response: Response<List<Professor>>) {
                if (response.isSuccessful){
                    response.body()?.let { professores -> professorAdapter.setData(professores)
                    }
                }
            }

            override fun onFailure(call: Call<List<Professor>>, t: Throwable) {
                //Falha
            }
        })

        binding.EncaminharP.setOnClickListener {
            val intent = Intent (this, CadastroProfessor::class.java)
            startActivity( intent )
        }
    }

    private fun setupRecyclerView() {
        professorAdapter = ProfessorAdapter(this) { professorId ->
            deleteProfessor(professorId)
        }
        binding.professoresRecyclerView.layoutManager = LinearLayoutManager (this)
        binding.professoresRecyclerView.adapter = professorAdapter
    }
    private fun loadProfessor() {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.getProfessor()

        call.enqueue(object : Callback<List<Professor>> {
            override fun onResponse(call: Call<List<Professor>>, response: Response<List<Professor>>) {
                if (response.isSuccessful) {
                    response.body()?.let { professores ->
                        professorAdapter.setData(professores)
                    }
                } else {
                    Toast.makeText(
                        this@ListagemProfessor,
                        "Falha ao carregar alunos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Professor>>, t: Throwable) {
                Toast.makeText(this@ListagemProfessor, "Erro: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun deleteProfessor(professorId: Int) {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.excluirProfessor(professorId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ListagemProfessor,
                        "Professor excluído com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadProfessor() // Atualizar a lista de alunos após exclusão
                } else {
                    Toast.makeText(
                        this@ListagemProfessor,
                        "Falha ao excluir professor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ListagemProfessor, "Erro: ${t.message}", Toast.LENGTH_SHORT)
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