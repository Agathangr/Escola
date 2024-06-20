package com.example.toolbar

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.toolbar.Api.EnderecoApi
import com.example.toolbar.Api.RetrofitHelper
import com.example.toolbar.databinding.ActivityCadastroProfessorBinding
import com.example.toolbar.databinding.ActivityListagemAlunoBinding
import com.example.toolbar.model.Aluno
import com.example.toolbar.model.Professor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroProfessor : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroProfessorBinding.inflate(layoutInflater)
    }

    private var professorId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        professorId = intent.getIntExtra("PROFESSOR_ID", -1)
        if (professorId != -1) {
            binding.EditP.setText(intent.getStringExtra("PROFESSOR_NOME"))
            binding.EditEm.setText(intent.getStringExtra("PROFESSOR_EMAIL"))
        }
        binding.btnCP.setOnClickListener {
            val nome = binding.EditP.text.toString()
            val email = binding.EditEm.text.toString()

            if(nome.isNotEmpty() && email.isNotEmpty()){

                //Verificar açaão de Salva ou Editar
                val professor = Professor ( professorId ?: 0, nome, email)
                if(professorId != null && professorId!= -1){
                    alterarProfessor(professor)
                }else{
                    salvarProfessor(professor)
                }

            }else{
                Toast.makeText(this,
                    "Por favor preencha todos os campos!",
                    Toast.LENGTH_SHORT).show()
            }
        }
        }
private fun salvarProfessor(professor: Professor) {
    val retrofit = RetrofitHelper.getRetrofitInstance()
    val service = retrofit.create(EnderecoApi::class.java)
    val call = service.inserirProfessor(professor)

    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this@CadastroProfessor, "Erro ao salvar professor.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(this@CadastroProfessor, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

private fun alterarProfessor(professor: Professor) {
    val retrofit = RetrofitHelper.getRetrofitInstance()
    val service = retrofit.create(EnderecoApi::class.java)
    val call = service.alterarProfessor(professor)

    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                setResult(Activity.RESULT_OK, Intent().putExtra("PROFESSOR_ALTERADO", true))
                finish()
            } else {
                Toast.makeText(this@CadastroProfessor, "Erro ao alterar professor.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(this@CadastroProfessor, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
}

