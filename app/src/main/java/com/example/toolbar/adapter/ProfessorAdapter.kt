package com.example.toolbar.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.toolbar.CadastroProfessor
import com.example.toolbar.databinding.ActivityListagemProfessorItemBinding
import com.example.toolbar.model.Professor

class ProfessorAdapter (
    private val context: Context,
    private val deleteCallback: (Int) -> Unit
    ): RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {
    private var professores: List<Professor> = emptyList()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfessorAdapter.ProfessorViewHolder {
        val binding = ActivityListagemProfessorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfessorViewHolder (binding)
    }

    override fun onBindViewHolder(holder: ProfessorAdapter.ProfessorViewHolder, position: Int) {
        val professor = professores[position]
        holder.bind(professor)

        holder.binding.btnDeleteP.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Excluir Professor")
                .setMessage("Deseja realmente excluir o professor ${professor.nome}?")
                .setPositiveButton("Sim") { _, _ ->
                    deleteCallback(professor.id)
                }
                .setNegativeButton("Não", null)
                .show()
        }

        holder.binding.btnEditP.setOnClickListener {
            val intent = Intent(context, CadastroProfessor::class.java)
            intent.putExtra("PROFESSOR_ID", professor.id)
            intent.putExtra("PROFESSOR_NOME", professor.nome)
            intent.putExtra("PROFESSOR_EMAIL", professor.email)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return professores.size
    }

    fun setData(professores: List<Professor>) {
        this.professores = professores
        notifyDataSetChanged()
    }

    inner class ProfessorViewHolder(val binding: ActivityListagemProfessorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(professor: Professor) {
            binding.apply {
                txtNomeProf.text = professor.nome
                txtEmailProf.text = professor.email
            }
        }
    }
}