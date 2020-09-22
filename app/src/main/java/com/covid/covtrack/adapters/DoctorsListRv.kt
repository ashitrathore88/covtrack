package com.covid.covtrack.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.models.Doctors
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.PreferenceUtil

class DoctorsListRv (
    val doclist : ArrayList<Doctors.Doctor>, val listener: onItemClickListener
): RecyclerView.Adapter<DoctorsListRv.ViewHolder>(){
    var localeStr : String? = null
    private lateinit var parentContext : Context

    init {
        localeStr = PreferenceUtil.ins.getValueString("language");
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
        val elevation = itemView.resources.getDimension(R.dimen.cardElevation)
        ViewCompat.setElevation(itemView, elevation)
        parentContext = parent.context
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(doclist[position], position, doclist.size)
        val model = doclist[position]

        if (localeStr == "pa") {
            holder.tv_name.text = model.doctor_name_pb
            holder.tv_speciality.text = model.speciality_pb
            holder.tv_hospital.text = model.hospital_name_pb
        } else if (localeStr=="en" ) {
            holder.tv_name.text = model.doctor_name
            holder.tv_speciality.text = model.speciality
            holder.tv_hospital.text = model.hospital_name
        }else {
            holder.tv_name.text = model.doctor_name
            holder.tv_speciality.text = model.speciality
            holder.tv_hospital.text = model.hospital_name
        }


        holder.callBtn.setOnClickListener {
            listener.onItemClick(position,model.mobile_no)
        }
    }

    override fun getItemCount(): Int {
        return doclist.size
    }
    interface onItemClickListener{
        fun onItemClick(position: Int, mobileNum: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val callBtn = itemView.findViewById(R.id.call_btn) as ImageView
        val tv_name = itemView.findViewById(R.id.tv_name) as TextView
        val tv_speciality = itemView.findViewById(R.id.tv_speciality) as TextView
        val tv_hospital = itemView.findViewById(R.id.tv_hospital) as TextView

        private val contentPadding: Int = Constants.dp2px(16).toInt()


        fun bind(item: Doctors.Doctor, position: Int, size: Int) = with(itemView) {
            bindBackground(itemView, position, size)
            //bindOutlineProvider(itemView, position, size)


        }


        private val defaultOutline: ViewOutlineProvider? by lazy {
            if (Constants.isLollipop()) ViewOutlineProvider.BACKGROUND else null
        }
        private val fixedOutline: ViewOutlineProvider? by lazy {
            if (Constants.isLollipop()) {
                object : ViewOutlineProvider() {
                    @SuppressLint("NewApi")
                    override fun getOutline(view: View, outline: Outline) {
                        outline.setRect(
                            0,
                            view.resources.getDimensionPixelSize(R.dimen.cardElevation),
                            view.width,
                            view.height
                        )
                    }
                }
            } else null
        }

        private fun bindBackground(itemView: View, position: Int, size: Int) {
            val drawableRes: Int = when {
                size == 1 -> R.drawable.item_top_bottom
                position == 0 -> R.drawable.item_top
                position == size - 1 -> R.drawable.item_bottom
                else -> R.drawable.item_middle
            }
            itemView.background = ContextCompat
                .getDrawable(itemView.context, drawableRes)
            itemView.setPadding(contentPadding, contentPadding,
                contentPadding, contentPadding)
        }

        private fun bindOutlineProvider(itemView: View, position: Int, size: Int) {
            if (!Constants.isLollipop()) return
            itemView.outlineProvider = if (size == 1 || position == 0) defaultOutline else fixedOutline
        }
    }
}