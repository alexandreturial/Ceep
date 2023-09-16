package br.com.alura.ceep.repository

import br.com.alura.ceep.database.dao.NotaDao
import br.com.alura.ceep.model.Nota
import br.com.alura.ceep.webclient.NotaWebClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NotaRepository(private val dao: NotaDao, private val webClient: NotaWebClient) {
    fun buscaTodas () : Flow<List<Nota>> {
        return dao.buscaTodas()
    }

    private suspend fun atualizaTodas(){

        webClient.buscaTodas()?.let {notas ->
            val notasSnicronizadas = notas.map { nota ->
                nota.copy(sincronizada = true)
            }
            dao.salva(notasSnicronizadas)
        }
    }

    fun buscaPorId(id: String): Flow<Nota> {
        return dao.buscaPorId(id)
    }

    suspend fun remove(id: String) {
        dao.desativa(id)

        deletedNota(id)

    }

    private suspend fun deletedNota(id: String) {
        if (webClient.remove(id)) {
            dao.remove(id)
        }
    }

    suspend fun salva(nota: Nota) {
        dao.salva(nota)
        if(webClient.salvar(nota)){
            val notaSincronizada = nota.copy(sincronizada = true, desativada = false);

            dao.salva(notaSincronizada)
        }
    }
    suspend fun sincroniza(){
        val notasDesativadas = dao.buscaDesativadas().first()

        notasDesativadas.forEach{ notaDesativada ->
            deletedNota(notaDesativada.id)
        }

        val notasNaoSincronizada = dao.buscaNaoSIncronizadas().first()

        notasNaoSincronizada.forEach{notaNaoSincronizada ->
            salva(notaNaoSincronizada)

        }
        atualizaTodas()
    }

}