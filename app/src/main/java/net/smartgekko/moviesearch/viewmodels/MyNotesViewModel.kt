package net.smartgekko.moviesearch.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.smartgekko.moviesearch.model.database.entities.Notes
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.models.Note
import net.smartgekko.moviesearch.model.repositories.MoviesRepository

class MyNotesViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel(), LifecycleObserver, InetConnectable {
    private var noteId: Long = 0

    override fun initLiveData() {
        MoviesRepository.setLiveData(liveDataToObserve)
    }

    fun getLiveData() = liveDataToObserve

    fun getNoteById(id: Long) = getDataFromDbSource(id)
    fun saveNote(noteToSave: Note) = saveNoteToDb(noteToSave)

    private fun getDataFromDbSource(id: Long) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            liveDataToObserve.postValue(AppState.SuccessNote(MoviesRepository.getNoteById(id)!!))
        }.start()
    }

    private fun saveNoteToDb(note: Note) {
        liveDataToObserve.value = AppState.Loading
        Thread {

            fun onNotesFetched() {
                liveDataToObserve.postValue(AppState.SuccessNote(note))
            }

            fun onError() {}

            MoviesRepository.addToNotes(
                Notes(
                    note.movie_id,
                    note.stars,
                    note.note_positive,
                    note.note_negative,
                    note.resume
                ),
                onSuccess = ::onNotesFetched,
                onError = ::onError
            )
        }.start()
    }
}