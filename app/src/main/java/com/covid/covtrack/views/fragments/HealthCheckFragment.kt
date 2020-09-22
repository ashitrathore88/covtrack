package com.covid.covtrack.views.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.adapters.CovidQuestionsRv
import com.covid.covtrack.models.CovidQuestions
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.Constants.NAVIGATE_HEALTHCHECK
import com.covid.covtrack.utilities.Constants.NAVIGATE_HOMEFRAG
import com.covid.covtrack.utilities.PreferenceUtil
import com.covid.covtrack.viewModels.HealthCheckViewModel
import com.covid.covtrack.views.activities.HomeActivity
import com.covid.covtrack.views.activities.LoginActivity
import com.covid.covtrack.views.activities.NavDrawerActivity
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_health_check.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.NumberFormatException

class HealthCheckFragment : Fragment(), View.OnClickListener {

    internal var TAG = javaClass.simpleName
    private lateinit var healthCheckViewModel: HealthCheckViewModel
    var aswersList = ArrayList<Boolean>()
    var covidQuestionsRv: CovidQuestionsRv? = null
    var questionsList: ArrayList<CovidQuestions.Question>? = null
    var questionsList12: ArrayList<CovidQuestions.Question>? = null
    var rv_questions: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        healthCheckViewModel = ViewModelProvider(this).get(HealthCheckViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_health_check, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        healthCheckViewModel.getCovidQuestions()
        healthCheckViewModel.covidquestionsResponse.observe(this, Observer {
            val status = it.err_code
            if (status.equals("valid")) {
                try {
                    val userAge: Int? = it.data.user_age.toInt()
                    if (userAge!! > 15) {
                        rl_static_info.visibility = View.GONE
                        rl_questions.visibility = View.VISIBLE
                        questionsList12 = ArrayList<CovidQuestions.Question>()
                        questionsList12!!.addAll(it.data.questions)
                        loadQuestions()
                    } else {
                        rl_static_info.visibility = View.VISIBLE
                        rl_questions.visibility = View.GONE
                    }
                } catch (ex: NumberFormatException) {
                    ex.printStackTrace()
                }

            } else {
                if (activity != null) AlertMessages.getInstance().alertMsgBox(
                    it.message,
                    activity!!
                )
            }
        })

        healthCheckViewModel.covidAnsersResponse.observe(this, Observer {
            if (it.status.equals("valid")) {
                confirmationMsg()
            }
        })

        healthCheckViewModel.progressDialog.observe(this, Observer {
            if (activity != null) showProgress(it)
        })

        healthCheckViewModel.alertMessage.observe(this, Observer {
            if (activity != null) AlertMessages.getInstance().alertMsgBox(it, activity!!)
            //confirmationMsg()
        })

        btn_submit.setOnClickListener(this)
        btn_clearall.setOnClickListener(this)
    }

    private fun confirmationMsg() {
        val builder = AlertDialog.Builder(activity!!, R.style.CustomAlertDialog)
        builder.setMessage(getString(R.string.str_thank_you_txt))
            .setPositiveButton("ok") { dialog, id ->
                dialog.dismiss()

                Log.d(TAG,"is TRUE "+covidQuestionsRv!!.checkIfTrue())
                if(covidQuestionsRv!!.checkIfTrue())
                    cautionMsg()
                    //(activity as NavDrawerActivity).performDrawerItemClick(NAVIGATE_HEALTHCHECK)
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun cautionMsg(){
        val builder = AlertDialog.Builder(activity!!, R.style.CustomAlertDialog)
        builder.setMessage(getString(R.string.str_caution_msg))
            .setPositiveButton("ok") { dialog, id ->
                dialog.dismiss()

                (activity as NavDrawerActivity).performDrawerItemClick(NAVIGATE_HEALTHCHECK)
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun loadQuestions() {
        questionsList = questionsList12
        rv_questions = view!!.findViewById<RecyclerView>(R.id.rv_questions)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_questions!!.layoutManager = linearLayoutManager
        rv_questions!!.setHasFixedSize(true)
        covidQuestionsRv = CovidQuestionsRv(questionsList!!)
        rv_questions!!.adapter = covidQuestionsRv
    }


    override fun onClick(v: View?) {
        val id = v!!.id
        if (id == R.id.btn_submit) {
            uploadAns()
        } else if (id == R.id.btn_clearall) {
            // clear recyclerview
            questionsList!!.clear();
            questionsList!!.addAll(questionsList12!!)
            covidQuestionsRv!!.notifyDataSetChanged();

        }
    }

    fun uploadAns() {
        aswersList = covidQuestionsRv!!.selectedAnswers()
        val user_ID = PreferenceUtil.ins.getValueString(Constants.USER_ID)
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", user_ID)
        val jsonArray = JsonArray()
        for (i in questionsList!!.indices) {
            Log.d(TAG, "QuestionId: " + questionsList!![i].id + " answer: " + aswersList[i])
            val answersObj = JsonObject()
            answersObj.addProperty("questionId", questionsList!![i].id)
            if (aswersList[i]) answersObj.addProperty("answer", "yes")
            else answersObj.addProperty("answer", "no")
            jsonArray.add(answersObj)
        }
        jsonObject.add("qus_ans", jsonArray)

       // Log.d(TAG, "sri_request: " + jsonObject.toString())

        healthCheckViewModel.uploadAnswers(jsonObject)
    }


    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(activity!!, show)
    }
}