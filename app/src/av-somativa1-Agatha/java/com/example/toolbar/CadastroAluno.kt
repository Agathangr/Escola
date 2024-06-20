package com.example.toolbar

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.toolbar.databinding.ActivityCadastroAlunoBinding
import com.example.toolbar.databinding.ActivityListagemAlunoBinding

class CadastroAluno : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroAlunoBinding.inflate(layoutInflater)
    }

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

        acessarGaleria()


        binding.perfil.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            gerenciaCamera.launch( intent )
        }
    }

    private fun acessarGaleria() {
        binding.galeria.setOnClickListener {
            gestaoGaleria.launch("image/*")
        }
    }
}