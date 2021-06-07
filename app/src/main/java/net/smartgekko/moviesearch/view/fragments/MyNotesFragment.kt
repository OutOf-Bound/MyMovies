package net.smartgekko.moviesearch.view.fragments

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.models.Note
import net.smartgekko.moviesearch.viewmodels.MyNotesViewModel


class MyNotesFragment : Fragment() {
    lateinit var resumeTV: EditText
    lateinit var positiveTV: EditText
    lateinit var negativeTV: EditText
    lateinit var saveNoteIV: ImageView
    lateinit var starCounter: TextView
    lateinit var starsLayout: LinearLayout
    lateinit var saveButton: LinearLayout
    lateinit var viewPager: ViewPager2


    companion object {
        var noteId: Long = 0
        fun newInstance(note: Long): MyNotesFragment {
            noteId = note
            return MyNotesFragment()
        }
    }

    private lateinit var viewModel: MyNotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.my_notes_fragment, container, false)

        resumeTV = view.findViewById(R.id.resumeTV)
        positiveTV = view.findViewById(R.id.positiveTV)
        negativeTV = view.findViewById(R.id.negativeTV)
        starCounter = view.findViewById(R.id.starCounter)
        saveNoteIV = view.findViewById(R.id.saveNoteIV)
        saveButton = view.findViewById(R.id.saveButton)
        saveButton.setOnClickListener(View.OnClickListener {
            saveNote()
        })
        val scrollView = view.findViewById<ScrollView>(R.id.scrollView2)
        scrollView.getLayoutParams().height =
            getScreenHeight() - convertDpToPixel(120, view.context).toInt()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        starsLayout = activity?.findViewById(R.id.starsHeaderLayout)!!
        viewPager = activity?.findViewById(R.id.viewPager2)!!
        viewModel = ViewModelProvider(this).get(MyNotesViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getNoteById(noteId)
        lifecycle.addObserver(viewModel)
    }

    override fun onPause() {
        saveNote()
        super.onPause()
    }

    private fun renderData(appState: AppState) {
        val loadingLayout: FrameLayout? = view?.findViewById(R.id.loadingLayoutPopular)

        when (appState) {
            is AppState.SuccessNote -> {
                setData(appState.note)
            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
            }
        }
    }

    private fun setData(note: Note) {

        resumeTV.setText(note.resume)
        positiveTV.setText(note.note_positive)
        negativeTV.setText(note.note_negative)
        starCounter.setText(note.stars.toString())

        setStars(starsLayout, note.stars)
    }

    private fun setStars(layout: LinearLayout, starsCount: Int) {

        layout.removeAllViews()

        if (starsCount > 0) {
            for (i in 1..starsCount) {
                val iv = ImageView(context)
                iv.setImageResource(R.drawable.ic_star_white_filled)
                iv.setOnClickListener {
                    starCounter.setText(i.toString())
                    setStars(layout, i)
                }
                layout.addView(iv)
                iv.layoutParams.width = 60
            }

            for (i in starsCount + 1..5) {
                val iv = ImageView(context)
                iv.setImageResource(R.drawable.ic_star_white_stroke)
                iv.setOnClickListener {
                    starCounter.setText(i.toString())
                    setStars(layout, i)
                }
                layout.addView(iv)
                iv.layoutParams.width = 60
            }
        } else {
            for (i in 1..5) {
                val iv = ImageView(context)
                iv.setImageResource(R.drawable.ic_star_white_stroke)
                iv.setOnClickListener {
                    starCounter.setText(i.toString())
                    setStars(layout, i)
                }
                layout.addView(iv)
                iv.layoutParams.width = 60
            }
        }
    }

    private fun saveNote() {

        var note: Note = Note(
            noteId,
            starCounter.text.toString().toInt(),
            positiveTV.text.toString(),
            negativeTV.text.toString(),
            resumeTV.text.toString()
        )
        viewModel.saveNote(note)
        collapseBottomSheet(activity?.findViewById(R.id.bottomSheetLayout)!!)
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().getDisplayMetrics().heightPixels
    }

    fun convertDpToPixel(dp: Int, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun collapseBottomSheet(botomsheet: LinearLayout) {
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from<View>(botomsheet)
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    fun expandBottomSheet(botomsheet: LinearLayout) {
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from<View>(botomsheet)
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

}