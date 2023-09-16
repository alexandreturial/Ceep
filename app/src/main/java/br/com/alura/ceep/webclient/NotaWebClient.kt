package br.com.alura.ceep.webclient

import android.util.Log
import br.com.alura.ceep.model.Nota
import br.com.alura.ceep.webclient.model.NotaRequisicao
import br.com.alura.ceep.webclient.services.NotaService

private const val TAG = "webclient"


class NotaWebClient {
    private val notaService: NotaService = RetrofitInicializador().notaService;
    suspend  fun buscaTodas() : List<Nota>? {
        return try {
            val notasResposta = notaService.buscaTodas()
             notasResposta.map{notaResposta ->
                notaResposta.nota
            }
        } catch (e: Exception) {
            Log.e(TAG, "busca todos: ", e)
            null
        }
    }

    suspend fun salvar(nota: Nota): Boolean {
        try {
            val response = notaService.salvar(
                nota.id, NotaRequisicao(
                    titulo = nota.titulo,
                    descricao = nota.descricao,
                    imagem = nota.imagem
                )
            )
            return response.isSuccessful;
        } catch (e: Exception) {
            Log.e(TAG, "Falhar ao tentar salvar", e)
        }
        return false
    }

    suspend fun remove(id: String) : Boolean {
        try {
            notaService.remove(id)
            return true;
        }catch (e: Exception){
            Log.e(TAG, "Falhar ao tentar deletar", e)
            return false
        }

    }
}