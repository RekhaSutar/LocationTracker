package rekha.com.locationtracker.view.userjourney

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_user_journey.view.*
import kotlinx.android.synthetic.main.user_journey_details.view.*
import kotlinx.android.synthetic.main.user_journey_details.view.txtJourneyId as popUpJourneyId
import rekha.com.locationtracker.R
import rekha.com.locationtracker.data.db.UserJourney
import rekha.com.locationtracker.view.userjourney.UserJourneyListAdapter.ViewHolder
import java.util.*


class UserJourneyListAdapter(private val context: Context) : RecyclerView.Adapter<ViewHolder>(), View.OnClickListener {
    override fun onClick(v: View?) {
        val userJourney = v?.tag as UserJourney
        val intent = Intent(context, UserJourneyDetailsActivity::class.java)
        intent.putExtra(ARG_JOURNEY_ID, userJourney.id)
        context.startActivity(intent)
//        initiatePopupWindow(v, userJourney)
    }

    var userJourneyList: List<UserJourney>? = null
    fun addUserJourneyList(userJourneyList: List<UserJourney>){
        this.userJourneyList = userJourneyList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return userJourneyList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return ViewHolder(inflater.inflate(R.layout.row_user_journey, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.bindItems(userJourneyList!![position], this)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(userJourney: UserJourney, listener : View.OnClickListener) = with(itemView) {
            row_parent.setOnClickListener(listener)
            row_parent.tag = userJourney
            txtJourneyId.text = userJourney.id.toString().prependIndent(indent = "Journey Id : ")
            txtJourneyStartTime.text = DateFormat.format("dd-MMM-yy hh.mm aa", Date(userJourney.user_journey[0].time)).toString()
                    .prependIndent(indent = "start time : ")
        }
    }

    private fun initiatePopupWindow(v: View, userJourney: UserJourney) {
        if (userJourney.user_journey == null || userJourney.user_journey.size == 0){ return }

        val alert = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val alertLayout = inflater.inflate(R.layout.user_journey_details, null)
        alertLayout.popUpJourneyId.text = userJourney.id.toString().prependIndent(indent =  "Journey Id : ")
        alertLayout.txtStartTime.text = DateFormat.format("dd-MMM-yy hh.mm aa", Date(userJourney.user_journey[0].time)).toString()
                .prependIndent(indent = "start time : ")
        alertLayout.txtEndTime.text = DateFormat.format("dd-MMM-yy hh.mm aa", Date(userJourney.user_journey[userJourney.user_journey.size - 1].time)).toString()
                .prependIndent(indent = "end time : ")
        alert.setView(alertLayout)
                .setCancelable(true)
                .setTitle("Your journey Details")
                .setPositiveButton("Ok") { dialog, _ ->  dialog.dismiss()}

        val dialog = alert.create()
        dialog.show()

    }
}