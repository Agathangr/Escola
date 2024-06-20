package com.example.toolbar.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.toolbar.CadastroTurmas
import com.example.toolbar.databinding.ActivityListagemTurmaItemBinding
import com.example.toolbar.model.Turma


class TurmaAdapter (private val context: Context,
                    private val deleteCallback: (Int) -> Unit
): RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder>() {
    private var turmas: List<Turma> = emptyList()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TurmaAdapter.TurmaViewHolder {
        val binding = ActivityListagemTurmaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TurmaViewHolder (binding)
    }

    override fun onBindViewHolder(holder: TurmaAdapter.TurmaViewHolder, position: Int) {
        val turma = turmas[position]
        holder.bind(turma)

        holder.binding.btnDeleteT.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Excluir Turma")
                .setMessage("Deseja realmente excluir a turma ${turma.nome}?")
                .setPositiveButton("Sim") { _, _ ->
                    deleteCallback(turma.id)
                }
                .setNegativeButton("Não", null)
                .show()
        }

        holder.binding.btnEditT.setOnClickListener {
            val intent = Intent(context, CadastroTurmas::class.java)
            intent.putExtra("TURMA_ID", turma.id)
            intent.putExtra("TURMA_NOME", turma.nome)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return turmas.size
    }

    fun setData(turmas: List<Turma>) {
        this.turmas = turmas
        notifyDataSetChanged()
    }

    inner class TurmaViewHolder(val binding: ActivityListagemTurmaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(turma: Turma) {
            binding.apply {
                txtNomeTurma.text = turma.nome
            }
        }
    }
}
