package com.example.toolbar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.toolbar.Api.EnderecoApi
import com.example.toolbar.Api.RetrofitHelper
import com.example.toolbar.databinding.ActivityCadastroAlunoBinding
import com.example.toolbar.model.Aluno
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroAluno : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroAlunoBinding.inflate(layoutInflater)
    }

    //Variavel do Id do Aluno
    private var alunoId: Int? = null

    private var permissaoCamera = false
    private var permissaoGaleria = false

    private var bitmapImagemCamera: Bitmap? = null

    private val gestaoGaleria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.perfil.setImageURI(uri)
            Toast.makeText(this, "Imagem Selecionada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Nenhuma imagem Selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    //Permissão da Cãmera
    private val gerenciaCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultadoActivity ->
        bitmapImagemCamera = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resultadoActivity.data?.extras?.getParcelable("data", Bitmap::class.java)
        } else {
            resultadoActivity.data?.extras?.getParcelable("data")
        }
        binding.perfil.setImageBitmap(bitmapImagemCamera)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        alunoId = intent.getIntExtra("ALUNO_ID",-1)
        if (alunoId != -1) {
            binding.EditA.setText(intent.getStringExtra("ALUNO_NOME"))
            binding.editC.setText(intent.getStringExtra("ALUNO_CPF"))
            binding.EditE.setText(intent.getStringExtra("ALUNO_EMAIL"))
            binding.editM.setText(intent.getStringExtra("ALUNO_MATRICULA"))
        }
        binding.btnCA.setOnClickListener {
            val nome = binding.EditA.text.toString()
            val cpf = binding.editC.text.toString()
            val email = binding.EditE.text.toString()
            val matricula = binding.editM.text.toString()

            //Verificação se os campos foram preechidos
            if(nome.isNotEmpty() && cpf.isNotEmpty() && email.isNotEmpty() && matricula.isNotEmpty()){
                
                //Verificar açaão de Salva ou Editar
                val aluno = Aluno ( alunoId ?: 0, nome, cpf, email, matricula)
                if(alunoId != null && alunoId!= -1){
                    alterarAluno(aluno)
                }else{
                    salvarAluno(aluno)
                }

            }else{
                Toast.makeText(this,
                    "Por favor preencha todos os campos!",
                    Toast.LENGTH_SHORT).show()
            }
        }
        acessarGaleria()


        binding.perfil.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            gerenciaCamera.launch( intent )
        }
    }
    private fun salvarAluno(aluno: Aluno) {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.inserirAluno(aluno)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@CadastroAluno, "Erro ao salvar aluno.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CadastroAluno, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun alterarAluno(aluno: Aluno) {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val service = retrofit.create(EnderecoApi::class.java)
        val call = service.alterarAluno(aluno)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK, Intent().putExtra("ALUNO_ALTERADO", true))
                    finish()
                } else {
                    Toast.makeText(this@CadastroAluno, "Erro ao alterar aluno.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CadastroAluno, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun acessarGaleria() {
        binding.galeria.setOnClickListener {
            gestaoGaleria.launch("image/*")
        }
    }
}