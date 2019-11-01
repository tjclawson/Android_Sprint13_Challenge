package com.example.android_sprint13_challenge

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.makeup_list_view.view.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var makeupService: MakeupService

    private val makeupList = mutableListOf<Makeup>()
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).appComponent.inject(this)

        context = this

        recycler_view_main.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MakeupListAdapter(makeupList)
        }

        search_view_main.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                makeupService.getMakeupList("${p0}").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({ data -> recycler_view_main.adapter = MakeupListAdapter(data) },
                        { data -> Toast.makeText(context, data.message, Toast.LENGTH_LONG).show() })

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    inner class MakeupListAdapter(private val makeupList: List<Makeup>): RecyclerView.Adapter<MakeupListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.makeup_list_view, parent, false))
        }

        override fun getItemCount(): Int {
            return makeupList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = makeupList[position]
            holder.nameTextView.text = data.name
            holder.priceTextView.text = data.price
            holder.ratingTextView.text = data.rating
            Picasso.get().load(data.image_link).into(holder.imageView)
        }


        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

            val nameTextView: TextView = view.text_view_name
            val priceTextView: TextView = view.text_view_price
            val ratingTextView: TextView = view.text_view_rating
            val imageView: ImageView = view.image_view_makeup_image
        }
    }
}
