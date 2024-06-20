package com.example.toolbar

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.toolbar.Api.EnderecoApi
import com.example.toolbar.Api.RetrofitHelper
import com.example.toolbar.databinding.ActivityCadastroTurmasBinding
import com.example.toolbar.databinding.ActivityListagemAlunoBinding
import com.example.toolbar.model.Professor
import com.example.toolbar.model.Turma
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroTurmas : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroTurmasBinding.inflate(layoutInflater)
    }

    private var turmaId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        turmaId = intent.getIntExtra("TURMA_ID", -1)
        if (turmaId != -1) {
            binding.EditT.setText(intent.getStringExtra("PROFESSOR_NOME"))
        }
        binding.btnCT.setOnClickListener {
            val nome = binding.EditT.text.toString()

            if (nome.isNotEmpty()) {

                //Verificar açaão de Salva ou Editar
                val turma = Turma(turmaId ?: 0, nome)
                if (turmaId != null && turmaId != -1) {
                    alterarTurma(turma)
                } else {
                    salvarTurma(turma)
                }

            } else {
                Toast.makeText(
                    this,
                    "Por favor preencha todos os campos!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun salvarTurma(turma: Turma) {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.inserirTurma(turma)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@CadastroTurmas, "Erro ao salvar turma.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CadastroTurmas, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun alterarTurma(turma: Turma) {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.alterarTurma(turma)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK, Intent().putExtra("TURMA_ALTERADA", true))
                    finish()
                } else {
                    Toast.makeText(this@CadastroTurmas, "Erro ao alterar turma.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CadastroTurmas, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}